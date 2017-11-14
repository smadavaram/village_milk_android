package com.villagemilk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.villagemilk.BaseApplication;
import com.villagemilk.beans.FeedbackObject;
import com.villagemilk.util.Fonts;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.R;
import com.villagemilk.customviews.FeedbackView;
import com.villagemilk.customviews.QuantityKnob;
import com.villagemilk.model.calendar.CalendarResponse;
import com.villagemilk.model.calendar.Subscription;

import java.util.List;

/**
 * Created by android on 1/12/16.
 */

public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarItemAdapter.ViewHolder> {


    Context mContext;

    CalendarResponse calendarResponse;

    List<Subscription> subscriptions;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public static final int DN_FULFILLED = 1;
    public static final int DN_CANCELLED = 2;
    public static final int DN_RESCHEDULED = 3;



    protected Typeface robotoLight;
    protected Typeface robotoRegular;
    protected Typeface robotoMedium;

//    final HashMap<Integer,String> feedbackMap = new HashMap<>();

    OnFeedbackUpdated onFeedbackUpdated;


    boolean showFeedback;

    boolean isQuantityModifiable = true;

    OnProductUpdated onProductUpdated;


    public CalendarItemAdapter(Context mContext, CalendarResponse calendarResponse) {
        this.mContext = mContext;
        this.calendarResponse = calendarResponse;
        this.subscriptions = calendarResponse.getSubscriptions();


        robotoLight = Fonts.getTypeface(mContext, Fonts.FONT_ROBOTO_LIGHT);
        robotoRegular = Fonts.getTypeface(mContext, Fonts.ROBOTO_REGULAR);
        robotoMedium = Fonts.getTypeface(mContext, Fonts.ROBOTO_MEDIUM);

        onFeedbackUpdated = (OnFeedbackUpdated)mContext;
        onProductUpdated = (OnProductUpdated)mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        CheckoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.checkout_item,parent,false);

        View view = LayoutInflater.from(mContext).inflate(R.layout.checkout_item,parent,false);


        return new ViewHolder(view);
    }


    public void showFeedback(boolean show){

        showFeedback = show;

        notifyDataSetChanged();


    }

    public void quantityModifiable(boolean set){

        isQuantityModifiable = set;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        CalendarItemViewModel calendarItemViewModel = new CalendarItemViewModel(mContext,subscriptions.get(position));
//
//        holder.binding.setViewModel(calendarItemViewModel);

        final Subscription subscription = subscriptions.get(position);

        imageLoader.displayImage(subscription.getProductImage(),holder.ivProduct);

        holder.tvProductName.setText(subscription.getProductName());
        holder.tvProductName.setTypeface(robotoRegular);
        holder.tvProductQuantity.setText(subscription.getProductUnit());
        holder.tvProductQuantity.setTypeface(robotoLight);
        holder.tvProductUnitPrice.setText("$" + String.valueOf(subscription.getProductUnitCost()));
        holder.tvProductUnitPrice.setTypeface(robotoMedium);


        if(!TextUtils.isEmpty(subscription.getSpecialText())) {
            holder.tvSpecialText.setVisibility(View.VISIBLE);
            holder.tvSpecialText.setText(subscription.getSpecialText());

        }else{
            holder.tvSpecialText.setVisibility(View.GONE);
        }

        subscription.setProductTransientQuantity(subscription.getProductQuantity());

        if(showFeedback) {
            holder.feedbackView.setVisibility(View.VISIBLE);



            holder.feedbackView.addOnItemSelectedListener(new FeedbackView.OnItemSelected() {
                @Override
                public void onItemSelected(int pos, String text) {


                    String itemFeedback =  subscription.getProductName() + " " + text + "\n";
//                    feedback = feedback.concat(itemFeedback);

//                    feedbackMap.put(position,itemFeedback);

                    FeedbackObject feedbackObject = new FeedbackObject();
                    feedbackObject.setDate(calendarResponse.getDate());
                    feedbackObject.setStatus(pos + 1);
                    feedbackObject.setFeedbackText(itemFeedback);
                    feedbackObject.setSubscriptionId(subscription.getId());
                    feedbackObject.setBuildingId(BaseApplication.getInstance().getUser().getBuilding().getId());
                    feedbackObject.setUserId(BaseApplication.getInstance().getUser().getId());

                    onFeedbackUpdated.onFeedbackAdded(position,feedbackObject);
//                    ((CalendarActivity) context).updateReportView();

                }

                @Override
                public void onItemDeselected(int pos) {

                    onFeedbackUpdated.onFeedbackRemoved(position);

                }
            });

            if(subscription.getUserAction() !=null){

                switch (subscription.getUserAction()){

                    case 1:
                        holder.feedbackView.setSelectedItemPosition(0);
                        break;
                    case 2:
                        holder.feedbackView.setSelectedItemPosition(1);
                        break;
                    case 3:
                        holder.feedbackView.setSelectedItemPosition(2);
                        break;


                }


            }


        }
        else {
            holder.feedbackView.setVisibility(View.GONE);
        }

        holder.quantityKnob.isModifiable(isQuantityModifiable);
        holder.quantityKnob.setProductQuantity(subscription.getProductQuantity());

        if(subscription.getProductQuantity() == 0){

            holder.quantityKnob.setVisibility(View.INVISIBLE);

        }else {

            holder.quantityKnob.setVisibility(View.VISIBLE);

        }

        if(isQuantityModifiable) {

            holder.quantityKnob.addOnQuantityUpdatedListener(new QuantityKnob.OnQuantityUpdated() {
                @Override
                public void onQuantityAdded(int quantity) {

                    subscription.setProductQuantity(quantity);

                    if(isSubscriptionsUpdated()){

                        onProductUpdated.onProductUpdated(subscriptions);

                    }else{

                        onProductUpdated.onProductUnchanged();

                    }

                }

                @Override
                public void onQuantitySubtracted(int quantity) {

                    subscription.setProductQuantity(quantity);

                    if(isSubscriptionsUpdated()){

                        onProductUpdated.onProductUpdated(subscriptions);

                    }else{

                        onProductUpdated.onProductUnchanged();

                    }

                }
            });


        }


        if(subscription.getProductType()!=null){

            if(subscription.getProductType() == 53) {
                holder.quantityKnob.isModifiable(false);
                holder.feedbackView.setVisibility(View.GONE);
            }
        }

        if(subscription.getDnAction() != null) {
            switch (subscription.getDnAction()){

                case DN_FULFILLED:
//                    holder.ivStatus.setVisibility(View.GONE);
                    holder.tvStatus.setVisibility(View.GONE);
//                    holder.llCheckoutItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    holder.overlay.setVisibility(View.GONE);
//                    holder.overlay.setVisibility(View.GONE);
//                    holder.llCheckoutItem.setAlpha(1.0f);

                    break;

                case DN_CANCELLED:

                 holder.tvStatus.setText("Cancelled");

                    holder.quantityKnob.isModifiable(false);
//                 holder.ivStatus.setBackgroundColor(Color.parseColor("#14FF0000"));
                     holder.tvStatus.setBackgroundColor(Color.parseColor("#FF0000"));                    holder.llCheckoutItem.setBackgroundColor(Color.parseColor("#22000000"));
//                    holder.llCheckoutItem.setBackgroundColor(Color.parseColor("#22000000"));

//                    holder.llCheckoutItem.setAlpha(0.5f);
//                    holder.tvSpecialText.setText("Cancelled");
                    holder.tvStatus.setVisibility(View.VISIBLE);
//                    holder.overlay.setVisibility(View.VISIBLE);


                    break;
                case DN_RESCHEDULED:

//                 holder.ivStatus.setText("Rescheduled");
//                    holder.ivStatus.setBackgroundResource(R.color.ninja_green);
                    holder.quantityKnob.isModifiable(false);

                    holder.tvStatus.setText("Rescheduled");
//                 holder.ivStatus.setBackgroundColor(Color.parseColor("#14FF0000"));
                    holder.tvStatus.setBackgroundColor(Color.parseColor("#0000FF"));
//                    holder.llCheckoutItem.setBackgroundColor(Color.parseColor("#22000000"));

//                    holder.llCheckoutItem.setAlpha(0.5f);
                    holder.tvStatus.setVisibility(View.VISIBLE);
//                    holder.overlay.setVisibility(View.VISIBLE);



                    break;



            }
        }



    }



    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

