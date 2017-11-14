package com.villagemilk.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.beans.User;
import com.villagemilk.beans.signup.SignUpBean;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;
import com.villagemilk.view.ActivityBuildingList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arsh on 17/11/16.
 */

public class ActivityMultipleUser extends BaseActivity {

    EditText etEmail;
    Button btnSubmit;

    User user;

    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.multiple_user);

        user = BaseApplication.getInstance().getUser();

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        setUpToolbar();
        initViews();

        initListeners();

    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter Email");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    private void initListeners() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                if(isValidEmail(email)){

                    user.setEmail(email);
                    loginUser(user);


                }else{

                    Toast.makeText(ActivityMultipleUser.this,"Enter Valid Email",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void initViews() {

        etEmail = (EditText)findViewById(R.id.etEmail);
        btnSubmit = (Button) findViewById(R.id.button);
        btnSubmit.setText("Submit");

    }

    private boolean isValidMail(String email){

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


    private void loginUser(final User user){

        showDialog();
        if(isNetworkAvailable()){
            final String url = Constants.API_PROD + "/user/findByEmailAndPhone";

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    hideDialog();
                    Log.e("Response : ",response.toString());

                    Gson gson = new Gson();

                    SignUpBean signUpBean = gson.fromJson(response.toString(),SignUpBean.class);

                    Log.e("SignUp",signUpBean.toString());

                    switch (signUpBean.getStatus()){

                        case 0:
                            Toast.makeText(ActivityMultipleUser.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            // check for the status codes
                            finishLoginFlow(signUpBean.getUser());
                            break;


                    }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    alert("Connection Error");

                    hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("mobileNumber", "+1"+user.getMobileNumber());
                        jsonObject.put("emailId",  user.getEmail());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
            Toast.makeText(ActivityMultipleUser.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    public void finishLoginFlow(User user){

        Intent intent;

        BaseApplication.getInstance().setUser(user);

        editor.putString(Constants.USER_ID, user.getId());
        String id = user.getId();
        editor.putString(Constants.USER_NAME, user.getUserName());
        editor.putString(Constants.USER_EMAIL, user.getEmail());

        if(user.getStatus()!=null)
            editor.putInt(Constants.STATUS, user.getStatus());

        editor.putInt(Constants.USER_STATUS, user.getUserStatus());

        editor.apply();


        switch (user.getUserStatus()){

            case 0:

            case 1:

            case 2:

            case 3:
                intent = new Intent(ActivityMultipleUser.this,ActivityBuildingList.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(ActivityMultipleUser.this,HomeActivity.class);
                startActivity(intent);

                break;

        }

        finish();

    }


}
