package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.BillingAdapter;
import com.villagemilk.beans.BillingItem;
import com.villagemilk.beans.BillingMaster;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

public class BillingListActivity extends BaseActivity {
    private static final String TAG = "Billing List Activity";

    private ArrayList<BillingItem> items = new ArrayList<>();
    private ListView listview;
    private Integer green_dot;
    private Integer yellow_tick;
    private BillingAdapter adapter;
    private Button btnPayments;
    private Button btnLogs;

    private TextView tvNoPaymentHistory;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_billing_list);
        initSimpleToolbar();

        tvNoPaymentHistory = (TextView) findViewById(R.id.tvNoPaymentHistory);
        
        btnPayments = (Button) findViewById(R.id.btnPayments);
        btnPayments.setTypeface(robotoBold);
        btnPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityCalendar = new Intent(BillingListActivity.this, PaymentsActivity.class);
                startActivity(activityCalendar);
                finish();
            }
        });

        btnLogs = (Button) findViewById(R.id.btnLogs);
        btnLogs.setTypeface(robotoBold);
        btnLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt = new Intent(BillingListActivity.this, BillingHistoryActivity.class);
                startActivity(intnt);
            }
        });

        listview=(ListView)findViewById(R.id.listView_main);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                BillingItem ei = items.get(position);
//                Intent intent = new Intent(BillingListActivity.this, BillingHistoryActivity.class);
//                startActivity(intent);
            }
        });

        green_dot=R.drawable.green_dot;
        yellow_tick=R.drawable.yellow_tick;

        sharedPreferences= getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        getBillingData();
    }

    private void getBillingData() {
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/billingMaster/findByUser";
            Log.d(TAG, "Url : " + url);

            final MyPostJsonArrayRequest jsonArrayRequest = new MyPostJsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, "Response :" + response.toString());
                    Gson gson = new Gson();

                    if(response.length() > 0){
                        for (int i = 0; i < response.length(); i++){
                            try {
                                BillingMaster billingMaster = gson.fromJson(String.valueOf(response.getJSONObject(i)), BillingMaster.class);
                                if(billingMaster.isCurrent()){
                                    if(DateOperations.getDateFromLong(billingMaster.getStartDate()) != null){
                                        items.add(new BillingItem("Current Balance",
                                                "$ " + billingMaster.getAmount(),
                                                "From " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getStartDate())),
                                                billingMaster.getAmount() >= 0?R.drawable.big_red_dot:R.drawable.green_dot, billingMaster.getAmount() , billingMaster.isCurrent()));
                                    }
                                } else {
                                    if(DateOperations.getDateFromLong(billingMaster.getStartDate()) != null && DateOperations.getDateFromLong(billingMaster.getPaymentDate()) != null
                                            && DateOperations.getDateFromLong(billingMaster.getEndDate()) != null){
                                        items.add(new BillingItem( "Paid On " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getPaymentDate())),
                                                "$ " + billingMaster.getAmount(),
                                                "From " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getStartDate())) + " to " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getEndDate())),
                                                yellow_tick, billingMaster.getAmount(), billingMaster.isCurrent()));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    initializeListView();
                    hideDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                }
            }){
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", sharedPreferences.getString(Constants.USER_ID, ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Login body : " + jsonObject.toString());
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
            Toast.makeText(BillingListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeListView() {
        if (items != null && items.size() > 0) {
            adapter= new BillingAdapter(this, items);
            listview.setAdapter(adapter);

            listview.setVisibility(View.VISIBLE);
            tvNoPaymentHistory.setVisibility(GONE);
        } else {
            listview.setVisibility(GONE);
            tvNoPaymentHistory.setVisibility(View.VISIBLE);
        }
    }

    public String getFormattedDate(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            String formattedString = dateFormat.format(d);
            Log.d(TAG, "Date : " + d.toString());
            Log.d(TAG, "Date String : " + formattedString);
            return formattedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}