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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.DrawerProductCategoryListAdapter;
import com.villagemilk.beans.ItemNew;
import com.villagemilk.beans.ProductCategory;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Fonts;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.gson.Gson;
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** this page fetches data from db and displays dynamically based on building selected***/
public class ProductListActivity extends BaseActivity {
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

    private ArrayList<ItemNew> items = new ArrayList<>();

    private HashMap<String, ProductMaster> productMasterMap = new HashMap<>();

    private AnimatedExpandableListView listView;
    private ProductListAdapter adapter;

    ProductMaster[] productMasters;

    private List<ProductCategory> productCategoryList = new ArrayList<>();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private SharedPreferences sharedPreferences;

    private int productCategoryId;

    private String categoryName;

    public String value;

    public RelativeLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            findViewById(R.id.ivBackBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

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
        listView = (AnimatedExpandableListView)findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        fetchAllProducts();

//        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("product_category_id")) {
//            productCategoryId = getIntent().getExtras().getInt("product_category_id");
//            categoryName = getIntent().getExtras().getString("categoryName",null);
//            getProducts();
//        }
    }

    private void initViews(){
        tvCartAmount.setText("₹" + Commons.getCartAmount());

        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

        ivDrawerCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        productCategoryList = BaseApplication.getInstance().getProductCategoryList();

        drawerProductCategoryListAdapter = new DrawerProductCategoryListAdapter(ProductListActivity.this, productCategoryList, robotoBold,robotoLight);

        listViewDrawer.setAdapter(drawerProductCategoryListAdapter);
        drawerProductCategoryListAdapter.notifyDataSetChanged();

        rlCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

//        rlSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProductListActivity.this, ProductSearchActivity.class);
//                startActivity(intent);
//            }
//        });

        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
//                    try {
//                        JSONObject props = new JSONObject();
//                        props.put("SubCategory Name", items.get(groupPosition).getTitle());
//                        mixpanel.track("Sub-Category Click", props);
//                    } catch (Exception e) {
//                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                    }
                }

                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ProductMaster retrievedProductMaster = items.get(groupPosition).productMasterList.get(childPosition);

                if (retrievedProductMaster.getStatus() == 2) {
                    alert("This item will be available soon !!!");
                    return true;
                }

                ProductMaster productMaster = productMasterMap.get(retrievedProductMaster.getId());

                String productMasterGson = new Gson().toJson(productMaster, ProductMaster.class);

