package com.villagemilk.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.adapters.AdapterProductList;
import com.villagemilk.adapters.AdapterProductListNew;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.databinding.ActivitySearchBinding;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by dailyninja on 16/09/17.
 */

public class ActivitySearch extends BaseActivity {

    ActivitySearchBinding binding;

    boolean isLoading;

    boolean isSearchEmpty;

    String searchResultText = "";
    String searchText = "";

    AdapterProductList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);


        searchText = getIntent().getExtras().getString("searchText");

        setUpToolbar();

        performSearch();

        binding.rvProductSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));




    }

    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(searchText);

        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }


    private void performSearch() {
        if(Util.isNetworkAvailable(this)){
            isLoading = true;

            binding.progressBar.setVisibility(View.VISIBLE);
//            Util.showDialog(mContext);

            String url = Constants.API_PROD + "/productMaster/getProductsForQuery";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Util.hideDialog();

                    isLoading = false;
                    binding.progressBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    ProductMaster[] productMasters = gson.fromJson(response,ProductMaster[].class);

                    searchResultText= productMasters.length + " results for \""+ searchText + "\"";

                    binding.tvSearchResult.setText(searchResultText);

                    if(productMasters.length == 0)
                        isSearchEmpty = (true);
                    else
                        isSearchEmpty = (false);

                    adapter = (new AdapterProductList(ActivitySearch.this, Arrays.asList(productMasters)));

                    binding.rvProductSearch.setAdapter(adapter);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Util.alert(ActivitySearch.this,"Connection Error");
                    isLoading = (false);

                }
            }){
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId", BaseApplication.getInstance().getUser().getId());
                        jsonObject.put("query", searchText);
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

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(ActivitySearch.this, R.string.network_error, Toast.LENGTH_SHORT).show();

        }
    }




}
