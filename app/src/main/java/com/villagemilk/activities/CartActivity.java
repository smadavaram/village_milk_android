package com.villagemilk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.villagemilk.adapters.CartProductListAdapter;
import com.villagemilk.beans.ApplyPromoBean;
import com.villagemilk.beans.Cart;
import com.villagemilk.beans.CartCheckoutResponseBean;
import com.villagemilk.beans.CartItem;
import com.villagemilk.beans.CartItemMaster;
import com.villagemilk.beans.CartOffer;
import com.villagemilk.beans.Offer;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.dialogs.SweetProductDialog;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.view.ActivityCalendar;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class CartActivity extends BaseActivity {
    private static final String TAG = "CartActivity";

    private TextView tvOfferText;

    private RecyclerView rvProducts;
    private CartProductListAdapter cartProductListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private RelativeLayout rlPromo;

    CartItemMaster cartItemMaster;

    boolean couponApplied = false;

    //Cart Layout
    public LinearLayout llCart;

    private RelativeLayout rlBill;
    public TextView tvCartAmount;
    public TextView tvCartQuantity;

    private RelativeLayout rlCheckout;

    private List<Offer> offerList = new ArrayList<>();

    private List<CartItem> cartItemList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    private ProductMaster popupProduct;

    private int cartQuantity;

    private double cartAmount;

    public boolean applyPromoCode;

    private boolean promoCodePresent;
    private TextView tvCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Confirm Order");
        }

        tvOfferText = (TextView) findViewById(R.id.tvOfferText);

        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);

        rlPromo = (RelativeLayout) findViewById(R.id.rlPromo);

        llCart = (LinearLayout) findViewById(R.id.llCart);

        rlBill = (RelativeLayout) llCart.findViewById(R.id.rlBill);
        tvCartAmount = (TextView) rlBill.findViewById(R.id.tvCartAmount);
        tvCartQuantity = (TextView) rlBill.findViewById(R.id.tvCartQuantity);

        rlCheckout = (RelativeLayout) llCart.findViewById(R.id.rlCheckout);
        tvCheckout = (TextView)llCart.findViewById(R.id.tvCheckout);
        tvCheckout.setText("Confirm");

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        BaseApplication.getInstance().getCart().getTotalProductList().clear();
        BaseApplication.getInstance().getCart().getTotalProductList().addAll(BaseApplication.getInstance().getCart().getCartProductList());
        BaseApplication.getInstance().getCart().getTotalProductList().addAll(BaseApplication.getInstance().getCart().getComingTomorrowProductList());

        buildCartMaster(false);
    }

    private void initViews(){

        for (int i = 0; i < BaseApplication.getInstance().getCart().getComingTomorrowProductList().size(); i++) {
            ProductMaster productMaster = BaseApplication.getInstance().getCart().getComingTomorrowProductList().get(i);
            productMaster.setProductQuantity(productMaster.getTransientProductQuantity());
        }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvProducts.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvProducts.setLayoutManager(layoutManager);

        tvCartAmount.setText("₹" + Commons.getCartAmount());

        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

        cartProductListAdapter = new CartProductListAdapter(this, false);
        rvProducts.setAdapter(cartProductListAdapter);

        rlPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ApplyPromoActivity.class);
                startActivity(intent);
            }
        });

        rlCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(BaseApplication.getInstance().getCart().getPromoCode())){
                    promoCodePresent = false;
                    showPopupProduct();
                } else {
//                    buildCartMaster(true);
                    checkoutCart(cartItemMaster);
                }
            }
        });

        if (!TextUtils.isEmpty(BaseApplication.getInstance().getCart().getPromoCode())){
            promoCodePresent = true;
            cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());
            applyPromoCode(cartItemMaster);
