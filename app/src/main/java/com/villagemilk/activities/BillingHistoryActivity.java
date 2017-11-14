package com.villagemilk.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.BillingHistoryAdapter;
import com.villagemilk.beans.BillingLog;
import com.villagemilk.beans.BillingLogMaster;
import com.villagemilk.beans.BillingLogResponseBean;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BillingHistoryActivity extends BaseActivity {
    private static final String TAG = "BillingHistoryActivity";

    private RecyclerView rvBillingLogs;
    private BillingHistoryAdapter billingHistoryAdapter;
    private LinearLayoutManager layoutManager;

    private TextView tvNoBillingHistory;

    private Map<Long, List<BillingLog>> billingLogMap = new LinkedHashMap<>();

    private List<BillingLogMaster> billingLogMasterList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    private String paginationUrl;

    private boolean inProgress;

    private boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Billing History");
        }

        rvBillingLogs = (RecyclerView) findViewById(R.id.rvBillingLogs);

        tvNoBillingHistory = (TextView) findViewById(R.id.tvNoBillingHistory);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        fetchBillingLog();
    }

    private void initViews(){
        loaded = true;

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvBillingLogs.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvBillingLogs.setLayoutManager(layoutManager);

        if (billingLogMasterList != null && billingLogMasterList.size() > 0) {
            billingHistoryAdapter = new BillingHistoryAdapter(this, billingLogMasterList);
            rvBillingLogs.setAdapter(billingHistoryAdapter);

            rvBillingLogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    int firstVisibleItemIndex = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if(newState == RecyclerView.SCROLL_STATE_IDLE && (billingLogMasterList.size() - firstVisibleItemIndex) <= 5){
                        fetchBillingLog();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            rvBillingLogs.setVisibility(View.VISIBLE);
            tvNoBillingHistory.setVisibility(View.GONE);
        } else {
            rvBillingLogs.setVisibility(View.GONE);
            tvNoBillingHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(BillingHistoryActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    public void fetchBillingLog() {
        if (isNetworkAvailable()) {
            if(!inProgress){
                inProgress = true;

                if(paginationUrl == null && !loaded){
                    showDialog();
                }

                String url;

                if(paginationUrl == null){
                    if(!loaded) {
                        url = Constants.API_PROD + "/billingLog/findByUserPaginated/" + sharedPreferences.getString(Constants.USER_ID, "");
                    } else {
                        return;
                    }
                } else {
                    url = Constants.API_PROD + paginationUrl;
                }

                Log.d(TAG, "Get Billing Log Url : " + url);

                final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get Billing Log Response : " + response);

                        BillingLogResponseBean billingLogResponseBean = new Gson().fromJson(response, BillingLogResponseBean.class);

                        if (billingLogResponseBean != null) {
                            for (int i = 0; i < billingLogResponseBean.getBillingLogList().size(); i++) {
                                Long key = billingLogResponseBean.getBillingLogList().get(i).getBillingLogProperties().getDate();

                                if (billingLogMap.containsKey(key)) {
                                    List<BillingLog> billingLogList = billingLogMap.get(key);
                                    billingLogList.add(billingLogResponseBean.getBillingLogList().get(i));

                                    billingLogMap.put(key, billingLogList);
                                } else {
                                    List<BillingLog> billingLogList = new ArrayList<>();
                                    billingLogList.add(billingLogResponseBean.getBillingLogList().get(i));

                                    billingLogMap.put(key, billingLogList);
                                }
                            }

                            for (Long key : billingLogMap.keySet()) {
                                BillingLogMaster billingLogMaster = new BillingLogMaster();
                                billingLogMaster.setId(key);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(key);

                                billingLogMaster.setDate(calendar.getTime());
                                billingLogMaster.setDateString(Commons.dateFormat.format(calendar.getTime()));
                                billingLogMaster.setBillingLogList(billingLogMap.get(key));

                                double totalDeduction = 0;
                                double totalAddition = 0;

                                for (int i = 0; i < billingLogMaster.getBillingLogList().size(); i++) {
                                    if(billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getDailyLogType() == 1){
                                        billingLogMaster.setBalance(billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getAmount());
                                    } else if(billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getDailyLogType() == 2){
                                        totalDeduction = totalDeduction + billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getAmount();
                                    } else if(billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getDailyLogType() == 3){
                                        totalAddition = totalAddition + billingLogMaster.getBillingLogList().get(i).getBillingLogProperties().getAmount();
                                    }
                                }

                                if(totalAddition != 0){
                                    billingLogMaster.getBillingLogTransactionList().add("$" + (-1)*totalAddition + " Added via Paytm");
                                }

                                if(totalDeduction != 0){
                                    billingLogMaster.getBillingLogTransactionList().add("$" + totalDeduction + " Deduct");
                                }

                                if(billingLogMasterList.contains(billingLogMaster)){
                                    int position = billingLogMasterList.indexOf(billingLogMaster);

                                    billingLogMasterList.remove(position);

                                    billingLogMasterList.add(billingLogMaster);
                                } else {
                                    billingLogMasterList.add(billingLogMaster);
                                }
                            }

                            if(paginationUrl == null){
                                initViews();
                            } else {
                                if(billingHistoryAdapter != null){
                                    billingHistoryAdapter.notifyDataSetChanged();
                                }
                            }

                            if(billingLogResponseBean.getPagination()!=null)
                                paginationUrl = billingLogResponseBean.getPagination().getNext();
                        } else {
                            Toast.makeText(BillingHistoryActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }

                        hideDialog();

                        inProgress = false;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Get Billing Log Data Error : " + error.toString());

                        hideDialog();
                        Toast.makeText(BillingHistoryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

                        inProgress = false;
                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
            }
        } else {
            Toast.makeText(BillingHistoryActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
