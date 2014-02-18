package io.button.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
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
import io.button.dagger.annotation.Button;

public class ButtonMainActivity extends BaseActionBarActivity {
    @Inject
    Provider<ParseUser> currentUser;

    @Inject
    NfcAdapter nfcAdapter;

    @Inject
    @Button
    IntentFilter buttonNdefIntentFilter;

    @InjectView(R.id.text_view_user_check)
    TextView userCheckTextView;

    @InjectView(R.id.text_view_button_check)
    TextView buttonCheckTextView;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_main);

        // Inject our views
        ButterKnife.inject(this);

        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ParseUser user = currentUser.get();

        // We must check if a user is logged in
        if (user != null) {
            if (buttonScanned(intent)) {
                String buttonId = getButtonId(intent);

                // Notify the user of the button we found
                buttonCheckTextView.setText("User " + user.getUsername() + " found button " + buttonId + "!");
            }
            userCheckTextView.setText("User " + user.getUsername() + " is logged in");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that we intercept any additional button scans
        enableForegroundDispatch();

        // We check our user
        ParseUser user = currentUser.get();
        if (user == null) {
            startActivity(new Intent(this, SignUpOrLoginActivity.class));
        } else {
            userCheckTextView.setText("User " + user.getUsername() + " is logged in");

            // Check to see if this is a button scan
            if (buttonScanned(getIntent())) {
                String buttonId = getButtonId(getIntent());

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
            }
        }
    }
}

    @Override
    public void onPause() {
        super.onPause();

        // Disable our foreground dispatch
        disableForegroundDispatch();
    }

    /**
     * Method that tells us whether the activity was created as a result of a scanned button
     *
     * @param intent
     * @return true if intent was from a button scan false if not
     */
    private boolean buttonScanned(Intent intent) {
        // TODO add more stringent check for this intent
        return NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction());
    }

    /**
     * Retrieves a button id from an intent.
     *
     * @param intent
     * @return
     */
    private String getButtonId(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }

            // Get the button id
            String buttonId = new String(msgs[0].getRecords()[0].getPayload());

            return buttonId;
        }

        return "";
    }

    /**
     * Utility method to enable foreground dispatch
     */
    private void enableForegroundDispatch() {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {buttonNdefIntentFilter}, null);
    }

    /**
     * Utility method to disable foreground dispatch
     */
    private void disableForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this);
    }
}
