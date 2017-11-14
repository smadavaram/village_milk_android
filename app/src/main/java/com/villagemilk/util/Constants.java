package com.villagemilk.util;

import java.text.DecimalFormat;

/**
 * Created by Deepam on 15-Apr-15.
 */
public class Constants {
    public static final DecimalFormat priceDisplay = new DecimalFormat("#.##");

    public static Double usersLat = 19.624897;//19.218331;
    public static Double usersLng = 73.474874;//72.97809;

//    public static final String API_PROD = "http://production.dailyninja.appspire.in:8080";  /*(For Production Purpose)*/
    public static final String API_PROD = "http://139.59.33.222:8080";  /*(For Production Purpose)*/
//     public static final String API_PROD = "http://dev.dailyninja.in:8080";
//     public static final String API_PROD = "http://1A92.168.100.31:8085";
//    public static final String API_PROD = "http://54.169.207.85:8080"; /*(For Development Purpose)*/

    //User Preferences
    public static final String NINJA_PREFS = "NINJA_PREFS";
    public static final String USER_ID = "USER_ID";
    public static final String PROFILE_PIC_URL = "PROFILE_PIC_URL";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String STATUS = "STATUS";
    public static final String USER_STATUS = "USER_STATUS";

    public static final String userDoesntLikeUs = "userDoesntLikeUs";

    public static final String UserType = "UserType";
    public static final String BuildingUpdated = "Building Updated";

    public static final String paytm_retail_id_ref = "paytm_retail_id_ref";

    public static final int SPLASH_TIME_OUT = 1500;

    public static final int GET_ACCOUNTS = 101;

    public static final String HOTLINE_APP_ID = "7492b6e0-1bb9-445d-af88-0464d638039b";
    public static final String HOTLINE_APP_KEY = "be7d9e10-1c55-4b57-b519-4fd74e55d10b";

    //Intent Extra Constants
    public static final String INTENT_SOURCE = "intent_source";
    public static final String BUILDING_ID = "BUILDING_ID";
    public static final String BUILDING_NAME = "BUILDING_NAME";
    public static final String BANNER_ID = "BANNER_ID";
    public static final String PRODUCT_CATEGORY_ID = "PRODUCT_CATEGORY_ID";

    public static final String HOME_ACTIVITY = "HomeActivity";
    public static final String CALENDAR_ACTIVITY = "CalendarActivity";
    public static final String NEW_CART_ACTIVITY = "CartActivity";

    public static final String GCM_TOKEN = "GCM_TOKEN";
}
