package com.villagemilk.activities.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.User;
import com.villagemilk.model.calendar.CalendarDataObject;
import com.villagemilk.model.calendar.CalendarResponse;
import com.villagemilk.util.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by android on 3/12/16.
 */

public class CalendarPagerAdapter extends PagerAdapter {


    Context mContext;

    Calendar calendar;

    RecyclerView rvCalendar;
    User user;


    public CalendarPagerAdapter(Context mContext) {
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

        ViewGroup layout = (ViewGroup)LayoutInflater.from(mContext).inflate(R.layout.calendar_pager_item,container,false);


        calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR,(position - 15));

        user = BaseApplication.getInstance().getUser();

        rvCalendar = (RecyclerView)layout.findViewById(R.id.rvCalendar);

        container.addView(layout);
        return layout;


//        return super.instantiateItem(container, position);
    }

    private void fetchSubscriptions() {
//        if(isNetworkAvailable()) {
//            showDialog();

        String url = Constants.API_PROD + "/calendar/getCalendarByDateRange";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                CalendarResponse[] calendarResponse = gson.fromJson(response.toString(),CalendarResponse[].class);

                setUpRecyclerView(calendarResponse[0]);

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
//                calendarDataObject.setStartDate("2016-12-01");
//                calendarDataObject.setEndDate("2016-12-04");
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

    private void setUpRecyclerView(CalendarResponse calendarResponse) {

        CalendarItemAdapter calendarItemAdapter = new CalendarItemAdapter(mContext, calendarResponse.getSubscriptions());

//        binding.rvCalendar.setLayoutManager(new LinearLayoutManager(mContext));

        rvCalendar.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar.setLayoutManager(linearLayoutManager);

        rvCalendar.setAdapter(calendarItemAdapter);


    }


}