//        private CheckoutItemBinding binding;

        private TextView tvProductName;
        private TextView tvProductQuantity;
        private TextView tvSpecialText;
        private TextView tvProductUnitPrice;
//        private ImageView ivStatus;
        private TextView tvStatus;
        private QuantityKnob quantityKnob;
        private FeedbackView feedbackView;
        private RelativeLayout rlCheckoutItem;
        private LinearLayout llCheckoutItem;
        private ImageView ivProduct;
//        private View overlay;


        public ViewHolder(View view) {
            super(view);

//            this.binding = binding;

            tvProductName = (TextView)view.findViewById(R.id.tvProductName);
            tvProductQuantity = (TextView)view.findViewById(R.id.tvProductQuantity);
            tvSpecialText = (TextView)view.findViewById(R.id.tvSpecialText);
//            ivStatus = (ImageView)view.findViewById(R.id.ivStatus);
            tvProductUnitPrice = (TextView)view.findViewById(R.id.tvProductUnitPrice);
            tvStatus = (TextView)view.findViewById(R.id.tvStatus);
            quantityKnob = (QuantityKnob)view.findViewById(R.id.quantityKnob);
            feedbackView = (FeedbackView) view.findViewById(R.id.feedbackView);
            ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
            rlCheckoutItem = (RelativeLayout) view.findViewById(R.id.rlCheckoutItem);
            llCheckoutItem = (LinearLayout) view.findViewById(R.id.llCheckoutItem);
//            overlay = view.findViewById(R.id.overlay);


        }
    }


/*
    public String getFeedback() {

        String feedback = "";
        Iterator it = feedbackMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            feedback = feedback.concat(pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return feedback;
    }
*/



    public boolean isSubscriptionsUpdated() {


        for (Subscription subscription : subscriptions) {
            if (subscription.getProductQuantity() != subscription.getProductTransientQuantity()) {
                return true;
            }

        }

        return false;

    }

    public interface OnFeedbackUpdated {

        void onFeedbackAdded(int position, FeedbackObject feedbackObject);

        void onFeedbackRemoved(int position);

    }

    public interface OnProductUpdated{

        void onProductUpdated(List<Subscription> subscriptions);

        void onProductUnchanged();

    }


}
