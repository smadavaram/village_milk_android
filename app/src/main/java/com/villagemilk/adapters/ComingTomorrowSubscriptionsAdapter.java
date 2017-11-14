package com.villagemilk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by akash.mercer on 18-Sep-16.
 */

public class ComingTomorrowSubscriptionsAdapter extends ArrayAdapter<SubscriptionMasterSmall> {
    private static final String TAG = "ComingTomoroowSubscript";

    private Context context;
    private ArrayList<SubscriptionMasterSmall> subscriptionMasterList;
    private LayoutInflater vi;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public ComingTomorrowSubscriptionsAdapter(Context context, ArrayList<SubscriptionMasterSmall> subscriptionMasterList){
        super(context,0, subscriptionMasterList);
        this.context = context;
        this.subscriptionMasterList = subscriptionMasterList;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final SubscriptionMasterSmall subscriptionMaster = subscriptionMasterList.get(position);

        if (subscriptionMaster != null) {
            view = vi.inflate(R.layout.home_subscription_item_entry, null);
            final TextView product_name = (TextView)view.findViewById(R.id.list_item_entry_title);
            final TextView product_price=(TextView)view.findViewById(R.id.tvCost);
            final TextView product_unit=(TextView)view.findViewById(R.id.tvQuantity);

            ImageView productImageView = (ImageView) view.findViewById(R.id.im_productImage1);
            productImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.product_default));
            if (product_name != null)
                product_name.setText(subscriptionMaster.getProductName());

            if(product_unit != null)
                product_unit.setText(subscriptionMaster.getProductQuantity() + " x " + subscriptionMaster.getProductUnitSize());
            if(subscriptionMaster.getSubscriptionType() == 1)//Hardcoded for subscription type 1- Everyday,2-Alternate Day,3-Every 2 days
                product_price.setText("₹"+ Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost()*subscriptionMaster.getProductQuantity()));
            else if(subscriptionMaster.getSubscriptionType() == 2)
                product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost()*subscriptionMaster.getProductQuantity()));
            else if(subscriptionMaster.getSubscriptionType() == 3)
                product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost()*subscriptionMaster.getProductQuantity()));
            else{//If this is an exception, then it will populate Exception date
                product_price.setText("₹"+Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost()*subscriptionMaster.getProductQuantity()));
            }

            if(subscriptionMaster.getProductImageUrl() != null){
                imageLoader.displayImage(subscriptionMaster.getProductImageUrl(),productImageView);
//                Picasso.with(context).load(subscriptionMaster.getProductImageUrl()).into(productImageView);
            }
        }
        return view;
    }
}

