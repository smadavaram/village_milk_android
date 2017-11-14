package com.villagemilk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.beans.BillingItem;
import com.villagemilk.util.Constants;
import com.villagemilk.util.Fonts;

import java.util.ArrayList;


public class BillingAdapter extends ArrayAdapter<BillingItem> {
    private static final String TAG = "Billing Adapter";

    private Context context;
    private ArrayList<BillingItem> items;
    private LayoutInflater vi;
    public boolean onHomeScreen = false;
    private Typeface robotoBold;
    private Typeface robotoLight;

    public BillingAdapter(Context context, ArrayList<BillingItem> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        robotoBold = Fonts.getTypeface(context,Fonts.FONT_ROBOTO_BOLD_CONDENSED);
        robotoLight = Fonts.getTypeface(context,Fonts.FONT_ROBOTO_LIGHT);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final BillingItem i = items.get(position);
        if (i != null) {
            BillingItem ei = i;

            if(onHomeScreen){
                v = vi.inflate(R.layout.bill_item_entry_home, null);
            } else {
                v = vi.inflate(R.layout.bill_item_entry, null);
            }

            final TextView paid_date=(TextView)v.findViewById(R.id.paid_date);
            paid_date.setTypeface(robotoLight);
            final TextView rupee_value = (TextView)v.findViewById(R.id.ruppes_value);
            final TextView date=(TextView)v.findViewById(R.id.start_end_date);
            final ImageView item_image=(ImageView)v.findViewById((R.id.imageView1));

            if (paid_date != null){
                paid_date.setText(ei.paid_date);
            }

            if(rupee_value != null) {
                rupee_value.setTextColor(context.getResources().getColor(R.color.black));
                if(ei.isCurrent && ei.amount > 0 ) {
                    rupee_value.setTextColor(context.getResources().getColor(R.color.red_btn_bg_pressed_color));
                    rupee_value.setText("$ -" + Constants.priceDisplay.format(ei.amount));
                } else if(ei.isCurrent && ei.amount < 0) {
                    rupee_value.setText("$ " + (-1 * ei.amount));
                } else{
                    rupee_value.setText(ei.rupees);
                }
            }

            if(date != null){
                date.setText(ei.start_end_date);
            }

            if(item_image!=null) {
                item_image.setImageResource(ei.imageid);
            }
        }
        return v;
    }
}
