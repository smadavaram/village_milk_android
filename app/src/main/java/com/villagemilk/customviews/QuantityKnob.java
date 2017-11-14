package com.villagemilk.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villagemilk.R;


/**
 * Created by arsh on 7/11/16.
 */

public class QuantityKnob extends LinearLayout implements View.OnClickListener {

    int quantity = 0;

    OnQuantityUpdated onQuantityUpdated;

    LinearLayout llAdd, llQuantity;

    TextView tvProductQuantity,tvAdd;

    ImageView ivAdd, ivSubtract;

    int type;

    public QuantityKnob(Context context, AttributeSet attrs) {
        super(context, attrs);

        addView(LayoutInflater.from(context).inflate(R.layout.quantity_knob,null));

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.QuantityKnob,
                0, 0);

        type = a.getInt(R.styleable.QuantityKnob_type,1);


        llAdd = (LinearLayout)findViewById(R.id.llAdd);
        llQuantity = (LinearLayout)findViewById(R.id.llQuantityModification);
        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        ivSubtract = (ImageView) findViewById(R.id.ivSubtract);
        tvProductQuantity = (TextView) findViewById(R.id.tvProductQuantity);
        tvAdd = (TextView) findViewById(R.id.tvAdd);



        switch (type){

            case 0:
                setBackgroundResource(R.drawable.cart_quantity_modification_background);
                llQuantity.setBackgroundResource(R.drawable.cart_quantity_modification_background);
                tvAdd.setText("ADD");
                tvAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                ivAdd.setImageResource(R.drawable.ic_add_green);
                ivSubtract.setImageResource(R.drawable.ic_subtract_green);
                tvProductQuantity.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);


                break;
            case 1:
                break;

            case 2:
                setBackgroundResource(0);
                llQuantity.setBackgroundResource(0);
                llAdd.setVisibility(GONE);
                llQuantity.setVisibility(VISIBLE);
                ivAdd.setImageResource(R.drawable.ic_add_black_24dp);
                ivSubtract.setImageResource(R.drawable.ic_remove_black_24dp);
                tvProductQuantity.setBackgroundResource(0);
                tvProductQuantity.setTextColor(getResources().getColor(R.color.secondary_text));
                break;

        }

        initListeners();


    }

    private void initListeners() {

        llAdd.setOnClickListener(this);
        llQuantity.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivSubtract.setOnClickListener(this);

    }


    public void add(){


        quantity++;

        updateView();

        if(onQuantityUpdated !=null)
            onQuantityUpdated.onQuantityAdded(quantity);

    }

    public void subtract(){

        if(quantity == 0)
            return;

        quantity --;

//        if(quantity <= 0){
//
//            llAdd.setVisibility(VISIBLE);
//            llQuantity.setVisibility(GONE);
//
//        }

        updateView();

        if(onQuantityUpdated !=null)
            onQuantityUpdated.onQuantitySubtracted(quantity);

    }

    public void updateView(){

        tvProductQuantity.setText(String.valueOf(quantity));

        if(type == 2)
            return;

        if(quantity <= 0){

            llAdd.setVisibility(VISIBLE);
            llQuantity.setVisibility(GONE);

        }else {

            llAdd.setVisibility(GONE);
            llQuantity.setVisibility(VISIBLE);

        }



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.llAdd:
                add();
                break;
            case R.id.ivAdd:
                add();
                break;
            case R.id.ivSubtract:
                subtract();
                break;


        }

    }

    public void isModifiable(boolean modifiable){

        if(modifiable) {
            ivAdd.setVisibility(VISIBLE);
            ivSubtract.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.cart_quantity_modification_background);
            llQuantity.setBackgroundResource(R.drawable.cart_quantity_modification_background);
        }else{
            ivAdd.setVisibility(GONE);
            ivSubtract.setVisibility(GONE);
            setBackgroundResource(0);
            llQuantity.setBackgroundResource(0);

        }


    }

    public void addOnQuantityUpdatedListener(OnQuantityUpdated onQuantityUpdated){

        this.onQuantityUpdated = onQuantityUpdated;

    }

    public interface OnQuantityUpdated{

        void onQuantityAdded(int quantity);
        void onQuantitySubtracted(int quantity);


    }

    public void setProductQuantity(int quantity){

        this.quantity = quantity;
        updateView();


    }
}
