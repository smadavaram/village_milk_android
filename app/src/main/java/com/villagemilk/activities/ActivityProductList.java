package com.villagemilk.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.villagemilk.adapters.AdapterProductListNew;
import com.villagemilk.beans.ProductCategory;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.ProductSubCategory;
import com.villagemilk.beans.User;
import com.villagemilk.databinding.ActivityProductListNewBinding;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by arsh on 11/6/17.
 */

public class ActivityProductList extends BaseActivity{


//    ProductMaster[] productMasters;

    List<ProductMaster> productMasters;

    ActivityProductListNewBinding binding;

    int selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_list_new);

        selectedCategory = getIntent().getExtras().getInt("id");

        setUpToolbar();

        fetchAllProducts();
    }

    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Products");


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }


    private void fetchAllProducts() {
        if(isNetworkAvailable()) {
            Util.showDialog(this);

            String url = Constants.API_PROD + "/productMaster/getProductsForUserBuilding";

            final StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();
                    Gson gson = new Gson();

                    ProductMaster[] products  = gson.fromJson(response,ProductMaster[].class);
                    productMasters = Arrays.asList(products);

                    Log.e("Size of me",productMasters.size() +"");
                    fetchAllCategories();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                }
            }){
                @Override
                public byte[] getBody() {

                    User user = new User();
                    user.setId(Util.getUserId(ActivityProductList.this));

                    return new Gson().toJson(user,User.class).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

//            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(ActivityProductList.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void getSubCategories(final int category) {
        if(isNetworkAvailable()) {
            Util.showDialog(this);

            String url = Constants.API_PROD + "/productMaster/getAllProductSubCategories/" + category;


            final StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();
                    Gson gson = new Gson();

                    if(response.length() > 0){

                        ProductSubCategory[] productCategories = gson.fromJson(response, ProductSubCategory[].class);

                        for(ProductSubCategory productSubCategory:productCategories){
                            ArrayList<ProductMaster> products = new ArrayList<>();
                            productSubCategory.setProductList(products);
                        }

                        for(ProductSubCategory productCategory:productCategories){

                            for(ProductMaster productMaster:productMasters){

                                    if (productMaster.getProductSubCategory().equals(productCategory.getProductSubCategoryId()))
                                        productCategory.getProductList().add(productMaster);

                            }


                        }

                        setUpAdapter(productCategories);






                    } else {
                        alert("No products found for your locality.");

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                   alert("Connection Error");
                    Util.hideDialog();
                }
            }){
/*
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
*/

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(ActivityProductList.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setUpAdapter(ProductSubCategory[] productCategories){

        AdapterProductListNew adapter = new AdapterProductListNew(ActivityProductList.this,productCategories);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {

                if (binding.listView.isGroupExpanded(groupPosition)) {
                    binding.listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    binding.listView.expandGroupWithAnimation(groupPosition);

                }

                return true;
            }
        });

    }

    private void fetchAllCategories() {
        if(isNetworkAvailable()) {
            Util.showDialog(this);

            String url = Constants.API_PROD + "/productMaster/getAllProductCategories";

            final StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Util.hideDialog();

                    Gson gson = new Gson();

                    if(response.length() > 0) {

                        final ProductCategory[] productCategories = gson.fromJson(response, ProductCategory[].class);

                        final List<ProductCategory> productCategoryList = new ArrayList<>();

                        for(ProductCategory productCategory : productCategories){

                            for(ProductMaster productMaster: productMasters){

                                if(productMaster.getProductCategory().intValue() == productCategory.getProductCategoryId().intValue()){

                                    productCategoryList.add(productCategory);

                                    break;

                                }

                            }



                        }

                        int currentTab = 0;

                        for(final ProductCategory productCategory : productCategoryList){

                            if(selectedCategory == productCategory.getProductCategoryId())
                                currentTab = productCategoryList.indexOf(productCategory);

                            TabLayout.Tab tab = binding.tlCategories.newTab();

                            View v = LayoutInflater.from(ActivityProductList.this).inflate(R.layout.category_tab,null);
                            ImageView imageView = (ImageView) v.findViewById(R.id.ivCategoryImage);
                            TextView textView = (TextView) v.findViewById(R.id.tvProductCategory);


                            ImageLoader.getInstance().displayImage(productCategory.getProductCategoryImage(),imageView);

                            textView.setText(productCategory.getProductCategoryName());

                            tab.setCustomView(v);

                            binding.tlCategories.addTab(tab);

/*
                            List<ProductMaster> list = new ArrayList<>();

                            for(ProductMaster productMaster : productMasters){

                                if(productMaster.getProductCategory() == productCategory.getProductCategoryId()){

                                    list.add(productMaster);

                                }

                            }

                            productCategory.setProductList(list);

*/

                        }

                        if(binding.tlCategories.getTabCount() <=1)
                            binding.tlCategories.setVisibility(View.GONE);

                        binding.tlCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {

                                getSubCategories(productCategoryList.get(tab.getPosition()).getProductCategoryId());

                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });


                        getSubCategories(productCategoryList.get(currentTab).getProductCategoryId());


                        final int finalCurrentTab = currentTab;
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override public void run() {

                                        binding.tlCategories.getTabAt(finalCurrentTab).select();
                                    }
                                }, 100);
//                        binding.tlCategories.

                    }


                    }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                }
            }){
/*
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();

                    return jsonObject.toString().getBytes();
                }
*/

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(ActivityProductList.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
