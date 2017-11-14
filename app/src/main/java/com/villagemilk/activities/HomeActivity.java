package com.villagemilk.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.BannerPagerAdapter;
import com.villagemilk.adapters.ComingTomorrowSubscriptionsAdapter;
import com.villagemilk.adapters.ProductCategoriesListAdapter;
import com.villagemilk.adapters.QuickAddProductsListAdapter;
import com.villagemilk.beans.Banner;
import com.villagemilk.beans.DeliveryDays;
import com.villagemilk.beans.HomeBean;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.beans.User;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.dialogs.SweetProductDialog;
import com.villagemilk.fragments.FragmentHomeCategories;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.services.RegistrationIntentService;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.view.ActivityBillingAddress;
import com.villagemilk.view.ActivityBuildingList;
import com.villagemilk.view.ActivityCalendar;
import com.easyandroidanimations.library.BounceAnimation;
import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineCallbackStatus;
import com.freshdesk.hotline.HotlineUser;
import com.freshdesk.hotline.UnreadCountCallback;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.villagemilk.view.ActivitySearch;
import com.villagemilk.view.ActivitySubscriptionList;
import com.villagemilk.view.ActivityUserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//import cn.pedant.SweetAlert.SweetAlertDialog;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import hotchemi.android.rate.StoreType;

