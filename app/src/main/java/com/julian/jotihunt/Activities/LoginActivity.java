package com.julian.jotihunt.Activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.julian.jotihunt.Logics.AppController;
import com.julian.jotihunt.Logics.DataManager;
import com.julian.jotihunt.Logics.SessionManager;
import com.julian.jotihunt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    int errorr = 0;
    String errormessage;
    SessionManager session;


    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        // Session manager
        session = new SessionManager(getApplicationContext());

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

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        DataManager.setMail(_emailText.getText().toString());
        DataManager.setPassword(_passwordText.getText().toString());

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed


                        String tag_json_obj = "json_obj_req";
                        String url = "http://145.116.203.82/api/v1/login";


                        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG + "good", response.toString());
                                try {
                                    //Do it with this it will work
                                    JSONObject login = new JSONObject(response);
                                    DataManager.setError(login.getBoolean("error"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Error bolean", DataManager.getError().toString());

                                if (!DataManager.getError()) {

                                    //Starting profile activity
                                    // Start the Signup activity
                                    onLoginSuccess();
                                    progressDialog.hide();
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
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", DataManager.getMail());
                                params.put("password", DataManager.getPassword());
                                Log.d("Input ", params.toString());
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
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

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Log.i(TAG, "Succesvol ingelogd");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Log.i(TAG, "Start intent mainactivity");
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), errormessage, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Fout bij inloggen");
        _loginButton.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }




}
