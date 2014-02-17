package io.button.dagger;

import android.content.Context;
import android.util.Log;

public class Injector {

    public static boolean inject(Object object, Context context) {
        if (context.getApplicationContext() instanceof InjectableApplication) {
            InjectableApplication injectableApplication = (InjectableApplication)context.getApplicationContext();
            injectableApplication.inject(object);
            return true;
        } else {
            Log.wtf("Injector", object.getClass().getSimpleName() + " cannot be injected because it is not in a injectable application.");
            return false;
        }
    }
}
