package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.villagemilk.adapters.CalendarAdapter;
import com.villagemilk.beans.CartCheckoutResponseBean;
import com.villagemilk.beans.CartItem;
import com.villagemilk.beans.CartItemMaster;
import com.villagemilk.beans.ScheduleBean;
import com.villagemilk.beans.StatusBean;
import com.villagemilk.beans.SubscriptionException;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.beans.SubscriptionUpdateBean;
import com.villagemilk.customviews.MyHorizontalScrollView;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.freshdesk.hotline.Hotline;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CalendarActivity extends BaseActivity {
    private static final String TAG = "CalendarActivity";

    private MyHorizontalScrollView hsvCalendar;

    private LinearLayout llCalendar;

    private TextView tvMonth;

    private TextView tvSelectedDate;

    public Double getTotalAmount() {
        return totalAmount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        tvTotalAmount.setText("₹" + totalAmount);
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        tvTotalQuantity.setText(totalQuantity + " items of worth");
    }

    Double totalAmount = 0.0;
    int totalQuantity = 0;

    private RecyclerView rvSubscriptions;
    private CalendarAdapter calendarAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ImageView ivEmptyBasket;
    private TextView tvNoSubscriptions;

    private RelativeLayout rlAddProducts;
    private ImageView ivAdd;
    private TextView tvAddProducts;

    private RelativeLayout rlSaveChanges;
    private TextView tvSaveChanges;

    private TextView tvTotalAmount;

    private TextView tvTotalQuantity;

//    private boolean isEditable;

    private SharedPreferences sharedPreferences;

    private Calendar calendar = Calendar.getInstance();

    private int dateViewWidth = 0;

    public Date selectedDate;

    private ScheduleBean scheduleBean;

    private Map<Long, List<SubscriptionMasterSmall>> subscriptionMasterMap = new LinkedHashMap<>();

    private List<SubscriptionMasterSmall> subscriptionMasterList = new ArrayList<>();

    private Animation slideUpAnimation;
    private Animation slideDownAnimation;

    public boolean scrollCalendar = true;

    int firstVisibleDate,lastVisibleDate;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        hsvCalendar = (MyHorizontalScrollView) findViewById(R.id.hsvCalendar);
//
//        tvMonth = (TextView) findViewById(R.id.tvMonth);
//
//        llCalendar = (LinearLayout) findViewById(R.id.llCalendar);
//
//        tvSelectedDate = (TextView) findViewById(R.id.tvSelectedDate);
//
//        rvSubscriptions = (RecyclerView) findViewById(R.id.rvSubscriptions);
//
//        ivEmptyBasket = (ImageView) findViewById(R.id.ivEmptyBasket);
//        tvNoSubscriptions = (TextView) findViewById(R.id.tvNoSubscriptions);
//
//        rlAddProducts = (RelativeLayout) findViewById(R.id.rlAddProducts);
//        ivAdd = (ImageView) findViewById(R.id.ivAdd);
//        tvAddProducts = (TextView) findViewById(R.id.tvAddProducts);
//
//        rlSaveChanges = (RelativeLayout) findViewById(R.id.rlSaveChanges);
//        tvSaveChanges = (TextView) findViewById(R.id.tvSaveChanges);
//
//        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
//
//        tvTotalQuantity = (TextView) findViewById(R.id.tvTotalQuantity);
//
//        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
//
//        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
//
//        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_animation);
//
//        initViews();
//
//        getCalendar();
//    }

    private void initViews(){
        calendar.setTime(DateOperations.getTodayStartDate());
        calendar.add(Calendar.DATE, -15);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);

        for (int i = 0; i < 30; i++){
            subscriptionMasterMap.put(calendar.getTimeInMillis(), new ArrayList<SubscriptionMasterSmall>());

            View view = LayoutInflater.from(this).inflate(R.layout.calendar_date_view_item, null, false);
            TextView tvDay = (TextView) view.findViewById(R.id.tvDay);
            TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
            ImageView ivDot = (ImageView)view.findViewById(R.id.ivDot);

            view.measure(0, 0);
            dateViewWidth = tvDate.getMeasuredWidth();


            if(calendar.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) ){
                ivDot.setVisibility(View.VISIBLE);
                selectedDate = calendar.getTime();

                tvDate.setBackgroundResource(R.drawable.calendar_date_background);
                tvSelectedDate.setText(Commons.dateFormat.format(calendar.getTime()));

                tvMonth.setText(calendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH));
            }

            tvDay.setText(Commons.calendarDayFormat.format(calendar.getTime()));
            tvDate.setText(Commons.calendarDateFormat.format(calendar.getTime()));
            tvDate.setTag(calendar.getTime());

            llCalendar.addView(view);

            calendar.add(Calendar.DATE, 1);


            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < llCalendar.getChildCount(); i++) {
                        TextView tvDate = (TextView) llCalendar.getChildAt(i).findViewById(R.id.tvDate);
                        tvDate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                    TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
                    tvDate.setBackgroundResource(R.drawable.calendar_date_background);

                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -15);

                    selectedDate = (Date) tvDate.getTag();

                    if(rlSaveChanges.getVisibility() == View.VISIBLE){
                        rlSaveChanges.startAnimation(slideDownAnimation);
                        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                rlSaveChanges.setVisibility(View.GONE);
                                rlAddProducts.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }


                    int difference = DateOperations.getDateDifference(calendar.getTime(), selectedDate);

                    if(difference - firstVisibleDate < 3)
                        hsvCalendar.smoothScrollTo((difference - 2) * dateViewWidth, 0);
                    else if(difference - lastVisibleDate > -3)
                        hsvCalendar.smoothScrollTo((firstVisibleDate + 1) * dateViewWidth, 0);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(selectedDate);

                    tvMonth.setText(cal.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH));


                    updateUI();
                }
            });
        }

        llCalendar.getChildAt(0).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        dateViewWidth = llCalendar.getChildAt(0).getMeasuredWidth();

