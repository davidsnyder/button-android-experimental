package io.button.activity;

import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.button.R;
import io.button.base.BaseActionBarActivity;

public class SignUpOrLoginActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_login);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_login)
    void goToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.button_sign_up)
    void goToSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
