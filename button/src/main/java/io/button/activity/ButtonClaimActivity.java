
package io.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.parse.*;
import com.parse.ParseQuery;
import io.button.R;
import io.button.dagger.Injector;
import io.button.models.*;

import javax.inject.Inject;
import javax.inject.Provider;

public class ButtonClaimActivity extends Activity {

    @Inject
    Provider<ParseUser> currentUser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_button_claim);

        getActionBar().hide();

        Injector.inject(this, getApplicationContext());
        ButterKnife.inject(this);

        final String buttonId = getIntent().getStringExtra("buttonId");
        ((TextView) findViewById(R.id.buttonId)).setText(buttonId);

        Button claimButton = (Button) findViewById(R.id.button_claim_button);
        claimButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOwnerAndOpenProfile(buttonId);

            }
        });

    }

    public void setOwnerAndOpenProfile(String buttonId) {
        final Activity activity = this;
        ParseQuery<io.button.models.Button> query = ParseQuery.getQuery(io.button.models.Button.class);
        query.getInBackground(buttonId, new GetCallback<io.button.models.Button>() {
            public void done(io.button.models.Button button, ParseException e) {
                if (e == null) {
                    button.setOwner(ParseUser.getCurrentUser());
                    button.saveInBackground();

                    Intent intent = new Intent(activity, ButtonProfileActivity.class);
                    intent.putExtra("buttonLinkId", getIntent().getStringExtra("buttonLinkId"));
                    startActivity(intent);
                    finish();
                } else {
                    // handle error
                }

            }
        });
    }


}