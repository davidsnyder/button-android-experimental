/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.button.activity;

import android.util.Log;
import io.button.fragment.CameraSectionFragment;
import io.button.fragment.CameraPreviewFragment;
import io.button.fragment.ButtonsSectionFragment;
import io.button.fragment.ProfileSectionFragment;
import io.button.fragment.FeedSectionFragment;
import io.button.R;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.content.Context;

import android.hardware.Camera;
import android.app.ActionBar;
import android.app.PendingIntent;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;

import com.parse.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import javax.inject.Inject;
import javax.inject.Provider;
import io.button.dagger.annotation.Button;
import io.button.dagger.Injector;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.commonsware.cwac.camera.PictureTransaction;

import java.lang.Override;

public class MainActivity extends FragmentActivity implements CameraHostProvider,
        ButtonsSectionFragment.OnButtonSelectedListener {

    @Inject
    Provider<ParseUser> currentUser;

    @Inject
    NfcAdapter nfcAdapter;

    @Inject
    @Button
    IntentFilter buttonNdefIntentFilter;

    AppSectionsPagerAdapter collectionPagerAdapter;
    ViewPager mViewPager;

    private static final String sections[] = {"Camera", "Feed", "Buttons"};
    private static final int NUM_PAGER_SECTIONS = sections.length;
    private static final int NUM_DEFAULT_PAGE = 1;

    private PendingIntent pendingIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

        getActionBar().hide();

        Intent intent = getIntent();
        // We were started via a button scan; do not pass go and proceed directly to button profile
        if (buttonScanned(intent)) {
            onButtonProfileSelected(getButtonId(intent), false);
        }

        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        collectionPagerAdapter= new AppSectionsPagerAdapter(fragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(collectionPagerAdapter);
        mViewPager.setCurrentItem(NUM_DEFAULT_PAGE);
        mViewPager.setOffscreenPageLimit(NUM_PAGER_SECTIONS);

        // Create a generic PendingIntent that will be delivered to this activity.
        // The NFC stack will fill in the intent with the details
        // of the discovered tag before delivering to this activity.
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new CameraSectionFragment();
                case 1:
                    return new FeedSectionFragment();
                case 2:
                    return new ButtonsSectionFragment();
                default:
                    return new FeedSectionFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGER_SECTIONS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sections[position];
        }
    }

    @Override
    public CameraHost getCameraHost() {
        return(new MyCameraHost(this));
    }

    class MyCameraHost extends SimpleCameraHost {

        public MyCameraHost(Context _context) {
            super(_context);
        }

        // Not really saving; we're only forwarding on to the
        // CameraPreviewFragment which will handle saving if the user commits
        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {
            onPhotoTaken(image);
        }

        @Override
        public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
            parameters.setRotation(90);
            return parameters;
        }
    }

    @Override
    // Intents that arrive while the app is already opened will come through here
    // If the app is not opened, the intent will proceed through MainActivity.onCreate
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ParseUser user = currentUser.get();

        // We must check if a user is logged in
        if (user != null) {
            if (buttonScanned(intent)) {
                //TODO: if there's already a fragment of this button up, don't do anything
                onButtonProfileSelected(getButtonId(intent), true);
                attemptButtonClaim(getButtonId(intent));
            }
          //  userCheckTextView.setText("User " + user.getUsername() + " is logged in");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that we intercept any additional button scans
        enableForegroundDispatch();

        Intent intent = getIntent();
        // We check our user
        ParseUser user = currentUser.get();
        if (user == null) {
         //   startActivity(new Intent(this, SignUpOrLoginActivity.class));
        } else {
            // Check to see if this is a button scan
            if (buttonScanned(intent)) {
                attemptButtonClaim(getButtonId(intent));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disableForegroundDispatch();
    }

    /**
     * Method that tells us whether the activity was created as a result of a scanned button
     *
     * @param intent
     * @return true if intent was from a button scan false if not
     */
    private boolean buttonScanned(Intent intent) {
        // TODO add more stringent check for this intent
        return NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction());
    }

    /**
     * Retrieves a button id from an intent.
     *
     * @param intent
     * @return
     */
    private String getButtonId(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        String buttonId = "";
        if (rawMsgs != null) {
            NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            // Get the button id
            buttonId = new String(msgs[0].getRecords()[0].getPayload());
        }
        return buttonId;
    }

    /**
     * Attempts to claim a button scanned by the user
     *
     * @param buttonId
     */
    private void attemptButtonClaim(String buttonId) {
        Log.d(getClass().getSimpleName(), buttonId);
    }

    /**
     * Utility method to enable foreground dispatch
     */
    private void enableForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {buttonNdefIntentFilter}, null);
        }
    }

    /**
     * Utility method to disable foreground dispatch
     */
    private void disableForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void onPhotoTaken(byte[] image) {
        CameraPreviewFragment fragment = new CameraPreviewFragment();

        CameraPreviewFragment.imageToShow = image;

        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, fragment, "cameraPreview");
        fragmentTransaction.addToBackStack("cameraPreview");
        fragmentTransaction.commit();
    }

    /**
     * Event callback for ButtonsSectionFragment.OnButtonSelectedListener
     */
    public void onButtonProfileSelected(String buttonId, boolean addToBackStack) {
        ProfileSectionFragment fragment = new ProfileSectionFragment();

        Bundle mBundle = new Bundle();
        mBundle.putString("buttonId", buttonId);
        fragment.setArguments(mBundle);

        final FragmentManager fragmentManager = this.getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag(buttonId) != null) {
            //the profile fragment corresponding to this buttonId is already in the foreground, do not transition
        } else {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment, buttonId);
            if(addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

}