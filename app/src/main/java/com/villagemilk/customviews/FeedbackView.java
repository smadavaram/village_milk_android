package com.villagemilk.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.annotation.Px;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.villagemilk.R;

/**
 * Created by android on 26/11/16.
 */

public class FeedbackView extends LinearLayout implements View.OnClickListener{

    String[] options;// = {"Delivered","Not Delivered","Damaged"};
    int[] selected_drawables;// = {R.drawable.others_selected,R.drawable.not_delivered_selected,R.drawable.damaged_selected};
    int[] unselected_drawables;// = {R.drawable.other_unselected,R.drawable.not_delivered_unselected,R.drawable.damaged_unselected};
    OnItemSelected onItemSelected;

    boolean isPaused = false;

    int type;

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;

        ImageView imageView = (ImageView)findViewWithTag("iv" + selectedItemPosition);

        TextView textView = (TextView)findViewWithTag(selectedItemPosition);

        imageView.setImageResource(selected_drawables[selectedItemPosition]);
        if(selectedItemPosition == 0) {
            textView.setTextColor(getResources().getColor(R.color.ninja_green));
            imageView.setColorFilter(getResources().getColor(R.color.ninja_green));
        }
        else {

            textView.setTextColor(getResources().getColor(R.color.red));
            imageView.setColorFilter(0);

        }

        if(onItemSelected != null){
            onItemSelected.onItemSelected(selectedItemPosition,textView.getText().toString());
        }


    }

    int selectedItemPosition = -1;


    public FeedbackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FeedbackView,
                0, 0);

        type = a.getInt(R.styleable.FeedbackView_state,0);

        switch (type){

            case 0:
                options = new String[]{"Delivered", "Not Delivered", "Damaged"};
                selected_drawables = new int[]{R.drawable.others_selected, R.drawable.not_delivered_selected, R.drawable.damaged_selected};
                unselected_drawables = new int[]{R.drawable.other_unselected, R.drawable.not_delivered_unselected, R.drawable.damaged_unselected};

                break;


        }

        setWeightSum(options.length);
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.top_border);
        setGravity(Gravity.CENTER_VERTICAL);

        for(int i=0;i<options.length;i++){

            View linearLayout = LayoutInflater.from(context).inflate(R.layout.feedback_item,null);

            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ivFeedback);
            imageView.setTag("iv" + i);
            imageView.setImageResource(unselected_drawables[i]);

            TextView textView = (TextView) linearLayout.findViewById(R.id.tvFeedback);
            textView.setText(options[i]);
            textView.setTag(i);
            textView.setSingleLine(true);
            textView.setMaxLines(1);
            linearLayout.setOnClickListener(this);
//            radioButton.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams params = generateLayoutParams(attrs);
            params.weight = 1;
            params.setMargins(0,0,0,0);
            params.height = LayoutParams.WRAP_CONTENT;

            addView(linearLayout,params);

            if(i != options.length -1) {
                View view = new View(context);
                view.setBackgroundResource(R.color.seperator);
                params = generateLayoutParams(attrs);
                params.width = 3;
                params.setMargins(0,8,0,0);
                params.height = LayoutParams.MATCH_PARENT;
                addView(view, params);
            }


        }
    }

    @Override
    public void onClick(View view) {

        TextView tv = (TextView) view.findViewById(R.id.tvFeedback);

        if(type == 0) {

            for (int i = 0; i < options.length; i++) {

                TextView textView = (TextView) findViewWithTag(i);
                ImageView imageView = (ImageView) findViewWithTag("iv" + i);
//            RadioButton textView = (RadioButton) findViewWithTag(i);

                if ((int) textView.getTag() == selectedItemPosition) {

                    onItemSelected.onItemDeselected(selectedItemPosition);
                    textView.setTextColor(getResources().getColor(R.color.black_text_color));
                    imageView.setImageResource(unselected_drawables[selectedItemPosition]);
                    imageView.setColorFilter(0);
                    selectedItemPosition = -1;

                } else if ((int) textView.getTag() == (int) tv.getTag()) {

                    selectedItemPosition = (int) tv.getTag();

                    imageView.setImageResource(selected_drawables[selectedItemPosition]);
                    if ((int) tv.getTag() == 0) {
                        textView.setTextColor(getResources().getColor(R.color.ninja_green));
                        imageView.setColorFilter(getResources().getColor(R.color.ninja_green));
                    } else {

                        textView.setTextColor(getResources().getColor(R.color.red));
                        imageView.setColorFilter(0);

                    }

                    if (onItemSelected != null) {
                        onItemSelected.onItemSelected(i, textView.getText().toString());
                    }

                } else {

                    textView.setTextColor(getResources().getColor(R.color.black_text_color));

                    imageView.setImageResource(unselected_drawables[i]);

                    imageView.setColorFilter(0);

                }


            }

        }else if(type == 1){

            for (int i = 0; i < options.length; i++) {

                TextView textView = (TextView) findViewWithTag(i);

                if ((int) textView.getTag() == (int) tv.getTag()) {

                    if (onItemSelected != null) {
                        onItemSelected.onItemSelected(i, textView.getText().toString());
                    }

                }

            }

        }

    }

    public void addOnItemSelectedListener(final OnItemSelected onItemSelected){

        this.onItemSelected = onItemSelected;

    }

    public interface OnItemSelected{

        void onItemSelected(int position, String text);

        void onItemDeselected(int position);

    }


    public void isPaused(boolean isPaused){

        this.isPaused = isPaused;

        ImageView imageView = (ImageView)findViewWithTag("iv" + 1);

        TextView textView = (TextView)findViewWithTag(1);

        if(isPaused){

            textView.setText("Resume");
            imageView.setImageResource(unselected_drawables[1]);

        }else{

            textView.setText("Pause");
            imageView.setImageResource(selected_drawables[1]);

        }




    }


}
