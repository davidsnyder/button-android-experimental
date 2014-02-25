/***
 7  Copyright (c) 2013 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package io.button.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import io.button.R;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.lang.CharSequence;
import java.lang.Override;

public class ButtonsSectionFragment extends ListFragment {

    final static CharSequence EMPTY_BUTTON_LIST_TEXT = "Scan buttons and they will show up here";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.button_list, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] values = new String[] {"1","2","3","4","5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);   //TODO: Replace with R.layout.button_list_row
        setListAdapter(adapter);

        //Some bug prevents this from working
        //https://code.google.com/p/android/issues/detail?id=21742
        //setEmptyText(EMPTY_BUTTON_LIST_TEXT);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data

        ProfileSectionFragment fragment = new ProfileSectionFragment();

        Bundle mBundle = new Bundle();
        mBundle.putInt("arg", position); //TODO: stick buttonId in here
        fragment.setArguments(mBundle);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.detail_container, fragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
