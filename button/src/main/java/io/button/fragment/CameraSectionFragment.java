///***
// 7  Copyright (c) 2013 CommonsWare, LLC
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may
// not use this file except in compliance with the License. You may obtain
// a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// */
//
//package io.button.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import io.button.R;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.support.v4.app.FragmentActivity;
//import io.button.fragment.CameraFragment;
//import com.commonsware.cwac.camera.CameraView;
//import com.commonsware.cwac.camera.PictureTransaction;
//import io.button.activity.MainActivity;
//import com.commonsware.cwac.camera.CameraHost;
//
//import java.lang.Override;
//
//public class CameraSectionFragment extends CameraFragment {
//
//    private CameraHost cameraHost = null;
//    private CameraView cameraView = null;
//    private Button takePhotoButton;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//        View content = inflater.inflate(R.layout.camera, container, false);
//        cameraView = (CameraView)content.findViewById(R.id.camera);
//
//        setCameraView(cameraView);
//
//        FragmentActivity parentActivity = getActivity();
//        if(parentActivity instanceof MainActivity) {
//           // setHost(((MainActivity) parentActivity).getCameraHost());
//            cameraHost = ((MainActivity) parentActivity).getCameraHost();
//        }
//
//        takePhotoButton = (Button) content.findViewById(R.id.button_take_photo);
//        takePhotoButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                takePhotoButton.setEnabled(false);
//                PictureTransaction xact = new PictureTransaction(cameraHost);
//                takePicture(xact);
//            }
//        });
//
//        return(content);
//    }
//
//    @Override
//    public void onResume () {
//        Log.d("CameraSectionFragment", "ON RESUME");
//        if(takePhotoButton != null) {
//            takePhotoButton.setEnabled(true);
//        }
//        super.onResume();
//    }
//
//
//}