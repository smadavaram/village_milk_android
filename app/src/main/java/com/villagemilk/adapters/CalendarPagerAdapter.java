package com.villagemilk.adapters;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;


import com.villagemilk.BaseApplication;
import com.villagemilk.R;

import com.villagemilk.activities.BaseActivity;
import com.villagemilk.view.FragmentCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by android on 30/11/16.
 */



/*{
        "userId" : "UhhAiJjUU5",
        "startDate" : "2016-11-25",
        "endDate" : "2016-12-03"

}*/

public class CalendarPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
//    Calendar calendar;

    public CalendarPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mContext = context;
//        calendar = Calendar.getInstance();

    }



    @Override
    public int getCount() {
        return 90;
    }

    @Override
    public FragmentCalendar getItem(int position) {

        FragmentCalendar fragmentCalendar = FragmentCalendar.getInstance();

//        calendar.add(Calendar.DAY_OF_YEAR,(position - 60));

        Bundle args = new Bundle();
        args.putInt(mContext.getResources().getString(R.string.position),position);

        fragmentCalendar.setArguments(args);

        return fragmentCalendar;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return "";
    }

    public View getTabView(int position) {
        final View tab = LayoutInflater.from(mContext).inflate(R.layout.calendar_tab, null);
        TextView tvDate = (TextView) tab.findViewById(R.id.tvDate);
        TextView tvDay = (TextView) tab.findViewById(R.id.tvDay);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,(position - 60));

        tvDate.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = calendar.getTime();
        String dayOfTheWeek = sdf.format(d);
        tvDay.setText(dayOfTheWeek);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);


        if(BaseApplication.getInstance().getDeliveryDays().contains(dayFormat.format(calendar.getTime()))){

                tvDate.setTextColor(mContext.getResources().getColor(R.color.white));
                tvDay.setTextColor(mContext.getResources().getColor(R.color.white));


            }else {

                tab.setOnClickListener(null);
                tvDate.setTextColor(mContext.getResources().getColor(R.color.separator));
                tvDay.setTextColor(mContext.getResources().getColor(R.color.separator));



        }


        return tab;
    }

}
