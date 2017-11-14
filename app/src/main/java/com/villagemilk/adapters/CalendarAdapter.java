package com.villagemilk.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.CalendarActivity;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.customviews.FeedbackView;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by akash.mercer on 04-Sep-16.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private static final String TAG = "CalendarAdapter";

    private Context context;
    private List<SubscriptionMasterSmall> subscriptionMasterList = new ArrayList<>();

    CalendarActivity calendarActivity;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public void showFeedbackButtons(boolean showFeedbackButtons) {
        this.showFeedbackButtons = showFeedbackButtons;
    }

    boolean showFeedbackButtons = false;

    private List<Integer> subscriptionProductTypes;

    boolean isUpdateAllowed;

    public String getFeedback() {

        Iterator it = feedbackMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            feedback = feedback.concat(pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return feedback;
    }

    private String feedback = "This is for testing. Kindly ignore :) . \n";

    HashMap<Integer,String> feedbackMap;

    Double totalAmount = 0.0;
    int totalQuantity = 0;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;

        if(currentDate.after(DateOperations.getTodayStartDate())){
            isUpdateAllowed = true;
        } else {
            isUpdateAllowed = false;
        }
    }

    Date currentDate;


    public CalendarAdapter(Context context, List<SubscriptionMasterSmall> subscriptionMasterList, Calendar selectedDate) {
        this.context = context;
        calendarActivity = (CalendarActivity)context;
        this.subscriptionMasterList = subscriptionMasterList;
        subscriptionProductTypes = BaseApplication.getInstance().getSubscriptionProductTypes();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rootView;
        private ImageView ivProduct;
        private TextView tvProductName;
        private TextView tvProductUnitSize;
        private TextView tvProductUnitPrice;
        private TextView tvStrikePrice;
        private View viewSeparator;
        private TextView tvSpecialText;
        private LinearLayout llQuantityModification;
        private LinearLayout llAdd;
        private ImageView ivSubtract;
        private TextView tvProductQuantity;
        private ImageView ivAdd;
        private FeedbackView feedbackView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = (RelativeLayout) itemView.findViewById(R.id.rootView);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductUnitSize = (TextView) itemView.findViewById(R.id.tvProductUnitSize);
            tvProductUnitPrice = (TextView) itemView.findViewById(R.id.tvProductUnitPrice);
            tvStrikePrice = (TextView) itemView.findViewById(R.id.tvStrikePrice);
            viewSeparator = itemView.findViewById(R.id.viewSeparator);
            tvSpecialText = (TextView) itemView.findViewById(R.id.tvSpecialText);
            llQuantityModification = (LinearLayout) itemView.findViewById(R.id.llQuantityModification);
            llAdd = (LinearLayout) itemView.findViewById(R.id.llAdd);
            ivSubtract = (ImageView) itemView.findViewById(R.id.ivSubtract);
            tvProductQuantity = (TextView) itemView.findViewById(R.id.tvProductQuantity);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
//            feedbackView = (FeedbackView)itemView.findViewById(R.id.fvCalendar);

            llAdd.setVisibility(View.GONE);

            llAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llAdd.setVisibility(View.GONE);
                    llQuantityModification.setVisibility(View.VISIBLE);

                    tvProductQuantity.setText(String.valueOf(1));
                    subscriptionMasterList.get(getAdapterPosition()).setProductQuantity(1);

                    addItem(subscriptionMasterList.get(getAdapterPosition()));

                    ((CalendarActivity) context).checkIfSubscriptionsUpdated();

                }
            });

            ivSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());

                    productQuantity--;

                    if(productQuantity <= 0){
                        llAdd.setVisibility(View.VISIBLE);
                        llQuantityModification.setVisibility(View.GONE);
                    }

                    tvProductQuantity.setText(String.valueOf(productQuantity));
                    subscriptionMasterList.get(getAdapterPosition()).setProductQuantity(productQuantity);

                    substractItem(subscriptionMasterList.get(getAdapterPosition()));

                    ((CalendarActivity) context).checkIfSubscriptionsUpdated();
                }
            });

            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productQuantity = Integer.valueOf(tvProductQuantity.getText().toString());
                    productQuantity++;

                    tvProductQuantity.setText(String.valueOf(productQuantity));
                    subscriptionMasterList.get(getAdapterPosition()).setProductQuantity(productQuantity);

                    addItem(subscriptionMasterList.get(getAdapterPosition()));

                    ((CalendarActivity) context).checkIfSubscriptionsUpdated();
                }
            });

            feedbackView.addOnItemSelectedListener(new FeedbackView.OnItemSelected() {
                @Override
                public void onItemSelected(int position, String text) {

                    String itemFeedback =  subscriptionMasterList.get(getAdapterPosition()).getProductName() + " " + text + "\n";
//                    feedback = feedback.concat(itemFeedback);

                    feedbackMap.put(getAdapterPosition(),itemFeedback);

                    ((CalendarActivity) context).updateReportView();


                }

                @Override
                public void onItemDeselected(int position) {

                }
            });
        }

        public void addItem(SubscriptionMasterSmall subscriptionMasterSmall){

            totalAmount = calendarActivity.getTotalAmount();
            totalQuantity = calendarActivity.getTotalQuantity();

            totalQuantity++;
            totalAmount += subscriptionMasterSmall.getProductUnitCost();

            calendarActivity.setTotalQuantity(totalQuantity);
            calendarActivity.setTotalAmount(totalAmount);


        }

        public void substractItem(SubscriptionMasterSmall subscriptionMasterSmall){

            totalAmount = calendarActivity.getTotalAmount();
            totalQuantity = calendarActivity.getTotalQuantity();
            totalQuantity--;
            totalAmount -= subscriptionMasterSmall.getProductUnitCost();

            calendarActivity.setTotalQuantity(totalQuantity);
            calendarActivity.setTotalAmount(totalAmount);

        }
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_cart_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CalendarAdapter.ViewHolder holder, int position) {
        final SubscriptionMasterSmall subscriptionMaster = subscriptionMasterList.get(position);

        if(showFeedbackButtons) {
            holder.feedbackView.setVisibility(View.VISIBLE);
            if(feedbackMap == null){

                feedbackMap = new HashMap<>();

            }
        }
        else
            holder.feedbackView.setVisibility(View.GONE);

//        Picasso.with(context).load(subscriptionMaster.getProductImageUrl()).into(holder.ivProduct);

        imageLoader.displayImage(subscriptionMaster.getProductImageUrl(),holder.ivProduct);
        holder.tvProductName.setText(subscriptionMaster.getProductName());
        holder.tvProductUnitSize.setText(subscriptionMaster.getProductUnitSize());
        holder.tvProductUnitPrice.setText("\u20B9" + Constants.priceDisplay.format(subscriptionMaster.getProductUnitCost()));
        holder.tvProductQuantity.setText(String.valueOf(subscriptionMaster.getProductQuantity()));

        holder.llQuantityModification.setVisibility(View.VISIBLE);
        holder.tvStrikePrice.setVisibility(View.GONE);
        holder.tvSpecialText.setVisibility(View.GONE);
        holder.viewSeparator.setVisibility(View.GONE);

        if(isUpdateAllowed){
            if (subscriptionMaster.getProductQuantity() == 0){
                holder.ivAdd.setVisibility(View.GONE);
                holder.ivSubtract.setVisibility(View.GONE);
                holder.llAdd.setVisibility(View.VISIBLE);
            } else {
                holder.ivAdd.setVisibility(View.VISIBLE);
                holder.ivSubtract.setVisibility(View.VISIBLE);
                holder.llAdd.setVisibility(View.GONE);
            }
        } else {
            holder.ivAdd.setVisibility(View.GONE);
            holder.ivSubtract.setVisibility(View.GONE);
            holder.llAdd.setVisibility(View.GONE);
        }

        if (subscriptionMaster.getProductType() == 53){
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.promo_color));

            holder.llAdd.setVisibility(View.GONE);
            holder.llQuantityModification.setVisibility(View.VISIBLE);
            holder.ivAdd.setVisibility(View.GONE);
            holder.ivSubtract.setVisibility(View.GONE);
        } else {
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionMasterList.size();
    }


}

