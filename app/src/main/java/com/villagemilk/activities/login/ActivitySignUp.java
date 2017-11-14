package com.villagemilk.activities.login;

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
import com.villagemilk.activities.AddressListActivity;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.beans.User;
import com.villagemilk.beans.signup.SignUpBean;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;
import com.villagemilk.view.ActivityBuildingList;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySignUp extends BaseActivity {
    private static final String TAG = "ActivitySignUp";

    private EditText etEmail;

    private EditText etName;

    private CardView cardView;
    private Button btnSelectAddress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userId;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter your details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = BaseApplication.getInstance().getUser();

        etEmail = (EditText) findViewById(R.id.etEmail);

        etEmail.findFocus();

        etName = (EditText) findViewById(R.id.etName);

        cardView = (CardView) findViewById(R.id.cardView);
        btnSelectAddress = (Button) cardView.findViewById(R.id.button);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        initViews();
    }

    private void initViews(){
        btnSelectAddress.setText("Proceed");

        userId = sharedPreferences.getString(Constants.USER_ID, "");


        if(sharedPreferences.getString(Constants.USER_EMAIL, "") != null && sharedPreferences.getString(Constants.USER_EMAIL, "").length() > 0){
            etEmail.setText(sharedPreferences.getString(Constants.USER_EMAIL, ""));
            etEmail.setEnabled(false);
            etEmail.setFocusable(false);
            etEmail.setClickable(false);
        }

        btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (etName.getText().toString() != null && etName.getText().length() != 0) {
                    if (etEmail.getText().toString() != null && isValidEmail(etEmail.getText().toString())) {


                        user.setEmail(etEmail.getText().toString());
                        user.setFirstName(etName.getText().toString());

                        signUp();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showAddressList(){
        hideDialog();

        Intent intent = new Intent(ActivitySignUp.this, ActivityBuildingList.class);

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

    private void signUp() {
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/user/userSignUp";
            Log.d(TAG, "Url : " + url);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Success : " + response.toString());

                    Gson gson = new Gson();

                    SignUpBean signUpBean = gson.fromJson(response,SignUpBean.class);

                    BaseApplication.getInstance().setUser(signUpBean.getUser());

                    editor.putString(Constants.USER_EMAIL, etEmail.getText().toString());

                    editor.putString(Constants.USER_ID, signUpBean.getUser().getId());


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
                        jsonObject.put("deviceType", "Android");
                        jsonObject.put("userName", user.getFirstName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("accountType", "FbAccountKit");
                        jsonObject.put("mobileNumber", "+1"+user.getMobileNumber());
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
            Toast.makeText(ActivitySignUp.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

}
