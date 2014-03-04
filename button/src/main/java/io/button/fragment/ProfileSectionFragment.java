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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQuery;
import com.parse.ParseObject;
import io.button.R;
import io.button.adapter.PostAdapter;
import io.button.models.Post;

public class ProfileSectionFragment extends Fragment {

    NewPostListener mCallback;

    private PostAdapter postAdapter;

    public interface NewPostListener {
        public void onNewPostSelected(String buttonId);
    }

    private Button photoButton;
    private String buttonId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_button_profile, container, false);
        Log.d("ProfileSectionFragment", Long.toString(Runtime.getRuntime
                ().totalMemory()));
        buttonId = getArguments().getString("buttonId");
        ((TextView) rootView.findViewById(R.id.buttonId)).setText(buttonId);

        photoButton = (Button) rootView.findViewById(R.id.button_goto_camera);
        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onNewPostSelected(buttonId);
            }
        });

        ListView buttonPostList = ((ListView) rootView.findViewById(android.R.id.list));

        ParseQueryAdapter.QueryFactory<Post> postQueryFactory = new ParseQueryAdapter.QueryFactory<Post>() {
            public ParseQuery<Post> create() {
                ParseQuery query = new ParseQuery("Post");
                ParseObject button = ParseObject.createWithoutData("Button", buttonId);
                query.whereEqualTo("button", button);
                return query;
            }
        };
        postAdapter = new PostAdapter(this.getActivity(), postQueryFactory);
        buttonPostList.setAdapter(postAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (NewPostListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewPostListener");
        }
    }


}