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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.GetCallback;
import com.parse.ParseException;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.InjectView;
import io.button.R;
import io.button.base.BaseActionBarActivity;

public class ButtonMainActivity extends BaseActionBarActivity {
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
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMsgs != null) {
                    NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }

                    // Get the button id
                    String buttonId = new String(msgs[0].getRecords()[0].getPayload());

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Button");
                    query.include("buttonClaim");
                    query.getInBackground(buttonId, new GetCallback<ParseObject>() {
                        public void done(ParseObject button, ParseException e) {
                            if (e == null) {
                                ParseObject buttonClaim = button.getParseObject("buttonClaim");
                                if (buttonClaim != null) { // button already has an owner
                                    Log.d(getClass().getSimpleName(), "button already claimed, launching buttonView activity");
                                    Intent intent = new Intent(ButtonMainActivity.this, ButtonViewActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("buttonId", button.getObjectId());
                                    startActivity(intent);
                                } else { // dibs
                                    Log.d(getClass().getSimpleName(), "button is unclaimed, launching claim activity");
                                    Intent intent = new Intent(ButtonMainActivity.this, ButtonClaimActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("buttonId", button.getObjectId());
                                    startActivity(intent);
                                }
                            } else {
                                Log.d(getClass().getSimpleName(), "buttonId "+button.getObjectId()+" does not exist in db");
                                // something went wrong

                            }
                        }
                    });

                    userCheckTextView.setText("User " + user.getUsername() + " found button " + buttonId + "!");
                }
            } else {
                userCheckTextView.setText("User " + user.getUsername() + " is logged in");
            }
        }
    }
}
