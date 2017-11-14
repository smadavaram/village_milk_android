package com.villagemilk.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.util.CustomProgressDialog;
import com.villagemilk.util.Fonts;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;


import org.json.JSONArray;
import org.json.JSONException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by sagaryarnalkar on 02/05/15.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public CustomProgressDialog customProgressDialog;

    protected Typeface robotoBold;
    protected Typeface robotoLight;
    protected Typeface robotoRegular;
    protected Typeface robotoMedium;

//    public MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFonts();

//        mixpanel = MixpanelAPI.getInstance(this, BaseApplication.mixpanelToken);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initFonts() {
        robotoBold = Fonts.getTypeface(this, Fonts.FONT_ROBOTO_BOLD_CONDENSED);
        robotoLight = Fonts.getTypeface(this, Fonts.FONT_ROBOTO_LIGHT);
        robotoMedium = Fonts.getTypeface(this, Fonts.ROBOTO_MEDIUM);
        robotoRegular = Fonts.getTypeface(this, Fonts.ROBOTO_REGULAR);
    }
    protected void showDialog() {
        if(customProgressDialog == null || !customProgressDialog.isShowing()) {
            customProgressDialog = new CustomProgressDialog(this);
//            customProgressDialog.setMessage("Loading....");
            customProgressDialog.setCancelable(false);
            customProgressDialog.show();
            dialogHandler=null;
            dialogTime = System.currentTimeMillis();
        }
    }

    Handler dialogHandler;
    long dialogTime = 0;
    static final long dialogMaxTime = 1500;

    protected void hideDialog() {
        if(customProgressDialog !=null && customProgressDialog.isShowing()) {
            if(System.currentTimeMillis()-dialogTime>dialogMaxTime) {
                customProgressDialog.dismiss();
                dialogHandler=null;
            } else {
                dialogHandler = new Handler();
                dialogHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogHandler=null;
                        customProgressDialog.dismiss();
                    }
                }, (int) (System.currentTimeMillis() - dialogTime));
            }
        }
    }

    @Override
    public void finish() {
        if(dialogHandler!=null)
            dialogHandler.removeCallbacksAndMessages(null);
        if(customProgressDialog !=null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        super.finish();
    }

    public void initSimpleToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            findViewById(R.id.ivBackBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            TextView tv = (TextView) findViewById(R.id.abTitle);
            tv.setTypeface(robotoBold);
            setSupportActionBar(toolbar);
        }
    }

    public void alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class MyPostJsonArrayRequest extends JsonRequest<JSONArray> {

        private JSONArray jsonArray;

        public MyPostJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, null, listener, errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            String jsonString = new String(response.data);
            try {
                jsonArray = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
//        GoogleAnalytics.getInstance(this).reportActivityStart(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
//        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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

}
