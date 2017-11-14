package com.villagemilk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.villagemilk.BaseApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by android on 28/12/16.
 */

public class Util {


    private static int dialogCall = 0;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showDialog(Context context) {

        dialogCall++;
        CustomProgressDialog progressDialog = BaseApplication.getInstance().getProgressDialog();

        if(progressDialog==null || !progressDialog.isShowing()) {
            progressDialog = new CustomProgressDialog(context);
            BaseApplication.getInstance().setProgressDialog(progressDialog);
            progressDialog.setCancelable(false);
            progressDialog.show();
//            dialogHandler=null;
//            dialogTime = System.currentTimeMillis();
        }
    }



    public static void hideDialog() {

        dialogCall--;

        if(dialogCall != 0)
            return;

        final CustomProgressDialog progressDialog = BaseApplication.getInstance().getProgressDialog();

        if(progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void alert(Context context,String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }


    public static String getUserId(Context context){

        if(BaseApplication.getInstance().getUser()!=null)
            return BaseApplication.getInstance().getUser().getId();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        return sharedPreferences.getString(Constants.USER_ID, "");


    }

    public static int getNextDeliveryDay(){



        Calendar calendar = Calendar.getInstance();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        List<Integer> days = new ArrayList<>();

        for(String day:BaseApplication.getInstance().getDeliveryDays()){

            if(day.equalsIgnoreCase("Sunday")){

                days.add(1);

            }else if(day.equalsIgnoreCase("Monday")){

                days.add(2);

            }else if(day.equalsIgnoreCase("Tuesday")){

                days.add(3);

            }else if(day.equalsIgnoreCase("Wednesday")){

                days.add(4);

            }else if(day.equalsIgnoreCase("Thursday")){

                days.add(5);

            }else if(day.equalsIgnoreCase("Friday")){

                days.add(6);

            }else if(day.equalsIgnoreCase("Saturday")){

                days.add(7);

            }





        }

        int big = 0,small = 8;

        for(int i:days){

            if(i<=dayOfWeek){

                if(small>i){
                    small = i;
                }


            }else{

                if(i > big){
                    big = i;
                    break;
                }

            }


        }

        if(big > dayOfWeek){
            return big - dayOfWeek -1;
        }else {
           return 7 + small - dayOfWeek -1;
        }

    }

}
