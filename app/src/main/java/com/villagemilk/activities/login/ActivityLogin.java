package com.villagemilk.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.AddressListActivity;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.beans.User;
import com.villagemilk.beans.signup.SignUpBean;
import com.villagemilk.util.Constants;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.gson.Gson;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueButton;
import com.truecaller.android.sdk.TrueClient;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.villagemilk.view.ActivityBuildingList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arsh on 4/11/16.
 */

public class ActivityLogin extends BaseActivity implements ITrueCallback{


    private static final String TAG = "ActivityLogin";
    TrueClient mTrueClient;
    TrueButton trueButton;

    EditText etPhoneNumber;
//    EditText etEmail;

//    private ImageView ivLoginPic;

    Button btnLogin;

    boolean isUserVerified;

    SharedPreferences sharedPreferences;

    User user;

    public static int APP_REQUEST_CODE = 99;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_activity);

        mTrueClient = new TrueClient(this, this);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        user = new User();

        initViews();

        initListeners();

    }

    public void initViews(){


        trueButton = ((TrueButton) findViewById(R.id.com_truecaller_android_sdk_truebutton));

        etPhoneNumber = ((EditText) findViewById(R.id.etPhoneNumber));

//        etEmail= ((EditText) findViewById(R.id.etEmail));

        btnLogin = (Button)findViewById(R.id.btnlogin);

//        ivLoginPic = (ImageView) findViewById(R.id.ivLoginPic);

//        ivLoginPic.setBackgroundResource(R.drawable.login_screen_image_anim);
//        AnimationDrawable anim = (AnimationDrawable) ivLoginPic.getBackground();
//        anim.setOneShot(false);
//        anim.start();

        trueButton.setTrueClient(mTrueClient);

        if(trueButton.isUsable())
            trueButton.setVisibility(View.VISIBLE);



    }


    public void initListeners(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                user = new User();
//                user.setMobileNumber(etPhoneNumber.getText().toString());
//                checkUser();

//                isUserVerified = true;
//                user.setMobileNumber(etPhoneNumber.getText().toString());

                if(isUserVerified)
                    checkUser();
//                    loginUser();
                else if(isValidMobile(etPhoneNumber.getText().toString())) {

//                    if(isValidEmail(etEmail.getText())) {

                        onLoginPhone(etPhoneNumber.getText().toString());

//                    }else{

//                        Toast.makeText(ActivityLogin.this,"Enter valid email",Toast.LENGTH_LONG).show();

//                    }

                }else{

                    Toast.makeText(ActivityLogin.this,"Enter valid number",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onSuccesProfileShared(@NonNull TrueProfile trueProfile) {


        String number = trueProfile.phoneNumber;
        if(trueProfile.phoneNumber.contains("+91") && trueProfile.phoneNumber.length() == 13)
            number = number.substring(3);

        user.setMobileNumber(number);
//        user.setEmail(trueProfile.email);

        etPhoneNumber.setText(number);
//        etEmail.setText(trueProfile.email);

        etPhoneNumber.setEnabled(false);
//        etEmail.setEnabled(false);

        isUserVerified = true;

    }

    @Override
    public void onFailureProfileShared(@NonNull TrueError trueError) {

        Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (mTrueClient.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
//                showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format("Success:%s...",loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
//                goToMyLoggedInActivity();

                user.setFirstName("");
                user.setMobileNumber(etPhoneNumber.getText().toString());
//                user.setEmail(etEmail.getText().toString());

//                loginUser();
                checkUser();

            }

            // Surface the result to your user in an appropriate way.
//            Toast.makeText(this,toastMessage,Toast.LENGTH_LONG).show();

        }
    }

    private void checkUser(){

        showDialog();
        if(isNetworkAvailable()){
            final String url = Constants.API_PROD + "/user/findByMobile";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Response : ",response.toString());

                    Gson gson = new Gson();

                    BaseApplication.getInstance().setUser(user);


                    SignUpBean signUpBean = gson.fromJson(response.toString(),SignUpBean.class);

                    Log.e("SignUp",signUpBean.toString());

                    Intent intent;

                    switch (signUpBean.getStatus()){

                        case 0:
                            intent = new Intent(ActivityLogin.this,ActivitySignUp.class);
                            startActivity(intent);
                            break;

                        case 1:
                            // check for the status codes
                            finishLoginFlow(signUpBean.getUser());
                            break;

                        case 2:
                            // Go to the email entry screen
                            intent = new Intent(ActivityLogin.this,ActivityMultipleUser.class);
                            startActivity(intent);

                            break;


                    }

                    finish();

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

                        jsonObject.put("mobileNumber","+1"+user.getMobileNumber());

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
            Toast.makeText(ActivityLogin.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }



/*
    private void loginUser(){
        showDialog();
        if(isNetworkAvailable()){
            final String url = Constants.API_PROD + "/user/login";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());
                    hideDialog();
                    int status = 0;
                    JSONObject jsonObject;
                    Gson gson = new Gson();
                    User user=null;

                    try {
                        status = response.getInt("status");
                        jsonObject = response.getJSONObject("user");

                        if(jsonObject != null){
                            user = gson.fromJson(String.valueOf(jsonObject), User.class);

                            BaseApplication.getInstance().setUser(user);

                            editor.putString(Constants.USER_ID, user.getId());
                            editor.putString(Constants.USER_NAME, user.getUserName());
                            editor.putString(Constants.USER_EMAIL, user.getEmail());

                            String profilePicUrl = user.getProfilePic();

                            if(profilePicUrl != null){
                                editor.putString(Constants.PROFILE_PIC_URL, profilePicUrl);
                            }

                            if(user.getStatus() != 0){
                                editor.putInt(Constants.STATUS, user.getStatus());
                            } else {
                                editor.putInt(Constants.STATUS, 2);
                            }

                            editor.apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(status == 1 || status == 4){
                        if(user != null && user.getStatus() == 1){
                            Intent intent = new Intent(ActivityLogin.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ActivityLogin.this, UserProfileActivity.class);
                            startActivity(intent);
                        }

                        finish();
                    } else if(status == 2){
                        hideDialog();
                        Toast.makeText(ActivityLogin.this, "No such user exists!", Toast.LENGTH_SHORT).show();
                    } else if(status == 3){
                        hideDialog();
                        Toast.makeText(ActivityLogin.this, "Username and password do not match", Toast.LENGTH_SHORT).show();
                    }
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
                        jsonObject.put("userName", user.getFirstName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("mobileNumber", user.getMobileNumber());
                        jsonObject.put("profilePic", "");
                        jsonObject.put("deviceType", "ANDROID");

//                        if(fbClicked){
//                            jsonObject.put("accountType", "FACEBOOK");
//                        } else {
//                            jsonObject.put("accountType", "GOOGLE");
//                        }

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
            Toast.makeText(ActivityLogin.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
*/

    private boolean isValidMail(String email){

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private boolean isValidMobile(String phone){

        return android.util.Patterns.PHONE.matcher(phone).matches();

    }

    public void onLoginPhone(String number) {

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        configurationBuilder.setInitialPhoneNumber(new PhoneNumber("+1",number));
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }


    public void finishLoginFlow(User user){

        Intent intent;

        BaseApplication.getInstance().setUser(user);

        editor.putString(Constants.USER_ID, user.getId());
        String id = user.getId();
        editor.putString(Constants.USER_NAME, user.getUserName());
        editor.putString(Constants.USER_EMAIL, user.getEmail());

        if(user.getStatus() !=null)
            editor.putInt(Constants.STATUS, user.getStatus());

        editor.putInt(Constants.USER_STATUS, user.getUserStatus());
        editor.apply();


        switch (user.getUserStatus()){

            case 0:

            case 1:

            case 2:

            case 3:
                intent = new Intent(ActivityLogin.this,ActivityBuildingList.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(ActivityLogin.this,HomeActivity.class);
                startActivity(intent);

                break;

        }



    }

}
