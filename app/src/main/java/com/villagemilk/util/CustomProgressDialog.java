package com.villagemilk.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.villagemilk.R;

/**
 * Created by sagaryarnalkar on 14/05/15.
 */
public class CustomProgressDialog extends Dialog {

    private ImageView iv;

    public CustomProgressDialog(Context context) {
        super(context, R.style.transparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        iv = new ImageView(context);
//	iv.setImageResource(resourceIdOfImage);
        layout.addView(iv, params);

        iv.setVisibility(View.GONE);

        ProgressBar progressBar = new ProgressBar(context);

        layout.addView(progressBar,params);


        TextView tv = new TextView(context);
        tv.setText("Loading...");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        layout.addView(tv,params);

        addContentView(layout, params);

        WindowManager.LayoutParams lp=getWindow().getAttributes();
        //set transparency of background
        lp.dimAmount=0.8f;  // dimAmount between 0.0f and 1.0f, 1.0f is completely dark
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

//    private AnimationDrawable progressAnimation;

    @Override
    public void dismiss()
    {
//        if(progressAnimation!=null)
//        {
//            if(progressAnimation.isRunning())
//            {
//                progressAnimation.stop();
//            }
//        }
        super.dismiss();
    }


    @Override
    public void show() {
//        try {
//            iv.setBackgroundResource(R.drawable.loading_anim);

//	ColorDrawable colorDrawable = new ColorDrawable();
            // ColorDrawable colorDrawable =
//	     new ColorDrawable(0xFF00FF00);    // With a custom default color.

//	colorDrawable.setColor(0xFFFF0000);
//	getWindow().setBackgroundDrawable(colorDrawable);
            //Get the image background and attach the AnimationDrawable to it.
//            progressAnimation = (AnimationDrawable) iv.getBackground();

            //Start the animation after the dialog is displayed.
//            setOnShowListener(new OnShowListener() {
//                @Override
//                public void onShow(DialogInterface dialog) {
//                    try {
//                        progressAnimation.start();
//                    }catch(OutOfMemoryError ex)
//                    {
//                        System.gc();
//                    }
//                }
//            });
//        }
//        catch(OutOfMemoryError e){
//            System.gc();
//
//        }
        super.show();
//    dialog.show();

    }
}
