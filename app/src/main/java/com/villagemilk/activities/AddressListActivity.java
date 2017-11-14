package com.villagemilk.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.BuildingListAdapter;
import com.villagemilk.beans.Building;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends BaseActivity {
    private static final String TAG = "AddressListActivity";

    private EditText etSearch;

    private RecyclerView rvBuildings;
    private BuildingListAdapter buildingListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private List<Building> buildingList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userId;

    public String buildingId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Select your apartment");

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("Select your apartment");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            getSupportActionBar().setHomeButtonEnabled(true);
        }

//        try {
//            mixpanel.track("Building Selector Screen");
//        } catch (Exception e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }

        etSearch = (EditText) findViewById(R.id.etSearch);

        rvBuildings = (RecyclerView) findViewById(R.id.rvBuildings);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        getBuildingList();
    }

    private void initViews(){
        userId = sharedPreferences.getString(Constants.USER_ID, "");

        if(BaseApplication.getInstance().getUser() != null){
            if(BaseApplication.getInstance().getUser().getBuilding() != null){
                buildingId = BaseApplication.getInstance().getUser().getBuilding().getId();
            }
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final List<Building> filteredBuildingList = filter(buildingList, s.toString());
                buildingListAdapter.animateTo(filteredBuildingList);
                rvBuildings.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvBuildings.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        rvBuildings.setLayoutManager(linearLayoutManager);

        buildingListAdapter = new BuildingListAdapter(this, buildingList);
        rvBuildings.setAdapter(buildingListAdapter);
    }

    private List<Building> filter(List<Building> buildingList, String query) {
        query = query.toLowerCase();

        final List<Building> filteredBuildingList = new ArrayList<>();

        for (Building building : buildingList) {
            final String text = building.getBuildingName().toLowerCase();

            if (text.contains(query)) {
                try {
                    filteredBuildingList.add(building.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        return filteredBuildingList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(AddressListActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void getBuildingList() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/building/getActiveBuildings";
            Log.d(TAG, "Url : " + url);

            final BaseActivity.MyPostJsonArrayRequest jsonArrayRequest = new BaseActivity.MyPostJsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    Gson gson = new Gson();

                    if(response != null){
                        buildingList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Building building = gson.fromJson(String.valueOf(response.getJSONObject(i)), Building.class);
                                buildingList.add(building);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        initViews();
                    } else {
                        Toast.makeText(AddressListActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
                        jsonObject.put("latitude", 0);
                        jsonObject.put("longitude", 0);
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
            Toast.makeText(AddressListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
