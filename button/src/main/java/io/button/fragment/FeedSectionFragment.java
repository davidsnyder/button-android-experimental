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
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import io.button.R;
import io.button.adapter.PostAdapter;
import io.button.models.*;

public class FeedSectionFragment extends ListFragment {

    private PostAdapter postAdapter;

    //OnButtonSelectedListener mCallback;
//
//    public interface OnButtonSelectedListener {
//        public void onButtonProfileSelected(String buttonId, boolean addToBackStack);
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnButtonSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnButtonSelectedListener");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_feed, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseQueryAdapter.QueryFactory<Post> postQueryFactory = new ParseQueryAdapter.QueryFactory<Post>() {
            public ParseQuery<Post> create() {
                ParseQuery query = new ParseQuery("Post");
                query.include("user");
                query.include("button");
                // TODO: query.where(this button is followed by me);
                return query;
            }
        };
        postAdapter = new PostAdapter(this.getActivity(), postQueryFactory);

        setListAdapter(postAdapter);
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        String buttonId = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
//        mCallback.onButtonProfileSelected(buttonId, true);
//    }
}