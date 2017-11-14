package com.villagemilk.activities.calendar;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;

/**
 * Created by android on 3/12/16.
 */

public class ActivityCalendarNew extends BaseActivity {

    ViewPager vpCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_new);

        vpCalendar = (ViewPager)findViewById(R.id.vpCalendar);




    }
}
