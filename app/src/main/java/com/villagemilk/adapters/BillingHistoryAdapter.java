package com.villagemilk.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.BillingLog;
import com.villagemilk.beans.BillingLogMaster;
import com.villagemilk.util.Commons;
import com.villagemilk.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by akash.mercer on 16-Aug-16.
 */
public class BillingHistoryAdapter extends RecyclerView.Adapter<BillingHistoryAdapter.ViewHolder> {
    private static final String TAG = "BillingHistoryAdapter";

    private Context context;
    private List<BillingLogMaster> billingLogMasterList = new ArrayList<>();

    public BillingHistoryAdapter(Context context, List<BillingLogMaster> billingLogMasterList) {
        this.context = context;
        this.billingLogMasterList = billingLogMasterList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView ivBillingLog;
//        private TextView tvDate;
//        private TextView tvOpeningBalance;
//        private LinearLayout llTransactions;
//        private LinearLayout llTransactionLogs;

        private TextView mTvWeekday;
        private TextView mTvDateText;
        private LinearLayout llAddEvent;
        private TextView tvAddevnt;
        private LinearLayout llEventContainer;

        public ViewHolder(View itemView) {
            super(itemView);

//            ivBillingLog = (ImageView) itemView.findViewById(R.id.ivBillingLog);
//            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
//            tvOpeningBalance = (TextView) itemView.findViewById(R.id.tvOpeningBalance);
//            llTransactions = (LinearLayout) itemView.findViewById(R.id.llTransactions);
//            llTransactionLogs = (LinearLayout) itemView.findViewById(R.id.llTransactionLogs);

            mTvWeekday = (TextView) itemView.findViewById(R.id.tvWeekDay);
            mTvDateText = (TextView) itemView.findViewById(R.id.tvDateText);
            llAddEvent = (LinearLayout) itemView.findViewById(R.id.llAddEvent);
            tvAddevnt = (TextView) itemView.findViewById(R.id.tvAddevnt);
            llEventContainer = (LinearLayout) itemView.findViewById(R.id.llEventContainer);
        }
    }

    @Override
    public BillingHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillingHistoryAdapter.ViewHolder holder, int position) {
        BillingLogMaster billingLogMaster = billingLogMasterList.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(billingLogMaster.getDate());

        holder.mTvWeekday.setText(Commons.weekDay.format(calendar.getTime()));
        holder.mTvDateText.setText(Commons.dateSdf.format(calendar.getTime()));

        holder.llEventContainer.removeAllViews();

//        holder.tvDate.setText(billingLogMaster.getDateString());

//        if (billingLogMaster.getBalance() != null) {
//            holder.tvOpeningBalance.setText("$" + Constants.priceDisplay.format(billingLogMaster.getBalance()).toString());
//            holder.tvOpeningBalance.setVisibility(View.VISIBLE);
//        } else {
//            holder.tvOpeningBalance.setVisibility(View.GONE);
//        }
//
//        holder.llTransactions.removeAllViews();
//
//        if(billingLogMaster.getBillingLogTransactionList() != null && billingLogMaster.getBillingLogTransactionList().size() > 0){
//            for (int i = 0; i < billingLogMaster.getBillingLogTransactionList().size(); i++) {
//                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.billing_log_transaction_item, null, false);
//                textView.setText(billingLogMaster.getBillingLogTransactionList().get(i));
//
//                holder.llTransactions.addView(textView);
//            }
//        }
//
//        holder.llTransactionLogs.removeAllViews();

        if(billingLogMaster.getBillingLogList() != null && billingLogMaster.getBillingLogList().size() > 0){
            for (int i = 0; i < billingLogMaster.getBillingLogList().size(); i++) {
                BillingLog billingLog = billingLogMaster.getBillingLogList().get(i);

                View view = LayoutInflater.from(context).inflate(R.layout.item_dailylog, null, false);

//                TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
//                TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
//
//                tvDescription.setText(billingLog.getBillingLogProperties().getDescription());
//                tvAmount.setText("$" + Constants.priceDisplay.format((billingLog.getBillingLogProperties().getAmount()*-1)));
//
//                if(billingLog.getBillingLogProperties().getAmount() < 0){
//                    tvAmount.setTextColor(ContextCompat.getColor(context, R.color.ninja_green));
//                } else {
//                    tvAmount.setTextColor(ContextCompat.getColor(context, R.color.ninja_orange));
//                }
//
//                if(billingLog.getBillingLogProperties().getDailyLogType() == 5){
//                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.promo_color));
//                } else {
//                    view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
//                }

                TextView tvDescription = (TextView) view.findViewById(R.id.tvDesc);
                tvDescription.setText(billingLog.getBillingLogProperties().getDescription());

                TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
                tvAmount.setText("$" + Constants.priceDisplay.format(billingLog.getBillingLogProperties().getAmount()));

                ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

                if(billingLog.getBillingLogProperties().getAmount() > 0){
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.big_red_dot));
                }

                if(billingLog.getBillingLogProperties().getDailyLogType() == 3) {
                    tvAmount.setText("$" + Constants.priceDisplay.format(billingLog.getBillingLogProperties().getAmount()*-1));
                }

                if(billingLog.getBillingLogProperties().getDailyLogType() == 1) {
                    imageView.setVisibility(View.GONE);

                    imageView = (ImageView) view.findViewById(R.id.imageView1);
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.big_red_dot));

                    tvAmount.setText("$" + Constants.priceDisplay.format(billingLog.getBillingLogProperties().getAmount()));
                    tvDescription.setText(billingLog.getBillingLogProperties().getDescription());
                }

                if(billingLog.getBillingLogProperties().getDailyLogType() == 5) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.promo_color));
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
                }

                holder.llEventContainer.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return billingLogMasterList.size();
    }
}
