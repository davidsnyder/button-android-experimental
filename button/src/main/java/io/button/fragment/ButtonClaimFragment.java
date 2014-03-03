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
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.app.Activity;
import io.button.fragment.ButtonsSectionFragment;

import com.parse.*;

public class ButtonClaimFragment extends Fragment {

    //TODO: These interfaces should be pulled out
    ButtonsSectionFragment.OnButtonSelectedListener mCallback;

    private Button claimButton;
    private String buttonId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_button_claim, container, false);

        buttonId = getArguments().getString("buttonId");
        ((TextView) rootView.findViewById(R.id.buttonId)).setText(buttonId);

        claimButton = (Button) rootView.findViewById(R.id.button_claim_button);
        claimButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseQuery<io.button.models.Button> query = ParseQuery.getQuery(io.button.models.Button.class);
                query.include("buttonClaim.user");
                // FIXME: things like getParseObject("buttonClaim") won't work unless the object is included here.
                // maybe encapsulate these queries in the Button class
                query.getInBackground(buttonId, new GetCallback<io.button.models.Button>() {
                    public void done(io.button.models.Button button, ParseException e) {
                        if (e == null) {
                            button.setOwner(ParseUser.getCurrentUser());
                            button.saveInBackground();

                            mCallback.onButtonProfileSelected(buttonId, false);
                        }
                    }
                });

            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ButtonsSectionFragment.OnButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }
}