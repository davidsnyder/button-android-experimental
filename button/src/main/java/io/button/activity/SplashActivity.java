package io.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import io.button.R;

public class SplashActivity extends Activity {
    private final int SPLASH_DURATION = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = getIntent();
                Log.d("splashActivity", intent.getAction());
                Intent buttonMainIntent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(buttonMainIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
