package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.activities.SubscriptionActivity;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by akash.mercer on 18-Sep-16.
 */

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {
    private static final String TAG = "SubscriptionListAdapter";

    private Context context;

    private List<SubscriptionMasterSmall> subscriptionMasterList = new ArrayList<>();

    public SubscriptionListAdapter(Context context, List<SubscriptionMasterSmall> subscriptionMasterList){
        this.context = context;
        this.subscriptionMasterList = subscriptionMasterList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView product_name;
        private TextView product_price;
        private TextView product_unit;
        private LinearLayout llEdit;
        private ImageView productImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.list_item_entry_title);
            product_price =(TextView) itemView.findViewById(R.id.tvCost);
            product_unit = (TextView) itemView.findViewById(R.id.tvQuantity);
            llEdit = (LinearLayout) itemView.findViewById(R.id.llEdit);
            productImageView = (ImageView) itemView.findViewById(R.id.im_productImage1);

            llEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addSubscriptionIntent = new Intent(context, SubscriptionActivity.class);
                    addSubscriptionIntent.putExtra(Constants.INTENT_SOURCE, "UpdateSubscription");
                    addSubscriptionIntent.putExtra("subscription_master", new Gson().toJson(subscriptionMasterList.get(getLayoutPosition()), SubscriptionMasterSmall.class));
                    context.startActivity(addSubscriptionIntent);
                }
            });
        }
    }

    @Override
    public SubscriptionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_subscription_item_entry, parent, false);
        return new SubscriptionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubscriptionListAdapter.ViewHolder holder, int position) {
        SubscriptionMasterSmall subscriptionMaster = subscriptionMasterList.get(position);

        holder.productImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.product_default));

        holder.product_name.setText(subscriptionMaster.getProductName());

        String tillDate = "";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 15);

        if(DateOperations.getDateFromLong(subscriptionMaster.getEndDate()).after(cal.getTime())) {

        } else {
            tillDate = " till "+ Commons.dateSdf.format(DateOperations.getDateFromLong(subscriptionMaster.getEndDate()));
        }

        if(holder.product_unit != null)
            holder.product_unit.setText(subscriptionMaster.getProductQuantity() + " x " + subscriptionMaster.getProductUnitSize());
        if(subscriptionMaster.getSubscriptionType() == 1)//Hardcoded for subscription type 1- Everyday,2-Alternate Day,3-Every 2 days
            holder.product_price.setText("₹"+ Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost() * subscriptionMaster.getProductQuantity())+" Everyday"+tillDate);
        else if(subscriptionMaster.getSubscriptionType() == 2)
            holder.product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost() * subscriptionMaster.getProductQuantity())+" Alternate Day"+tillDate);
        else if(subscriptionMaster.getSubscriptionType() == 3)
            holder.product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost() * subscriptionMaster.getProductQuantity())+" Every 3 Days"+tillDate);
        else{//If this is an exception, then it will populate Exception date
            holder.product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost() * subscriptionMaster.getProductQuantity())+" for a day "+Commons.dateSdf.format(DateOperations.getDateFromLong(subscriptionMaster.getEndDate())));
        }

        if(subscriptionMaster.getProductImageUrl() != null){
            ImageLoader.getInstance().displayImage(subscriptionMaster.getProductImageUrl(),holder.productImageView);
            
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionMasterList.size();
    }

}
