package io.button.dagger;


import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.PatternMatcher;
import com.parse.ParseUser;
import dagger.Module;
import dagger.Provides;
import io.button.ButtonApplication;
import io.button.R;
import io.button.activity.ButtonClaimActivity;
import io.button.activity.ScanActivity;
import io.button.activity.ButtonProfileActivity;
import io.button.activity.PostActivity;
import io.button.activity.MainActivity;
import io.button.dagger.annotation.Application;
import io.button.dagger.annotation.Button;

//import io.button.activity.LoginActivity;
//import io.button.activity.SignUpActivity;
//import io.button.activity.SignUpOrLoginActivity;


@Module(
        complete = true,
        injects = {
                MainActivity.class,
                ButtonApplication.class,
                ButtonProfileActivity.class,
                ScanActivity.class,
                ButtonClaimActivity.class,
                PostActivity.class,
            //    SignUpOrLoginActivity.class,
           //     SignUpActivity.class,
           //     LoginActivity.class,
        },
        includes = {
                AndroidModule.class
        }
)
public class ButtonModule {

    @Provides
    ParseUser providesCurrentUser() {
        return ParseUser.getCurrentUser();
    }

    @Provides
    @Button
    IntentFilter providesButtonIntentFilter(@Application Context context) {
        IntentFilter buttonNdefIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        // Configure our intent filter for buttons
        buttonNdefIntentFilter.addDataScheme(context.getString(R.string.button_data_scheme));
        buttonNdefIntentFilter.addDataAuthority(context.getString(R.string.nfc_data_host), null);
        buttonNdefIntentFilter.addDataPath(context.getString(R.string.button_data_path_prefix),
                PatternMatcher.PATTERN_PREFIX);

        return buttonNdefIntentFilter;
    }
}
