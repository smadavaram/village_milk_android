package com.villagemilk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.UserExpenditure;
import com.villagemilk.util.Fonts;

/**
 * Created by arsh on 17/10/16.
 */

public class AdapterUserSpends extends RecyclerView.Adapter<AdapterUserSpends.ViewHolder> {


    public AdapterUserSpends(Context mContext, UserExpenditure[] userExpenditures) {
        this.mContext = mContext;
        this.userExpenditures = userExpenditures;

    }

    Context mContext;
    UserExpenditure[] userExpenditures;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_spends, parent, false);
        return new ViewHolder(mContext,view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserExpenditure userExpenditure = userExpenditures[position];
        holder.tvExpenditureName.setText(userExpenditure.getCategoryName());
        holder.tvExpenditureAmount.setText(String.valueOf(userExpenditure.getExpense()));
        holder.pbExpenditure.setProgress((int)(userExpenditure.getFraction()*100));

    }

    @Override
    public int getItemCount() {
        return (userExpenditures.length - 1);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvExpenditureName;
        TextView tvExpenditureAmount;
        ProgressBar pbExpenditure;

        protected Typeface robotoLight;

        public ViewHolder(Context context,View itemView) {
            super(itemView);

             robotoLight = Fonts.getTypeface(context, Fonts.FONT_ROBOTO_LIGHT);



            tvExpenditureName = (TextView)itemView.findViewById(R.id.tvExpenditureName);
            tvExpenditureAmount = (TextView)itemView.findViewById(R.id.tvExpenditureAmount);
            pbExpenditure = (ProgressBar) itemView.findViewById(R.id.pbExpenditure);

            tvExpenditureName.setTypeface(robotoLight);
            tvExpenditureAmount.setTypeface(robotoLight);

        }
    }

}
