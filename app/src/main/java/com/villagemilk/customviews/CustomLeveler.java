package com.villagemilk.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.villagemilk.R;

/**
 * Created by arsh on 18/10/16.
 */

public class CustomLeveler extends LinearLayout {


    ValueAnimator valueAnimator;
    View view,view2;
    float level;



    public CustomLeveler(Context context) {
        super(context);
    }

    public CustomLeveler(Context context, AttributeSet attrs) {
        super(context, attrs);

        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0.0f,1.0f);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        setOrientation(HORIZONTAL);
        setWeightSum(1.0f);

        setBackgroundResource(R.drawable.bordered_box);

        view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.accent));

        LayoutParams params = new LayoutParams(context,attrs);

        params.height = 100;
        params.width = LayoutParams.WRAP_CONTENT;


        view.setLayoutParams(params);

        addView(view);


        view2 = new View(context);
        view2.setBackgroundColor(context.getResources().getColor(R.color.white));

        params = new LayoutParams(context,attrs);

        params.height = 100;
        params.weight = LayoutParams.WRAP_CONTENT;


        view2.setLayoutParams(params);

        addView(view2);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                ((LayoutParams)view.getLayoutParams()).weight = level*animation.getAnimatedFraction();

            }
        });


    }

    public CustomLeveler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setLevel(float level){

//        this.level = level;

//        valueAnimator.start();

        view.setMinimumWidth((int)level*100);
        view2.setMinimumWidth((int)(1 -level)*10);

    }
}
