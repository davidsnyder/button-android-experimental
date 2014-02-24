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

import io.button.fragment.CameraSectionFragment;
import io.button.fragment.ButtonsSectionFragment;
import io.button.fragment.ProfileSectionFragment;
import io.button.fragment.FeedSectionFragment;

import io.button.R;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.SimpleCameraHost;

public class MainActivity extends FragmentActivity implements
        CameraHostProvider {

    AppSectionsPagerAdapter collectionPagerAdapter;
    ViewPager mViewPager;

    private static final String sections[] = {"Camera", "Feed", "Buttons", "Button Profile"};
    private static final int NUM_PAGER_SECTIONS = 4;
    private static final int NUM_PAGE_FEED = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        collectionPagerAdapter= new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(collectionPagerAdapter);
        mViewPager.setCurrentItem(NUM_PAGE_FEED);
        mViewPager.setOffscreenPageLimit(NUM_PAGER_SECTIONS);
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
                case 3:
                    return new ProfileSectionFragment();
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

}