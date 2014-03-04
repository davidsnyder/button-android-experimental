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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQuery;
import io.button.R;
import io.button.models.Button;
import io.button.adapter.ButtonAdapter;
import android.util.Log;

public class ButtonsSectionFragment extends ListFragment {

    final static CharSequence EMPTY_BUTTON_LIST_TEXT = "When you scan buttons they will show up here";

    private ButtonAdapter buttonAdapter;

    OnButtonSelectedListener mCallback;

    public interface OnButtonSelectedListener {
        public void onButtonProfileSelected(String buttonId, boolean addToBackStack);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_button_list, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseQueryAdapter.QueryFactory<Button> buttonQueryFactory = new ParseQueryAdapter.QueryFactory<Button>() {
            public ParseQuery<Button> create() {
                ParseQuery query = new ParseQuery("Button");
                // TODO: query.where(I own or have scanned this button);
                return query;
            }
        };
        buttonAdapter = new ButtonAdapter(this.getActivity(), buttonQueryFactory);

        setListAdapter(buttonAdapter);

        //Some bug prevents this from working
        //https://code.google.com/p/android/issues/detail?id=21742
        //setEmptyText(EMPTY_BUTTON_LIST_TEXT);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String buttonId = ((TextView) v.findViewById(R.id.button_id)).getText().toString();
        mCallback.onButtonProfileSelected(buttonId, true);
    }
}
