package com.villagemilk.customviews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villagemilk.R;

/**
 * Created by arsh on 7/12/16.
 */

public class CartBottomView extends LinearLayout {


    TextView tvTotalItems,tvAmount,tvCalendarAction;

    ImageView ivCalendarAction;

    LinearLayout llCalendarAction;

    OnButtonClickListener onButtonClickListener;


    public static final int DISABLED= 0;
    public static final int ADD_MORE_PRODUCTS = 0;
    public static final int SAVE_CHANGES = 2;
    public static final int FEEDBACK = 3;

    public int getType() {
        return type;
    }

    int type = 0;


    public CartBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.calendar_bottom_button,null);

        LayoutParams params = generateLayoutParams(attrs);

        addView(view,params);

        tvTotalItems = (TextView)view.findViewById(R.id.tvTotalItems);
        tvAmount = (TextView)view.findViewById(R.id.tvAmount);
        tvCalendarAction = (TextView)view.findViewById(R.id.tvCalendarAction);
        ivCalendarAction = (ImageView) view.findViewById(R.id.ivCalendarAction);
        llCalendarAction = (LinearLayout) view.findViewById(R.id.llCalendarAction);

        llCalendarAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onButtonClickListener != null)
                    onButtonClickListener.onClick(type);

            }
        });




    }


    public void updateValues(double amount,int quantity){

        tvAmount.setText("\u20B9 " + amount);
        tvTotalItems.setText(quantity + " items");


    }

    public void setType(int type){

        this.type = type;
        switch (type){

            case DISABLED:
                tvCalendarAction.setText("");
                tvCalendarAction.setTextColor(getResources().getColor(R.color.light_gray_6));
                llCalendarAction.setBackgroundResource(R.color.light_gray);
                setVisibility(GONE);

                break;


            case SAVE_CHANGES:
                tvCalendarAction.setText("Save changes");
                tvCalendarAction.setTextColor(Color.parseColor("#FFFFFF"));
                ivCalendarAction.setImageResource(0);
                llCalendarAction.setBackgroundResource(R.color.ninja_green);

                setVisibility(VISIBLE);

                break;

            case FEEDBACK:
                tvCalendarAction.setText("Report");
                tvCalendarAction.setTextColor(Color.parseColor("#FFFFFF"));
                ivCalendarAction.setImageResource(0);
                llCalendarAction.setBackgroundResource(R.color.ninja_green);

                setVisibility(VISIBLE);
                break;

        }



    }

    public void addOnButtonClickListener(OnButtonClickListener onButtonClickListener){

        this.onButtonClickListener = onButtonClickListener;


    }


    public interface OnButtonClickListener{

        void onClick(int type);

    }



}
