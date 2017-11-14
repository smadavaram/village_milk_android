package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.customviews.QuantityKnob;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.util.DateOperations;
import com.villagemilk.view.ActivitySubscription;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class QuickAddProductsListAdapter extends RecyclerView.Adapter<QuickAddProductsListAdapter.ViewHolder> {
    private static final String TAG = "QuickAddProductsListAdapter";

    private Context context;
    private List<ProductMaster> productMasterList = new ArrayList<>();

    ImageLoader imageLoader = ImageLoader.getInstance();

    public QuickAddProductsListAdapter(Context context, List<ProductMaster> productMasterList) {
        this.context = context;
        this.productMasterList = productMasterList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvProductName;
        private TextView tvProductUnitSize;
        private TextView tvProductUnitPrice;
        private TextView tvStrikePrice;
        private TextView tvSubscribe;
        private LinearLayout llFlashItem;

        private TextView tvSpecialText;

        private LinearLayout llSubscribe;


        public ViewHolder(View itemView) {
            super(itemView);

            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductUnitSize = (TextView) itemView.findViewById(R.id.tvProductUnitSize);
            tvProductUnitPrice = (TextView) itemView.findViewById(R.id.tvProductUnitPrice);
            tvStrikePrice = (TextView) itemView.findViewById(R.id.tvStrikePrice);
            llFlashItem = (LinearLayout) itemView.findViewById(R.id.llFlashItem);
            llSubscribe = (LinearLayout) itemView.findViewById(R.id.llSubscribe);
            tvSpecialText = (TextView) itemView.findViewById(R.id.tvSpecialText);
            tvSubscribe = (TextView) itemView.findViewById(R.id.tvSubscribe);


        }
    }

    @Override
    public QuickAddProductsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flash_product_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuickAddProductsListAdapter.ViewHolder holder, final int position) {
        final ProductMaster productMaster = productMasterList.get(position);

//        Picasso.with(context).load(productMaster.getProductImage()).into(holder.ivProduct);

        imageLoader.displayImage(productMaster.getProductImage(),holder.ivProduct);

        holder.tvProductName.setText(productMaster.getProductName());

        holder.tvProductUnitSize.setText(productMaster.getProductUnitSize());

        StringBuilder priceString = new StringBuilder().append("$").append(Constants.priceDisplay.format(productMaster.getProductUnitPrice()));
        holder.tvProductUnitPrice.setText(priceString);

        if (productMaster.getStrikePrice() != null) {
            StringBuilder strikePriceString = new StringBuilder().append("$").append(Constants.priceDisplay.format(productMaster.getStrikePrice()));

            SpannableString spannableString = new SpannableString(strikePriceString);
            spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvStrikePrice.setText(spannableString);

            holder.tvStrikePrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvStrikePrice.setVisibility(View.GONE);
        }



        SubscriptionWrapper subscription = null;



        for(SubscriptionWrapper subscriptionMasterSmall: BaseApplication.getInstance().getSubscriptionList()) {

            if (subscriptionMasterSmall.getProductMaster().equals(productMaster.getId())) {

                holder.tvSubscribe.setText("SUBSCRIBED");
                holder.llSubscribe.setBackgroundResource(R.color.gray_btn_bg_pressed_color);
                subscription = subscriptionMasterSmall;
                break;
            } else {

                holder.tvSubscribe.setText("SUBSCRIBE");
                holder.llSubscribe.setBackgroundResource(R.color.ninja_green);


            }

        }

        final SubscriptionWrapper finalSubscription = subscription;
        holder.llSubscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(BaseApplication.getInstance().containsSubscriptionMaster(productMaster.getId())!=null){

//                    finalSubscription.setUserId(BaseApplication.getInstance().getUser().getId());

                    Intent intent = new Intent(context, ActivitySubscription.class);
                    intent.putExtra("product_master", finalSubscription.getProductMaster());


                    context.startActivity(intent);


                }else {
                    SubscriptionMaster subscriptionMaster = new SubscriptionMaster();
                    subscriptionMaster.setProductMaster(productMaster);
                    subscriptionMaster.setProductImage(productMaster.getProductImage());
                    subscriptionMaster.setProductName(productMaster.getProductName());
                    subscriptionMaster.setProductUnitCost(productMaster.getProductUnitPrice());
                    subscriptionMaster.setStartDate(DateOperations.getTomorrowStartDate().getTime());

                    subscriptionMaster.setUserId(BaseApplication.getInstance().getUser().getId());

                    Intent intent = new Intent(context, ActivitySubscription.class);
                    intent.putExtra("subscription_master", new Gson().toJson(subscriptionMaster, SubscriptionMaster.class));


                    context.startActivity(intent);
                }


            }
        });


        if(productMaster.getSpecialText() != null && productMaster.getSpecialText().length() > 0) {

            String specialText = productMaster.getSpecialText();
            holder.tvSpecialText.setText(specialText);
            holder.tvSpecialText.setVisibility(View.VISIBLE);
            if(specialText.length() > 12 && specialText.length() < 20)
                holder.tvSpecialText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12.0f);
            else if(specialText.length() > 20)
                holder.tvSpecialText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10.0f);

        }

    }

    @Override
    public int getItemCount() {
        return productMasterList.size();
    }
}