import static com.villagemilk.R.id.home;
import static com.villagemilk.R.id.rlNoTomm;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

    private NavigationView navigationView;

    private DrawerLayout drawerLayout;

    private RelativeLayout rlSearch;

    private NestedScrollView nestedScrollView;

    private HorizontalScrollView hsvTomorrow;
    private ImageView ivExpandButton;

    private RelativeLayout rlNoTomorrow;
    private RelativeLayout cardTomorrow;

    private LinearLayout lvTomorrow;

    private ComingTomorrowSubscriptionsAdapter comingTomorrowSubscriptionsAdapter;

    private ImageView offerImg;

    private ImageView ivFeaturedProduct;

    private TextView tvFlashSale;

    private EditText etSearch;

    private RecyclerView rvQuickAddProducts;
    private QuickAddProductsListAdapter quickAddProductsListAdapter;
    private RecyclerView.LayoutManager layoutManagerQuickAddProducts;

    private TextView tvProductCategories;

    private RecyclerView rvProductCategories;
    private ProductCategoriesListAdapter productCategoriesListAdapter;
    private RecyclerView.LayoutManager layoutManagerProductCategories;

    private ViewPager viewPagerBanner;

    private BannerPagerAdapter bannerPagerAdapter;

    private CirclePageIndicator circlePageIndicator;

    //Cart Layout
    public LinearLayout llCart;


    public LinearLayout llFragment;

    private RelativeLayout rlBill;
    public TextView tvCartAmount;
    public TextView tvCartQuantity;

    public TextView tvchatbadge;

    private RelativeLayout rlCheckout;

    //Tab Layout
    private RelativeLayout llHomeTab;

    private RelativeLayout rlMySubscriptions;

    private RelativeLayout rlSchedule;

    private RelativeLayout rlWallet;
    private ImageView ivWallet;
    private TextView tvWallet;

    private TextView tvExclusiveOffers;

    private RelativeLayout rlSupport;

    boolean autoRotate = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private HomeBean homeBean;

    FragmentHomeCategories fragmentHomeCategories;

    private ArrayList<SubscriptionMasterSmall> tomorrowItems = new ArrayList<>();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private int tommDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        fragmentHomeCategories = new FragmentHomeCategories();

        getSupportFragmentManager().beginTransaction().replace(R.id.llFragment,fragmentHomeCategories).commit();


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollview);

        hsvTomorrow = (HorizontalScrollView) findViewById(R.id.hsvtomorrow);
        ivExpandButton = (ImageView) findViewById(R.id.ivexpandbutton);

        cardTomorrow = (RelativeLayout) findViewById(R.id.cardTomorrow);

        lvTomorrow = (LinearLayout) findViewById(R.id.lvTomorrow);

        rlNoTomorrow = (RelativeLayout) findViewById(rlNoTomm);

        offerImg = (ImageView) findViewById(R.id.offerImg);

        ivFeaturedProduct = (ImageView) findViewById(R.id.ivFeaturedProduct);

        etSearch = (EditText) findViewById(R.id.etSearch);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(TextUtils.isEmpty(etSearch.getText())){
                        Toast.makeText(HomeActivity.this,"Enter Search Text",Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    Intent intent = new Intent(HomeActivity.this, ActivitySearch.class);
                    intent.putExtra("searchText",v.getText().toString());

                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        tvFlashSale = (TextView) findViewById(R.id.tvFlashSale);
        tvFlashSale.setTypeface(robotoBold);

        rvQuickAddProducts = (RecyclerView) findViewById(R.id.rvQuickAddProducts);

        tvProductCategories = (TextView) findViewById(R.id.tvProductCategories);
        tvProductCategories.setTypeface(robotoBold);

        tvchatbadge = (TextView) findViewById(R.id.tvchatbadge);

        rvProductCategories = (RecyclerView) findViewById(R.id.rvProductCategories);

        viewPagerBanner = (ViewPager) findViewById(R.id.viewPagerBanner);

        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);


        llCart = (LinearLayout) findViewById(R.id.llCart);

        llFragment = (LinearLayout) findViewById(R.id.llFragment);

        rlBill = (RelativeLayout) llCart.findViewById(R.id.rlBill);
        tvCartAmount = (TextView) rlBill.findViewById(R.id.tvCartAmount);
        tvCartQuantity = (TextView) rlBill.findViewById(R.id.tvCartQuantity);

        rlCheckout = (RelativeLayout) llCart.findViewById(R.id.rlCheckout);

        llHomeTab = (RelativeLayout) findViewById(R.id.llHomeTab);

        rlMySubscriptions = (RelativeLayout) llHomeTab.findViewById(R.id.rlMySubscriptions);

        rlSchedule = (RelativeLayout) llHomeTab.findViewById(R.id.rlSchedule);

        rlWallet = (RelativeLayout) llHomeTab.findViewById(R.id.rlWallet);
        ivWallet = (ImageView) rlWallet.findViewById(R.id.ivWallet);
        tvWallet = (TextView) rlWallet.findViewById(R.id.tvWallet);

        tvExclusiveOffers = (TextView)findViewById(R.id.tvExclusiveOffers);

        rlSupport = (RelativeLayout) llHomeTab.findViewById(R.id.rlSupport);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        if (getIntent().getExtras() != null) {
            String deeplinkVal = getIntent().getStringExtra("deeplnk");

            if (deeplinkVal != null && deeplinkVal.length() > 0) {
                gotoListingPage(Integer.parseInt(deeplinkVal));
                return;
            }
        }

        getHome(false);
    }

    private void initViews(final boolean onResume) {
        comingTomorrowSubscriptionsAdapter = new ComingTomorrowSubscriptionsAdapter(this, tomorrowItems);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
                boolean sentToken = sharedPreferences.getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (!homeBean.getPushTokenSet() && checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

//        getTomorrowsItem();

        if (!onResume) {
            showRechargePopup(homeBean.getBillingMaster().getAmount());
        }

        rlNoTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();
            }
        });

        if (homeBean.getPaytmRetailId() != null && homeBean.getPaytmRetailId().length() > 0) {
            editor.putString(Constants.paytm_retail_id_ref, homeBean.getPaytmRetailId());
            editor.apply();
        }

        if (homeBean.getAppUpdatePromptStatus() == 3) {
            showUnsupportedDialog();
        } else if (homeBean.getAppUpdatePromptStatus() == 2) {
            showUpgradeDialog();
        } else if (!onResume) {
            appRater();
        }

        if (homeBean.getCustomerPromoMessageText() != null && homeBean.getCustomerPromoMessageText().length() > 0) {
            findViewById(R.id.userOffer).setVisibility(View.VISIBLE);

            TextView tv = (TextView) findViewById(R.id.tvUserOffer);
            tv.setText(homeBean.getCustomerPromoMessageText());
            tv.setTypeface(robotoBold);
        } else {
            findViewById(R.id.userOffer).setVisibility(View.GONE);
        }


        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hsvTomorrow.getVisibility() == View.GONE) {
                    hsvTomorrow.setVisibility(View.VISIBLE);
                    ivExpandButton.setImageDrawable(getResources().getDrawable(R.drawable.u_arrow));
                } else if (hsvTomorrow.getVisibility() == View.VISIBLE) {
                    hsvTomorrow.setVisibility(View.GONE);
                    ivExpandButton.setImageDrawable(getResources().getDrawable(R.drawable.d_arrow));
                }
            }
        });

        if (homeBean.getWelcomeMessage() != null && homeBean.getWelcomeMessage().length() > 0) {
            new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                    .setContentText(homeBean.getWelcomeMessage())
                    .setTitleText("Welcome")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog2) {
                            sDialog2.dismissWithAnimation();
                        }
                    }).show();
        }

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ProductSearchActivity.class);
//                intent.putExtra("intent_source", "HomeActivity");
//                intent.putExtra("intent_flag", "no_product");
//                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();


        bannerPagerAdapter = new BannerPagerAdapter(getSupportFragmentManager(), homeBean.getBannerList());

        viewPagerBanner.setAdapter(bannerPagerAdapter);


        viewPagerBanner.setOffscreenPageLimit(homeBean.getBannerList().size());


        circlePageIndicator.setViewPager(viewPagerBanner);

        if(!homeBean.getBannerList().isEmpty()) {

            if (!autoRotate) {
                pageSwitcher();
                autoRotate = true;

                viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        page = position;

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


            }

        }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvProductCategories.setHasFixedSize(true);

        // use a linear layout manager
        layoutManagerProductCategories = new GridLayoutManager(this, 4);
        rvProductCategories.setLayoutManager(layoutManagerProductCategories);

        productCategoriesListAdapter = new ProductCategoriesListAdapter(this, homeBean.getProductCategoryList());
        rvProductCategories.setAdapter(productCategoriesListAdapter);
        rvProductCategories.setNestedScrollingEnabled(false);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvQuickAddProducts.setHasFixedSize(true);

        // use a linear layout manager
        layoutManagerQuickAddProducts = new LinearLayoutManager(this);
        layoutManagerQuickAddProducts = new StaggeredGridLayoutManager(3,1);
        rvQuickAddProducts.setLayoutManager(layoutManagerQuickAddProducts);
        rvQuickAddProducts.setNestedScrollingEnabled(false);

        popupObject();

        quickAddProductsListAdapter = new QuickAddProductsListAdapter(this, homeBean.getFlashProductList());
        rvQuickAddProducts.setAdapter(quickAddProductsListAdapter);
        if(homeBean.getFlashProductList().size() == 0)
            tvFlashSale.setVisibility(View.GONE);
        else
            tvFlashSale.setVisibility(View.VISIBLE);

        if(homeBean.getBillingMaster().getAmount()!=0)
            tvWallet.setText("$ " + String.format("%.2f", homeBean.getBillingMaster().getAmount()*(-1)));
        else
            tvWallet.setText("$ " + 0.00);

        try {

            if(homeBean.getBillingMaster().getAmount() >=0){

                tvWallet.setTextColor(Color.parseColor("#FF0000"));
                ivWallet.setImageResource(R.drawable.ic_wallet_red);

            }else if(homeBean.getBillingMaster().getAmount()>=-150){
                tvWallet.setTextColor(Color.parseColor("#FFFCCC0A"));
                ivWallet.setImageResource(R.drawable.ic_wallet_yellow);

            }else {

                tvWallet.setTextColor(Color.parseColor("#00FF00"));
                ivWallet.setImageResource(R.drawable.ic_wallet_green);

            }

        }catch (Exception e){
            e.printStackTrace();
        }



        rlCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ActivityProductList.class);
                startActivity(intent);
            }
        });

        rlMySubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ActivitySubscriptionList.class);
                startActivity(intent);
            }
        });

        rlSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ActivityCalendar.class);
                startActivity(intent);
            }
        });

        rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PaymentsActivity.class);
                startActivity(intent);
            }
        });

        rlSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hotline.showConversations(getApplicationContext());
