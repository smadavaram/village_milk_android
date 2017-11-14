package com.villagemilk.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.customviews.FeedbackView;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.SubscriptionObject;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Util;
import com.villagemilk.view.ActivitySubscription;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by android on 29/12/16.
 */

public class SubscriptionItemViewModel {

    Context mContext;
    public SubscriptionMaster object;

    public ObservableField<String> productName;
    public ObservableField<String> productQuantity;
    public ObservableField<String> productAmount;
    public ObservableField<String> productDescription;
    public ObservableField<String> imageUrl;
    public ObservableBoolean isPaused;

    public SubscriptionItemViewModel(Context context, SubscriptionMaster object) {
        this.object = object;
        mContext = context;

        productName = new ObservableField<>(object.getProductName());
        productQuantity = new ObservableField<>(object.getProductQuantity() + " x " + object.getProductUnit());
        productAmount = new ObservableField<>("$ " + object.getProductQuantity()*object.getProductUnitCost());
//        productDescription = new ObservableField<>(object.getSpecialText());
        imageUrl = new ObservableField<>(object.getProductImage());


    }


    public void setModel(SubscriptionMaster object){

        this.object = object;

        productName.set(object.getProductName());
        productQuantity.set(object.getProductQuantity() + " x " + object.getProductUnit());
        productAmount.set("$ " + object.getProductQuantity()*object.getProductUnitCost());
//        productDescription.set(object.getSpecialText());
        imageUrl.set(object.getProductImage());



    }


    public View.OnClickListener getEditSubscription(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SubscriptionMaster subscriptionMaster = new SubscriptionMaster(object,BaseApplication.getInstance().getUser().getId());
                Intent addSubscriptionIntent = new Intent(mContext, ActivitySubscription.class);
                addSubscriptionIntent.putExtra("product_master", object.getProductMaster().getId());
                Log.e("fucker", "onClick: " + object.getProductMaster().getId() );
                mContext.startActivity(addSubscriptionIntent);


            }
        };


    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        ImageLoader.getInstance().displayImage(imageUrl,view);

//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .placeholder(R.drawable.product_default)
//                .into(view);
    }



    public FeedbackView.OnItemSelected getFeedbackOnItemSelected() {
        return new FeedbackView.OnItemSelected() {

            @Override
            public void onItemSelected(int position, String text) {

                switch (position){

                    case 0:

//                        SubscriptionMaster subscriptionMaster = new SubscriptionMaster(object,BaseApplication.getInstance().getUser().getId());
                        Intent addSubscriptionIntent = new Intent(mContext, ActivitySubscription.class);
                        addSubscriptionIntent.putExtra("subscription_master", new Gson().toJson(object, SubscriptionMaster.class));
                        mContext.startActivity(addSubscriptionIntent);
                        break;

                    case 1:

                        pauseSubscription();

                        break;

                    case 2:
                        break;


                }

            }

            @Override
            public void onItemDeselected(int position) {

            }

        };
    }



    public void pauseSubscription(){

        if (Util.isNetworkAvailable(mContext)) {
            Util.showDialog(mContext);

            String url = Constants.API_PROD + "/subscriptionMaster/pauseSubscription";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();

                    if(response != null){

                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("status") == 1){

                                String title;
                                String message;

                                if(isPaused.get()){
                                    title = "Resumed";
                                    message = "Your subscription has been resumed.";
                                    productDescription.set(jsonObject.getString("nextDelivery"));
                                    object.setSubscriptionState(1);


                                }else{
                                    title = "Paused";
                                    message = "Your subscription has been paused!";

                                    productDescription.set("Delivery paused");
                                    object.setSubscriptionState(0);


                                }


                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE, null, null)
                                        .setTitleText(title)
                                        .setContentText(message)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                isPaused.set(!isPaused.get());
                                            }
                                        })
                                        .show();

                            }

                        }catch (Exception e){

                        }



                    } else {
                        Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    Util.hideDialog();
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date = dateFormat.format(Calendar.getInstance().getTime());
                        jsonObject.put("subscriptionId", object.getId());
                        jsonObject.put("date",date);
                       /* if(object.getSubscriptionState() == 0)
                            jsonObject.put("subscriptionState", 1);
                        else
                            jsonObject.put("subscriptionState", 0);*/
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




    /**
     *
     * Calendar c = Calendar.getInstance();
     * c.setTimeZone(TimeZone.getTimeZone("IST"));
     *
     * {
     "subscriptionId" : "2963789b-cae9-4fb8-a986-8e8697d252ff",
     "date" : "2016-12-29",
     "subscriptionState" : 0
     }
     *
     */
}