//        tvMonth.setText(Commons.calendarMonthFormat.format(Calendar.getInstance().getTime()));

        hsvCalendar.setOnScrollChangedListener(new MyHorizontalScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {

                firstVisibleDate = x/dateViewWidth;
                lastVisibleDate = firstVisibleDate + scrollView.getWidth()/dateViewWidth;

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scrollCalendar){
                    hsvCalendar.smoothScrollTo(14 * dateViewWidth, 0);
                }
            }
        }, 500);


        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvSubscriptions.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        rvSubscriptions.setLayoutManager(linearLayoutManager);

        calendarAdapter = new CalendarAdapter(this, subscriptionMasterList,cal);
        rvSubscriptions.setAdapter(calendarAdapter);
        rvSubscriptions.setNestedScrollingEnabled(false);


        tvSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DateOperations.getDateDifference(selectedDate, DateOperations.getTomorrowStartDate()) == 0){
                    buildCartMaster();
                }else {
                    buildSubscriptionUpdateList();
                }
            }
        });
    }

    private void buildSubscriptionUpdateList(){
        List<SubscriptionUpdateBean> subscriptionUpdateBeanList = new ArrayList<>();

        for (SubscriptionMasterSmall subscriptionMaster : subscriptionMasterList) {
            SubscriptionUpdateBean subscriptionUpdateBean = new SubscriptionUpdateBean();
            subscriptionUpdateBean.setSubscriptionMasterId(subscriptionMaster.getId());
            subscriptionUpdateBean.setQuantity(subscriptionMaster.getProductQuantity());
            subscriptionUpdateBean.setDate(DateOperations.getStartOfDate(selectedDate).getTime());

            subscriptionUpdateBeanList.add(subscriptionUpdateBean);
        }

        saveChanges(subscriptionUpdateBeanList);
    }

    private void updateUI(){

        subscriptionMasterList.clear();

//        isEditable = false;

        populateScheduleMaterBean(selectedDate.getTime());

        calendarAdapter.setCurrentDate(selectedDate);
        subscriptionMasterList.addAll(subscriptionMasterMap.get(DateOperations.getStartOfDate(selectedDate).getTime()));

        calendarAdapter.notifyDataSetChanged();

//        for(int i=0 ; i<subscriptionMasterList.size();i++) {
//
//            if(!BaseApplication.getInstance().getSubscriptionProductTypes().contains(subscriptionMasterList.get(i).getProductType())){
//
//                isEditable = true;
//                break;
//
//            }
//
//
//        }

        if(DateOperations.getDateDifference(selectedDate, DateOperations.getTomorrowStartDate()) == 0){

            ivAdd.setImageResource(R.drawable.ic_add_green);
            rlAddProducts.setBackgroundColor(getResources().getColor(android.R.color.white));

            calendarAdapter.showFeedbackButtons(false);

            tvAddProducts.setText("ADD MORE PRODUCTS");

                tvAddProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CalendarActivity.this, ProductCategoryListActivity.class);
                        startActivity(intent);
                    }
                });
