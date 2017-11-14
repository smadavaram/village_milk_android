package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.SubscriptionListAdapter;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionListActivity extends BaseActivity {
    private static final String TAG = "SubscriptionListActivity";

    private RecyclerView rvSubscriptions;
    private SubscriptionListAdapter subscriptionListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ImageView ivEmptyBasket;
    private TextView tvNoSubscriptions;

    private List<SubscriptionMasterSmall> subscriptionMasterList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My subscriptions");
        }

        rvSubscriptions = (RecyclerView) findViewById(R.id.rvSubscriptions);

        ivEmptyBasket = (ImageView) findViewById(R.id.ivEmptyBasket);
        tvNoSubscriptions = (TextView) findViewById(R.id.tvNoSubscriptions);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        fetchSubscriptions();
    }

    private void initViews(){
        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvSubscriptions.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        rvSubscriptions.setLayoutManager(linearLayoutManager);


        if (subscriptionMasterList != null && subscriptionMasterList.size() > 0){
            subscriptionListAdapter = new SubscriptionListAdapter(this, subscriptionMasterList);
            rvSubscriptions.setAdapter(subscriptionListAdapter);

            rvSubscriptions.setVisibility(View.VISIBLE);
            ivEmptyBasket.setVisibility(View.GONE);
            tvNoSubscriptions.setVisibility(View.GONE);
        } else {
            rvSubscriptions.setVisibility(View.GONE);
            ivEmptyBasket.setVisibility(View.VISIBLE);
            tvNoSubscriptions.setVisibility(View.VISIBLE);
        }

        ivEmptyBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionListActivity.this, ProductCategoryListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvNoSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionListActivity.this, ProductCategoryListActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
    private void fetchSubscriptions() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/subscriptionMaster/listUserSubscriptions/" + sharedPreferences.getString(Constants.USER_ID, "");
            Log.d(TAG, "Url : " + url);

            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    Gson gson = new Gson();

                    subscriptionMasterList.clear();

                    if (response != null){
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                SubscriptionMasterSmall subscriptionMaster = gson.fromJson(String.valueOf(response.getJSONObject(i)), SubscriptionMasterSmall.class);
                                subscriptionMasterList.add(subscriptionMaster);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        initViews();
                    } else {
                        Toast.makeText(SubscriptionListActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");

                    hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", sharedPreferences.getString(Constants.USER_ID, ""));
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

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(SubscriptionListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
