package io.button.dagger;


import com.parse.ParseUser;

import dagger.Module;
import dagger.Provides;
import io.button.ButtonApplication;
import io.button.activity.ButtonMainActivity;
import io.button.activity.LoginActivity;
import io.button.activity.SignUpActivity;
import io.button.activity.SignUpOrLoginActivity;

@Module(
        complete = true,
        injects = {
                ButtonApplication.class,
                ButtonMainActivity.class,
                SignUpOrLoginActivity.class,
                SignUpActivity.class,
                LoginActivity.class
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
}
