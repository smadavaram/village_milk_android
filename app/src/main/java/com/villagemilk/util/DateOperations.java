package com.villagemilk.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Deepam on 13-May-15.
 */
public class DateOperations {

//    //Local Database
//    //Check if subscription is to be sent tomorrow.``
//    public static boolean isSubscriptionTomorrow(Date start_date,Date end_date,Date today_date,int subType){
//        Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("IST"));
//        c.setTime(today_date);
//        c.add(Calendar.DATE, 1);
//        if(subType==1 && start_date.before(c.getTime()))
//            return true;//Default for subType = everyday
//        else{
//            if(end_date!=null){
//                if(end_date.after(today_date) && start_date.before(c.getTime())) {//Check only if end_date is greater than today
//                    long dateDifference = c.getTime().getTime() - start_date.getTime();
//                    long dayDifference = dateDifference / (1000 * 60 * 60 * 24);
//                    Log.d("***","****day diff " + dayDifference);
//                    if (subType == 2){
//                        if (dayDifference%2 == 0)
//                            return true;
//                    }
//                    if(subType == 3){
//                        if(dayDifference % 3 == 0)
//                            return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    public static Date getTodayStartDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.set(Calendar.HOUR_OF_DAY, - 5);
//        calendar.set(Calendar.MINUTE, - 30);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getTodayEndDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 29);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getTomorrowStartDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.add(Calendar.DATE, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, -5);
//        calendar.set(Calendar.MINUTE, - 30);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getYesterdayStartDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.add(Calendar.DATE, -1);
//        calendar.set(Calendar.HOUR_OF_DAY, -5);
//        calendar.set(Calendar.MINUTE, - 30);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getYesterdayEndDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.add(Calendar.DATE, -1);
//        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 29);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static String getDateString(Date d){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST")); //To save date in Indian Standard time
//        String returnDate = simpleDateFormat.format(d);
//        return returnDate;
//    }
//
//    public static Date getStringToDate(String dateString){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST")); //To save date in Indian Standard time
//        Date date = new Date();
//        try {
//            date = simpleDateFormat.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    public static int getDateDifference(Date date1, Date date2){
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(date1);
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTime(date2);
//
//        return calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
//    }
//
//    public static Date getStartOfDate(Date date){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.set(Calendar.HOUR_OF_DAY, -5);
//        calendar.set(Calendar.MINUTE, - 30);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getEndOfDate(Date date){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 29);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    public static Date getDateFromLong(long millis){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(millis);
//        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
//        return calendar.getTime();
//    }

    //Amazon Database
    //Check if subscription is to be sent tomorrow.``
    public static boolean isSubscriptionTomorrow(Date start_date,Date end_date,Date today_date,int subType){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("IST"));
        c.setTime(today_date);
        c.add(Calendar.DATE, 1);
        if(subType==1 && start_date.before(c.getTime()))
            return true;//Default for subType = everyday
        else{
            if(end_date!=null){
                if(end_date.after(today_date) && start_date.before(c.getTime())) {//Check only if end_date is greater than today
                    long dateDifference = c.getTime().getTime() - start_date.getTime();
                    long dayDifference = dateDifference / (1000 * 60 * 60 * 24);
                    Log.d("***", "****day diff " + dayDifference);
                    if (subType == 2){
                        if (dayDifference%2 == 0)
                            return true;
                    }
                    if(subType == 3){
                        if(dayDifference % 3 == 0)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static Date getTodayStartDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getTodayEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getTomorrowStartDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        //TODO Change this on timezone change
        int minutes = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60 + Calendar.getInstance().get(Calendar.MINUTE);
        if(minutes <= 329){
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getYesterdayStartDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.DATE, - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getYesterdayEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.DATE, - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String getDateString(Date d){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST")); //To save date in Indian Standard time
        String returnDate = simpleDateFormat.format(d);
        return returnDate;
    }

    public static String getDateString(long date){

        return getDateString(getDateFromLong(date));
    }

    public static Date getStringToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST")); //To save date in Indian Standard time
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int getDateDifference(Date date1, Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDateDifferenceFromLong(Long date1, Long date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);

        return calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
    }

    public static Date getStartOfDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateFromLong(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        return calendar.getTime();
    }

    public static Date getFutureSubscriptionDate(){

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("IST"));
        c.set(Calendar.YEAR,2027);
        c.set(Calendar.DAY_OF_YEAR,1);
        return c.getTime();
    }
}
