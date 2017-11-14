package com.villagemilk.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";

    private EditText etUserName;

    private EditText etEmail;

    private CardView cardView;
    private Button btnSignUp;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign Up");
        }

        etUserName = (EditText) findViewById(R.id.etUserName);

        etEmail = (EditText) findViewById(R.id.etEmail);

        cardView = (CardView) findViewById(R.id.cardView);
        btnSignUp = (Button) cardView.findViewById(R.id.button);
        btnSignUp.setText("SIGN UP");

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        initViews();
    }

    private void initViews(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                if (etUserName.getText() != null && etUserName.getText().length() != 0) {
                    if (etEmail.getText() != null && etEmail.getText().length() != 0 && isValidEmail(etEmail.getText())) {
//                        try {
//                            JSONObject props = new JSONObject();
//                            props.put("Email", etEmail.getText());
//                            mixpanel.track("Sign Up",props);
//                        } catch (Exception e) {
//
//                        }

                        signUpUser();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please provide valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide full Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUserDetailsActivity() {
        hideDialog();

        Intent intent = new Intent(this, UserProfileActivity.class);
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
    private void signUpUser(){
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/user/signUp";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());
//                    try {
//                        int status = response.getInt("status");
//                        String userId = response.getString("id");
//                        User user = new Gson().fromJson(response.getString("user"), User.class);
//
//                        BaseApplication.getInstance().setUser(user);
//
//                        editor.putString(Constants.USER_ID, userId);
//                        editor.putString(Constants.USER_NAME, etUserName.getText().toString());
//                        editor.putString(Constants.USER_EMAIL, etEmail.getText().toString());
//                        editor.putInt(Constants.STATUS, 2);
//                        editor.apply();
//
////                        if(status == 1){
////                            mixpanel.alias(sharedPreferences.getString(Constants.USER_ID, ""),null);
////                            mixpanel.getPeople().identify(sharedPreferences.getString(Constants.USER_ID, ""));
////                            mixpanel.getPeople().set(Constants.UserType, "Registered");
////                            mixpanel.getPeople().set("$email",etEmail.getText().toString());
////                            editor.putString(Constants.UserType,"Registered");
////                            editor.apply();
////
////                            showUserDetailsActivity();
////                        } else if(status == 2){
//                            hideDialog();
//                            Toast.makeText(SignUpActivity.this, "User with entered email already exists!", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userName", etUserName.getText().toString());
                        jsonObject.put("email", etEmail.getText().toString());
                        jsonObject.put("deviceType", "ANDROID");
                        jsonObject.put("accountType", "NINJA");
                        jsonObject.put("status", 2);
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
            Toast.makeText(SignUpActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
}
