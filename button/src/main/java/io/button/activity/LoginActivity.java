//package io.button.activity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//import com.parse.LogInCallback;
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;
//import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
//import io.button.R;
//import io.button.base.BaseActionBarActivity;
//
//public class LoginActivity extends BaseActionBarActivity {
//    @InjectView(R.id.edit_text_username)
//    EditText userNameEditText;
//
//    @InjectView(R.id.edit_text_password)
//    EditText passwordEditText;
//
//    @InjectView(R.id.progress_bar_waiting)
//    SmoothProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        ButterKnife.inject(this);
//    }
//
//    @OnClick(R.id.button_login)
//    void attemptLogin() {
//        // Set up a new Parse user
//        ParseUser user = new ParseUser();
//        user.setUsername(userNameEditText.getText().toString());
//        user.setPassword(passwordEditText.getText().toString());
//
//        // Call the parse login
//        progressBar.setVisibility(View.VISIBLE);
//        ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(),
//                new LogInCallback() {
//                    @Override
//                    public void done(ParseUser parseUser, ParseException e) {
//                        progressBar.setVisibility(View.GONE);
//                        if (e != null) {
//                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                        } else {
//                            // Start an intent for the dispatch activity
//                            Intent intent = new Intent(LoginActivity.this, ButtonMainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    }
//                });
//    }
//}