//            }

            tvAddProducts.setTextColor(getResources().getColor(R.color.ninja_green));
        } else  if(DateOperations.getDateDifference(selectedDate, DateOperations.getTodayStartDate()) == 0){

            calendarAdapter.showFeedbackButtons(true);

//            }


        }else{
            calendarAdapter.showFeedbackButtons(false);
            rlAddProducts.setBackgroundColor(getResources().getColor(R.color.light_gray_1));
            ivAdd.setImageResource(R.drawable.ic_add_gray);
            tvAddProducts.setText("ADD MORE PRODUCTS");
            tvAddProducts.setTextColor(getResources().getColor(R.color.light_gray_6));
            tvAddProducts.setOnClickListener(null);
        }

        tvSelectedDate.setText(Commons.dateFormat.format(selectedDate));

        totalAmount = 0.0;
        totalQuantity = 0;

        for (SubscriptionMasterSmall subscriptionMasterSmall : subscriptionMasterList) {
            totalAmount += subscriptionMasterSmall.getProductUnitCost() * subscriptionMasterSmall.getProductQuantity();
            totalQuantity += subscriptionMasterSmall.getProductQuantity();
        }

        tvTotalAmount.setText("₹" + totalAmount);

        tvTotalQuantity.setText(totalQuantity + " items of worth");

        calendarAdapter.notifyDataSetChanged();

        if (subscriptionMasterList != null && subscriptionMasterList.size() > 0) {
            rvSubscriptions.setVisibility(View.VISIBLE);
            ivEmptyBasket.setVisibility(View.GONE);
            tvNoSubscriptions.setVisibility(View.GONE);
        } else {
            rvSubscriptions.setVisibility(View.GONE);

            if (selectedDate.after(DateOperations.getYesterdayStartDate())){
                ivEmptyBasket.setVisibility(View.VISIBLE);
                tvNoSubscriptions.setVisibility(View.VISIBLE);
            } else {
                ivEmptyBasket.setVisibility(View.GONE);
                tvNoSubscriptions.setVisibility(View.GONE);
            }
        }
    }

    private void populateScheduleMaterBean(Long key){

            subscriptionMasterMap.get(key).clear();
            for (int i = 0; i < scheduleBean.getSubscriptionMasterList().size(); i++) {
                SubscriptionMasterSmall subscriptionMasterSmall = scheduleBean.getSubscriptionMasterList().get(i);
                subscriptionMasterSmall.setTransientCalendarProductQuantity(subscriptionMasterSmall.getProductQuantity());

                if (subscriptionMasterSmall.getStartDate() <= key && subscriptionMasterSmall.getEndDate() >= key &&
                        DateOperations.getDateDifferenceFromLong(key, subscriptionMasterSmall.getStartDate()) % subscriptionMasterSmall.getSubscriptionType() == 0){
                    try {
                        subscriptionMasterMap.get(key).add(subscriptionMasterSmall.clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (int i = 0; i < scheduleBean.getSubscriptionExceptionList().size(); i++) {
                SubscriptionException subscriptionException = scheduleBean.getSubscriptionExceptionList().get(i);

                if (subscriptionException.getExceptionDate().equals(key)){
                    List<SubscriptionMasterSmall> subscriptionBeans = subscriptionMasterMap.get(key);

                    SubscriptionMasterSmall subscriptionMasterSmall = new SubscriptionMasterSmall();
                    subscriptionMasterSmall.setId(subscriptionException.getSubscriptionMasterId());

                    int index = subscriptionBeans.indexOf(subscriptionMasterSmall);

                    if (index >= 0){
                        if (subscriptionException.getProductQuantity() != 0){
                            subscriptionBeans.get(index).setProductQuantity(subscriptionException.getProductQuantity());
                            subscriptionBeans.get(index).setTransientCalendarProductQuantity(subscriptionException.getProductQuantity());
                            subscriptionBeans.get(index).setSubscriptionExceptionId(subscriptionException.getId());
                        } else {
                            subscriptionBeans.remove(index);
                        }
                    }
                }
            }

    }

    public void updateReportView(){

        ivAdd.setImageResource(R.drawable.report);
        rlAddProducts.setBackgroundColor(getResources().getColor(R.color.ninja_green));

        tvAddProducts.setText("REPORT");

        tvAddProducts.setTextColor(getResources().getColor(R.color.white));

        tvAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(CalendarActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
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
                Hotline.sendMessage(CalendarActivity.this,"Other Products not delivered", calendarAdapter.getFeedback());

            }
        });



    }

    public void checkIfSubscriptionsUpdated(){
        boolean saveChanges = false;

        for (SubscriptionMasterSmall subscriptionMaster : subscriptionMasterList) {
            if (subscriptionMaster.getProductQuantity() != subscriptionMaster.getTransientCalendarProductQuantity()){
                saveChanges = true;
                break;
            }
        }

        if (saveChanges){
            if(rlSaveChanges.getVisibility() == View.GONE){
                rlSaveChanges.setVisibility(View.VISIBLE);
                rlSaveChanges.startAnimation(slideUpAnimation);
            }

//            isEditable = true;
        } else {
            if(rlSaveChanges.getVisibility() == View.VISIBLE){
                rlSaveChanges.startAnimation(slideDownAnimation);
                slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rlSaveChanges.setVisibility(View.GONE);
                        rlAddProducts.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

//            isEditable = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(CalendarActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(CalendarActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    public void getCalendar(){
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/calendar/getCalendarNewApi";
            Log.d(TAG, "Get Calendar Url : " + url);

            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Get Calendar Response : " + response);

                    scheduleBean = new Gson().fromJson(String.valueOf(response), ScheduleBean.class);

                    if(scheduleBean != null){
                        updateUI();
                    } else{
                        Toast.makeText(CalendarActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Get Calendar Data Error : " + error.toString());

                    hideDialog();
                    Toast.makeText(CalendarActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("id", sharedPreferences.getString(Constants.USER_ID, ""));
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

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(CalendarActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveChanges(final List<SubscriptionUpdateBean> subscriptionUpdateBeanList){
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/subscriptionException/updateSubscription";
            Log.d(TAG, "Update Subscription Url : " + url);

            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Update Subscription Response : " + response);

                    StatusBean statusBean = new Gson().fromJson(String.valueOf(response), StatusBean.class);

                    if(statusBean != null){
                        if(statusBean.getStatus() == 1){
                            Toast.makeText(CalendarActivity.this, "Your changes have been successfully updated", Toast.LENGTH_SHORT).show();
                        } else {
                            if (statusBean.getMessage() != null){
                                Toast.makeText(CalendarActivity.this, statusBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CalendarActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }

                        scrollCalendar = false;

                        rlSaveChanges.setVisibility(View.GONE);
                        rlAddProducts.setVisibility(View.VISIBLE);

                        getCalendar();
                    } else{
                        Toast.makeText(CalendarActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Update Subscription  Data Error : " + error.toString());

                    hideDialog();
                    Toast.makeText(CalendarActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CalendarActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void checkoutCart(final CartItemMaster cartItemMaster) {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/cart/checkout";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response :" + response.toString());
                    Gson gson = new Gson();

                    CartCheckoutResponseBean cartCheckoutResponseBean = new Gson().fromJson(String.valueOf(response), CartCheckoutResponseBean.class);

                    if(response != null) {
                        hideDialog();

                        if(cartCheckoutResponseBean.getStatus()) {

                            if(rlSaveChanges.getVisibility() == View.VISIBLE){
                                rlSaveChanges.startAnimation(slideDownAnimation);
                                slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        rlSaveChanges.setVisibility(View.GONE);
                                        rlAddProducts.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }


                            new SweetAlertDialog(CalendarActivity.this, SweetAlertDialog.SUCCESS_TYPE, robotoBold, robotoLight)
                                    .setTitleText("Order has been placed successfully")
                                    .setContentText(getString((R.string.thanks_subscription_normal)))
                                    .setConfirmText("Ok,cool !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            getCalendar();

//                                            BaseApplication.reloadList = true;
//
//                                            sendEcommerce(BaseApplication.getInstance().getCart().getTotalProductList());
//
//                                            BaseApplication.getInstance().setCart(new Cart());
//
//                                            Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
//                                            BaseApplication.reloadHomePage = true;
//                                            startActivity(intent);
//
//                                            finish();
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
                            new SweetAlertDialog(CalendarActivity.this, SweetAlertDialog.ERROR_TYPE, robotoBold, robotoLight)
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
                    Log.e(TAG, "Error : " + error.toString());
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

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(CalendarActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void buildCartMaster(){

        CartItemMaster cartItemMaster = new CartItemMaster();
        cartItemMaster.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
        cartItemMaster.setPromoCode(null);

        cartItemMaster.getCartItemList().clear();

        for (int i = 0; i < subscriptionMasterList.size(); i++) {
            SubscriptionMasterSmall subscriptionMasterSmall = subscriptionMasterList.get(i);

            CartItem cartItem = new CartItem();
            cartItem.setProductMasterId(subscriptionMasterSmall.getProductMasterId());
            cartItem.setQuantity(subscriptionMasterSmall.getProductQuantity());

            if (subscriptionMasterSmall.getId() != null) {
                cartItem.setSubscriptionId(subscriptionMasterSmall.getId());
                cartItemMaster.getCartItemList().add(cartItem);
            } else {
                if(subscriptionMasterSmall.getProductType() != null){
                    if (subscriptionMasterSmall.getProductQuantity() != 0 && subscriptionMasterSmall.getProductType() != 53) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                } else {
                    if (subscriptionMasterSmall.getProductQuantity() != 0) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                }
            }
        }

        checkoutCart(cartItemMaster);

    }



}
