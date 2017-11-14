package com.villagemilk;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import com.villagemilk.beans.Cart;
import com.villagemilk.beans.ProductCategory;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.beans.User;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.model.calendar.Subscription;
import com.villagemilk.util.Constants;
import com.villagemilk.util.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends MultiDexApplication {

    public static boolean reloadHomePage = false;

    public static boolean reloadList = false;

    private Cart cart = new Cart();

    private List<ProductCategory> productCategoryList = new ArrayList<>();

    public List<SubscriptionWrapper> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<SubscriptionWrapper> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    private List<SubscriptionWrapper> subscriptionList = new ArrayList<>();

    public List<String> getDeliveryDays() {
        return Arrays.asList(deliveryDays.split("\\s*,\\s*"));
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    String deliveryDays;

    public List<Integer> getSubscriptionProductTypes() {
        return subscriptionProductTypes;
    }

    public void setSubscriptionProductTypes(List<Integer> subscriptionProductTypes) {
        this.subscriptionProductTypes = subscriptionProductTypes;
    }

    private List<Integer> subscriptionProductTypes;

    private Set<Integer> excludedCartProductTypes = new HashSet<>();

    private User user;

    public CustomProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(CustomProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    CustomProgressDialog progressDialog;


    //    public static final String mixpanelToken="6586815eedd2b349aab34a57cfd69423";//DEV
    public static final String mixpanelToken="ef3ec9aadda31eff4349cce4de21ce94";//Prod
    public static final String google_project_no="770204622";

    @Override
    public void onCreate() {
        super.onCreate();

        // Register LocalyticsActivityLifecycleCallbacks
//        registerActivityLifecycleCallbacks(new LocalyticsActivityLifecycleCallbacks(this));

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Condensed.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        FacebookSdk.sdkInitialize(getApplicationContext());

        AccountKit.initialize(getApplicationContext());

        HotlineConfig hlConfig=new HotlineConfig(Constants.HOTLINE_APP_ID,Constants.HOTLINE_APP_KEY);
        Hotline.getInstance(getApplicationContext()).init(hlConfig);
        hlConfig.setVoiceMessagingEnabled(false);
        hlConfig.setCameraCaptureEnabled(true);
        hlConfig.setPictureMessagingEnabled(true);

//         Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);

        sInstance = this;
    }

    public void removeSubscription(String productMaster){

        SubscriptionWrapper wrapper = null;


        for(SubscriptionWrapper subscription :subscriptionList){

            if(subscription.getProductMaster().equals(productMaster)) {
                wrapper = subscription;
                break;
            }


        }

        subscriptionList.remove(wrapper);



    }

    public SubscriptionWrapper containsSubscriptionMaster(String productMaster){

        if(subscriptionList == null)
            return null;

        for(SubscriptionWrapper subscription :subscriptionList){

            if(subscription.getProductMaster().equals(productMaster))
                return subscription;

        }

        return null;
    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public Set<Integer> getExcludedCartProductTypes() {
        return excludedCartProductTypes;
    }

    public void setExcludedCartProductTypes(Set<Integer> excludedCartProductTypes) {
        this.excludedCartProductTypes = excludedCartProductTypes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            Tracker t = analytics.newTracker(R.xml.custom_tracker);
//            t.enableAdvertisingIdCollection(true);
//            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static BaseApplication sInstance;

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized BaseApplication getInstance()
    {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}