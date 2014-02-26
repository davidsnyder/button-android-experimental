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

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import io.button.R;

public class CameraPreviewFragment extends Fragment {

    static byte[] imageToShow = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.camera_preview, container, false);

        imageToShow = getArguments().getByteArray("image");

        if(imageToShow != null) {
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inPurgeable=true;
            opts.inInputShareable=true;
            opts.inMutable=false;
            opts.inSampleSize=2;

            ImageView iv = ((ImageView)rootView.findViewById(R.id.camera_preview));
            iv.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow,
                    0,
                    imageToShow.length,
                    opts));

            imageToShow = null;

            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            Log.d(getClass().getSimpleName(),"imageToShow is null");
        }

        final Button button = (Button)rootView.findViewById(R.id.button_discard_photo);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack("cameraPreview", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return rootView;
    }
}