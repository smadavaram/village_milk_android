package com.villagemilk.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;

import java.text.SimpleDateFormat;

/**
 * Created by akash.mercer on 24-02-2016.
 */
public class Commons {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

    public static final SimpleDateFormat calendarMonthFormat = new SimpleDateFormat("MMM");

    public static final SimpleDateFormat calendarDayFormat = new SimpleDateFormat("EEE");

    public static final SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd");

    public static final SimpleDateFormat weekDay = new SimpleDateFormat("EEEE");

    public static final SimpleDateFormat dateSdf = new SimpleDateFormat("MMMM dd, yyyy");

    public static final SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat eventDispSdf = new SimpleDateFormat("EEEE,dd MMMM yyyy, HH:mm");

    public static final SimpleDateFormat smallMonthSdf = new SimpleDateFormat("MMM");

    //HomeBean ActionBar Attributes
    public static void showHomeActionBar(AppCompatActivity context) {
        ActionBar actionBar = context.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.icn_hdr_hamburger);
        }
    }

    //General ActionBar Attributes
    public static void showBackActionBarBlue(AppCompatActivity context) {
        ActionBar actionBar = context.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            try {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;

                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    if (item != null) {
                        item.measure(0, 0);
                        totalItemsHeight += item.getMeasuredHeight();
                    }
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + numberOfItems*30;
                listView.setLayoutParams(params);
                listView.requestLayout();
            }catch(Exception e){}

            return true;
        } else {
            return false;
        }
    }

    public static int getCartQuantity(){
        int cartQuantity = 0;

        for (int i = 0; i < BaseApplication.getInstance().getCart().getCartProductList().size(); i++) {
            cartQuantity += BaseApplication.getInstance().getCart().getCartProductList().get(i).getProductQuantity();
        }

        return cartQuantity;
    }

    public static int getTotalQuantity(){
        int totalQuantity = 0;

        for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {
            totalQuantity += BaseApplication.getInstance().getCart().getTotalProductList().get(i).getProductQuantity();
        }

        return totalQuantity;
    }

    public static double getCartAmount(){
        int cartAmount = 0;

        for (int i = 0; i < BaseApplication.getInstance().getCart().getCartProductList().size(); i++) {
            cartAmount += (BaseApplication.getInstance().getCart().getCartProductList().get(i).getProductQuantity() * BaseApplication.getInstance().getCart().getCartProductList().get(i).getProductUnitPrice());
        }

        return cartAmount;
    }

    public static double getTotalAmount(){
        int totalAmount = 0;

        for (int i = 0; i < BaseApplication.getInstance().getCart().getTotalProductList().size(); i++) {
            totalAmount += (BaseApplication.getInstance().getCart().getTotalProductList().get(i).getProductQuantity() * BaseApplication.getInstance().getCart().getTotalProductList().get(i).getProductUnitPrice());
        }

        return totalAmount;
    }

}
