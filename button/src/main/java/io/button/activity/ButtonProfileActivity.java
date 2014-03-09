
package io.button.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.parse.*;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import io.button.R;
import io.button.activity.PostActivity;
import io.button.adapter.PostAdapter;
import io.button.dagger.Injector;
import io.button.models.*;

import javax.inject.Inject;
import javax.inject.Provider;

public class ButtonProfileActivity extends Activity {

    @Inject
    Provider<ParseUser> currentUser;

    @Inject
    NfcAdapter nfcAdapter;

    private PendingIntent pendingIntent;

    private io.button.models.Button _button;
    private io.button.models.ButtonLink _buttonLink;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_button_profile);

        getActionBar().hide();

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

//        // Create a pending intent to capture any future NFC scans that
//        // take place while the app is already in the foreground.
//        pendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        final String buttonLinkId = getIntent().getStringExtra("buttonLinkId");
        final Boolean fromScan = getIntent().getBooleanExtra("fromScan", false);

        ParseQuery<ButtonLink> query = ParseQuery.getQuery("ButtonLink");
        query.include("button.owner");
        query.getInBackground(buttonLinkId, new GetCallback<ButtonLink>() {
            public void done(ButtonLink buttonLink, ParseException e) {
                if (e == null) {
                    renderButtonProfile(buttonLink.getButton(), buttonLink, fromScan);
                } else {
                    // something went wrong
                }
            }
        });

    }

    public void renderButtonProfile(final io.button.models.Button button, final ButtonLink buttonLink, boolean fromScan) {

        String buttonName;
        if (button.getName() == null) {
            buttonName = button.getObjectId();
        } else {
            buttonName = button.getName();
        }
        ((TextView) findViewById(R.id.buttonId)).setText(buttonName);

        if (fromScan) {
            photoButton.setVisibility(View.VISIBLE);
            Button photoButton = (Button) findViewById(R.id.button_goto_camera);

            photoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launchNewPostActivity(button);
                }
            });
        }

        ListView buttonPostList = ((ListView) findViewById(android.R.id.list));

        ParseQueryAdapter.QueryFactory<Post> postQueryFactory = new ParseQueryAdapter.QueryFactory<Post>() {
            public ParseQuery<Post> create() {
                ParseQuery query = new ParseQuery("Post");
                query.include("button");
                query.include("user");
                query.whereEqualTo("button", button);
                return query;
            }
        };

        PostAdapter postAdapter = new PostAdapter(this, postQueryFactory);
        buttonPostList.setAdapter(postAdapter);

    }

    public void launchNewPostActivity(io.button.models.Button button) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("buttonId", button.getObjectId());
        startActivity(intent);
    }

}