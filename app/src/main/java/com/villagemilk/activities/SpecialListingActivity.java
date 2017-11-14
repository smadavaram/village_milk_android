package com.villagemilk.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.DrawerProductCategoryListAdapter;
import com.villagemilk.beans.EntryItem;
import com.villagemilk.beans.ItemNew;
import com.villagemilk.beans.ProductCategory;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Fonts;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagaryarnalkar on 29/08/16.
 */
public class SpecialListingActivity extends BaseActivity {

    private static final String TAG = "Product List Activity";

//    private TextView tvSearch;
//
//    private ImageView ivCancel;
//
//    private ImageView ivSearch;

    private DrawerLayout drawer;

    private RelativeLayout rlSearch;

    //Cart Layout
    public LinearLayout llCart;

    private RelativeLayout rlBill;
    public TextView tvCartAmount;
    public TextView tvCartQuantity;

    private RelativeLayout rlCheckout;

    private RelativeLayout rlNoProds;

    private ImageView ivDrawerCategories;

    private ListView listViewDrawer;

    private DrawerProductCategoryListAdapter drawerProductCategoryListAdapter;

    private ListView lvSpecial;
    private ExampleAdapter adapter;

    private List<ProductCategory> productCategoryList = new ArrayList<>();

    private ArrayList<ProductMaster> productList = new ArrayList<>();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private SharedPreferences sharedPreferences;

    private int cartQuantity;

    private double cartTotal;

    private int productCategoryId;

    private String categoryName;

//    public BadgeView mBadgeView;  /******Declaring the BadgeView Variable**********/

    private TextView mBadgeView;

    public String value;

    public RelativeLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_listing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            findViewById(R.id.ivBackBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

//        try {
//            mixpanel.track("Featured Screen");
//        } catch (Exception e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);

        llCart = (LinearLayout) findViewById(R.id.llCart);

        rlBill = (RelativeLayout) llCart.findViewById(R.id.rlBill);
        tvCartAmount = (TextView) rlBill.findViewById(R.id.tvCartAmount);
        tvCartQuantity = (TextView) rlBill.findViewById(R.id.tvCartQuantity);

        rlCheckout = (RelativeLayout) llCart.findViewById(R.id.rlCheckout);

        listViewDrawer =(ListView) findViewById(R.id.listViewDrawer);

        ivDrawerCategories = (ImageView) findViewById(R.id.ivDrawerCategories);

        productCategoryList = BaseApplication.getInstance().getProductCategoryList();

        rlNoProds = (RelativeLayout) findViewById(R.id.rlNoProds);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        ivDrawerCategories = (ImageView) findViewById(R.id.ivDrawerCategories);

        productCategoryList = BaseApplication.getInstance().getProductCategoryList();

        drawerProductCategoryListAdapter = new DrawerProductCategoryListAdapter(SpecialListingActivity.this, productCategoryList, robotoBold,robotoLight);

        listViewDrawer.setAdapter(drawerProductCategoryListAdapter);
        drawerProductCategoryListAdapter.notifyDataSetChanged();

        lvSpecial=(ListView) findViewById(R.id.lvSpecial);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        initCart();

        getProducts();
    }

