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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import io.button.R;
import java.io.File;
import java.io.IOException;
import io.button.models.Post;

import com.parse.*;

public class NewPostFragment extends Fragment {

    private Button submitButton;
    private String buttonId;
    private Uri imageUri;
    private Post post;

    private ParseFile photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_new_post, container, false);

        imageUri = Uri.parse(getArguments().getString("fileUri"));
        buttonId = getArguments().getString("buttonId");

        ((TextView) rootView.findViewById(R.id.text2)).setText(buttonId);
        ((ImageView) rootView.findViewById(R.id.post_image)).setImageURI(imageUri);

        submitButton = (Button) rootView.findViewById(R.id.button_submit_post);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitPost(imageUri);

                // Return to button profile
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack("newPost", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return rootView;
    }

    private void submitPost(Uri imageUri) {
        Log.d(getClass().getSimpleName(), "submitPost");

        try {
            File file = new File(imageUri.getPath());
            byte[] imageData = org.apache.commons.io.FileUtils.readFileToByteArray(file);

            photoFile = new ParseFile(imageUri.getLastPathSegment(), imageData);
            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("NewPostFragment parsefile save", e.getMessage());
                    } else {
                        post = new Post();
                        post.setPhotoFile(photoFile);
                        post.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                } else {
                                    Log.d("NewPostFragment post save", e.getMessage());
                                }
                            }

                        });
                    }
                }
            });
        } catch (IOException e) {

        }

    }

}