//                try {
//                    mixpanel.track("Talk to Us");
//                } catch (Exception e) {
//                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                }
            }
        });

        if (navigationView != null) {
            ImageView ivProfilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivProfilePic);
            TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
            TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
            TextView tvBuildingName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserAddress);

            tvUserName.setTypeface(robotoMedium);
            tvEmail.setTypeface(robotoMedium);
            tvBuildingName.setTypeface(robotoLight);

            if (!TextUtils.isEmpty(sharedPreferences.getString(Constants.PROFILE_PIC_URL, ""))) {
//                Picasso.with(this).load(sharedPreferences.getString(Constants.PROFILE_PIC_URL, "")).into(ivProfilePic);
                ImageLoader.getInstance().displayImage(sharedPreferences.getString(Constants.PROFILE_PIC_URL,""),ivProfilePic);
//                Picasso.with(this).load(sharedPreferences.getString(Constants.PROFILE_PIC_URL, "")).into(ivProfilePic);
            }

            tvUserName.setText(sharedPreferences.getString(Constants.USER_NAME, ""));
            tvEmail.setText(sharedPreferences.getString(Constants.USER_EMAIL, ""));

            if(homeBean.getBillingMaster().getUser().getFlat()!=null && homeBean.getBillingMaster().getUser().getBuilding()!=null)
                tvBuildingName.setText(homeBean.getBillingMaster().getUser().getFlat() + ", " + homeBean.getBillingMaster().getUser().getBuilding().getBuildingName());

            navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userProfileIntent = new Intent(HomeActivity.this, ActivityUserProfile.class);
                    Bundle extras = new Bundle();
                    extras.putString("balance","$ "+homeBean.getBillingMaster().getAmount());
                    userProfileIntent.putExtras(extras);
                    startActivity(userProfileIntent);
                }
            });
        }
    }



    private void registerReceiver() {
        if (mRegistrationBroadcastReceiver != null && !isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    public void showCalendar() {
        Intent intent = new Intent(this, ActivityCalendar.class);
        startActivity(intent);
    }

    public void showRechargePopup(double billAmount) {
        if (billAmount > 100) {
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                    .setTitleText("Recharge Now")
                    .setContentText(getString(R.string.pay_bill_now))
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            Intent intent = new Intent(HomeActivity.this, PaymentsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    public void gotoListingPage(int productCategory) {
//        Intent intent = new Intent(this, ProductListActivity.class);
//        intent.putExtra(Constants.PRODUCT_CATEGORY_ID, productCategory);
//        intent.putExtra(Constants.INTENT_SOURCE, Constants.HOME_ACTIVITY);
//        startActivity(intent);
    }

    public void getProductTypesInFocus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvProductCategories.measure(0, 0);
                nestedScrollView.smoothScrollTo(0, rvProductCategories.getTop());
            }
        }, 200);
    }

    private void initCart() {
        if (Commons.getCartQuantity() > 0) {
            tvCartAmount.setText("$" + Commons.getCartAmount());
            tvCartQuantity.setText(Commons.getCartQuantity() + " items");

            llCart.setVisibility(View.VISIBLE);
        } else {
            llCart.setVisibility(View.GONE);
        }

        if (quickAddProductsListAdapter != null) {
            quickAddProductsListAdapter.notifyDataSetChanged();
        }
    }

    public void popupObject() {
        final ProductMaster popupObject = homeBean.getPopupProduct();

        if (popupObject != null) {
            HashMap<String, Object> popupMap = new HashMap<>();
            popupMap.put("Title", popupObject.getProductName());
            popupMap.put("Price", popupObject.getProductUnitPrice());
            popupMap.put("Unit", popupObject.getProductUnitSize());
            if(popupObject.getStrikePrice()!=null)
                popupMap.put("Strikeprice", popupObject.getStrikePrice());
            if(popupObject.getProductImage()!=null)
                popupMap.put("Image", popupObject.getProductImage());
            if(popupObject.getSpecialText()!=null)
                popupMap.put("specialText", popupObject.getSpecialText());

            SweetProductDialog sweetProductDialog = new SweetProductDialog(this, SweetProductDialog.NORMAL_TYPE, robotoBold, robotoLight, popupMap);

            sweetProductDialog.setTitleText(popupObject.getFlashShortText());

            sweetProductDialog.setConfirmText("Add to Cart");
            sweetProductDialog.setConfirmClickListener(new SweetProductDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetProductDialog sweetAlertDialog) {
                    BaseApplication.reloadList = false;
                    sweetAlertDialog.dismissWithAnimation();

                    if (!BaseApplication.getInstance().getCart().getCartProductList().contains(popupObject)) {
                        popupObject.setProductQuantity(1);
                        BaseApplication.getInstance().getCart().getCartProductList().add(popupObject);
                        Toast.makeText(HomeActivity.this, "New Product added to cart. Please proceed to checkout", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(HomeActivity.this, "ProductMaster has been added to cart. Please checkout the cart to order", Toast.LENGTH_SHORT).show();
                        // Get tracker.
                        Tracker t = ((BaseApplication) getApplication()).getTracker(
                                BaseApplication.TrackerName.APP_TRACKER);

                        // Build and send an Event.
                        t.send(new HitBuilders.EventBuilder()
                                .setCategory("Purchase Events")
                                .setAction(("ProductMaster Order FlashPopup"))
                                .setLabel(("Flash Popup Buy"))
                                .build());

//                        try {
//                            mixpanel.track("Home Popup Buy Click");
//                        } catch (Exception e) {
//                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                        }

//                        Intent productListActivity = new Intent(HomeActivity.this, ProductCategoryListActivity.class);
//                        productListActivity.putExtra("intent_source", "HomeActivity");
//                        startActivity(productListActivity);

                        gotoListingPage(popupObject.getProductCategory());

                    } else {
                        Toast.makeText(HomeActivity.this, "You have already added this product to your cart. Please proceed to checkout", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            sweetProductDialog.show();
        } else {

        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void callTrackerForAnalytics() {
        //Toast.makeText(HomeActivity.this, "ProductMaster has been added to cart. Please checkout the cart to order", Toast.LENGTH_SHORT).show();
        // Get tracker.
        Tracker t = ((BaseApplication) getApplication()).getTracker(
                BaseApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("Purchase Events")
                .setAction(("ProductMaster Order"))
                .setLabel(("Flash Buy Now"))
                .build());
    }

    private void showUnsupportedDialog() {
        SweetAlertDialog sd = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE, robotoBold, robotoLight)
                .setTitleText("App Update").setContentText("This version of DailyNinja is not supported anymore. Please update now...")
                .setConfirmText("Update Now")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                });
        sd.setCancelable(false);
        sd.show();
    }

    private void showUpgradeDialog() {
        SweetAlertDialog sd = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                .setTitleText("App Update").setContentText("A new version of DailyNinja is available. Please update now...")
                .setConfirmText("Update Now")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                });

        sd.show();
    }

    private void appRater() {
        if (homeBean != null) {
            AppRate arater = AppRate.with(this)
                    .setStoreType(StoreType.GOOGLEPLAY) //default is Google, other option is Amazon
                    .setInstallDays(homeBean.getInstallDays()) // default 10, 0 means install day.
                    .setLaunchTimes(homeBean.getLaunchTimes()) // default 10 times.
                    .setRemindInterval(homeBean.getRemindInterval()) // default 1 day.
                    .setShowLaterButton(true) // default true.
                    .setDebug(false) // default false.
                    .setCancelable(false) // default false.
                    .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                        @Override
                        public void onClickButton(int which) {
                            Log.d(HomeActivity.class.getName(), Integer.toString(which));

                        }
                    })
                    .setTitle(R.string.new_rate_dialog_title)
                    .setTextLater(R.string.new_rate_dialog_later)
                    .setTextNever(R.string.new_rate_dialog_never)
                    .setTextRateNow(R.string.new_rate_dialog_ok);
            arater.monitor();

            if (arater.shouldShowRateDialog() && !sharedPreferences.getBoolean(Constants.userDoesntLikeUs, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setTitle("Sample Alert");
                builder.setMessage("Do you love Dailyninja ?");
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                editor.putBoolean(Constants.userDoesntLikeUs, true);
                                editor.commit();
                                Hotline.showConversations(getApplicationContext());
//                                try {
//                                    mixpanel.track("I dont love DN");
//                                } catch (Exception e) {
//                                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                                }
                            }
                        });

                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
//                                try {
//                                    mixpanel.track("I love DN");
//                                } catch (Exception e) {
//                                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                                }
                                dialog.dismiss();
                                AppRate.showRateDialogIfMeetsConditions(HomeActivity.this);
                            }
                        });
                builder.show();
            }
        }
    }

    public List<Banner> getBannerList() {
        if (homeBean != null) {
            return homeBean.getBannerList();
        }

        return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_calendar) {
            Intent calendarActivity = new Intent(this, ActivityCalendar.class);
            startActivity(calendarActivity);
        } else if (id == R.id.nav_my_subscriptions) {
            Intent subscriptionListActivity = new Intent(this, ActivitySubscriptionList.class);
            startActivity(subscriptionListActivity);
        } else if (id == R.id.nav_billing_history) {
            Intent billingIntent = new Intent(this, BillingHistoryActivity.class);
            startActivity(billingIntent);
        } else if (id == R.id.nav_payment_history) {
            Intent billingIntent = new Intent(this, BillingListActivity.class);
            startActivity(billingIntent);
        } else if (id == R.id.nav_recharge_wallet) {
            Intent paymentIntent = new Intent(this, PaymentsActivity.class);
            startActivity(paymentIntent);
        } else if (id == R.id.nav_contact_us) {
            Intent contactUsIntent = new Intent(this, ContactUsActivity.class);
            startActivity(contactUsIntent);
        } else if (id == R.id.nav_about_us) {
            Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
            startActivity(aboutUsIntent);
        } /*else if (id == R.id.nav_exclusive_offer) {
            Intent exclusiveOfferIntent = new Intent(this, ActivityExclusiveOffer.class);
            startActivity(exclusiveOfferIntent);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver();

//        if (rvQuickAddProducts != null && rvQuickAddProducts.getVisibility() == View.VISIBLE) {
//            new BounceAnimation(rvQuickAddProducts)
//                    .setBounceDistance(5)
//                    .setNumOfBounces(10)
//                    .setDuration(5000)
//                    .animate();
//        }

        Hotline.getInstance(getApplicationContext()).getUnreadCountAsync(new UnreadCountCallback() {
            @Override
            public void onResult(HotlineCallbackStatus hotlineCallbackStatus, int i) {
                if(i>0) {
                    tvchatbadge.setVisibility(View.VISIBLE);
                    tvchatbadge.setText(Integer.toString(i));
                }
                else
                    tvchatbadge.setVisibility(View.GONE);
            }
        });
        getHome(true);
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        timer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeBean/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

            case R.id.itemCalendar:
                showCalendar();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchDeliveryDays() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/building/delivery/" + homeBean.getBillingMaster().getUser().getBuilding().getId();
            Log.d(TAG, "Url : " + url);

            final StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());

                    DeliveryDays deliveryDays = new Gson().fromJson(response,DeliveryDays.class);

                    BaseApplication.getInstance().setDeliveryDays(deliveryDays.getDeliveryDay());

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
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchSubscriptions() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/subscriptionMaster/listUserSubscriptions/" + sharedPreferences.getString(Constants.USER_ID, "");
            Log.d(TAG, "Url : " + url);

            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    Gson gson = new Gson();

//                    List<SubscriptionMaster> subscriptionMasterList = new ArrayList<>();

                    if (response != null){


                        BaseApplication.getInstance().setSubscriptionList(new ArrayList<SubscriptionWrapper>());
                        SubscriptionWrapper object;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                SubscriptionMasterSmall subscriptionMasterSmall = gson.fromJson(String.valueOf(response.getJSONObject(i)), SubscriptionMasterSmall.class);
                                SubscriptionMaster subscriptionMaster = new SubscriptionMaster(subscriptionMasterSmall, BaseApplication.getInstance().getUser().getId());
                                if(BaseApplication.getInstance().containsSubscriptionMaster(subscriptionMaster.getProductMaster().getId())!=null){
                                    object = BaseApplication.getInstance().containsSubscriptionMaster(subscriptionMaster.getProductMaster().getId());


                                }
                                else {
                                    object = new SubscriptionWrapper();
                                    object.setProductMaster(subscriptionMaster.getProductMaster().getId());
//                                    object.getSubscriptionMasters().add(subscriptionMaster);//subscriptionMaster;
                                    BaseApplication.getInstance().getSubscriptionList().add(object);
                                }

                                object.getSubscriptionMasters().add(subscriptionMaster);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(subscriptionMaster.getStartDate());

                                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                                object.getDeliveryDays().add(dayFormat.format(calendar.getTime()));
                                object.getSubscriptionIds().add(subscriptionMaster.getId());
//                                object.addSubscriptionMaster(subscriptionMaster);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        quickAddProductsListAdapter.notifyDataSetChanged();
//                        BaseApplication.getInstance().setSubscriptionList(subscriptionMasterList);
                    } else {
                        Toast.makeText(HomeActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }



    //API Calls
    public void getHome(final boolean onResume) {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/home/getHomeV3";
            Log.d(TAG, "Get HomeBean Url : " + url);

            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Get HomeBean Response : " + response);

                    homeBean = new Gson().fromJson(String.valueOf(response), HomeBean.class);

                    try {
                        editor.putInt(Constants.USER_STATUS, homeBean.getBillingMaster().getUser().getUserStatus());
                        editor.apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (homeBean != null) {
                        BaseApplication.getInstance().setUser(homeBean.getBillingMaster().getUser());

                        fragmentHomeCategories.setHomeBean(homeBean);

                        BaseApplication.getInstance().setSubscriptionProductTypes(homeBean.getSubscriptionProductTypes());

                        BaseApplication.getInstance().setProductCategoryList(homeBean.getProductCategoryList());

                        BaseApplication.getInstance().getCart().getComingTomorrowProductList().clear();

                        if (homeBean.getSubscriptionProductTypes() != null) {
                            BaseApplication.getInstance().getExcludedCartProductTypes().addAll(homeBean.getSubscriptionProductTypes());
                        }

                        for (int i = 0; i < homeBean.getComingTomorrowList().size(); i++) {
                            ProductMaster productMaster = new ProductMaster(homeBean.getComingTomorrowList().get(i));
                            productMaster.setTransientProductQuantity(productMaster.getProductQuantity());

                            if (productMaster.getProductType() != null && !BaseApplication.getInstance().getExcludedCartProductTypes().contains(productMaster.getProductType().getType())){
                                BaseApplication.getInstance().getCart().getComingTomorrowProductList().add(productMaster);
                            }

                            if (productMaster.getProductType() != null && productMaster.getProductType().getType() == 53){
                                BaseApplication.getInstance().getCart().setPromoCode("");
                            }
                        }

                        if(homeBean.getBillingMaster() != null){
                            if(homeBean.getBillingMaster().getUser() != null)
                            {
                                /****Adding the id from billingmaster to sharedpreferences*********/
                                editor.putString("id",homeBean.getBillingMaster().getId());
                                editor.commit();
                                /**************************************************************/


                                BaseApplication.getInstance().setUser(homeBean.getBillingMaster().getUser());

                                if (isNetworkAvailable())
                                {

                                    try
                                    {


                                        //Hotline
                                        //Get the user object for the current installation
                                        HotlineUser hlUser=Hotline.getInstance(getApplicationContext()).getUser();

                                        hlUser.setName(sharedPreferences.getString(Constants.USER_NAME, ""));
                                        hlUser.setEmail(sharedPreferences.getString(Constants.USER_EMAIL, ""));
                                        hlUser.setExternalId(sharedPreferences.getString(Constants.USER_ID, ""));
                                        hlUser.setPhone("+91", BaseApplication.getInstance().getUser().getMobileNumber());

                                        Hotline.getInstance(getApplicationContext()).updateUser(hlUser);

                                        /* Set any custom metadata to give agents more context, and for segmentation for marketing or pro-active messaging */
                                        Map<String, String> userMeta = new HashMap<String, String>();
                                        userMeta.put("buildingName", BaseApplication.getInstance().getUser().getBuilding().getBuildingName());
                                        userMeta.put("flatNo", BaseApplication.getInstance().getUser().getFlat());
                                        userMeta.put("email", sharedPreferences.getString(Constants.USER_EMAIL, ""));
                                        userMeta.put("walletValue", String.valueOf(homeBean.getBillingMaster().getAmount()*(-1)));
                                        userMeta.put("vendorName", homeBean.getBillingMaster().getUser().getBuilding().getVendorName());
                                        userMeta.put("vendorPhone", homeBean.getBillingMaster().getUser().getBuilding().getVendorPhone());
                                        userMeta.put("mobileNo", "+91" + BaseApplication.getInstance().getUser().getMobileNumber());

                                        Hotline.getInstance(getApplicationContext()).updateUserProperties(userMeta);




                                    }catch(Exception ec){}

                                } else {
                                    hideDialog();
                                    Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        initViews(onResume);
                        initCart();

                        if(homeBean.isShowReferalPage()) {
                            navigationView.getMenu().findItem(R.id.nav_refer_and_earn).setVisible(true);
                        }


                        fetchSubscriptions();
                        fetchDeliveryDays();


                    } else {
                        Toast.makeText(HomeActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Get HomeBean Data Error : " + error.toString());

                    hideDialog();
                    Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }) {
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
            Toast.makeText(HomeActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    int page = 0;

    public void pageSwitcher() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

                viewPagerBanner.setCurrentItem(page%viewPagerBanner.getAdapter().getCount(), true);
                page++;
                if(page == viewPagerBanner.getAdapter().getCount())
                    page = 0;
            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 4000);

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPagerBanner.getContext(),new DecelerateInterpolator());
//             scroller.setFixedDuration(5000);
            mScroller.set(viewPagerBanner, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...

    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 800;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}
