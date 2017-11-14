package com.villagemilk.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.User;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends BaseActivity {
    private static final String TAG = "SignInActivity";

    private EditText etEmail;

    private EditText etPin;

    private TextView tvResendPin;

    private CardView cardView;
    private Button btnSignIn;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign In");
        }

        etEmail = (EditText) findViewById(R.id.etEmail);

        etPin = (EditText) findViewById(R.id.etPin);

        tvResendPin = (TextView) findViewById(R.id.tvResendPin);

        cardView = (CardView) findViewById(R.id.cardView);
        btnSignIn = (Button) cardView.findViewById(R.id.button);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        initViews();
    }

    private void initViews(){
        btnSignIn.setText("Get Login Pin");

        SpannableString spannableString = new SpannableString(tvResendPin.getText().toString());
        spannableString.setSpan(new UnderlineSpan(), 29, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.ninja_black)), 29, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvResendPin.setText(spannableString);

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                if(etPin.getVisibility() == View.VISIBLE && etPin.getText() != null && etPin.getText().length() > 0){
                    loginUser();
                } else {
                    if (etEmail.getText() != null && etEmail.getText().length() != 0 && isValidEmail(etEmail.getText())) {
                        getLoginPin();
                    } else {
                        Toast.makeText(SignInActivity.this, "Please provide valid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvResendPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText() != null && etEmail.getText().length() != 0 && isValidEmail(etEmail.getText())) {
                    getLoginPin();
                } else {
                    Toast.makeText(SignInActivity.this, "Please provide valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUserDetailsActivity() {
        hideDialog();

        Intent intent = new Intent(SignInActivity.this, UserProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void loginUser() {
        if(isNetworkAvailable()) {
            showDialog();

            final String url = Constants.API_PROD + "/user/login";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());
                    int status = 0;
                    JSONObject jsonObject;
                    Gson gson = new Gson();

                    try {
                        status = response.getInt("status");
                        jsonObject = response.getJSONObject("user");

                        if(jsonObject != null){
                            User user = gson.fromJson(String.valueOf(jsonObject), User.class);

                            BaseApplication.getInstance().setUser(user);

                            editor.putString(Constants.USER_ID, user.getId());
                            String id = user.getId();
                            editor.putString(Constants.USER_NAME, user.getUserName());
                            editor.putString(Constants.USER_EMAIL, user.getEmail());

                            if(user.getProfilePic() != null){
                                editor.putString(Constants.PROFILE_PIC_URL, user.getProfilePic());
                            }

                            editor.putInt(Constants.STATUS, user.getStatus());
                            editor.apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(status == 1) {

                        showUserDetailsActivity();
                    } else if(status == 2){

                        hideDialog();
                        Toast.makeText(SignInActivity.this, "No such user exists!", Toast.LENGTH_SHORT).show();
                    } else if(status == 3){

                        hideDialog();
                        Toast.makeText(SignInActivity.this, "Username and password do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("email", etEmail.getText().toString());
                        jsonObject.put("password", etPin.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Body : " + jsonObject.toString());
                    return jsonObject.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(SignInActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    private void getLoginPin() {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/user/getPassword";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());

                    try {
                        int status = response.getInt("status");

                        if(status == 1) {
                            etPin.setVisibility(View.VISIBLE);
                            btnSignIn.setText("Sign In");

                            etPin.requestFocus();

                            hideDialog();
                        } else if (status == 2) {
                            hideDialog();
                            Toast.makeText(SignInActivity.this, "Please Enter Registered Email", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");

                    hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("email", etEmail.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Body : " + jsonObject.toString());
                    return jsonObject.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(SignInActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
}
