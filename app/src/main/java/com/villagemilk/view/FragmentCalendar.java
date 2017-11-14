package com.villagemilk.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.adapters.CalendarItemAdapter;
import com.villagemilk.beans.User;
import com.villagemilk.customviews.CartBottomView;
import com.villagemilk.databinding.CalendarPagerItemBinding;
import com.villagemilk.model.CalendarReportModel;
import com.villagemilk.model.calendar.CalendarDataObject;
import com.villagemilk.model.calendar.CalendarResponse;
import com.villagemilk.model.calendar.Subscription;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;
import com.villagemilk.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by android on 3/12/16.
 */

public class FragmentCalendar extends Fragment{

    Context mContext;

    RecyclerView rvCalendar;

    public Calendar getCalendar() {
        return calendar;
    }

    Calendar calendar;

    User user;

    CalendarPagerItemBinding binding;

    int position;

    public int getPageState() {
        return pageState;
    }

    public void setPageState(int pageState) {
        this.pageState = pageState;
    }

    private int pageState = 0;

    TextView tvCalendarDate;

    LinearLayout llCalendarItem,llEmptyScreen;

    ProgressBar progressBar;

    private UpdateCart updateCart;

    View parentView;

    CalendarResponse calendarResponse;

    public static FragmentCalendar getInstance(){

        return new FragmentCalendar();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (parentView == null) {

            binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.calendar_pager_item,container,false);

            parentView = binding.getRoot();

            position = getArguments().getInt(getResources().getString(R.string.position));

            rvCalendar = (RecyclerView) parentView.findViewById(R.id.rvCalendar);

            tvCalendarDate = (TextView) parentView.findViewById(R.id.tvCalendarDate);

            llCalendarItem = (LinearLayout) parentView.findViewById(R.id.llCalendarItem);

            llEmptyScreen = (LinearLayout) parentView.findViewById(R.id.llEmptyScreen);

            progressBar = (ProgressBar) parentView.findViewById(R.id.pbCalendar);

            calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_YEAR, (position - 60));

            user = BaseApplication.getInstance().getUser();


            if(position == 61) {
                setPageState(CartBottomView.ADD_MORE_PRODUCTS);
            }



            fetchSubscriptions();


        }
        return parentView;

    }

    void setUpRecyclerView(){


        CalendarItemAdapter calendarItemAdapter = new CalendarItemAdapter(mContext, calendarResponse);

        rvCalendar.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar.setLayoutManager(linearLayoutManager);

        rvCalendar.setAdapter(calendarItemAdapter);

        if(position == 60) {
            calendarItemAdapter.showFeedback(true);

        }
        if(position<=60) {
            calendarItemAdapter.quantityModifiable(false);
        }






    }

    public void fetchReport() {

        if (Util.isNetworkAvailable(mContext)) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(calendar.getTime());

            String url = Constants.API_PROD + "/billingMaster/reportsByUserAndDate/" + user.getId() + "/" + formattedDate;

            final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressBar.setVisibility(View.GONE);

                    llCalendarItem.setVisibility(View.VISIBLE);

                    binding.llReport.setVisibility(View.VISIBLE);

                    CalendarReportModel calendarReportModel = new Gson().fromJson(response, CalendarReportModel.class);

                    binding.tvTotal.setText(String.valueOf(calendarReportModel.getTotal()));


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    /*alert("Connection Error");

                    hideDialog();*/
                }
            }) {
                @Override
                public byte[] getBody() {

                    CalendarDataObject calendarDataObject = new CalendarDataObject();
                    calendarDataObject.setUserId(user.getId());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(calendar.getTime());
                    calendarDataObject.setStartDate(formattedDate);
                    calendarDataObject.setEndDate(formattedDate);

                    return new Gson().toJson(calendarDataObject, CalendarDataObject.class).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
//        } else {
//            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
//        }
        }

    }


    public void fetchSubscriptions() {
//        if(isNetworkAvailable()) {
//            showDialog();

        String url = Constants.API_PROD + "/calendar/getCalendarByDateRange";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                CalendarResponse[] calendarResponse = gson.fromJson(response.toString(),CalendarResponse[].class);

                FragmentCalendar.this.calendarResponse = calendarResponse[0];

                try {

                    String date = calendarResponse[0].getDate();
                    int year, month, day;

                    year = (Integer.parseInt(date.split("-")[0]));
                    month = (Integer.parseInt(date.split("-")[1]));
                    day = (Integer.parseInt(date.split("-")[2]));

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month - 1, day);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

                    simpleDateFormat.format(calendar.getTime());


                    tvCalendarDate.setText(simpleDateFormat.format(calendar.getTime()));

                    double totalAmount = 0;

                    for(Subscription subscription:FragmentCalendar.this.calendarResponse.getSubscriptions()){

                        totalAmount += subscription.getProductUnitCost()*subscription.getProductQuantity();

                    }

                    binding.tvSubTotal.setText("$" + totalAmount);


                }catch (Exception e){

                    e.printStackTrace();

                }
                if (calendarResponse[0].getSubscriptions().isEmpty()) {
                    llEmptyScreen.setVisibility(View.VISIBLE);
                    rvCalendar.setAdapter(null);
                    progressBar.setVisibility(View.GONE);
                    llCalendarItem.setVisibility(View.VISIBLE);

                }
                else {
                    llEmptyScreen.setVisibility(View.GONE);
                    setUpRecyclerView();

                    fetchReport();
                }
//                tvTotalItems.setText(calendarResponse[0].getSubscriptions().size() + " items");

                if(position == ActivityCalendar.currentPagePosition)
                    updateCart();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                    /*alert("Connection Error");

                    hideDialog();*/
            }
        }) {
            @Override
            public byte[] getBody() {

                CalendarDataObject calendarDataObject = new CalendarDataObject();
                calendarDataObject.setUserId(user.getId());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(calendar.getTime());
                calendarDataObject.setStartDate(formattedDate);
                calendarDataObject.setEndDate(formattedDate);

                return new Gson().toJson(calendarDataObject, CalendarDataObject.class).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getInstance().addToRequestQueue(stringRequest);
//        } else {
//            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
//        }
    }

    public interface UpdateCart{

        void onUpdate(CalendarResponse calendarResponse);
    }

    public void updateCart(){

        if(calendarResponse == null)
            return;

        updateCart = (UpdateCart)mContext;
        updateCart.onUpdate(calendarResponse);


    }

}
