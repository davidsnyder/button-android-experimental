package io.button.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.content.Context;
import java.util.List;
import android.app.ActionBar;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import io.button.R;

public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    public static final String TAG = "CameraFragment";

    private SurfaceView surfaceView;
    private ParseFile photoFile;
    private ImageButton photoButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        photoButton = (ImageButton) findViewById(R.id.camera_photo_button);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.e(TAG, "No camera with exception: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(this, "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera == null)
                    return;
                mCamera.takePicture(new Camera.ShutterCallback() {

                                       @Override
                                       public void onShutter() {
                                           // nothing to do
                                       }

                                   }, null, new Camera.PictureCallback() {

                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                saveScaledPhoto(data);
                            }

                        });

            }
        });
    }

    /*
     * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
     * they are saved. Since we never need a full-size image in our app, we'll
     * save a scaled one right away.
     */
    private void saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 200, 200
                * mealImage.getHeight() / mealImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("button_photo.jpg", scaledData);
        final Activity activity = this;
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(activity,
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity,
                            "saved image successfully",
                            Toast.LENGTH_LONG).show();
                //    addPhotoToMealAndReturn(photoFile);
                }
            }
        });
    }

    /*
     * Once the photo has saved successfully, we're ready to return to the
     * NewMealFragment. When we added the CameraFragment to the back stack, we
     * named it "NewMealFragment". Now we'll pop fragments off the back stack
     * until we reach that Fragment.
     */
//    private void addPhotoToMealAndReturn(ParseFile photoFile) {
//        ((NewMealActivity) getActivity()).getCurrentMeal().setPhotoFile(
//                photoFile);
//        FragmentManager fm = getActivity().getFragmentManager();
//        fm.popBackStack("NewMealFragment",
//                FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.i(TAG, "No camera: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(this, "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPause() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        super.onPause();
    }


    /** A basic Camera preview class */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;
        private List<Camera.Size> mSupportedPreviewSizes;
        private Camera.Size mPreviewSize;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mCamera.setDisplayOrientation(90);

            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (mSupportedPreviewSizes != null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            }
        }

        // Magic from http://stackoverflow.com/questions/19577299/android-camera-preview-stretched
        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio=(double)h / w;

            if (sizes == null) return null;

            Camera.Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            for (Camera.Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }
            return optimalSize;
        }

    }
}



