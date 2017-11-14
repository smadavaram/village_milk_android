package com.villagemilk.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.villagemilk.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dailyninja on 14/08/17.
 */

public class AdapterCalendarDatePager extends PagerAdapter {

    Context mContext;
    Date startDate;




    public AdapterCalendarDatePager(Context mContext) {
        this.mContext = mContext;
    }




    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {



        return super.instantiateItem(container, position);




    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext).inflate(R.layout.calendar_tab, null);
        TextView tvDate = (TextView) tab.findViewById(R.id.tvDate);
        TextView tvDay = (TextView) tab.findViewById(R.id.tvDay);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,(position - 60));

        tvDate.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = calendar.getTime();
        String dayOfTheWeek = sdf.format(d);
        tvDay.setText(dayOfTheWeek);
        return tab;
    }


}
