package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.PaymentsActivity;
import com.villagemilk.beans.ProductCategory;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.ProductSubCategory;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.villagemilk.util.Util;
import com.villagemilk.view.ActivityCalendar;
import com.villagemilk.view.ActivitySubscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by arsh on 11/6/17.
 */

public class AdapterProductListNew extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    Context mContext;
    List<ProductSubCategory> productSubCategories;

    public AdapterProductListNew(Context mContext, ProductSubCategory[] productCategories) {
        this.mContext = mContext;

        productSubCategories = new ArrayList<>();
        List<ProductSubCategory> list = Arrays.asList(productCategories);

        for(ProductSubCategory productSubCategory: productCategories){

            if(!productSubCategory.getProductList().isEmpty()){

                productSubCategories.add(productSubCategory);

            }

        }

    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        convertView = LayoutInflater.from(mContext).inflate(R.layout.product_item_cart_new,parent,false);

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
            title = (TextView)convertView.findViewById(R.id.list_item_entry_title);
            price = (TextView)convertView.findViewById(R.id.price);
            strikePrice = (TextView)convertView.findViewById(R.id.strikePrice);
            unit = (TextView)convertView.findViewById(R.id.unit);
            specialText = (TextView)convertView.findViewById(R.id.specialText);
            tvSubscribe = (TextView)convertView.findViewById(R.id.tvSubscribe);
            ivPoweredBy = (ImageView) convertView.findViewById(R.id.ivPoweredBy);
            productImageView = (ImageView) convertView.findViewById(R.id.im_productImage1);
            llSubscribe = (LinearLayout) convertView.findViewById(R.id.llSubscribe);
            llAdd = (LinearLayout) convertView.findViewById(R.id.llAddButton);


            final ProductMaster productList = getChild(groupPosition,childPosition);

        if(productList.getPoweredBy() != null && productList.getPoweredBy().length() > 0){
            ImageLoader.getInstance().displayImage(productList.getPoweredBy(), ivPoweredBy);
            ivPoweredBy.setVisibility(View.VISIBLE);
        } else {
            ivPoweredBy.setVisibility(GONE);
        }

/*
        llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchSubscriptionActivity(productList);

            }
        });

*/


        llAdd.setOnClickListener(new View.OnClickListener() {
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

        SubscriptionWrapper subscription = null;


        for(SubscriptionWrapper subscriptionMaster : BaseApplication.getInstance().getSubscriptionList()){

            if(subscriptionMaster.getProductMaster().equals(productList.getId())){

                tvSubscribe.setText("SUBSCRIBED");
                llAdd.setVisibility(GONE);
                llSubscribe.setBackgroundResource(R.drawable.rounded_button_gray);
                subscription = subscriptionMaster;
//                tvSubscribe.setOnClickListener(null);
                break;
            }else {

                llAdd.setVisibility(View.VISIBLE);
                tvSubscribe.setText("SUBSCRIBE");
                llSubscribe.setBackgroundResource(R.drawable.rounded_button1);

            }


        }

        final SubscriptionWrapper finalSubscription = subscription;
        llSubscribe.setOnClickListener(new View.OnClickListener() {
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
            llSubscribe.setVisibility(GONE);
        }else {
            llSubscribe.setVisibility(View.VISIBLE);

        }


        if(productList.getSpecialText() != null && productList.getSpecialText().length() > 0){
            specialText.setVisibility(View.VISIBLE);
            specialText.setText(productList.getSpecialText());
        } else {
            specialText.setVisibility(GONE);
        }

        if(productList.getStrikePrice() != null && productList.getStrikePrice() > -1 && productList.getStatus() != 2){
            strikePrice.setText("$"  + Constants.priceDisplay.format(productList.getStrikePrice()));
            strikePrice.setVisibility(View.VISIBLE);
            strikePrice.setPaintFlags(strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            strikePrice.setVisibility(GONE);
        }

        productImageView.setImageResource(R.drawable.product_default);

        if (title != null) {
            title.setText(productList.getProductName());
        }

        if(productList.getStatus() == 1 && price != null) {
            price.setText("$" + Constants.priceDisplay.format(productList.getProductUnitPrice()));
            price.setVisibility(View.VISIBLE);
        } else if(productList.getStatus() == 2 ) {
            if(price != null) {
            }
        }

        if(productList.getStatus() == 1 && unit != null) {
            unit.setText(productList.getProductUnitSize());
        } else if(productList.getStatus() == 2){
             unit.setText("Coming Soon !");
        }

        if(productList.getProductImage() != null) {
            ImageLoader.getInstance().displayImage(productList.getProductImage(), productImageView);
        }


        return convertView;
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


    @Override
    public int getRealChildrenCount(int groupPosition) {
        return productSubCategories.get(groupPosition).getProductList().size();
    }

    @Override
    public int getGroupCount() {
        return productSubCategories.size();
    }

    @Override
    public ProductSubCategory getGroup(int groupPosition) {
        return productSubCategories.get(groupPosition);
    }

    @Override
    public ProductMaster getChild(int groupPosition, int childPosition) {
        return productSubCategories.get(groupPosition).getProductList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.product_list_group,parent,false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.ivArrow);

        TextView textView = (TextView)convertView.findViewById(R.id.textView);

        ImageLoader.getInstance().displayImage(getGroup(groupPosition).getProductSubCategoryImage(),imageView);

        if (isExpanded){
            ivArrow.setRotation(180);
        }else
            ivArrow.setRotation(0);

        textView.setText(getGroup(groupPosition).getProductSubCategoryName());


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;

    }

}
