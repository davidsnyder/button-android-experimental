
package io.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import butterknife.ButterKnife;
import com.parse.*;
import io.button.R;
import io.button.dagger.Injector;
import io.button.dagger.annotation.Button;
import io.button.models.*;
import io.button.activity.ButtonProfileActivity;
import io.button.activity.ButtonClaimActivity;

import java.util.HashMap;

public class ScanActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActionBar().hide();

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

        Intent intent = getIntent();
        if (buttonScanned(intent)) {
            String buttonId = getButtonId(intent);
            if (buttonId.length() > 0) {
                handleButtonScan(buttonId);
            }
        }

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

    private void handleButtonScan(String buttonId) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("button_id", buttonId);
        final Activity activity = this;
        ParseCloud.callFunctionInBackground("incrementButtonLink", params, new FunctionCallback<ButtonLink>() {
            public void done(ButtonLink buttonLink, ParseException e) {
                if (e == null) {
                    io.button.models.Button button = (io.button.models.Button) buttonLink.getParseObject("button");
                    Intent intent;
                    if (button.hasOwner()) {
                        intent = new Intent(activity, ButtonProfileActivity.class);
                        intent.putExtra("buttonLinkId", buttonLink.getObjectId());
                        intent.putExtra("fromScan", true);
                    } else {
                        intent = new Intent(activity, ButtonClaimActivity.class);
                        intent.putExtra("buttonId", button.getObjectId());
                        intent.putExtra("buttonLinkId", buttonLink.getObjectId());
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("incrementButtonLink ParseErrorCode", Integer.toString(e.getCode()));
                }
            }
        });
    }

    /**
     * Retrieves a button id from an intent.
     *
     * @param intent
     * @return
     */
    private String getButtonId(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        String buttonId = "";
        if (rawMsgs != null) {
            NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            // Get the button id
            buttonId = new String(msgs[0].getRecords()[0].getPayload());
        }
        return buttonId;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

}