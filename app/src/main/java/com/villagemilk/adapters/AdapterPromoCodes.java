package com.villagemilk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.PromoObject;
import com.villagemilk.util.Fonts;

/**
 * Created by android on 8/12/16.
 */

public class AdapterPromoCodes extends RecyclerView.Adapter<AdapterPromoCodes.ViewHolder> {


    Context mContext;
    PromoObject[] promoObjects;

    OnPromoCodeSelected onPromoCodeSelected;

    protected Typeface robotoLight;
    protected Typeface robotoRegular;
    protected Typeface robotoMedium;


    public AdapterPromoCodes(Context mContext, PromoObject[] promoObjects) {

        this.mContext = mContext;
        this.promoObjects = promoObjects;

        robotoLight = Fonts.getTypeface(mContext, Fonts.FONT_ROBOTO_LIGHT);
        robotoRegular = Fonts.getTypeface(mContext, Fonts.ROBOTO_REGULAR);
        robotoMedium = Fonts.getTypeface(mContext, Fonts.ROBOTO_MEDIUM);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.promo_item,null);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final PromoObject promoObject = promoObjects[position];

        int colors[] = {R.color.blue,R.color.red,R.color.blue,R.color.red,R.color.blue,R.color.red,R.color.blue,R.color.red,R.color.blue,R.color.red};

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);

        gradientDrawable.setUseLevel(true);
//        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);

//        gradientDrawable.setCornerRadius(0f);

//        holder.llPromoItem.setBackgroundDrawable(gradientDrawable);

        holder.tvPromoDescription.setText(promoObject.getDescription());
        holder.tvPromoCode.setText("PROMOCODE : " + promoObject.getPromoCode());

        holder.tvPromoCode.setTypeface(robotoMedium);
        holder.tvPromoDescription.setTypeface(robotoRegular);
        holder.tvApplyPromo.setTypeface(robotoMedium);

        holder.tvApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onPromoCodeSelected != null)
                    onPromoCodeSelected.onCodeApplied(promoObject.getPromoCode());

            }
        });


    }

    @Override
    public int getItemCount() {
        return promoObjects.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPromoDescription,tvPromoCode,tvApplyPromo;

        LinearLayout llPromoItem;


        public ViewHolder(View itemView) {
            super(itemView);

            tvPromoDescription = (TextView)itemView.findViewById(R.id.tvPromoDescription);
            tvPromoCode = (TextView)itemView.findViewById(R.id.tvPromoCode);
            tvApplyPromo = (TextView)itemView.findViewById(R.id.tvApplyPromo);
            llPromoItem = (LinearLayout)itemView.findViewById(R.id.llPromoItem);

        }
    }


    public void addOnPromoCodeSelected(OnPromoCodeSelected onPromoCodeSelected){

        this.onPromoCodeSelected = onPromoCodeSelected;

    }

    public interface OnPromoCodeSelected{

        void onCodeApplied(String code);

    }
}
