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

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import butterknife.ButterKnife;
import com.parse.*;
import io.button.R;
import io.button.activity.ButtonProfileActivity;
import io.button.dagger.Injector;
import io.button.dagger.annotation.Button;
import io.button.fragment.ButtonsSectionFragment;
import io.button.fragment.FeedSectionFragment;
import io.button.models.*;

import javax.inject.Inject;
import javax.inject.Provider;

public class MainActivity extends FragmentActivity implements
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

    private static final String sections[] = {"UserProfile", "Feed", "Buttons"};
    private static final int NUM_PAGER_SECTIONS = sections.length;
    private static final int NUM_DEFAULT_PAGE = 1;

    //private PendingIntent pendingIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate");
        if(getIntent().getAction() != null) {
        Log.d("MainActivity", getIntent().getAction());
        }

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

        getActionBar().hide();

        //If the app launches via a button scan the intent will be caught here
//        Intent intent = getIntent();
//        if (buttonScanned(intent)) {
//            handleButtonScan(intent);
//        }

        // Set up SwipePager
        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        collectionPagerAdapter= new AppSectionsPagerAdapter(fragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(collectionPagerAdapter);
        mViewPager.setCurrentItem(NUM_DEFAULT_PAGE);
        mViewPager.setOffscreenPageLimit(NUM_PAGER_SECTIONS);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int page) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO: Try to handle back stack navigation for ViewPager here
            }

        });

        // Create a pending intent to capture any future NFC scans that
        // take place while the app is already in the foreground.
        // A scan intent will call MainActivity.onNewIntent()
//        pendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
                   // return new CameraSectionFragment();
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

    /**
     * Event callback for ButtonsSectionFragment.OnButtonSelectedListener
     */
    public void onButtonProfileSelected(String buttonLinkId) {
        Intent intent = new Intent(this, ButtonProfileActivity.class);
        intent.putExtra("buttonLinkId", buttonLinkId);
        startActivity(intent);
    }

    /**
     *  Intents that arrive while the app is already opened will come through here
     *  If the app is not opened, the intent will proceed through MainActivity.onCreate
     */
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        ParseUser user = currentUser.get();
//        if (user != null) {
//            if (buttonScanned(intent)) {
//                handleButtonScan(intent);
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

      // Ensure that we intercept any additional button scans
      //  enableForegroundDispatch();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //disableForegroundDispatch();
    }

    /**
     * Method that tells us whether the activity was created as a result of a scanned button
     *
     * @param intent
     * @return true if intent was from a button scan false if not
     */
//    private boolean buttonScanned(Intent intent) {
//        // TODO add more stringent check for this intent
//        return NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction());
//    }
//
//    private void handleButtonScan(Intent intent) {
//        //HANDLE BUTTON SCAN
//    }

    /**
     * Retrieves a button id from an intent.
     *
     * @param intent
     * @return
     */
//    private String getButtonId(Intent intent) {
//        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        String buttonId = "";
//        if (rawMsgs != null) {
//            NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
//            for (int i = 0; i < rawMsgs.length; i++) {
//                msgs[i] = (NdefMessage) rawMsgs[i];
//            }
//            // Get the button id
//            buttonId = new String(msgs[0].getRecords()[0].getPayload());
//        }
//        return buttonId;
//    }

//    /**
//     * Utility method to enable foreground dispatch
//     */
//    private void enableForegroundDispatch() {
//        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
//            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {buttonNdefIntentFilter}, null);
//        }
//    }
//
//    /**
//     * Utility method to disable foreground dispatch
//     */
//    private void disableForegroundDispatch() {
//        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
//            nfcAdapter.disableForegroundDispatch(this);
//        }
//    }

}