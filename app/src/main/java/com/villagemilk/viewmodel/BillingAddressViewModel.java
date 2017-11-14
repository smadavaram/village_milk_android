package com.villagemilk.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.activities.SelectFlatActivity;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Util;
import com.villagemilk.view.ActivityBillingAddress;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by android on 6/7/17.
 */

public class BillingAddressViewModel {

    Context context;


    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public ObservableField<String> address1;
    public ObservableField<String> address2;
    public ObservableField<String> city;
    public ObservableField<String> state;
    public ObservableField<String> zipCode;
    public ObservableField<String> billingAddress;

    public ObservableBoolean isChecked;

    private String buildingId;

    private String buildingName;

    private String userId;

    private String flat;

    boolean fromParentScreen;
    int status;

    public BillingAddressViewModel(Context context,String buildingId,String buildingName, boolean fromParentScreen) {

        this.context = context;
        this.buildingId = buildingId;
        this.buildingName = buildingName;

        address1 = new ObservableField<>();
        address2 = new ObservableField<>("");
        city = new ObservableField<>();
        state = new ObservableField<>();
        zipCode = new ObservableField<>();
        billingAddress = new ObservableField<>("");

        this.fromParentScreen = fromParentScreen;

        isChecked = new ObservableBoolean(false);

        sharedPreferences = context.getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        userId = sharedPreferences.getString(Constants.USER_ID, "");

        status = sharedPreferences.getInt(Constants.STATUS, 0);

        if(fromParentScreen){

            String address1 = sharedPreferences.getString("address1","");
            String address2 = sharedPreferences.getString("address2","");
            String city = sharedPreferences.getString("city","");
            String state = sharedPreferences.getString("state","");
            String zipcode = sharedPreferences.getString("zipcode","");
            String billingAddress = sharedPreferences.getString("billingAddress","");

            this.address1.set(address1);
            this.address2.set(address2);
            this.city.set(city);
            this.state.set(state);
            this.zipCode.set(zipcode);
            this.billingAddress.set(billingAddress);

            if(!TextUtils.isEmpty(billingAddress)){
                isChecked.set(true);
            }



        }


    }


    public void submit(View view){

        if(TextUtils.isEmpty(address1.get())){
            Toast.makeText(view.getContext(),"Enter Address 1",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(city.get())){
            Toast.makeText(view.getContext(),"Enter your City",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(state.get())){
            Toast.makeText(view.getContext(),"Enter your State",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(zipCode.get())){
            Toast.makeText(view.getContext(),"Enter Zip Code",Toast.LENGTH_SHORT).show();
            return;
        }else if(isChecked.get() && TextUtils.isEmpty(billingAddress.get())){
            Toast.makeText(view.getContext(),"Enter Zip Code",Toast.LENGTH_SHORT).show();
            return;
        }

        flat = address1.get() + " " + address2.get() + " " + city.get() + " " + state.get() + " " + zipCode.get() + " " + billingAddress.get() + " " ;

        updateUserAddress();

    }

    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener(){

        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {

                isChecked.set(checked);


            }
        };

    }

    private void updateUserAddress() {


        if (Util.isNetworkAvailable(context)) {
            Util.showDialog(context);

            String url = Constants.API_PROD + "/user/update/address";

            final StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();
                    editor.putInt(Constants.STATUS, 1);
                    editor.apply();

                    updateBillingAddress();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.alert(context,"Connection Error");
                    Util.hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("flat", flat);
                        jsonObject.put("id", userId);
                        jsonObject.put("building", new JSONObject().put("id", buildingId));
                        jsonObject.put("status", 1);
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
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void showHome() {

        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ActivityCompat.finishAffinity((BaseActivity)context);
    }


    private void updateBillingAddress() {


        if (Util.isNetworkAvailable(context)) {
            Util.showDialog(context);

            String url = Constants.API_PROD + "/user/updateBillingAddress";

            final StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();

                    editor.putString("address1",address1.get());
                    editor.putString("address2",address2.get());
                    editor.putString("city",city.get());
                    editor.putString("state",state.get());
                    editor.putString("zipcode",zipCode.get());
                    editor.putString("billingAddress",billingAddress.get());
                    editor.apply();

                    if(fromParentScreen){
                        ((AppCompatActivity)context).finish();
                    }else {
                        showHome();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.alert(context,"Connection Error");
                    Util.hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", userId);
                        jsonObject.put("address1", address1.get());
                        jsonObject.put("address2", address2.get()==null?"":address2.get());
                        jsonObject.put("city", city.get());
                        jsonObject.put("state", state.get());
                        jsonObject.put("zipCode", zipCode.get());
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
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }




}
