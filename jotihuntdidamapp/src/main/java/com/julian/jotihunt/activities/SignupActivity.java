package com.julian.jotihunt.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.julian.jotihunt.logics.AppController;
import com.julian.jotihunt.logics.Credentials;
import com.julian.jotihunt.logics.DataManager;
import com.julian.jotihunt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    Context context = this;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_invite) EditText _inviteText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);
        final String server_ip = this.getString(R.string.server_ip);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(this.getString(R.string.creating_account));
        progressDialog.show();

        Credentials.saveUsername(context, _emailText.getText().toString());
        Credentials.savePassword(context, _passwordText.getText().toString());
        DataManager.setName(_nameText.getText().toString());
        DataManager.setInvitecode(_inviteText.getText().toString());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String tag_json_obj = "json_obj_req";
                        String url = server_ip + "register";

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
                                Log.d("Error boolean", DataManager.getError().toString());

                                if (!DataManager.getError()) {
                                    //Starting profile activity
                                    // Start the Signup activity
                                    onSignupSuccess();
                                    progressDialog.dismiss();
                                } else {
                                    //If the server response is not success
                                    //Displaying an error message on toast
                                    onSignupFailed();
                                    progressDialog.hide();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG + "error 1", "Error: " + error.getMessage());
                                Log.d(TAG + "error 2", "" + error.getMessage() + "," + error.toString());
                                progressDialog.hide();
                                _signupButton.setEnabled(true);
                                Toast.makeText(context, context.getString(R.string.server_connection_failed), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("name", DataManager.getName());
                                params.put("email", Credentials.loadUsername(context));
                                params.put("password", Credentials.loadPassword(context));
                                params.put("invitecode", DataManager.getInvitecode());
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


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), this.getString(R.string.login_failed), Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(this.getString(R.string.validate_name));
            valid = false;
        } else {
            _nameText.setError(null);
        }

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