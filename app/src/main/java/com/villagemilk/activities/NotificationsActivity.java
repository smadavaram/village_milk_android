package com.villagemilk.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.NotificationsListAdapter;
import com.villagemilk.beans.Notification;
import com.villagemilk.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends BaseActivity {
    private static final String TAG = "Notifications Activity";

    private ListView lvNotifications;

    private NotificationsListAdapter notificationsListAdapter;

    private List<Notification> notificationList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initSimpleToolbar();

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        fetchNotifications();
    }

    private void initViews(){
        notificationsListAdapter = new NotificationsListAdapter(NotificationsActivity.this, notificationList);
        lvNotifications.setAdapter(notificationsListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void fetchNotifications() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/notification/findByUser";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());

                    initViews();
                    hideDialog();
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
                        jsonObject.put("id", sharedPreferences.getString(Constants.USER_ID, ""));
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
            Toast.makeText(NotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
