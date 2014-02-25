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
import io.button.fragment.ButtonsSectionFragment;
import io.button.fragment.ProfileSectionFragment;
import io.button.fragment.FeedSectionFragment;
import io.button.R;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;

import android.app.ActionBar;
import android.app.PendingIntent;
import android.app.FragmentTransaction;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

public class MainActivity extends FragmentActivity implements
        CameraHostProvider {

    @Inject
    Provider<ParseUser> currentUser;

    @Inject
    NfcAdapter nfcAdapter;

    @Inject
    @Button
    IntentFilter buttonNdefIntentFilter;

//    @InjectView(R.id.text_view_user_check)
//    TextView userCheckTextView;
//
//    @InjectView(R.id.text_view_button_check)
//    TextView buttonCheckTextView;

    AppSectionsPagerAdapter collectionPagerAdapter;
    ViewPager mViewPager;

    private static final String sections[] = {"Camera", "Feed", "Buttons"};
    private static final int NUM_PAGER_SECTIONS = sections.length;
    private static final int NUM_PAGE_FEED = 1;

    private PendingIntent pendingIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.inject(this, getApplicationContext());

        // Inject our views
        ButterKnife.inject(this);

        getActionBar().hide();

        collectionPagerAdapter= new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(collectionPagerAdapter);
        mViewPager.setCurrentItem(NUM_PAGE_FEED);
        mViewPager.setOffscreenPageLimit(NUM_PAGER_SECTIONS);

        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details
        // of the discovered tag before delivering to
        // this activity.
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
        return(new SimpleCameraHost(this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ParseUser user = currentUser.get();

        // We must check if a user is logged in
        if (user != null) {
            if (buttonScanned(intent)) {
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

        // We check our user
        ParseUser user = currentUser.get();
        if (user == null) {
         //   startActivity(new Intent(this, SignUpOrLoginActivity.class));
        } else {
           // userCheckTextView.setText("User " + user.getUsername() + " is logged in");

            // Check to see if this is a button scan
            if (buttonScanned(getIntent())) {
                attemptButtonClaim(getButtonId(getIntent()));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Disable our foreground dispatch
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
        Log.d(getClass().getSimpleName(),buttonId);
    }

    /**
     * Utility method to enable foreground dispatch
     */
    private void enableForegroundDispatch() {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {buttonNdefIntentFilter}, null);
    }

    /**
     * Utility method to disable foreground dispatch
     */
    private void disableForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this);
    }

}