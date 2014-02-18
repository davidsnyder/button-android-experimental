package io.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import com.parse.ParseObject;
import com.parse.ParseUser;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.InjectView;
import io.button.R;
import io.button.base.BaseActionBarActivity;

public class ButtonViewActivity extends BaseActionBarActivity {
    @Inject
    Provider<ParseUser> currentUser;

    @InjectView(R.id.text_view_user_check)
    TextView userCheckTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_main);

        // Inject our views
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // We check our user
        ParseUser user = currentUser.get();
        if (user == null) {
            startActivity(new Intent(this, SignUpOrLoginActivity.class));
        } else {
            Intent intent = getIntent();
            String buttonId = intent.getStringExtra("buttonId");
            //    ParseObject buttonOwner = buttonClaim.getParseUser("user");
            userCheckTextView.setText("ButtonViewActivity: User " + user.getUsername() + " is viewing the claimed button " + buttonId + "!");
        }
    }
}
