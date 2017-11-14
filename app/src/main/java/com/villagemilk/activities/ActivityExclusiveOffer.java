package com.villagemilk.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.AdapterUserSpends;
import com.villagemilk.beans.OfferString;
import com.villagemilk.beans.UserExpenditure;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by arsh on 17/10/16.
 */

public class ActivityExclusiveOffer extends BaseActivity {

    String userId;

    private SharedPreferences sharedPreferences;

    private SweetAlertDialog sDialog;

    private int apiCallCount;

    AdapterUserSpends adapterUserSpends;

    RecyclerView rvUserSpends;
    TextView tvOfferText;

    TextView tvTotalExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exclusive_offers);

        initSimpleToolbar();

        rvUserSpends = (RecyclerView)findViewById(R.id.rvExclusiveOffer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUserSpends.setLayoutManager(linearLayoutManager);

        tvOfferText = (TextView)findViewById(R.id.tvOffersText);

        tvTotalExpense = (TextView)findViewById(R.id.tvTotalExpense);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        userId = sharedPreferences.getString(Constants.USER_ID, "");

        fetchUserSalesOfferString();

        fetchUserSalesProfileForMonth();

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


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void fetchUserSalesOfferString(){

        showDialog();

        String url = Constants.API_PROD + "/user/getUserSalesOfferString/"+ userId;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                hideDialog();
                Gson gson = new Gson();
                OfferString offerString = gson.fromJson(response,OfferString.class);

                tvOfferText.setText(offerString.getOfferString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();

            }
        }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void fetchUserSalesProfileForMonth(){

        showDialog();

        String url = Constants.API_PROD + "/user/getUserSalesProfileForMonth/"+ userId;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                hideDialog();

                Gson gson = new Gson();
                UserExpenditure[] userExpenditures  = gson.fromJson(response,UserExpenditure[].class);

                Double total = userExpenditures[userExpenditures.length -1].getExpense();

                tvTotalExpense.setText(total.toString());

                for(int i=0;i<userExpenditures.length;i++){

                    userExpenditures[i].setFraction(userExpenditures[i].getExpense().floatValue()/total.floatValue());

                }

                adapterUserSpends = new AdapterUserSpends(ActivityExclusiveOffer.this,userExpenditures);

                rvUserSpends.setAdapter(adapterUserSpends);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();

            }
        }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getInstance().addToRequestQueue(stringRequest);

    }

    /**
     * GET REQUEST

     user/getUserSalesOfferString/<userId>



     GET REQUEST


     user/getUserSalesProfileForMonth/<userId>
     */

    @Override
    protected void showDialog() {
        super.showDialog();
        apiCallCount++;
    }

    @Override
    protected void hideDialog() {
        apiCallCount--;
        if(apiCallCount == 0)
            super.hideDialog();

    }
}
