package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.PaymentsActivity;
import com.villagemilk.activities.SubscriptionActivity;
import com.villagemilk.beans.ProductList;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.villagemilk.util.Util;
import com.villagemilk.view.ActivityCalendar;
import com.villagemilk.view.ActivitySubscription;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by android on 3/1/17.
 */

public class AdapterProductList extends RecyclerView.Adapter<AdapterProductList.ViewHolder> {

    Context mContext;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public void setProductList(List<ProductMaster> productLists) {
        this.productLists = productLists;
        notifyDataSetChanged();
    }

    List<ProductMaster> productLists;

    public AdapterProductList(Context mContext, List<ProductMaster> productLists) {
        this.mContext = mContext;
        this.productLists = productLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item_cart_new,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ProductMaster productList = productLists.get(position);


        if(productList.getPoweredBy() != null && productList.getPoweredBy().length() > 0){
            ImageLoader.getInstance().displayImage(productList.getPoweredBy(), holder.ivPoweredBy);
            holder.ivPoweredBy.setVisibility(View.VISIBLE);
        } else {
            holder.ivPoweredBy.setVisibility(View.GONE);
        }

/*
        llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchSubscriptionActivity(productList);

            }
        });

*/

        SubscriptionWrapper subscription = null;


        for(SubscriptionWrapper subscriptionMaster : BaseApplication.getInstance().getSubscriptionList()){

            if(subscriptionMaster.getProductMaster().equals(productList.getId())){

                holder.tvSubscribe.setText("SUBSCRIBED");
                holder.llSubscribe.setBackgroundResource(R.drawable.rounded_button_gray);
                subscription = subscriptionMaster;
//                tvSubscribe.setOnClickListener(null);
                break;
            }else {

                holder.tvSubscribe.setText("SUBSCRIBE");
                holder.llSubscribe.setBackgroundResource(R.drawable.rounded_button1);

            }


        }

        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SubscriptionMaster subscriptionMaster = new SubscriptionMaster();
                subscriptionMaster.setProductMaster(productList);
                subscriptionMaster.setProductImage(productList.getProductImage());
                subscriptionMaster.setProductName(productList.getProductName());
                subscriptionMaster.setProductUnitCost(productList.getProductUnitPrice());
                subscriptionMaster.setProductUnit(productList.getProductUnitSize());
                subscriptionMaster.setStartDate(DateOperations.getTomorrowStartDate().getTime());

                subscriptionMaster.setUserId(BaseApplication.getInstance().getUser().getId());

                subscribeProduct(subscriptionMaster);

            }
        });

        final SubscriptionWrapper finalSubscription = subscription;
        holder.llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalSubscription!=null && BaseApplication.getInstance().containsSubscriptionMaster(finalSubscription.getProductMaster())!=null){

//                    finalSubscription.setUserId(BaseApplication.getInstance().getUser().getId());

                    Intent intent = new Intent(mContext, ActivitySubscription.class);
                    intent.putExtra("product_master", finalSubscription.getProductMaster());


                    mContext.startActivity(intent);


                }else
                    launchSubscriptionActivity(productList);



            }
        });


        if(productList.getStatus() == 2){
            holder.llSubscribe.setVisibility(View.GONE);
        }else {
            holder.llSubscribe.setVisibility(View.VISIBLE);

        }


        if(productList.getSpecialText() != null && productList.getSpecialText().length() > 0){
            holder.specialText.setVisibility(View.VISIBLE);
            holder.specialText.setText(productList.getSpecialText());
        } else {
            holder.specialText.setVisibility(View.GONE);
        }

        if(productList.getStrikePrice() != null && productList.getStrikePrice() > -1 && productList.getStatus() != 2){
            holder.strikePrice.setText("$"  + Constants.priceDisplay.format(productList.getStrikePrice()));
            holder.strikePrice.setVisibility(View.VISIBLE);
            holder.strikePrice.setPaintFlags(holder.strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.strikePrice.setVisibility(View.GONE);
        }

        holder.productImageView.setImageResource(R.drawable.product_default);

        if (holder.title != null) {
            holder.title.setText(productList.getProductName());
        }

        if(productList.getStatus() == 1 && holder.price != null) {
            holder.price.setText("$" + Constants.priceDisplay.format(productList.getProductUnitPrice()));
            holder.price.setVisibility(View.VISIBLE);
        } else if(productList.getStatus() == 2 ) {
            if(holder.price != null) {
            }
        }

        if(productList.getStatus() == 1 && holder.unit != null) {
            holder.unit.setText(productList.getProductUnitSize());
        } else if(productList.getStatus() == 2){
            holder.unit.setText("Coming Soon !");
        }

        if(productList.getProductImage() != null) {
            ImageLoader.getInstance().displayImage(productList.getProductImage(), holder.productImageView);
        }



    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        TextView price;

        TextView strikePrice;

        TextView unit;

        TextView specialText;

        TextView tvSubscribe;

        ImageView ivPoweredBy;

        ImageView productImageView;

        LinearLayout llSubscribe;

        LinearLayout llAdd;


        public ViewHolder(View convertView) {
            super(convertView);

            title = (TextView)convertView.findViewById(R.id.list_item_entry_title);

            price = (TextView)convertView.findViewById(R.id.price);

            strikePrice = (TextView)convertView.findViewById(R.id.strikePrice);

            unit = (TextView)convertView.findViewById(R.id.unit);

            tvSubscribe = (TextView)convertView.findViewById(R.id.tvSubscribe);

            specialText = (TextView)convertView.findViewById(R.id.specialText);

            ivPoweredBy = (ImageView) convertView.findViewById(R.id.ivPoweredBy);

            productImageView = (ImageView) convertView.findViewById(R.id.im_productImage1);

            llSubscribe = (LinearLayout) convertView.findViewById(R.id.llSubscribe);

            llAdd = (LinearLayout) convertView.findViewById(R.id.llAddButton);

        }
    }

    public void launchSubscriptionActivity(ProductMaster productMaster){

        SubscriptionMaster subscriptionMaster = new SubscriptionMaster();
        subscriptionMaster.setProductMaster(productMaster);
        subscriptionMaster.setProductImage(productMaster.getProductImage());
        subscriptionMaster.setProductName(productMaster.getProductName());
        subscriptionMaster.setProductUnitCost(productMaster.getProductUnitPrice());
        subscriptionMaster.setProductUnit(productMaster.getProductUnitSize());
        subscriptionMaster.setStartDate(DateOperations.getTomorrowStartDate().getTime());

        subscriptionMaster.setUserId(BaseApplication.getInstance().getUser().getId());

        Intent intent = new Intent(mContext, ActivitySubscription.class);
        intent.putExtra("subscription_master", new Gson().toJson(subscriptionMaster, SubscriptionMaster.class));


        mContext.startActivity(intent);


    }


    public void subscribeProduct(final SubscriptionMaster subscriptionMaster){


        Calendar calendar = Calendar.getInstance();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        List<Integer> days = new ArrayList<>();

        for(String day:BaseApplication.getInstance().getDeliveryDays()){

            if(day.equalsIgnoreCase("Sunday")){

                days.add(1);

            }else if(day.equalsIgnoreCase("Monday")){

                days.add(2);

            }else if(day.equalsIgnoreCase("Tuesday")){

                days.add(3);

            }else if(day.equalsIgnoreCase("Wednesday")){

                days.add(4);

            }else if(day.equalsIgnoreCase("Thursday")){

                days.add(5);

            }else if(day.equalsIgnoreCase("Friday")){

                days.add(6);

            }else if(day.equalsIgnoreCase("Saturday")){

                days.add(7);

            }





        }

        int big = 0,small = 8;

        for(int i:days){

            if(i<=dayOfWeek){

                if(small>i){
                    small = i;
                }


            }else{

                if(i > big){
                    big = i;
                    break;
                }

            }


        }

        if(big > dayOfWeek){
            subscriptionMaster.addDeliveryDay(getDayFromDayOfWeek(big));
        }else {
            subscriptionMaster.addDeliveryDay(getDayFromDayOfWeek(small));
        }


        if(Util.isNetworkAvailable(mContext)) {

            final String url = Constants.API_PROD + "/subscriptionMaster/createNew";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Util.hideDialog();

                    Log.e("response aaya for ",response);


                    if (response.equals("true")){

                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE, null, null)
                                .setTitleText("Success")
                                .setContentText("Subscription added")
                                .setConfirmText("Ok,cool !")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();


                                        Intent homeActivityIntent = new Intent(mContext, ActivityCalendar.class);
                                        mContext.startActivity(homeActivityIntent);

                                    }
                                }).show();




                    } else {

                        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE, null, null)
                                .setTitleText("Error!")
                                .setContentText("Subscription Failed")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(mContext,PaymentsActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                }).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {



                    subscriptionMaster.setRecentlyChangedFlag(true);
                    subscriptionMaster.setActive(true);
                    subscriptionMaster.setVerifyStatus(1);
                    subscriptionMaster.setStartDate(DateOperations.getTomorrowStartDate().getTime());
                    subscriptionMaster.setEndDate(DateOperations.getFutureSubscriptionDate().getTime());
                    subscriptionMaster.setProductQuantity(1);
                    subscriptionMaster.setTransientSubscriptionType(4);

                    return new Gson().toJson(subscriptionMaster, SubscriptionMaster.class).getBytes();
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
            Toast.makeText(mContext, R.string.network_error,Toast.LENGTH_SHORT).show();
        }

    }


    public String getDayFromDayOfWeek(int day){

        switch (day){

            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default:return "Sunday";

        }

    }



}
