package com.villagemilk.viewmodel;

import android.content.Context;

import com.villagemilk.model.calendar.CalendarResponse;
import com.villagemilk.model.calendar.Subscription;

/**
 * Created by android on 2/12/16.
 */

public class CalendarItemViewModel {

    Context mContext;

    CalendarResponse calendarResponse;

    public Subscription subscription;


    public CalendarItemViewModel(Context mContext,Subscription subscription) {

        this.mContext = mContext;
        this.subscription = subscription;

    }






}
