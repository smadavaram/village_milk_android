package com.villagemilk.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.AdapterBuildingList;
import com.villagemilk.beans.Building;
import com.villagemilk.databinding.ActivityBuildingListBinding;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by android on 28/12/16.
 */

public class BUildingActivityViewModel {


    Context mContext;
    ActivityBuildingListBinding binding;
    ArrayList<Building> buildings;
    AdapterBuildingList adapter;




    public BUildingActivityViewModel(Context mContext, ActivityBuildingListBinding binding) {
        this.mContext = mContext;

        this.binding = binding;


        getBuildingList();


    }

    public void setUpRecyclerView(Building[] b){

        buildings = new ArrayList<>(Arrays.asList(b));
        adapter = new AdapterBuildingList(mContext,buildings);

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        binding.rvBuildings.setLayoutManager(manager);
        binding.rvBuildings.setAdapter(adapter);


    }

    public TextWatcher getTextWatcher() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                adapter.setBuildings(filter(buildings, charSequence.toString()));
                binding.rvBuildings.scrollToPosition(0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }


    private ArrayList<Building> filter(List<Building> buildingList, String query) {

        query = query.toLowerCase();

        final ArrayList<Building> filteredBuildingList = new ArrayList<>();

        for (Building building : buildingList) {
            final String text = building.getBuildingName().toLowerCase();

            if (text.contains(query)) {

                filteredBuildingList.add(building);

            }
        }

        return filteredBuildingList;
    }



    private void getBuildingList() {

        if (Util.isNetworkAvailable(mContext)) {
            Util.showDialog(mContext);

            String url = Constants.API_PROD + "/building/getActiveBuildings";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();

                    Gson gson = new Gson();

                    if(response != null){

                        Building[] buildings = gson.fromJson(response,Building[].class);
                        setUpRecyclerView(buildings);


                    } else {
                        Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Util.alert(mContext,"Connection Error");

                    Util.hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("latitude", 0);
                        jsonObject.put("longitude", 0);
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
            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

}
