package com.villagemilk.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.login.ActivityLogin;
import com.villagemilk.beans.User;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class LoginOptionsActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginOptionsActivity";

    private TextView tvFacebook;

    private TextView tvGoogle;

    private TextView tvSignIn;

    private TextView tvSignUp;

    private ImageView ivLoginPic;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean fbClicked;

    //Google Login
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /**
     * True if the sign-in button was clicked.  When true, we know to resolve all
     * issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;

    /**
     * True if we are in the process of resolving a ConnectionResult
     */
    private boolean mIntentInProgress;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /**
     * To check wether user has the updated Google Play Services or not
     * else show him the dialog to redirect to play store to update it
     */
    private static final String SUCCESS = "SUCCESS";

    private static String accessToken = null;

    CallbackManager callbackManager;

    private String userName;
    private String email;
    private String profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        tvFacebook = (TextView) findViewById(R.id.tvFacebook);

        tvGoogle = (TextView) findViewById(R.id.tvGoogle);

        tvSignIn = (TextView) findViewById(R.id.tvSignIn);

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);

        ivLoginPic = (ImageView) findViewById(R.id.ivLoginPic);

        ivLoginPic.setBackgroundResource(R.drawable.login_screen_image_anim);
        AnimationDrawable anim = (AnimationDrawable) ivLoginPic.getBackground();
        anim.setOneShot(false);
        anim.start();

//        try {
//            mixpanel.track("Walkthrough Screen");
//        } catch (Exception e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        initViews();
    }

    private void initViews() {
        SpannableString spannableString = new SpannableString(tvSignUp.getText());
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.ninja_blue_2)), 10, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUp.setText(spannableString);
        
        tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbClicked = true;
                mSignInClicked = false;
                if (isNetworkAvailable()) {
                    callbackManager = CallbackManager.Factory.create();
                    LoginManager.getInstance().logInWithReadPermissions(LoginOptionsActivity.this, Arrays.asList("public_profile", "email"));
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            getDataFromAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(LoginOptionsActivity.this, "Facebook Login canceled by User", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            alert("Network Error");
                            hideDialog();
                        }
                    });
                } else {
                    hideDialog();
                    Toast.makeText(LoginOptionsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbClicked = false;
                mSignInClicked = true;
                if (isNetworkAvailable()) {
                    mGoogleApiClient.connect();
                } else {
                    hideDialog();
                    Toast.makeText(LoginOptionsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fbClicked == true) {
            super.onActivityResult(requestCode, resultCode, data);
            showDialog();

            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (mSignInClicked == true) {
            if (requestCode == RC_SIGN_IN) {
                if (resultCode != RESULT_OK) {
                    mSignInClicked = false;
                }

                mIntentInProgress = false;

                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.reconnect();
                }
            }
        }
    }

    private void getDataFromAccessToken(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object == null) {
                    alert("Connection Error");
                }

                userName = object.optString("name");
                email = object.optString("email");
                profilePicUrl = "http://graph.facebook.com/" + object.optString("id") + "/picture?type=large";

                editor.putString(Constants.USER_NAME, userName);
                editor.putString(Constants.USER_EMAIL, email);
                editor.putString(Constants.PROFILE_PIC_URL, profilePicUrl);
                editor.apply();

                if (!TextUtils.isEmpty(email)){
                    loginUser();
                } else {
                    Toast.makeText(LoginOptionsActivity.this, "Email is mandatory. Please share your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "id,name,email");
        request.setParameters(params);
        request.executeAsync();
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            Log.e("accountName", accountName);

            String scope = "oauth2:" + Scopes.PLUS_LOGIN + " ";

            accessToken = null;

            if (ActivityCompat.checkSelfPermission(LoginOptionsActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                try {
                    accessToken = GoogleAuthUtil.getToken(getApplicationContext(), Plus.AccountApi.getAccountName(mGoogleApiClient), scope, null);
                    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                        userName = currentPerson.getDisplayName();
                        email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                        profilePicUrl = currentPerson.getImage().getUrl();

                        Log.d(TAG, "User Info : " + userName);
                    }
                } catch (IOException transientEx) {
                    // network or server error, the call is expected to succeed if you try again later.
                    // Don't attempt to call again immediately - the request is likely to
                    // fail, you'll hit quotas or back-off.
                    Log.e(TAG, "GOOGLE+ IOException : " + transientEx.getMessage());
                    return null;
                } catch (UserRecoverableAuthException e) {
                    // Recover
                    startActivityForResult(e.getIntent(), RC_SIGN_IN);
                    Log.e(TAG, "GOOGLE+ IOException : " + e.getMessage());
                    accessToken = null;
                } catch (GoogleAuthException authEx) {
                    // Failure. The call is not expected to ever succeed so it should not be
                    // retried.
                    Log.e(TAG, "GOOGLE+ AuthEx : " + authEx.getMessage());
                    return null;
                } catch (Exception e) {
                    Log.e(TAG, "GOOGLE+ IOException : " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            return accessToken;
        }

        @Override
        protected void onPostExecute(String s) {
            accessToken = s;
            Log.d(TAG, "Google Access Token : " + s);
            if (accessToken != null && accessToken.length() != 0) {
                loginUser();
            }
        }
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


    public void showGoogleAccounts() {
        // Check if the Google Accounts permission is granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // Google Accounts permission has not been granted.
            requestGetAccountsPermission();
        } else {
            // Google Accounts permissions is already available.
            new RetrieveTokenTask().execute(Plus.AccountApi.getAccountName(mGoogleApiClient));
        }
    }

    private void requestGetAccountsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Toast.makeText(LoginOptionsActivity.this, R.string.permission_get_accounts_rationale, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, Constants.GET_ACCOUNTS);
        } else {
            // Google Accounts permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, Constants.GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.GET_ACCOUNTS) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for Google Accounts permission.
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Google Accounts permission has been granted, preview can be displayed
                showDialog();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                new RetrieveTokenTask().execute(Plus.AccountApi.getAccountName(mGoogleApiClient));
            } else {
                Toast.makeText(LoginOptionsActivity.this, R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        showGoogleAccounts();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            if (mSignInClicked && result.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.

                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //API Calls
    private void loginUser(){
        if(isNetworkAvailable()){
            final String url = Constants.API_PROD + "/user/login";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());
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
                            Intent intent = new Intent(LoginOptionsActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginOptionsActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                        }

                        finish();
                    } else if(status == 2){
                        hideDialog();
                        Toast.makeText(LoginOptionsActivity.this, "No such user exists!", Toast.LENGTH_SHORT).show();
                    } else if(status == 3){
                        hideDialog();
                        Toast.makeText(LoginOptionsActivity.this, "Username and password do not match", Toast.LENGTH_SHORT).show();
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
                        jsonObject.put("userName", userName);
                        jsonObject.put("email", email);
                        jsonObject.put("profilePic", profilePicUrl);
                        jsonObject.put("deviceType", "ANDROID");

                        if(fbClicked){
                            jsonObject.put("accountType", "FACEBOOK");
                        } else {
                            jsonObject.put("accountType", "GOOGLE");
                        }

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
            Toast.makeText(LoginOptionsActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
}
