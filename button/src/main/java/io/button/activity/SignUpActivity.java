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
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;
//import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
//import io.button.R;
//import io.button.base.BaseActionBarActivity;
//
//public class SignUpActivity extends BaseActionBarActivity {
//    @InjectView(R.id.edit_text_username)
//    EditText userNameEditText;
//
//    @InjectView(R.id.edit_text_password)
//    EditText passwordEditText;
//
//    @InjectView(R.id.edit_text_password)
//    EditText passwordAgainEditText;
//
//    @InjectView(R.id.progress_bar_waiting)
//    SmoothProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        ButterKnife.inject(this);
//    }
//
//    @OnClick(R.id.button_sign_up)
//    void attemptSignUp() {
//        if (!isMatching(passwordEditText, passwordAgainEditText)) {
//            Toast.makeText(SignUpActivity.this, "passwords don't match", Toast.LENGTH_LONG);
//        } else {
//            // Set up a new Parse user
//            ParseUser user = new ParseUser();
//            user.setUsername(userNameEditText.getText().toString());
//            user.setPassword(passwordEditText.getText().toString());
//
//            // Call the Parse signup method
//            progressBar.setVisibility(View.VISIBLE);
//            user.signUpInBackground(new SignUpCallback() {
//                @Override
//                public void done(ParseException e) {
//                    progressBar.setVisibility(View.GONE);
//                    if (e != null) {
//                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        // Start an intent for the dispatch activity
//                        Intent intent = new Intent(SignUpActivity.this, ButtonMainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
//            });
//        }
//    }
//
//    private boolean isMatching(EditText etText1, EditText etText2) {
//        if (etText1.getText().toString().equals(etText2.getText().toString())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
