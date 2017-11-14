package com.villagemilk.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.HomeBean;
import com.villagemilk.util.Constants;
import com.freshdesk.hotline.Hotline;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.iid.InstanceID;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sagaryarnalkar on 08/05/16.
 */
public class RegistrationIntentService extends IntentService {

    private HomeBean homeBean;
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    boolean isPushTokenSet;
    Bundle extras ;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
//            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            String authorizedEntity = BaseApplication.google_project_no; // Project id from Google Developers Console
            String scope = "GCM";
            // e.g. communicating using GCM, but you can use any
            // URL-safe characters up to a maximum of 1000, or
            // you can also leave it blank.

            try {
                String token = FirebaseInstanceId.getInstance().getToken(getString(R.string.GCM_SENDER_ID), FirebaseMessaging.INSTANCE_ID_SCOPE);

//            String token = FirebaseInstanceId.getInstance().getToken(authorizedEntity,scope);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                isPushTokenSet = intent.getBooleanExtra("isPushTokenSet", false);

                if (!isPushTokenSet || !sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false)) {
                    sendRegistrationToServer(token);
                }

           /* if(!sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER,true)){
                sendRegistrationToServer(token);
            }*/


                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                // sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();

            } catch (IOException e) {
                e.printStackTrace();

            }

//            Localytics.setPushRegistrationId(token);
            //Apptentive.setPushNotificationIntegration(Apptentive.PUSH_PROVIDER_AMAZON_AWS_SNS, token);
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
//        String registrationId

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    /**
     * Persist registration to third-party servers.
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        try {
            // SEND TO DAILYNINJA API
            final String userId = sharedPreferences.getString(Constants.USER_ID, null);
//            Apptentive.setPushNotificationIntegration(this,Apptentive.PUSH_PROVIDER_APPTENTIVE, token);
            sharedPreferences.edit().putString(Constants.GCM_TOKEN, token).apply();

            Hotline.getInstance(this).updateGcmRegistrationToken(token);

            if(userId==null)
                return;

                String url = Constants.API_PROD + "/user/updateAndroidPushToken";
                Log.d(TAG, "Url : " + url);

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success : " + response.toString());
                        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error : " + error.toString());
                        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
                    }
                }
                ) {
                    @Override
                    public byte[] getBody() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", userId);
                            jsonObject.put("androidPushToken", token);
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

        }catch(Exception ex)
        {

        }
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}