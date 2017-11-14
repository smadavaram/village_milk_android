package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.login.ActivityLogin;
import com.villagemilk.util.Constants;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.villagemilk.view.ActivityBuildingList;

public class SplashActivity extends BaseActivity {
    public static final String TAG = "Splash Activity";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isServerNotif = getIntent().getBooleanExtra("serverNotif",false);

      /*  Tracker t = ((BaseApplication)getApplication()).getTracker(BaseApplication.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Splash Screen");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        try {
            mixpanel.track("Splash Screen");
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
*/
        if(isServerNotif){
//            try {
//                mixpanel.track("Server Notification Opened");
//            } catch (Exception e) {
//                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
                editor = sharedPreferences.edit();

                if(sharedPreferences.contains(Constants.STATUS)){
                    status = sharedPreferences.getInt(Constants.STATUS, 0);
                } else {
                    editor.putInt(Constants.STATUS, 0);
                    editor.apply();
                }

                showActivity(status);
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    private void showActivity(int status){
        if(status == 1){

            final Uri uri;
            int id=0;
            String deeplnkMsg = null;
            if(getIntent()!=null) {
                try {
                    uri = getIntent().getData();
                    if(uri != null) {
                        id = Integer.parseInt(uri.getPathSegments().get(0));
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (id != 0)
                    deeplnkMsg = id+"";
            }

            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            if(deeplnkMsg!=null)intent.putExtra("deeplnk",deeplnkMsg);
            startActivity(intent);
        } else if(status == 2){
            Intent intent = new Intent(SplashActivity.this, ActivityBuildingList.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, ActivityLogin.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Returns true if the push notification was for Apptentive, and we handled it.
//            shown = Apptentive.handleOpenedPushNotification(this);
        }
    }

}