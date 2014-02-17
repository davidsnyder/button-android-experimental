package io.button.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import io.button.dagger.Injector;

public class BaseActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(), "onCreate");

        // Inject this activity into the object graph
        Injector.inject(this, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "onDestroy");
    }
}
