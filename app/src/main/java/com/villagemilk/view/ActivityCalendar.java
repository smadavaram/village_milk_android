package com.villagemilk.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.activities.ProductCategoryListActivity;
import com.villagemilk.adapters.CalendarItemAdapter;
import com.villagemilk.adapters.CalendarPagerAdapter;
import com.villagemilk.beans.CartCheckoutResponseBean;
import com.villagemilk.beans.CartItem;
import com.villagemilk.beans.CartItemMaster;
import com.villagemilk.beans.FeedbackObject;
import com.villagemilk.beans.StatusBean;
import com.villagemilk.beans.SubscriptionUpdateBean;
import com.villagemilk.customviews.CartBottomView;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.calendar.CalendarResponse;
import com.villagemilk.model.calendar.Subscription;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.freshdesk.hotline.Hotline;
import com.google.gson.Gson;
import com.villagemilk.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by android on 30/11/16.
 */

public class ActivityCalendar extends BaseActivity implements FragmentCalendar.UpdateCart,CalendarItemAdapter.OnProductUpdated,CalendarItemAdapter.OnFeedbackUpdated {

    ViewPager vpCalendar;

    CartBottomView cartBottomView;

    private SharedPreferences sharedPreferences;

    List<Subscription> subscriptions;

    String feedback = "";//"This is for testing. Kindly ignore :)  \n";

    public static int currentPagePosition = 61;


    HashMap<Integer,FeedbackObject> feedbackObjectHashMap;

    FragmentCalendar currentPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_calendar_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentPagePosition += Util.getNextDeliveryDay();

        ImageView ivBackBtn = (ImageView)findViewById(R.id.ivBackBtn);

        ivBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Schedule");
            toolbar.setTitle("Calendar");
        }

        cartBottomView = (CartBottomView) findViewById(R.id.cartBottomView);
        cartBottomView.setType(CartBottomView.ADD_MORE_PRODUCTS);
        cartBottomView.addOnButtonClickListener(new CartBottomView.OnButtonClickListener() {
            @Override
            public void onClick(int type) {

                switch (type){

                    case CartBottomView.ADD_MORE_PRODUCTS:

/*                      Intent intent = new Intent(ActivityCalendar.this, ProductCategoryListActivity.class);
                        startActivity(intent);
*/
                        break;
                    case CartBottomView.SAVE_CHANGES:
                        if(currentPagePosition == 61)
                            buildCartMaster(subscriptions);
                        else if(currentPagePosition > 61)
                            buildSubscriptionUpdateList();
                        break;
                    
                    case CartBottomView.FEEDBACK:
                        sendDeliveryStatus();
                        break;

                }

            }
        });

        setUpViewpager();

    }

    private void setUpViewpager() {

        vpCalendar = (ViewPager)findViewById(R.id.vpCalendar);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tlCalendar);

        final CalendarPagerAdapter adapter = new CalendarPagerAdapter (getSupportFragmentManager(),this);

        int currentItem = 61 + Util.getNextDeliveryDay();

        Field field = null;
        try {
            field =ViewPager.class.getDeclaredField("mRestoredCurItem");
            field.setAccessible(true);
            field.set(vpCalendar, currentItem);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        vpCalendar.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                return true;
//            }
//        });


        vpCalendar.setAdapter(adapter);

        vpCalendar.setOffscreenPageLimit(0);

         tabLayout.setupWithViewPager(vpCalendar);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.ninja_yellow));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentPagePosition = position;

                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpCalendar + ":" + vpCalendar.getCurrentItem());
                // based on the current position you can then cast the page to the correct
                // class and call the method:
                if (page != null) {
                    currentPageFragment = ((FragmentCalendar)page);
                    currentPageFragment.updateCart();
                    cartBottomView.setType(currentPageFragment.getPageState());

                    if(position == 59 && feedbackObjectHashMap !=null){

                        cartBottomView.setType(CartBottomView.FEEDBACK);
                        currentPageFragment.setPageState(CartBottomView.FEEDBACK);

                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onUpdate(CalendarResponse calendarResponse) {

        double totalAmount = 0.0;
        int totalQuantity = 0;

        for(Subscription subscription : calendarResponse.getSubscriptions()){

            totalAmount += subscription.getProductUnitCost()*subscription.getProductQuantity();

            totalQuantity += subscription.getProductQuantity();

        }
        cartBottomView.updateValues(totalAmount,totalQuantity);

    }

    public String getFeedback(){

        Iterator it = feedbackObjectHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            feedback = feedback.concat(((FeedbackObject)pair.getValue()).getFeedbackText());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return feedback;
    }

    private void checkoutCart(final CartItemMaster cartItemMaster) {
        if(isNetworkAvailable()) {
            showDialog();

        String url = Constants.API_PROD + "/cart/checkout";

        final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();

                CartCheckoutResponseBean cartCheckoutResponseBean = gson.fromJson(String.valueOf(response), CartCheckoutResponseBean.class);

                if(response != null) {
                    hideDialog();

                    if(cartCheckoutResponseBean.getStatus()) {

                        new SweetAlertDialog(ActivityCalendar.this, SweetAlertDialog.SUCCESS_TYPE, robotoBold, robotoLight)
                                .setTitleText("Order Updated")
                                .setContentText(getString((R.string.thanks_subscription_normal)))
                                .setConfirmText("Ok,cool !")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        currentPageFragment.fetchSubscriptions();

                                        currentPageFragment.setPageState(CartBottomView.ADD_MORE_PRODUCTS);
                                        cartBottomView.setType(CartBottomView.ADD_MORE_PRODUCTS);

                                    }
                                })
                                .show();
                    } else {
                        String message;
                        if (cartCheckoutResponseBean.getMessage() != null) {
                            message = cartCheckoutResponseBean.getMessage();
                        } else {
                            message = getResources().getString(R.string.something_went_wrong);
                        }
                        new SweetAlertDialog(ActivityCalendar.this, SweetAlertDialog.ERROR_TYPE, robotoBold, robotoLight)
                                .setTitleText("Error")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                alert("Connection Error");

                hideDialog();
            }
        }){
            @Override
            public byte[] getBody() {
                return new Gson().toJson(cartItemMaster, CartItemMaster.class).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void buildCartMaster(List<Subscription> subscriptions){

        CartItemMaster cartItemMaster = new CartItemMaster();
        cartItemMaster.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
        cartItemMaster.setPromoCode(null);

        cartItemMaster.getCartItemList().clear();

        for (int i = 0; i < subscriptions.size(); i++) {
            Subscription subscription = subscriptions.get(i);

            CartItem cartItem = new CartItem();
            cartItem.setProductMasterId(subscription.getProductMasterId());
            cartItem.setQuantity(subscription.getProductQuantity());

            if (subscription.getId() != null) {
                cartItem.setSubscriptionId(subscription.getId());
                cartItemMaster.getCartItemList().add(cartItem);
            } else {
                if(subscription.getProductType() != null){
                    if (subscription.getProductQuantity() != 0 && subscription.getProductType() != 53) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                } else {
                    if (subscription.getProductQuantity() != 0) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                }
            }
        }

        checkoutCart(cartItemMaster);

        subscriptions = null;
    }

    @Override
    public void onFeedbackAdded(int position, FeedbackObject feedbackObject) {


        if(feedbackObjectHashMap == null)
            feedbackObjectHashMap = new HashMap<>();

       this.feedback = this.feedback.concat(feedback);

        feedbackObjectHashMap.put(position,feedbackObject);

        if(currentPagePosition == 60)
            cartBottomView.setType(CartBottomView.FEEDBACK);

        if(currentPageFragment != null)
            currentPageFragment.setPageState(CartBottomView.FEEDBACK);


    }

    @Override
    public void onFeedbackRemoved(int position) {

        if(feedbackObjectHashMap == null)
            return;
        feedbackObjectHashMap.remove(position);

        if(feedbackObjectHashMap.size() == 0) {

            cartBottomView.setType(CartBottomView.DISABLED);
            currentPageFragment.setPageState(CartBottomView.DISABLED);

        }
    }

    @Override
    public void onProductUpdated(final List<Subscription> subscriptions) {

        if(currentPageFragment == null) {
            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpCalendar + ":" + vpCalendar.getCurrentItem());

            if (page != null) {
                currentPageFragment = ((FragmentCalendar) page);
            }
        }
        this.subscriptions = subscriptions;

        cartBottomView.setType(CartBottomView.SAVE_CHANGES);

        currentPageFragment.setPageState(CartBottomView.SAVE_CHANGES);

        currentPageFragment.updateCart();


    }

    @Override
    public void onProductUnchanged() {

        if(currentPagePosition == 61) {
            currentPageFragment.setPageState(CartBottomView.ADD_MORE_PRODUCTS);
            cartBottomView.setType(CartBottomView.ADD_MORE_PRODUCTS);
        }else{
            currentPageFragment.setPageState(CartBottomView.DISABLED);
            cartBottomView.setType(CartBottomView.DISABLED);
        }

        currentPageFragment.updateCart();

    }


    public void sendDeliveryStatus(){

        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/deliveryStatus/save";

            final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();

                    try {
                        int status = response.getInt("status");

                        if(status == 1){

                            new SweetAlertDialog(ActivityCalendar.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                                    .setTitleText("Reported")
                                    .setContentText(getString((R.string.feedback_dialog_text)))
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            sDialog.dismissWithAnimation();

                                        }
                                    })
                                    .show();
                            Hotline.sendMessage(ActivityCalendar.this,"Other Products not delivered", getFeedback());

                        }else{

                            new SweetAlertDialog(ActivityCalendar.this, SweetAlertDialog.ERROR_TYPE, robotoBold, robotoLight)
                                    .setTitleText("ERROR")
                                    .setContentText("Something went wrong, please try again")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            sDialog.dismissWithAnimation();

                                        }
                                    })
                                    .show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    alert("Connection Error");

                    hideDialog();
                }
            }){
                @Override
                public byte[] getBody() {

                    return new Gson().toJson(feedbackObjectHashMap.values().toArray(), FeedbackObject[].class).getBytes();

                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        } else {

            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();

        }

    }


    private void buildSubscriptionUpdateList(){
        List<SubscriptionUpdateBean> subscriptionUpdateBeanList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,currentPagePosition - 60);

        for (Subscription subscription : subscriptions) {
            SubscriptionUpdateBean subscriptionUpdateBean = new SubscriptionUpdateBean();
            subscriptionUpdateBean.setSubscriptionMasterId(subscription.getId());
            subscriptionUpdateBean.setQuantity(subscription.getProductQuantity());
            subscriptionUpdateBean.setDate(DateOperations.getStartOfDate(calendar.getTime()).getTime());

            subscriptionUpdateBeanList.add(subscriptionUpdateBean);
        }

        saveChanges(subscriptionUpdateBeanList);
    }



    public void saveChanges(final List<SubscriptionUpdateBean> subscriptionUpdateBeanList){
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/subscriptionException/updateSubscription";


            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    StatusBean statusBean = new Gson().fromJson(String.valueOf(response), StatusBean.class);

                    if(statusBean != null){
                        if(statusBean.getStatus() == 1){

                            new SweetAlertDialog(ActivityCalendar.this, SweetAlertDialog.SUCCESS_TYPE, robotoBold, robotoLight)
                                    .setTitleText("Order Updated")
                                    .setContentText(getString((R.string.thanks_subscription_normal)))
                                    .setConfirmText("Ok,cool !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            currentPageFragment.fetchSubscriptions();

                                            currentPageFragment.setPageState(CartBottomView.DISABLED);
                                            cartBottomView.setType(CartBottomView.DISABLED);

                                        }
                                    })
                                    .show();


                        } else {
                            if (statusBean.getMessage() != null){
                                Toast.makeText(ActivityCalendar.this, statusBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityCalendar.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }


                    } else{
                        Toast.makeText(ActivityCalendar.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideDialog();
                    Toast.makeText(ActivityCalendar.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {

                    return new Gson().toJson(subscriptionUpdateBeanList).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(ActivityCalendar.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}