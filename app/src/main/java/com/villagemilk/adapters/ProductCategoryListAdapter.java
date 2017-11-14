package com.villagemilk.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.activities.ProductCategoryListActivity;
import com.villagemilk.beans.ProductCategory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.view.ActivitySubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 29-03-2016.
 */
public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.ViewHolder>{
    private static final String TAG = "Product Categories List Adapter";

    private ProductCategoryListActivity context;
    private List<ProductCategory> productCategoryList = new ArrayList<>();

    private boolean isExpanded;

    private ProductCategoryListAdapter homeCategoriesListAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private static final int CATEGORY_ITEM = 0;

    private static final int EXPAND_COLLAPSE_ITEM = 1;

    public ProductCategoryListAdapter(ProductCategoryListActivity context, List<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;

        homeCategoriesListAdapter = this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductCategory;
        private TextView tvProductCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProductCategory = (ImageView) itemView.findViewById(R.id.ivProductCategory);
            tvProductCategory = (TextView) itemView.findViewById(R.id.tvProductCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivitySubscription.class);
                    intent.putExtra("product_category_id", productCategoryList.get(getLayoutPosition()).getProductCategoryId());

                    if (context.getIntent().getExtras() != null) {
                        if (context.getIntent().getExtras().getString("intent_source") != null) {//Get source for dynamic visibility of RadioGroup on next page
                            intent.putExtra("intent_source", context.getIntent().getExtras().getString("intent_source"));
                            if (context.getIntent().getExtras().getString("intent_source").equalsIgnoreCase("CalendarActivity")) {
                                intent.putExtra("source_calendar_date", context.getIntent().getExtras().get("source_calendar_date").toString());
                                context.startActivity(intent);
                            } else {
                                intent.putExtra("intent_source", "HomeActivity");
                                context.startActivity(intent);
                            }
                        } else {
                            intent.putExtra("intent_source", "HomeActivity");
                            context.startActivity(intent);
                        }
                    } else {
                        intent.putExtra("intent_source", "HomeActivity");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public ProductCategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.home_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductCategoryListAdapter.ViewHolder holder, int position) {
        ProductCategory productCategory = productCategoryList.get(position);

        if(productCategory.getProductCategoryImage() != null && productCategory.getProductCategoryImage().length() > 0){
            imageLoader.displayImage(productCategory.getProductCategoryImage(), holder.ivProductCategory);
        }

        holder.tvProductCategory.setText(productCategory.getProductCategoryName());
        holder.tvProductCategory.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }
}