//            showPopupProduct();
        }


    }

    public void buildCartMaster(boolean checkoutCart){
        cartItemMaster = new CartItemMaster();
        cartItemMaster.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
        cartItemMaster.setPromoCode(null);

        cartItemMaster.getCartItemList().clear();//kuch bhi @Khunt

        for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {

            ProductMaster productMaster = BaseApplication.getInstance().getCart().getTotalProductList().get(i);
            CartItem cartItem = new CartItem();
            cartItem.setProductMasterId(productMaster.getId());
            cartItem.setQuantity(productMaster.getProductQuantity());

            if (productMaster.getSubscriptionId() != null) {
                cartItem.setSubscriptionId(productMaster.getSubscriptionId());
                cartItemMaster.getCartItemList().add(cartItem);
            } else {
                if(productMaster.getProductType() != null){
                    if (productMaster.getProductQuantity() != 0 && productMaster.getProductType().getType() != 53) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                } else {
                    if (productMaster.getProductQuantity() != 0) {
                        cartItemMaster.getCartItemList().add(cartItem);
                    }
                }
            }
        }


        computeCart(cartItemMaster);

//        if (checkoutCart){
//            checkoutCart(cartItemMaster);
//        } else if(applyPromoCode){
//            cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());
//            applyPromoCode(cartItemMaster);
//        } else {
//        }
    }

    private void showPopupProduct(){
        if(popupProduct != null && !BaseApplication.getInstance().getCart().getTotalProductList().contains(popupProduct)){
            final HashMap<String,Object> popupMap = new HashMap<>();
            popupMap.put("Title",popupProduct.getProductName());
            popupMap.put("Price",popupProduct.getProductUnitPrice());
            popupMap.put("Unit",popupProduct.getProductUnitSize());
            popupMap.put("Strikeprice",popupProduct.getStrikePrice());
            popupMap.put("Image", popupProduct.getProductImage());
            popupMap.put("specialText", popupProduct.getSpecialText());

            SweetProductDialog sweetProductDialog = new SweetProductDialog(CartActivity.this, SweetProductDialog.NORMAL_TYPE, robotoBold, robotoLight, popupMap);

            sweetProductDialog.setTitleText(popupProduct.getFlashShortText());

            sweetProductDialog.setConfirmText("I want this");
            sweetProductDialog.setConfirmClickListener(new SweetProductDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetProductDialog sweetAlertDialog) {
                    popupProduct.setProductQuantity(1);



                        ProductMaster productMaster = popupProduct;
                        CartItem cartItem = new CartItem();
                        cartItem.setProductMasterId(productMaster.getId());
                        cartItem.setQuantity(productMaster.getProductQuantity());

                        if (productMaster.getSubscriptionId() != null) {
                            cartItem.setSubscriptionId(productMaster.getSubscriptionId());
                            cartItemMaster.getCartItemList().add(cartItem);
                        } else {
                            if(productMaster.getProductType() != null){
                                if (productMaster.getProductQuantity() != 0 && productMaster.getProductType().getType() != 53) {
                                    cartItemMaster.getCartItemList().add(cartItem);
                                }
                            } else {
                                if (productMaster.getProductQuantity() != 0) {
                                    cartItemMaster.getCartItemList().add(cartItem);
                                }
                            }
                        }


                    BaseApplication.getInstance().getCart().getCartProductList().add(popupProduct);
                    BaseApplication.getInstance().getCart().getTotalProductList().add(popupProduct);

                    //Toast.makeText(HomeActivity.this, "New Product added to cart. Please proceed to checkout", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(HomeActivity.this, "ProductMaster has been added to cart. Please checkout the cart to order", Toast.LENGTH_SHORT).show();
                    // Get tracker.
                    Tracker t = ((BaseApplication) getApplication()).getTracker(
                            BaseApplication.TrackerName.APP_TRACKER);

                    // Build and send an Event.
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("Purchase Events")
                            .setAction(("Checkout Popup Buy"))
                            .setLabel(("Checkout Buy More"))
                            .build());

//                    try {
//                        JSONObject props = new JSONObject();
//                        props.put("Product Name", popupProduct.getProductName());
//                        mixpanel.track("Product Popup Buy",props);
//                    } catch (Exception e) {
//                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                    }

                    tvCartAmount.setText("₹" + Commons.getCartAmount());

                    tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                    sweetAlertDialog.dismiss();

                    if(promoCodePresent){

                        cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());
                        applyPromoCode(cartItemMaster);
//                        applyPromoCode = true;
//                        buildCartMaster(false);
                    } else {
                        checkoutCart(cartItemMaster);
                    }
                }
            });

            sweetProductDialog.setCancelClickListener(new SweetProductDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetProductDialog sweetAlertDialog) {
//                    try {
//                        mixpanel.track("Product Popup Skip");
//                    } catch (Exception e) {
//                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                    }

                    sweetAlertDialog.dismissWithAnimation();

                    if(!promoCodePresent){
                        checkoutCart(cartItemMaster);
                    }else{
                        cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());
                        applyPromoCode(cartItemMaster);

                    }
                }
            });

            sweetProductDialog.isCheckoutPopup = true;
            sweetProductDialog.show();
        } else{
            if(!promoCodePresent){
               checkoutCart(cartItemMaster);
            }else{
                cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());
                applyPromoCode(cartItemMaster);

            }
        }
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cartProductListAdapter != null) {
            for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {
                ProductMaster productMaster = BaseApplication.getInstance().getCart().getTotalProductList().get(i);

                if (productMaster.getProductType() != null && productMaster.getProductType().getType() == 53 && productMaster.getSubscriptionId() == null){
//                    BaseApplication.getInstance().getCart().getTotalProductList().remove(productMaster);
//
//                    BaseApplication.getInstance().getCart().setOfferProduct(null);
//                    BaseApplication.getInstance().getCart().setPromoCode(null);
                }
            }

            cartProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < BaseApplication.getInstance().getCart().getComingTomorrowProductList().size(); i++) {
            ProductMaster productMaster = BaseApplication.getInstance().getCart().getComingTomorrowProductList().get(i);
            productMaster.setProductQuantity(productMaster.getTransientProductQuantity());
        }

        for (int i = 0; i < BaseApplication.getInstance().getCart().getCartProductList().size(); i++) {
            ProductMaster productMaster = BaseApplication.getInstance().getCart().getCartProductList().get(i);

            if (productMaster.getProductQuantity() == 0){
                BaseApplication.getInstance().getCart().getCartProductList().remove(productMaster);
            }
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Commons.showBackActionBarBlue(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //API Calls
    private void computeCart(final CartItemMaster cartItemMaster) {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/cart/computeCart";

            final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response :" + response.toString());

                    if(response != null){
                        CartOffer cartOffer = new Gson().fromJson(String.valueOf(response), CartOffer.class);

                        if(cartOffer != null) {
                            cartQuantity = Commons.getCartQuantity();
                            cartAmount = Commons.getCartAmount();

                            if(cartOffer.getCartOfferString() != null && cartOffer.getCartOfferString().length() > 0){
                                tvOfferText.setText(cartOffer.getCartOfferString());
                                tvOfferText.setVisibility(View.VISIBLE);
                            } else {
                                tvOfferText.setVisibility(View.GONE);
                            }


                            popupProduct = cartOffer.getPopupProduct();

                            initViews();
                            hideDialog();
                        }
                    } else {
                        hideDialog();
                        return;
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
                    Log.d(TAG, "Body : " + new Gson().toJson(cartItemMaster, CartItemMaster.class));
                    return new Gson().toJson(cartItemMaster, CartItemMaster.class).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(CartActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void applyPromoCode(final CartItemMaster cartMaster){
        if(isNetworkAvailable()){
            showDialog();


            final CartItemMaster cartItemMaster = new CartItemMaster();

            cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());

            cartItemMaster.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));


            for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {

                ProductMaster productMaster = BaseApplication.getInstance().getCart().getTotalProductList().get(i);
                CartItem cartItem = new CartItem();
                cartItem.setProductMasterId(productMaster.getId());
                cartItem.setQuantity(productMaster.getProductQuantity());

                if (productMaster.getSubscriptionId() != null) {
                    cartItem.setSubscriptionId(productMaster.getSubscriptionId());
                    cartItemMaster.getCartItemList().add(cartItem);
                } else {
                    if(productMaster.getProductType() != null){
                        if (productMaster.getProductQuantity() != 0 && productMaster.getProductType().getType() != 53) {
                            cartItemMaster.getCartItemList().add(cartItem);
                        }
                    } else {
                        if (productMaster.getProductQuantity() != 0) {
                            cartItemMaster.getCartItemList().add(cartItem);
                        }
                    }
                }
            }



            String url = Constants.API_PROD + "/offer/applyOffer";
            Log.d(TAG, "Apply Offer Url : " + url);

            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Apply Offer Response : " + response);

                    if (response != null) {
                        ApplyPromoBean applyPromoBean = new Gson().fromJson(response, ApplyPromoBean.class);

                        if (applyPromoBean.getStatus() == 1){
                            couponApplied = true;

                            for (Iterator<ProductMaster> iterator = BaseApplication.getInstance().getCart().getTotalProductList().iterator(); iterator.hasNext(); ) {
                                ProductMaster productMaster = iterator.next();

                                if (productMaster.getProductType() != null && productMaster.getProductType().getType() == 53){
                                    iterator.remove();
                                }
                            }

                            applyPromoBean.getProduct().setProductQuantity(1);
                            BaseApplication.getInstance().getCart().setOfferProduct(applyPromoBean.getProduct());

                            BaseApplication.getInstance().getCart().getTotalProductList().add(0, applyPromoBean.getProduct());

                            BaseApplication.getInstance().getCart().setPromoCode(cartItemMaster.getPromoCode());

                            tvCartAmount.setText("₹" + Commons.getCartAmount());

                            tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                            cartProductListAdapter.notifyDataSetChanged();
                        } else {
                            couponApplied = false;
                            for (Iterator<ProductMaster> iterator = BaseApplication.getInstance().getCart().getTotalProductList().iterator(); iterator.hasNext(); ) {
                                ProductMaster productMaster = iterator.next();

                                if (productMaster.getProductType() != null && productMaster.getProductType().getType() == 53){
                                    iterator.remove();
                                }
                            }

                            BaseApplication.getInstance().getCart().getTotalProductList().remove(BaseApplication.getInstance().getCart().getOfferProduct());
                            BaseApplication.getInstance().getCart().setOfferProduct(null);

                            if(BaseApplication.getInstance().getCart().getTotalProductList().size() == 0){
                                finish();
                            }

                            cartProductListAdapter.notifyDataSetChanged();

                            tvCartAmount.setText("₹" + Commons.getCartAmount());

                            tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                            if (!TextUtils.isEmpty(applyPromoBean.getMessage())) {
                                Toast.makeText(CartActivity.this, applyPromoBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CartActivity.this, "Offer has been invalidated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    hideDialog();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Apply Offer Data Error : " + error.toString());

                    hideDialog();
                    Toast.makeText(CartActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return new Gson().toJson(cartItemMaster, CartItemMaster.class).getBytes();
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
            Toast.makeText(CartActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkoutCart( CartItemMaster cartMaster) {
        if(isNetworkAvailable()) {
            showDialog();

            final CartItemMaster cartItemMaster = new CartItemMaster();
            cartItemMaster.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));

            if (!TextUtils.isEmpty(BaseApplication.getInstance().getCart().getPromoCode())){

                cartItemMaster.setPromoCode(BaseApplication.getInstance().getCart().getPromoCode());

            }



            for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {

                ProductMaster productMaster = BaseApplication.getInstance().getCart().getTotalProductList().get(i);
                CartItem cartItem = new CartItem();
                cartItem.setProductMasterId(productMaster.getId());
                cartItem.setQuantity(productMaster.getProductQuantity());

                if (productMaster.getSubscriptionId() != null) {
                    cartItem.setSubscriptionId(productMaster.getSubscriptionId());
                    cartItemMaster.getCartItemList().add(cartItem);
                } else {
                    if(productMaster.getProductType() != null){
                        if (productMaster.getProductQuantity() != 0 && productMaster.getProductType().getType() != 53) {
                            cartItemMaster.getCartItemList().add(cartItem);
                        }
                    } else {
                        if (productMaster.getProductQuantity() != 0) {
                            cartItemMaster.getCartItemList().add(cartItem);
                        }
                    }
                }
            }


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
                            new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE, robotoBold, robotoLight)
                                    .setTitleText("Order has been placed successfully")
                                    .setContentText(getString((R.string.thanks_subscription_normal)))
                                    .setConfirmText("Ok,cool !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            BaseApplication.reloadList = true;

                                            sendEcommerce(BaseApplication.getInstance().getCart().getTotalProductList());

                                            BaseApplication.getInstance().setCart(new Cart());

                                            Intent intent = new Intent(CartActivity.this, ActivityCalendar.class);
                                            BaseApplication.reloadHomePage = true;
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                            finish();
                                        }
                                    })
                                    .show();

                            if(couponApplied){

//                                try {
//                                    JSONObject props = new JSONObject();
//                                    props.put("Coupon Code", cartItemMaster.getPromoCode());
//
//                                    mixpanel.track("Checkedout Success With Coupon",props);
//
//                                } catch (Exception e) {
//                                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                                }

                            }
                        } else {
                            if (cartCheckoutResponseBean.getMessage() != null) {
                                Toast.makeText(CartActivity.this, cartCheckoutResponseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CartActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }

                            if(couponApplied){

//                                try {
//                                    JSONObject props = new JSONObject();
//                                    props.put("Coupon Code", cartItemMaster.getPromoCode());
//
//                                    mixpanel.track("Checkedout Failure With Coupon",props);
//
//                                } catch (Exception e) {
//                                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                                }

                            }

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

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(CartActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendEcommerce(List<ProductMaster> productMasterList) {
        try {
            Tracker t = ((BaseApplication) getApplication()).getTracker(
                    BaseApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Checkout Screen");
            // Build and send an Event.
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Purchase Events")
                    .setAction(("ProductMaster Order"))
                    .setLabel(("Checkout Pressed"))
                    .setValue(Double.valueOf(cartAmount).longValue())
                    .build());

            Product product = new Product()
                    .setId("P12345")
                    .setName("DN Cart")
                    .setCategory(BaseApplication.getInstance().getUser().getBuilding().getId())
                    .setVariant("black")
                    .setPrice(cartAmount)
                    .setQuantity(1);
            ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                    .setTransactionId("T12345")
                    .setCheckoutStep(2)
                    .setTransactionAffiliation("Android")
                    .setTransactionRevenue(cartAmount);
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productAction);

//            try {
////                mixpanel.track("Successful Checkout");
//                mixpanel.getPeople().trackCharge(cartAmount, null);
//                mixpanel.getPeople().increment("Lifetime Revenue", cartAmount);
//            } catch (Exception e) {
//                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//            }

//            Tracker t = ((BaseApplication) getApplication()).getTracker(
//                    BaseApplication.TrackerName.APP_TRACKER);

            t.send(builder.build());

            Tracker ecommerceTracker = ((BaseApplication) getApplication()).getTracker(BaseApplication.TrackerName.ECOMMERCE_TRACKER);
            ecommerceTracker.setScreenName("Checkout Screen");
            ecommerceTracker.send(builder.build());
            JSONArray jarrName = new JSONArray();
            JSONArray jarrQuant = new JSONArray();
            JSONArray jarrPrice = new JSONArray();


            for(ProductMaster productMaster : productMasterList) {
                productBuyEvent(productMaster, productMaster.getId(), productMaster.getProductName(),
                        productMaster.getProductUnitPrice(), String.valueOf(productMaster.getProductQuantity()));
                jarrName.put(productMaster.getProductName());
                jarrQuant.put(String.valueOf(productMaster.getProductQuantity()));
                jarrPrice.put(productMaster.getProductUnitPrice() + "");

            }

            //Add hotline event
//            try {
//                JSONObject props = new JSONObject();
//                props.put("Products", jarrName);
//                props.put("Quantities", jarrQuant);
//                props.put("Prices", jarrPrice);
//                props.put("Total", cartAmount);
//                mixpanel.track("Successful Checkout",props);
//            } catch (Exception e) {
//                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//            }

        }catch(Exception e){}

        // Build the transaction.
        /*
        sendDataToTwoTrackers(new HitBuilders.TransactionBuilder()
                .set("userId", BaseApplication.getInstance().getUser().getId())
                .set("building", BaseApplication.getInstance().getUser().getBuilding().getId())
                .set("quantity", String.valueOf(cartQuantity))
                .setRevenue(cartAmount)
                .setCurrencyCode("INR")
                .build());
                */
    }

    private void productBuyEvent(ProductMaster productMaster, String id, String productName, Double price, String unit) {
        try {
            Product product = new Product()
                    .setId(id)
                    .setName(productName)
                    .setCategory(BaseApplication.getInstance().getUser().getBuilding().getId())
                    .setBrand((productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0) + "")
                    .setVariant(unit)
                    .setPrice(price)
                    .setQuantity(1);
            ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT);

            productAction.setTransactionId("T12345")
                    .setTransactionAffiliation("Android")
                    .setCheckoutStep(1)
                    .setTransactionRevenue(price);
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productAction);

            Tracker t = ((BaseApplication) getApplication()).getTracker(
                    BaseApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Checkout Screen");
            t.send(builder.build());

            Tracker ecommerceTracker = ((BaseApplication) getApplication()).getTracker(BaseApplication.TrackerName.ECOMMERCE_TRACKER);
            ecommerceTracker.setScreenName("Checkout Screen");
            ecommerceTracker.send(builder.build());
        }catch(Exception e){}
    }
}
