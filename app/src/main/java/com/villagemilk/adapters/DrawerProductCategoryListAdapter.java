package com.villagemilk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.ProductCategory;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 29-03-2016.
 */
public class DrawerProductCategoryListAdapter extends BaseAdapter {
    private static final String TAG = "Drawer Product Category List Adapter";

    private Context context;
    private List<ProductCategory> productCategoryList = new ArrayList<>();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Typeface robotoBold;
    private Typeface robotoLight;

    public DrawerProductCategoryListAdapter(Context context, List<ProductCategory> productCategoryList, Typeface robotoBold, Typeface robotoLight) {
        this.context = context;
        this.productCategoryList = productCategoryList;
        this.robotoBold = robotoBold;
        this.robotoLight = robotoLight;
    }

    @Override
    public int getCount() {
        return productCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return productCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.home_grid_item, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivProductCategory = (ImageView) view.findViewById(R.id.ivProductCategory);
            viewHolder.tvProductCategory = (TextView) view.findViewById(R.id.tvProductCategory);
            viewHolder.tvProductCategory.setTypeface(robotoBold);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        ProductCategory productCategory = productCategoryList.get(position);

        if(productCategory.getProductCategoryImage() != null && productCategory.getProductCategoryImage().length() > 0){
            imageLoader.displayImage(productCategory.getProductCategoryImage(), viewHolder.ivProductCategory);
        }

        viewHolder.tvProductCategory.setText(productCategory.getProductCategoryName());

        return view;
    }

    private class ViewHolder{
        private ImageView ivProductCategory;
        private TextView tvProductCategory;
    }
}
