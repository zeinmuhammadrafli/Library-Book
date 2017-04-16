/**
 * agus 2014
 **/
package id.co.visionet.bookinventory.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.visionet.bookinventory.MainActivity;
import id.co.visionet.bookinventory.R;
import id.co.visionet.bookinventory.helper.SessionManagement;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_email)
    EditText editEmail;
    @BindView(R.id.input_password)
    EditText editPassword;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.chkRememberMe)
    CheckBox chkRememberMe;
    SessionManagement session;
    HashMap<String, String> activeUser;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new SessionManagement(this);

        if (session.getRememberMe()) {
            activeUser = session.getActiveInformation();

            editEmail.setText(activeUser.get(SessionManagement.KEY_EMAIL));
            editPassword.setText(activeUser.get(SessionManagement.KEY_PASSWORD));
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // login simulation
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    public void onLoginSuccess() {
        session.setActiveInformation(editEmail.getText().toString(), editPassword.getText().toString(), chkRememberMe.isChecked());
        session.setKeyIsLoggedIn(true);
        _loginButton.setEnabled(true);
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editEmail.setError(null);
        }

        if (password.isEmpty()) {
            editPassword.setError("enter password");
            valid = false;
        } else if (password.length() < 5) {
            editPassword.setError("password minimal 5 characters");
            valid = false;
        } else {
            editPassword.setError(null);
        }

        return valid;
    }
}