                if (productMaster.getProductSubType() == 1){

//                    try {
//                        JSONObject props = new JSONObject();
//                        props.put("Product Name", productMaster.getProductName());
//                        mixpanel.track("Subscription Product Click", props);
//                    } catch (Exception e) {
//                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                    }

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

                return true;
            }
        });

        listViewDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);

                productCategoryId = productCategoryList.get(position).getProductCategoryId();

                getProducts();
            }
        });
    }

    private void fetchAllProducts() {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/productMaster/fetchAll";

            Log.d(TAG, "Url : " + url);

            final StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    Gson gson = new Gson();

                    productMasters = gson.fromJson(response,ProductMaster[].class);
                    getProducts();

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
                        jsonObject.put("productCategoryId", productCategoryId);
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
            Toast.makeText(ProductListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void getProducts() {
        if(isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/productMaster/getAllProductSubCategories";

            Log.d(TAG, "Url : " + url);

            final MyPostJsonArrayRequest jsonArrayRequest = new MyPostJsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, "Response : " + response.toString());

                    Gson gson = new Gson();

                    items.clear();

                    if(response.length() > 0){
                        try {
                            ProductCategory productCategory = gson.fromJson(String.valueOf(response.getJSONObject(0)), ProductCategory.class);

                            if(productCategory.getSubCategories().size() > 0){
                                for (int i = 0; i < productCategory.getSubCategories().size(); i++){
                                    ItemNew itemNew = new ItemNew(productCategory.getSubCategories().get(i).getProductSubCategoryName(),
                                            productCategory.getSubCategories().get(i).getDescription(),
                                            productCategory.getSubCategories().get(i).getProductSubCategoryImage());

                                    for (int j = 0; j < productCategory.getSubCategories().get(i).getProductList().size(); j++) {
                                        ProductMaster productMaster = productCategory.getSubCategories().get(i).getProductList().get(j);

                                        productMasterMap.put(productMaster.getId(), productMaster);

                                        if(productMaster.getStatus() == 3){
                                            continue;
                                        } else {
                                            itemNew.productMasterList.add(productMaster);
                                        }
                                    }

                                    items.add(itemNew);
                                }

                                rlNoProds.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            } else {
                                alert("No products found for your locality.");
                                rlNoProds.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        alert("No products found for your locality.");
                        rlNoProds.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
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
                        jsonObject.put("productCategoryId", productCategoryId);
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
            Toast.makeText(ProductListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeListView() {
        adapter = new ProductListAdapter(this, items);
        adapter.setData(items);
        listView.setAdapter(adapter);
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
     * Adapter for our list of {@link ItemNew}s.
     */
    private class ProductListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

        private ArrayList<ItemNew> items;
        private Context context;
        private LayoutInflater vi;
        private Typeface robotoBold;
        private Typeface robotoLight;

        public ProductListAdapter(Context context, ArrayList<ItemNew> items) {
            this.context = context;
            this.items = items;
            vi = LayoutInflater.from(context);
            robotoLight = Fonts.getTypeface(context,Fonts.FONT_ROBOTO_LIGHT);
            robotoBold = Fonts.getTypeface(context,Fonts.FONT_ROBOTO_BOLD_CONDENSED);
        }

        public void setData(ArrayList<ItemNew> items)
        {
            this.items = items;
        }

        @Override
        public ProductMaster getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).productMasterList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ProductMaster productMaster = getChild(groupPosition, childPosition);

            if(productMaster.getProductSubType() == 1 || productMaster.getStatus() == 2){
                convertView = vi.inflate(R.layout.list_item_entry, null);
            } else if (productMaster.getProductSubType() == 2){
                convertView = vi.inflate(R.layout.product_item_cart, null);
            }

            final TextView title = (TextView)convertView.findViewById(R.id.list_item_entry_title);
            title.setTypeface(robotoBold);
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
            } else if(productMaster.getStatus() == 2 && price != null) {
                price.setVisibility(View.GONE);
                strikePrice.setVisibility(View.GONE);
            }

            if(productMaster.getStatus() == 1 && unit != null) {
                unit.setText(productMaster.getProductUnitSize());
            } else if(productMaster.getStatus() == 2){
                unit.setText("Coming Soon !");
            }

            if(productMaster.getProductImage() != null) {
                imageLoader.displayImage(productMaster.getProductImage(), productImageView);
            }

            if (productMaster.getProductSubType() != 1 && productMaster.getStatus() != 2){
                llAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAdd.setVisibility(View.GONE);
                        llQuantityModification.setVisibility(View.VISIBLE);

                        getChild(groupPosition, childPosition).setProductQuantity(1);
                        BaseApplication.getInstance().getCart().getCartProductList().add(getChild(groupPosition, childPosition));

                        tvCartAmount.setText("₹" + Commons.getCartAmount());

                        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), true);
                    }
                });

                ivSubtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());

                        getChild(groupPosition, childPosition).setProductQuantity(productQuantity - 1);

                        tvCartAmount.setText("₹" + Commons.getCartAmount());

                        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                        if(productQuantity == 1){
                            llAdd.setVisibility(View.VISIBLE);
                            llQuantityModification.setVisibility(View.GONE);

                            BaseApplication.getInstance().getCart().getCartProductList().remove(getChild(groupPosition, childPosition));
                        } else {
                            tvProductQuantity.setText(String.valueOf(productQuantity - 1));
                        }

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), false);
                    }
                });

                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());

                        tvProductQuantity.setText(String.valueOf(productQuantity + 1));

                        int position = BaseApplication.getInstance().getCart().getCartProductList().indexOf(getChild(groupPosition, childPosition));

                        ProductMaster productMaster = BaseApplication.getInstance().getCart().getCartProductList().get(position);
                        productMaster.setProductQuantity(productQuantity + 1);

                        tvCartAmount.setText("₹" + Commons.getCartAmount());

                        tvCartQuantity.setText(Commons.getCartQuantity() + " items");

                        productBuyEvent(productMaster.getProductType() != null ? productMaster.getProductType().getType() : 0, productMaster.getId(), productMaster.getProductName(), productMaster.getProductUnitPrice(), productMaster.getProductUnitSize(), true);
                    }
                });
            }

            return convertView;
        }

        private void productBuyEvent(int type,String id,String productName, Double price, String unit, boolean add) {
            try {
                Product product = new Product()
                        .setId(id)
                        .setName(productName)
                        .setCategory(BaseApplication.getInstance().getUser().getBuilding().getId())
                        .setVariant(unit).setBrand(type+"")
                        .setPrice(price)
                        .setQuantity(1);
                ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);
                if(!add)
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
            }catch(Exception e){}
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).productMasterList.size();
        }

        @Override
        public ItemNew getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ItemNew item = getGroup(groupPosition);
            convertView = vi.inflate(R.layout.list_sub_categories_header, null);

//            if(isQuery){
//                convertView.setOnClickListener(null);
//                convertView.setOnLongClickListener(null);
//                convertView.setLongClickable(false);
//            }

            final TextView sectionView = (TextView) convertView.findViewById(R.id.list_item_section_text);
            final TextView sectionBodyView = (TextView) convertView.findViewById(R.id.list_item_section_body_text);

            ImageView ivHeader = (ImageView) convertView.findViewById(R.id.ivHeader);
            if (item.getImageUrl() != null && item.getImageUrl().length() > 0) {
                imageLoader.displayImage(item.getImageUrl(), ivHeader);
            }

            sectionView.setText(item.getTitle());
            sectionView.setTypeface(robotoBold);

            sectionBodyView.setText(item.getBody());
            sectionBodyView.setTypeface(robotoLight);

//            if (isQuery && !listView.isGroupExpanded(groupPosition)) {
//                listView.expandGroup(groupPosition);
//            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}