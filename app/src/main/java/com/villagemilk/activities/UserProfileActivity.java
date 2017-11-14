package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.view.ActivityBuildingList;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends BaseActivity {
    private static final String TAG = "ActivitySignUp";

    private EditText etEmail;

    private EditText etName;

    private CardView cardView;
    private Button btnSelectAddress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter phone number");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = (EditText) findViewById(R.id.etEmail);

        etName = (EditText) findViewById(R.id.etName);

        cardView = (CardView) findViewById(R.id.cardView);
        btnSelectAddress = (Button) cardView.findViewById(R.id.button);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        initViews();
    }

    private void initViews(){
        btnSelectAddress.setText("Update Mobile Number");

        userId = sharedPreferences.getString(Constants.USER_ID, "");

//        try {
//            mixpanel.track("User Profile Screen");
//        } catch (Exception e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }

        if(sharedPreferences.getString(Constants.USER_EMAIL, "") != null && sharedPreferences.getString(Constants.USER_EMAIL, "").length() > 0){
            etEmail.setText(sharedPreferences.getString(Constants.USER_EMAIL, ""));
            etEmail.setEnabled(false);
            etEmail.setFocusable(false);
            etEmail.setClickable(false);
        }

        if(BaseApplication.getInstance().getUser() != null){
            etName.setText(BaseApplication.getInstance().getUser().getMobileNumber());
            etName.setSelection(etName.getText().length());
        }

        btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etEmail.getText().toString() != null && isValidEmail(etEmail.getText().toString())) {
                    if (etName.getText().toString() != null && etName.getText().length() != 0) {
//                        try {
//                            JSONObject props = new JSONObject();
//                            props.put("Name", etName.getText());
//                            mixpanel.track("Name Updated", props);
//                        } catch (Exception e) {
//
//                        }

                        updateUserDetails();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid Email", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showAddressList(){
        hideDialog();

        Intent intent = new Intent(UserProfileActivity.this, ActivityBuildingList.class);

        if(getIntent().getExtras() != null){
            String source = getIntent().getExtras().getString("intent_source");
            intent.putExtra("intent_source", source);
        }

        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void updateUserDetails() {
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/user/update/mobileNumber";
            Log.d(TAG, "Url : " + url);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Success : " + response.toString());
//                    try {
//                        if(userId!=null) {
//                            mixpanel.identify(userId);
//                            mixpanel.getPeople().identify(userId);
//
//                            if (sharedPreferences.getString(Constants.USER_EMAIL, "") != null) {
//                                mixpanel.getPeople().set("$email", sharedPreferences.getString(Constants.USER_EMAIL, ""));
//                            } else {
//                                mixpanel.getPeople().set("$email", etEmail.getText().toString());
//                            }
//
//                            mixpanel.getPeople().set("$name", sharedPreferences.getString(Constants.USER_NAME, ""));
//                            mixpanel.getPeople().set("$phone", etName.getText().toString());
//                            mixpanel.getPeople().set("$username", etName.getText().toString());
//
//                            JSONObject props = new JSONObject();
//                            props.put("User Email", sharedPreferences.getString(Constants.USER_NAME, ""));
//                            mixpanel.registerSuperProperties(props);
//                            String temp =  sharedPreferences.getString(Constants.UserType,"");
//
//                            if(temp.equals("Registered")) {
//                                mixpanel.getPeople().set(Constants.UserType,"Phone No Updated");
//                                editor.putString(Constants.UserType,"Phone No Updated");
//                                editor.apply();
//                            }
//                        }
//                    } catch(Exception exp){
//
//                    }

                    editor.putString(Constants.USER_EMAIL, etEmail.getText().toString());

                    if(sharedPreferences.getInt(Constants.STATUS, 0) != 1){
                        editor.putInt(Constants.STATUS, 2);
                    }

                    editor.apply();

                    showAddressList();
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
                        jsonObject.put("id", userId);
                        jsonObject.put("mobileNumber", etName.getText().toString());
                        jsonObject.put("email", etEmail.getText().toString());
                        jsonObject.put("profilePic", sharedPreferences.getString(Constants.PROFILE_PIC_URL, ""));
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

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(UserProfileActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
}
