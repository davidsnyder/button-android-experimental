

package io.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.parse.*;
import io.button.R;
import io.button.dagger.Injector;
import io.button.models.*;
import io.button.models.Post;
import io.button.util.MediaUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PostActivity extends Activity {

    private static final int SCALED_IMAGE_DIM = 700;

    private Uri fileUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_post);

        getActionBar().hide();

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

        final String buttonId = getIntent().getStringExtra("buttonId");
        ((TextView) findViewById(R.id.text2)).setText(buttonId);

        // In case we need something from the button to render the layout.  I don't think we do.
//        ParseQuery<io.button.models.Button> query = ParseQuery.getQuery("Button");
//        query.getInBackground(buttonId, new GetCallback<io.button.models.Button>() {
//            public void done(io.button.models.Button button, ParseException e) {
//                if (e == null) {
//                    renderPost(button);
//                } else {
//                    // something went wrong
//                }
//            }
//        });

        Button submitButton = (Button) findViewById(R.id.button_submit_post);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getId() == R.id.button_submit_post) {
                    Button btn = (Button)findViewById(R.id.button_submit_post);
                    btn.setEnabled(false);
                }
                submitPost(buttonId, fileUri);
            }
        });

        startCameraActivity();
    }

    public void startCameraActivity() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = MediaUtils.getOutputMediaFileUri(MediaUtils.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, MediaUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MediaUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.post_image)).setImageURI(fileUri);
               // openNewPostFragment(getButton().getObjectId(), fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("PostActivity", "capture cancelled");
                finish();
            } else {
                Log.d("PostActivity", "capture failed");
                finish();
                // Image capture failed, advise user
            }
        }
    }

    private void submitPost(final String buttonId, final Uri imageUri) {

        try {
            File file = new File(imageUri.getPath());
            byte[] imageData = org.apache.commons.io.FileUtils.readFileToByteArray(file);

            byte[] scaledImage = scalePhoto(imageData);

            final ParseFile photoFile = new ParseFile(imageUri.getLastPathSegment(), scaledImage);
            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("NewPostFragment parsefile save", e.getMessage());
                    } else {
                        Post post = new Post();
                        post.setPhotoFile(photoFile);
                        post.setButton(buttonId);
                        post.setCreator(ParseUser.getCurrentUser());
                        post.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    exitActivity();
                                } else {
                                    Log.d("NewPostFragment post save", e.getMessage());
                                }
                            }

                        });
                    }
                }
            });
        } catch (IOException e) {
              Log.d("PostActivityIOException", e.getMessage());
        }

    }

    private byte[] scalePhoto(byte[] imageData) {
        Bitmap postImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        Bitmap postImageScaled = Bitmap.createScaledBitmap(postImage, SCALED_IMAGE_DIM, SCALED_IMAGE_DIM
                * postImage.getHeight() / postImage.getWidth(), false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        postImageScaled.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        return bos.toByteArray();
    }

    private void exitActivity() {
        finish();
    }



}