    private void initViews() {
        tvCartAmount.setText("₹" + Commons.getCartAmount());

        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

        ivDrawerCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        productCategoryList = BaseApplication.getInstance().getProductCategoryList();

        drawerProductCategoryListAdapter = new DrawerProductCategoryListAdapter(SpecialListingActivity.this, productCategoryList, robotoBold, robotoLight);

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SpecialListingActivity.this, ProductSearchActivity.class);
//                startActivity(intent);
            }
        });

        rlCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialListingActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        listViewDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);

                productCategoryId = productCategoryList.get(position).getProductCategoryId();

                Intent intent = new Intent(SpecialListingActivity.this, SpecialListingActivity.class);
                intent.putExtra("product_category_id", productCategoryId);
                intent.putExtra("intent_source", "HomeActivity");
                intent.putExtra("categoryName", productCategoryList.get(position).getProductCategoryName());
                startActivity(intent);
            }
        });

    }

    private void getProducts() {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/productMaster/fetchFeaturedProducts";

            Log.d(TAG, "Url : " + url);

            final MyPostJsonArrayRequest jsonArrayRequest = new MyPostJsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
//                    Log.d(TAG, "Response : " + response.toString());

                    if(response == null)
                        return;

                    Gson gson = new Gson();

                    productList.clear();

                    if(response.length() > 0){
                        try {
                            ArrayList<String> listdata = new ArrayList<>();

                            if (response != null) {
                                for (int i=0;i<response.length();i++){
                                    ProductMaster productMaster = gson.fromJson(response.get(i).toString(),ProductMaster.class);
                                    EntryItem entryItem = new EntryItem(productMaster.getProductName(),
                                            productMaster.getProductUnitPrice(), productMaster.getStrikePrice(),
                                            productMaster.getProductUnitSize(), productMaster.getProductImage(),
                                            productMaster.getId(), productMaster, productMaster.getSpecialText());
                                    productList.add(productMaster);
                                }
                            }
                            if(productList.size() > 0){
                                rlNoProds.setVisibility(View.GONE);
                                lvSpecial.setVisibility(View.VISIBLE);
                            } else {
                                alert("No products found for your locality.");
                                rlNoProds.setVisibility(View.VISIBLE);
                                lvSpecial.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        alert("No products found for your locality.");
                        rlNoProds.setVisibility(View.VISIBLE);
                        lvSpecial.setVisibility(View.GONE);
                    }

                    hideDialog();
                    initViews();
                    initializeListView();
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
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId", sharedPreferences.getString(Constants.USER_ID, ""));
//                        jsonObject.put("productCategoryId", productCategoryId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Request send");
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
            Toast.makeText(SpecialListingActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeListView() {
        adapter = new ExampleAdapter(this, productList);
        lvSpecial.setAdapter(adapter);
    }

    private void populateCart() {
        int totalQuantity = 0;

        if(BaseApplication.getInstance().getCart() != null && BaseApplication.getInstance().getCart().getCartProductList().size() > 0){
            for (ProductMaster productMaster : BaseApplication.getInstance().getCart().getCartProductList()) {
                totalQuantity += productMaster.getProductQuantity();
            }
        }

        if (totalQuantity > 0){
            tvCartAmount.setText("₹" + Commons.getCartAmount());
            tvCartQuantity.setText(Commons.getCartQuantity() + " items");
        } else {
            tvCartAmount.setText("₹" + 0);
            tvCartQuantity.setText(0 + " items");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateCart();

        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Adapter for our cartItemList of {@link ItemNew}s.
     */
    private class ExampleAdapter extends BaseAdapter {

        private ArrayList<ProductMaster> items;
        private Context context;
        private LayoutInflater vi;
        private Typeface robotoBold;
        private Typeface robotoLight;

        public ExampleAdapter(Context context, ArrayList<ProductMaster> items) {
            this.context = context;
            this.items = items;
            vi = LayoutInflater.from(context);
            robotoLight = Fonts.getTypeface(context, Fonts.FONT_ROBOTO_LIGHT);
            robotoBold = Fonts.getTypeface(context, Fonts.FONT_ROBOTO_BOLD_CONDENSED);
        }

//        public void setData(ArrayList<ItemNew> productMasterList)
//        {
//            this.productMasterList = productMasterList;
//        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ProductMaster productMaster = items.get(position);

            convertView = vi.inflate(R.layout.product_item_cart_new, parent,false);

            final LinearLayout llSubscribe = (LinearLayout) convertView.findViewById(R.id.llSubscribe);

            llSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    launchSubscriptionActivity(productMaster);

                }
            });
            final TextView title = (TextView)convertView.findViewById(R.id.list_item_entry_title);
            final TextView tvNotify = (TextView)convertView.findViewById(R.id.tvNotify);
            title.setTypeface(robotoBold);
            final LinearLayout llNotify = (LinearLayout) convertView.findViewById(R.id.llNotify);
            final TextView price = (TextView)convertView.findViewById(R.id.price);
            price.setTypeface(robotoBold);
            final TextView strikePrice = (TextView)convertView.findViewById(R.id.strikePrice);
            strikePrice.setTypeface(robotoBold);
            final TextView unit = (TextView)convertView.findViewById(R.id.unit);
            unit.setTypeface(robotoLight);
            final TextView specialText = (TextView)convertView.findViewById(R.id.specialText);
            specialText.setTypeface(robotoLight);
            final ImageView ivPoweredBy = (ImageView) convertView.findViewById(R.id.ivPoweredBy);

            final LinearLayout llAdd = (LinearLayout) convertView.findViewById(R.id.llAdd);
            final LinearLayout llQuantityModification = (LinearLayout) convertView.findViewById(R.id.llQuantityModification);

            ImageView ivSubtract = (ImageView) convertView.findViewById(R.id.ivSubtract);
            final TextView tvProductQuantity = (TextView) convertView.findViewById(R.id.tvProductQuantity);
            ImageView ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);

            if(BaseApplication.getInstance().getCart().getCartProductList().contains(productMaster)){
                int productPosition = BaseApplication.getInstance().getCart().getCartProductList().indexOf(productMaster);

                ProductMaster productMasterCart = BaseApplication.getInstance().getCart().getCartProductList().get(productPosition);

                if (productMasterCart.getProductQuantity() > 0){
                    tvProductQuantity.setText(String.valueOf(productMasterCart.getProductQuantity()));

                    llAdd.setVisibility(View.GONE);
                    llQuantityModification.setVisibility(View.VISIBLE);
                } else {
                    llAdd.setVisibility(View.VISIBLE);
                    llQuantityModification.setVisibility(View.GONE);
                }
            } else {
                if (productMaster.getProductSubType() != 1 && productMaster.getStatus() != 2){
                    llAdd.setVisibility(View.VISIBLE);
                    llQuantityModification.setVisibility(View.GONE);
                }
            }

            if(productMaster.getPoweredBy() != null && productMaster.getPoweredBy().length() > 0){
                imageLoader.displayImage(productMaster.getPoweredBy(), ivPoweredBy);
                ivPoweredBy.setVisibility(View.VISIBLE);
            } else {
                ivPoweredBy.setVisibility(View.GONE);
            }

            if(productMaster.getSpecialText() != null && productMaster.getSpecialText().length() > 0){
                specialText.setVisibility(View.VISIBLE);
                specialText.setText(productMaster.getSpecialText());
            } else {
                specialText.setVisibility(View.GONE);
            }

            if(productMaster.getStrikePrice() != null && productMaster.getStrikePrice() > -1 && productMaster.getStatus() != 2){
                strikePrice.setText("₹"  + Constants.priceDisplay.format(productMaster.getStrikePrice()));
                strikePrice.setVisibility(View.VISIBLE);
                strikePrice.setPaintFlags(strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                strikePrice.setVisibility(View.GONE);
            }

            final ImageView productImageView = (ImageView) convertView.findViewById(R.id.im_productImage1);
            productImageView.setImageResource(R.drawable.product_default);

            if (title != null) {
                title.setText(productMaster.getProductName());
            }

            if(productMaster.getStatus() == 1 && price != null) {
                price.setText("₹" + Constants.priceDisplay.format(productMaster.getProductUnitPrice()));
                price.setVisibility(View.VISIBLE);
            } else if(productMaster.getStatus() == 2 ) {
                if(price != null) {
                }
            }

            if(productMaster.getStatus() == 1 && unit != null) {
                unit.setText(productMaster.getProductUnitSize());
            } else if(productMaster.getStatus() == 2){
                unit.setText("Coming Soon !");
            }

            if(productMaster.getProductImage() != null) {
                imageLoader.displayImage(productMaster.getProductImage(), productImageView);
            }

            if (productMaster.getStatus() != 2){
                llAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAdd.setVisibility(View.GONE);
                        llQuantityModification.setVisibility(View.VISIBLE);

                        productMaster.setProductQuantity(1);
                        BaseApplication.getInstance().getCart().getCartProductList().add(productMaster);

                        initCart();

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), true);
                    }
                });

                ivSubtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());

                        productMaster.setProductQuantity(productQuantity - 1);

                        if(productQuantity == 1){
                            llAdd.setVisibility(View.VISIBLE);
                            llQuantityModification.setVisibility(View.GONE);

                            BaseApplication.getInstance().getCart().getCartProductList().remove(productMaster);
                        } else {
                            tvProductQuantity.setText(String.valueOf(productQuantity - 1));
                        }

                        initCart();

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), false);
                    }
                });

                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());

                        tvProductQuantity.setText(String.valueOf(productQuantity + 1));

                        int position = BaseApplication.getInstance().getCart().getCartProductList().indexOf(productMaster);

                        ProductMaster productMaster = BaseApplication.getInstance().getCart().getCartProductList().get(position);
                        productMaster.setProductQuantity(productQuantity + 1);

                        initCart();

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), true);
                    }
                });
            }

            //Condition for showing subscription type

            if(productMaster.getProductSubType() == 1 && productMaster.getStatus() != 2 )
                llSubscribe.setVisibility(View.VISIBLE);
            else
                llSubscribe.setVisibility(View.GONE);
