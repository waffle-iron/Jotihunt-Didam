package com.julian.jotihuntdidam.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.julian.jotihuntdidam.Logics.AppController;
import com.julian.jotihuntdidam.Logics.Credentials;
import com.julian.jotihuntdidam.Logics.DataManager;
import com.julian.jotihuntdidam.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    Context context = this;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    @InjectView(R.id.chk_login)
    CheckBox _savelogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        // Insert default mail and password
        if (Credentials.loadRememberPreference(context)) {
            _emailText.setText(Credentials.loadUsername(context));
            _passwordText.setText(Credentials.loadPassword(context));
            _savelogin.setChecked(true);
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void loginAfterRegister() {
        Credentials.saveRememberPreference(context, _savelogin.isChecked());
        Credentials.saveUsername(context, _emailText.getText().toString());
        Credentials.savePassword(context, _passwordText.getText().toString());
        _loginButton.setEnabled(false);

        final String server_ip = this.getString(R.string.server_ip);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(this.getString(R.string.authenticating));
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String tag_json_obj = "json_obj_req";
                        String url = server_ip + "login";

                        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG + "good", response);
                                try {
                                    //Do it with this it will work
                                    JSONObject login = new JSONObject(response);
                                    DataManager.setError(login.getBoolean("error"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Error bolean", DataManager.getError().toString());

                                if (!DataManager.getError()) {
                                    //Login succesfull without error in mail or pw
                                    progressDialog.dismiss();
                                    onLoginSuccess();
                                } else {
                                    //Login failed with error in mail or pw
                                    onLoginFailed();
                                    progressDialog.hide();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG + "error 1", "Error: " + error.getMessage());
                                Log.d(TAG + "error 2", "" + error.getMessage() + "," + error.toString());
                                progressDialog.hide();
                                _loginButton.setEnabled(true);
                                Toast.makeText(context, context.getString(R.string.server_connection_failed), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("email", Credentials.loadUsername(context));
                                params.put("password", Credentials.loadPassword(context));
                                Log.d("Input ", params.toString());
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                return headers;
                            }
                        };
                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
                    }

                }, 3000);
    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            return;
        }

        Credentials.saveRememberPreference(context, _savelogin.isChecked());
        if (_savelogin.isChecked()) {
            Credentials.saveUsername(context, _emailText.getText().toString());
            Credentials.savePassword(context, _passwordText.getText().toString());
        } else {
            Credentials.clearUsername(context);
            Credentials.clearPassword(context);
        }
        _loginButton.setEnabled(false);

        final String server_ip = this.getString(R.string.server_ip);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(this.getString(R.string.authenticating));
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String tag_json_obj = "json_obj_req";
                        String url = server_ip + "login";

                        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG + "good", response);
                                try {
                                    //Do it with this it will work
                                    JSONObject login = new JSONObject(response);
                                    DataManager.setError(login.getBoolean("error"));
                                    DataManager.setName(login.getString("name"));
                                    Credentials.saveUsername(context, login.getString("email"));
                                    DataManager.setApi_key(login.getString("apiKey"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Error boolean", DataManager.getError().toString());

                                if (!DataManager.getError()) {
                                    //Starting profile activity
                                    // Start the Signup activity
                                    progressDialog.dismiss();
                                    onLoginSuccess();
                                } else {
                                    //If the server response is not success
                                    //Displaying an error message on toast
                                    onLoginFailed();
                                    progressDialog.hide();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG + "error 1", "Error: " + error.getMessage());
                                Log.d(TAG + "error 2", "" + error.getMessage() + "," + error.toString());
                                progressDialog.hide();
                                _loginButton.setEnabled(true);
                                Toast.makeText(context, context.getString(R.string.server_connection_failed), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("email", Credentials.loadUsername(context));
                                params.put("password", Credentials.loadPassword(context));
                                Log.d("Input ", params.toString());
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                return headers;
                            }
                        };
                    // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
                    }

                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
            loginAfterRegister();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Log.i(TAG, "Succesvol ingelogd");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Toast.makeText(context, this.getString(R.string.login_succesfull), Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
        Log.i(TAG, "Fout bij inloggen");
        Toast.makeText(context, this.getString(R.string.login_failed_pw), Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(this.getString(R.string.validate_mail));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 40) {
            _passwordText.setError(this.getString(R.string.validate_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}