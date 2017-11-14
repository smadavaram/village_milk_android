package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.User;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class SelectFlatActivity extends BaseActivity {
    private static final String TAG = "SelectFlatActivity";

    private TextView tvBuildingName;

    private EditText etFlat;

    private CardView cardView;
    private Button btnNext;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String buildingId;

    private String buildingName;

    private String userId;

    private String flat;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_flat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter flat number");
        }

        tvBuildingName = (TextView) findViewById(R.id.tvBuildingName);

        etFlat = (EditText) findViewById(R.id.etFlat);

        cardView = (CardView) findViewById(R.id.cardView);
        btnNext = (Button) cardView.findViewById(R.id.button);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.BUILDING_ID) &&
                getIntent().getExtras().containsKey(Constants.BUILDING_NAME)) {
            buildingId = getIntent().getExtras().getString(Constants.BUILDING_ID);
            buildingName = getIntent().getExtras().getString(Constants.BUILDING_NAME);

            initViews();
        } else {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews(){
        btnNext.setText("Next");

        userId = sharedPreferences.getString(Constants.USER_ID, "");

        status = sharedPreferences.getInt(Constants.STATUS, 0);

        tvBuildingName.setText("You stay at " + buildingName);

        if(BaseApplication.getInstance().getUser() != null){
            User user = BaseApplication.getInstance().getUser();
            if(user.getFlat()!=null) {
                flat = BaseApplication.getInstance().getUser().getFlat();
                if(!TextUtils.isEmpty(flat)) {
                    etFlat.setText(flat);
                    etFlat.setSelection(flat.length());
                }
            }
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFlat.getText().toString().trim().length() == 0) {
                    enterFlatNumberDialog();
                } else {
                    updateUserAddress();
                }
            }
        });
    }

    private void enterFlatNumberDialog() {
        new SweetAlertDialog(SelectFlatActivity.this,SweetAlertDialog.ERROR_TYPE,robotoBold,robotoLight)
                .setTitleText("Error")
                .setContentText("Please Enter the Flat or House Number for Delivery").show();

    }

    private void showHome() {
        hideDialog();

        Intent intent = new Intent(SelectFlatActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void updateUserAddress() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/user/update/address";
            Log.d(TAG, "Url : " + url);

            final StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());

                    editor.putInt(Constants.STATUS, 1);
                    editor.apply();

//                    try {
//                        if(userId != null) {
//                            mixpanel.getPeople().set("BuildingName", buildingName);
//                            mixpanel.getPeople().set("BuidlingId", buildingId);
//                            mixpanel.getPeople().set("FlatNo", etFlat.getText().toString());
//
//                            JSONObject props = new JSONObject();
//                            props.put("User Building", buildingName);
//                            props.put("User Flat", etFlat.getText().toString());
//                            mixpanel.registerSuperProperties(props);
//
//                            String temp =  sharedPreferences.getString(Constants.UserType,"");
//
//                            if(temp.equals("Phone No Updated")) {
//                                mixpanel.getPeople().set(Constants.UserType,Constants.BuildingUpdated);
//                                editor.putString(Constants.UserType,Constants.BuildingUpdated);
//                                editor.apply();
//                            }
//                        }
//                    } catch(Exception exp){
//
//                    }

                    showHome();
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
                        jsonObject.put("flat", etFlat.getText().toString());
                        jsonObject.put("id", userId);
                        jsonObject.put("building", new JSONObject().put("id", buildingId));
                        jsonObject.put("status", 1);
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

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(SelectFlatActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