//
//            if(productMaster.getProductSubType() == 2) {
//                llSubscribe.setVisibility(View.GONE);
//            }else if(productMaster.getProductSubType() == 1) {
//                llSubscribe.setVisibility(View.VISIBLE);
//            }else if(productMaster.getProductSubType() == 3) {
//                llSubscribe.setVisibility(View.VISIBLE);
//            }

            if(productMaster.getStatus() == 2){

                llNotify.setVisibility(View.VISIBLE);
//                llSubscribe.setVisibility(View.GONE);
                llAdd.setVisibility(View.GONE);
                llQuantityModification.setVisibility(View.GONE);
                price.setVisibility(View.GONE);
                strikePrice.setVisibility(View.GONE);

                llNotify.setVisibility(View.VISIBLE);

                tvNotify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        JSONObject props = new JSONObject();
//                        try {
//                            props.put("Product Name", productMaster.getProductName());
//                            mixpanel.track("Notify About", props);
//                            SweetAlertDialog sd = new SweetAlertDialog(SpecialListingActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
//                                    .setTitleText("Heard You!").setContentText("We will notify you once the product is available with us. Thankyou!")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                        }
//                                    });
//                            sd.show();
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                });

            }else {
                llNotify.setVisibility(View.GONE);
                if(productMaster.getProductSubType()!=2)
//                    llSubscribe.setVisibility(View.VISIBLE);
                    price.setText("₹" + Constants.priceDisplay.format(productMaster.getProductUnitPrice()));
                price.setVisibility(View.VISIBLE);

            }


            return convertView;
        }

        private void productBuyEvent(int type, String id, String productName, Double price, String unit, boolean add) {
            try {
                Product product = new Product()
                        .setId(id)
                        .setName(productName)
                        .setCategory(BaseApplication.getInstance().getUser().getBuilding().getId())
                        .setVariant(unit).setBrand(type + "")
                        .setPrice(price)
                        .setQuantity(1);
                ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);
                if (!add)
                    productAction = new ProductAction(ProductAction.ACTION_REMOVE);
                productAction.setTransactionId("T12345")
                        .setTransactionAffiliation("Android")
                        .setCheckoutStep(1)
                        .setTransactionRevenue(price);
                HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                        .addProduct(product)
                        .setProductAction(productAction);

                Tracker t = ((BaseApplication) getApplication()).getTracker(
                        BaseApplication.TrackerName.APP_TRACKER);
                t.setScreenName("Product Screen");
                t.send(builder.build());

                Tracker ecommerceTracker = ((BaseApplication) getApplication()).getTracker(BaseApplication.TrackerName.ECOMMERCE_TRACKER);
                ecommerceTracker.setScreenName("Product Screen");
                ecommerceTracker.send(builder.build());
            } catch (Exception e) {
            }
        }
    }

    public void launchSubscriptionActivity(ProductMaster retrievedProductMaster){


        ProductMaster productMaster = retrievedProductMaster;

        String productMasterGson = new Gson().toJson(productMaster, ProductMaster.class);

        if (productMaster.getProductSubType() == 1){

            try {
                JSONObject props = new JSONObject();
                props.put("Product Name", productMaster.getProductName());
//                mixpanel.track("Subscription Product Click", props);
            } catch (Exception e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

            Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
            intent.putExtra("product_master", productMasterGson);

            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("intent_source") != null) {//Get source for dynamic visibility of RadioGroup on next page
                    intent.putExtra("intent_source", getIntent().getExtras().getString("intent_source"));
                    if (getIntent().getExtras().getString("intent_source").equalsIgnoreCase("CalendarActivity")) {
                        intent.putExtra("source_calendar_date", getIntent().getExtras().get("source_calendar_date").toString());
                        startActivity(intent);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            } else {
                startActivity(intent);
            }
        }



    }

    private void initCart() {
        if (Commons.getCartQuantity() > 0) {
            tvCartAmount.setText("₹" + Commons.getCartAmount());
            tvCartQuantity.setText(Commons.getCartQuantity() + " items");

            llCart.setVisibility(View.VISIBLE);
        } else {
            llCart.setVisibility(View.GONE);
        }

    }


}
