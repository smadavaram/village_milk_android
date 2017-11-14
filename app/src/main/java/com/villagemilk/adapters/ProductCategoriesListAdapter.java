package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.beans.ProductCategory;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class ProductCategoriesListAdapter extends RecyclerView.Adapter<ProductCategoriesListAdapter.ViewHolder> {
    private static final String TAG = "ProductCategoriesListAdapter";

    private Context context;
    private List<ProductCategory> productCategoryList = new ArrayList<>();

    private boolean isExpanded = false;

    private static final int CATEGORY_ITEM = 0;

    private static final int EXPAND_COLLAPSE_ITEM = 1;

    private boolean inProgress;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public ProductCategoriesListAdapter(Context context, List<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductCategory;
        private TextView tvProductCategory;

        private int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;

            if(viewType == CATEGORY_ITEM){
                ivProductCategory = (ImageView) itemView.findViewById(R.id.ivProductCategory);
                tvProductCategory = (TextView) itemView.findViewById(R.id.tvProductCategory);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ProductListActivity.class);
//                        intent.putExtra("product_category_id", productCategoryList.get(getLayoutPosition()).getProductCategoryId());
//                        intent.putExtra("intent_source", "HomeActivity");
//
//                        try {
//                            JSONObject props = new JSONObject();
//                            props.put("Category", productCategoryList.get(getLayoutPosition()).getProductCategoryName());
//                            ((HomeActivity) context).mixpanel.track("HomeBean Category Click", props);
//                            intent.putExtra("categoryName", productCategoryList.get(getLayoutPosition()).getProductCategoryName());
//                        } catch (Exception e) {
//                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                        }
//
//                        context.startActivity(intent);
                    }
                });
            } else {
                if(!inProgress){
                    inProgress = true;
                    ivProductCategory = (ImageView) itemView.findViewById(R.id.ivProductCategory);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isExpanded = !isExpanded;
                            ((HomeActivity) context).getProductTypesInFocus();
                            notifyDataSetChanged();
                        }
                    });
                }

                inProgress = false;
            }
        }
    }

    @Override
    public ProductCategoriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == CATEGORY_ITEM){
            view = LayoutInflater.from(context).inflate(R.layout.home_grid_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.expand_collapse_item, parent, false);
        }

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ProductCategoriesListAdapter.ViewHolder holder, int position) {
        if(holder.viewType == CATEGORY_ITEM){
            ProductCategory productCategory = productCategoryList.get(position);

            if(productCategory.getProductCategoryImage() != null && productCategory.getProductCategoryImage().length() > 0){
//                Picasso.with(context).load(productCategory.getProductCategoryImage()).into(holder.ivProductCategory);
                imageLoader.displayImage(productCategory.getProductCategoryImage(),holder.ivProductCategory);
            }

            holder.tvProductCategory.setText(productCategory.getProductCategoryName());
            holder.tvProductCategory.setVisibility(View.VISIBLE);
        } else {
            if(isExpanded){
                holder.ivProductCategory.setImageResource(R.drawable.ic_collapse);
            } else {
                holder.ivProductCategory.setImageResource(R.drawable.ic_expand);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();

//        if(isExpanded){
//            return productCategoryList.size() + 1;
//        } else {
//            if(productCategoryList.size() > 7){
//                return 8;
//            } else {
//                return productCategoryList.size();
//            }
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return CATEGORY_ITEM;

//        if(isExpanded){
//            if(position == productCategoryList.size()){
//                return EXPAND_COLLAPSE_ITEM;
//            } else {
//                return CATEGORY_ITEM;
//            }
//        } else {
//            if(position == 7 && productCategoryList.size()>=7){
//                return EXPAND_COLLAPSE_ITEM;
//            }
//            else {
//                return CATEGORY_ITEM;
//            }
//        }
    }
}

