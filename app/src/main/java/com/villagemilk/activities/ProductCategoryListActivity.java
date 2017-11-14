package com.villagemilk.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.ProductCategoryListAdapter;
import com.villagemilk.beans.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryListActivity extends BaseActivity {
    private static final String TAG = "Product Category List Activity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<ProductCategoryListAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ProductCategory> productCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category_list);

        initSimpleToolbar();

        productCategoryList = BaseApplication.getInstance().getProductCategoryList();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);

        BaseApplication.getInstance().setProductCategoryList(productCategoryList);
        adapter = new ProductCategoryListAdapter(this, productCategoryList);
        recyclerView.setAdapter(adapter);
    }
}
