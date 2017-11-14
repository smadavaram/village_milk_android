package com.villagemilk.viewmodel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appyvet.rangebar.RangeBar;
import com.google.gson.JsonArray;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.PaymentsActivity;
import com.villagemilk.beans.StatusBean;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.customviews.QuantityKnob;
import com.villagemilk.databinding.ActivitySubscriptionNewBinding;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.model.calendar.Subscription;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.villagemilk.util.Util;
import com.villagemilk.view.ActivityCalendar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by android on 4/1/17.
 */

public class SubscriptionViewModel {

    public ObservableField<String> productName;
    public ObservableField<String> productPrice;
    public ObservableInt productQuantity;
    public ObservableInt frequency;
    public ObservableLong start;
    public ObservableLong end;

    int reqCount = 0;

    public ObservableBoolean isEnabled;
    public boolean isNewSubscription;
    ArrayList<Integer> days;
//    public ObservableInt subscriptionType;

    public ObservableBoolean isSingleDay;

    public ObservableField<String> startDate;
    public ObservableField<String> endDate;
    public ObservableField<String> imageUrl;
    public ObservableField<String> frequencyString;

    public ArrayList<String> updateAddedDays;
    public ArrayList<String> updateRemovedDays;
    public ArrayList<String> updateNewDays;

    public SubscriptionMaster subscriptionMaster;

    SubscriptionWrapper subscriptionWrapper;

    String productMaster;

    ActivitySubscriptionNewBinding binding;

    Context mContext;


    public SubscriptionViewModel(Context context, boolean isNew,String productMaster, SubscriptionMaster subscriptionMaster,ActivitySubscriptionNewBinding binding) {

        this.subscriptionMaster = subscriptionMaster;

        this.binding = binding;

        updateAddedDays = new ArrayList<>();

        updateRemovedDays = new ArrayList<>();

        updateNewDays = new ArrayList<>();

        if(!TextUtils.isEmpty(productMaster)) {
            subscriptionWrapper = BaseApplication.getInstance().containsSubscriptionMaster(productMaster);
            subscriptionMaster = subscriptionWrapper.getSubscriptionMasters().get(0);

            updateAddedDays.addAll(subscriptionWrapper.getDeliveryDays());
        }
        isNewSubscription = isNew;

        this.productMaster = productMaster;

        isEnabled = new ObservableBoolean(true);

        days = new ArrayList<>();

//        if(subscriptionMaster.getSubscriptionType() !=null)
//            subscriptionType = new ObservableInt(subscriptionMaster.getSubscriptionType());
//        else
//            subscriptionType = new ObservableInt(0);

            productName = new ObservableField<>(subscriptionMaster.getProductName());
            productPrice = new ObservableField<>("$ " + subscriptionMaster.getProductUnitCost());

            if (subscriptionMaster.getStartDate() != null)
                startDate = new ObservableField<>(DateOperations.getDateString(subscriptionMaster.getStartDate()));

            if (subscriptionMaster.getEndDate() != null) {
                endDate = new ObservableField<>(DateOperations.getDateString(subscriptionMaster.getEndDate()));
                end = new ObservableLong(subscriptionMaster.getEndDate());
            } else {
                endDate = new ObservableField<>(DateOperations.getDateString(DateOperations.getFutureSubscriptionDate()));
                end = new ObservableLong(DateOperations.getFutureSubscriptionDate().getTime());
            }
            frequencyString = new ObservableField<>("Scheduled: Everyday");
            if (subscriptionMaster.getEndDate() != null)
                productQuantity = new ObservableInt(subscriptionMaster.getProductQuantity());
            else
                productQuantity = new ObservableInt(1);

            if (subscriptionMaster.getSubscriptionType() != null)
                frequency = new ObservableInt(subscriptionMaster.getSubscriptionType());
            else
                frequency = new ObservableInt(7);

            start = new ObservableLong(subscriptionMaster.getStartDate());
//        end = new ObservableLong(subscriptionMaster.getEndDate());
            imageUrl = new ObservableField<>(subscriptionMaster.getProductImage());

        isSingleDay = new ObservableBoolean(false);

             mContext = context;

    }

    public int getSubscriptionType() {

        if(subscriptionWrapper == null)
            return 7;

        if(subscriptionWrapper.getSubscriptionMasters().get(0) == null || subscriptionWrapper.getSubscriptionMasters().get(0).getSubscriptionType()  == 4)
            return 1;

        return (subscriptionWrapper.getSubscriptionMasters().get(0).getSubscriptionType());

    }



    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        ImageLoader.getInstance().displayImage(imageUrl,view);
//        ImageLoader.getInstance().
//        view.setImageBitmap(ImageHelper.getRoundedCornerBitmap(ImageLoader.getInstance().loadImageSync(imageUrl),100));

    }

    @BindingAdapter({"bind:isEnabled"})
    public static void isCheckBoxEnabled(CheckBox checkBox, boolean day) {

        switch (checkBox.getId()){

            case R.id.chkboxMon:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Monday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;
            case R.id.chkboxTue:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Tuesday"))
                checkBox.setEnabled(false);
            else
                checkBox.setEnabled(true);
                break;
            case R.id.chkboxWed:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Wednesday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;
            case R.id.chkboxThu:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Thursday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;
            case R.id.chkboxFri:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Friday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;
            case R.id.chkboxSat:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Saturday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;
            case R.id.chkboxSun:
                if(!BaseApplication.getInstance().getDeliveryDays().contains("Sunday"))
                    checkBox.setEnabled(false);
                else
                    checkBox.setEnabled(true);
                break;


        }



    }

    public CompoundButton.OnCheckedChangeListener checkChanged(){

        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isNewSubscription) {

                    switch (buttonView.getId()) {

                        case R.id.chkboxMon:
                            if (isChecked) {
                                days.add(Calendar.MONDAY);
                                subscriptionMaster.addDeliveryDay("Monday");
                            } else {
                                days.remove((Integer) Calendar.MONDAY);
                                subscriptionMaster.removeDeliveryDay("Monday");
                            }
                            Toast.makeText(buttonView.getContext(), "Monday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxTue:
                            if (isChecked) {
                                days.add(Calendar.TUESDAY);
                                subscriptionMaster.addDeliveryDay("Tuesday");
                            } else {
                                days.remove((Integer) Calendar.TUESDAY);
                                subscriptionMaster.removeDeliveryDay("Tuesday");
                            }
                            Toast.makeText(buttonView.getContext(), "Tuesday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxWed:
                            if (isChecked) {
                                subscriptionMaster.addDeliveryDay("Wednesday");
                                days.add(Calendar.WEDNESDAY);
                            } else {
                                days.remove((Integer) Calendar.WEDNESDAY);
                                subscriptionMaster.removeDeliveryDay("Wednesday");
                            }
                            Toast.makeText(buttonView.getContext(), "Wednesday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxThu:
                            if (isChecked) {
                                subscriptionMaster.addDeliveryDay("Thursday");
                                days.add(Calendar.THURSDAY);
                            } else {
                                days.remove((Integer) Calendar.THURSDAY);
                                subscriptionMaster.removeDeliveryDay("Thursday");

                            }
                            Toast.makeText(buttonView.getContext(), "Thursday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxFri:
                            if (isChecked) {
                                subscriptionMaster.addDeliveryDay("Friday");
                                days.add(Calendar.FRIDAY);
                            } else {
                                days.remove((Integer) Calendar.FRIDAY);
                                subscriptionMaster.removeDeliveryDay("Friday");
                            }
                            Toast.makeText(buttonView.getContext(), "Friday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxSat:
                            if (isChecked) {
                                subscriptionMaster.addDeliveryDay("Saturday");
                                days.add(Calendar.SATURDAY);
                            } else {
                                days.remove((Integer) Calendar.SATURDAY);
                                subscriptionMaster.removeDeliveryDay("Saturday");
                            }
                            Toast.makeText(buttonView.getContext(), "Saturday", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.chkboxSun:
                            if (isChecked) {
                                subscriptionMaster.addDeliveryDay("Sunday");
                                days.add(Calendar.SUNDAY);
                            } else {
                                days.remove((Integer) Calendar.SUNDAY);
                                subscriptionMaster.removeDeliveryDay("Sunday");
                            }
                            Toast.makeText(buttonView.getContext(), "Sunday", Toast.LENGTH_SHORT).show();
                            break;


                    }

                } else {

                    switch (buttonView.getId()) {


                        case R.id.chkboxMon:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Monday")) {
                                    updateRemovedDays.remove("Monday");
                                    updateAddedDays.add("Monday");
                                } else {
                                    updateNewDays.add("Monday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Monday")) {
                                    updateRemovedDays.add("Monday");
                                    updateAddedDays.remove("Monday");
                                } else {
                                    updateNewDays.remove("Monday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Monday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxTue:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Tuesday")) {
                                    updateRemovedDays.remove("Tuesday");
                                    updateAddedDays.add("Tuesday");
                                } else {
                                    updateNewDays.add("Tuesday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Tuesday")) {
                                    updateRemovedDays.add("Tuesday");
                                    updateAddedDays.remove("Tuesday");
                                } else {
                                    updateNewDays.remove("Tuesday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Tuesday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxWed:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Wednesday")) {
                                    updateRemovedDays.remove("Wednesday");
                                    updateAddedDays.add("Wednesday");
                                } else {
                                    updateNewDays.add("Wednesday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Wednesday")) {
                                    updateRemovedDays.add("Wednesday");
                                    updateAddedDays.remove("Wednesday");
                                } else {
                                    updateNewDays.remove("Wednesday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Wednesday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxThu:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Thursday")) {
                                    updateRemovedDays.remove("Thursday");
                                    updateAddedDays.add("Thursday");
                                } else {
                                    updateNewDays.add("Thursday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Thursday")) {
                                    updateRemovedDays.add("Thursday");
                                    updateAddedDays.remove("Thursday");
                                } else {
                                    updateNewDays.remove("Thursday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Thursday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxFri:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Friday")) {
                                    updateRemovedDays.remove("Friday");
                                    updateAddedDays.add("Friday");
                                } else {
                                    updateNewDays.add("Friday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Friday")) {
                                    updateRemovedDays.add("Friday");
                                    updateAddedDays.remove("Friday");
                                } else {
                                    updateNewDays.remove("Friday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Friday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxSat:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Saturday")) {
                                    updateRemovedDays.remove("Saturday");
                                    updateAddedDays.add("Saturday");
                                } else {
                                    updateNewDays.add("Saturday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Saturday")) {
                                    updateRemovedDays.add("Saturday");
                                    updateAddedDays.remove("Saturday");
                                } else {
                                    updateNewDays.remove("Saturday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Saturday", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.chkboxSun:
                            if (isChecked) {
                                if (subscriptionWrapper.getDeliveryDays().contains("Sunday")) {
                                    updateRemovedDays.remove("Sunday");
                                    updateAddedDays.add("Sunday");
                                } else {
                                    updateNewDays.add("Sunday");
                                }

                            } else {
                                if (subscriptionWrapper.getDeliveryDays().contains("Sunday")) {
                                    updateRemovedDays.add("Sunday");
                                    updateAddedDays.remove("Sunday");
                                } else {
                                    updateNewDays.remove("Sunday");
                                }
                            }
                            Toast.makeText(buttonView.getContext(), "Sunday", Toast.LENGTH_SHORT).show();
                            break;


                    }
                }

            }

            };


    }

    public RangeBar.OnRangeBarChangeListener getOnRangeBarChangeListener(){

        return new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                if(rightPinValue.equals("0")){

                    frequency.set(4);

                    frequencyString.set("Scheduled: One Time Delivery");

                    isSingleDay.set(true);

                 /*   if(isSingleDay.get()) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DateOperations.getStringToDate(startDate.get()));
                        calendar.add(Calendar.WEEK_OF_MONTH,1);
                        end.set(DateOperations.getEndOfDate(calendar.getTime()).getTime());
                        endDate.set(DateOperations.getDateString(end.get()));
                    }*/

                    return;
                }

                isSingleDay.set(false);
                frequency.set(Integer.parseInt(rightPinValue));

                frequencyString.set("Scheduled: Every " + rightPinValue + " days");

                end.set(DateOperations.getFutureSubscriptionDate().getTime());
                endDate.set(DateOperations.getDateString(end.get()));

            }

        };

    }

    public View.OnClickListener getSaveSubscriptions(){

        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(productQuantity.get() == 0){

                    Toast.makeText(mContext,"Add product quantity",Toast.LENGTH_SHORT).show();
                    return;

                }
                if(DateOperations.getStringToDate(endDate.get()).before(DateOperations.getStringToDate(startDate.get()))){
                    Toast.makeText(mContext,"End date cannot be before start date.",Toast.LENGTH_SHORT).show();
                    return;
                }



                if(isNewSubscription) {

                    if (days.isEmpty()) {

                        Toast.makeText(mContext, "Select delivery days", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE, null, null)
                            .setTitleText("Confirm Subscription?")
                            .setContentText("Are you sure you want to subscribe ?")
                            .setConfirmText("Yes, lets do it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {


                                    saveSubscription();
/*//                        addSubscription();
                                for(Integer integer:days){

                                    saveSubscription(integer);

                                }*/


                                }
                            })
                            .show();

                }

                else {

                        /*
    * 	{
		"status": "delete",
		"deliveryDay": "Friday",
		"subscriptionId": "",
		"productMasterId": "",
		"productUnitCost": 2.5,
		"productUnit": "",
		"userId": "",
		"frequency": 14,
		"quantity": 2

	}
    * */


                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE, null, null)
                            .setTitleText("Update Subscription?")
                            .setContentText("Are you sure you want to update subscription ?")
                            .setConfirmText("Yes, lets do it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    updateSubscription();

                                }
                            })
                            .show();
                }
            }
        };

    }


    public QuantityKnob.OnQuantityUpdated getOnQuantityUpdatedListener(){

        return new QuantityKnob.OnQuantityUpdated() {
            @Override
            public void onQuantityAdded(int quantity) {

                productQuantity.set(quantity);

            }

            @Override
            public void onQuantitySubtracted(int quantity) {

                productQuantity.set(quantity);
            }
        };

    }


    public View.OnClickListener getStartDateCalendar(){

//        if(!isNewSubscription)
//            return null;

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(DateOperations.getStringToDate(startDate.get()));

                DatePickerDialog datePicker = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            // when dialog box is closed, below method will be called.
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(selectedYear,selectedMonth,selectedDay);

                                start.set(DateOperations.getStartOfDate(calendar.getTime()).getTime());
                                startDate.set(DateOperations.getDateString(calendar.getTime()));

                                if(isSingleDay.get()){
                                    end.set(DateOperations.getStartOfDate(calendar.getTime()).getTime());
                                    endDate.set(DateOperations.getDateString(calendar.getTime()));
                                }



                            }
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker.setCancelable(false);

                datePicker.getDatePicker().setMinDate(start.get());

                datePicker.show();

            }
        };

    }

    public View.OnClickListener getEndDateCalendar(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSingleDay.get())
                    return;

                Calendar cal = Calendar.getInstance();
                cal.setTime(DateOperations.getStringToDate(endDate.get()));

                DatePickerDialog datePicker = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            // when dialog box is closed, below method will be called.
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(selectedYear,selectedMonth,selectedDay);
                                endDate.set(DateOperations.getDateString(calendar.getTime()));

                                end.set(DateOperations.getEndOfDate(calendar.getTime()).getTime());

                            }
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker.setCancelable(false);

                datePicker.getDatePicker().setMinDate(start.get());

                datePicker.show();


            }
        };

    }

    public CompoundButton.OnCheckedChangeListener getOnCheckChanged(){

        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                isSingleDay.set(b);

                if(isSingleDay.get()) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateOperations.getStringToDate(startDate.get()));
                    calendar.add(Calendar.WEEK_OF_MONTH,1);
                    end.set(DateOperations.getEndOfDate(calendar.getTime()).getTime());
                    endDate.set(DateOperations.getDateString(end.get()));
                }else {
                    end.set(DateOperations.getFutureSubscriptionDate().getTime());
                    endDate.set(DateOperations.getDateString(end.get()));

                }

            }
        };



    }

    /*
    subscriptionMaster/deleteNew
    Data Description :
    String subcription id
    Data :
    ["subcription id 1", "subcription id 2", "subcription id 3"]


*/

    private void endSubscription() {

        if(Util.isNetworkAvailable(mContext)) {

            Util.showDialog(mContext);

            final String url = Constants.API_PROD + "/subscriptionMaster/deleteNew";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("End Sub response",response);


                    if (response.equals("true")){

                        BaseApplication.getInstance().removeSubscription(productMaster);

                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE, null, null)
                                .setTitleText("Success")
                                .setContentText("Subscription removed")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();

                                        Intent homeActivityIntent = new Intent(mContext, ActivityCalendar.class);
                                        mContext.startActivity(homeActivityIntent);

                                    }
                                }).show();




                    } else {

                        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE, null, null)
                                .setTitleText("Error!")
                                .setContentText("Something went wrong")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(mContext,PaymentsActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                }).show();

                    }




            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {

//                    Log.e("end subscription",new Gson().toJson(subscriptionMaster.getSubscriptionIdList(), ArrayList.class));

                    return new Gson().toJson(subscriptionWrapper.getSubscriptionIds(), ArrayList.class).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(mContext, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }


    private void saveSubscription() {


        if(Util.isNetworkAvailable(mContext)) {

            if(reqCount==0)
                Util.showDialog(mContext);

            reqCount++;
            final String url = Constants.API_PROD + "/subscriptionMaster/createNew";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Util.hideDialog();

                    Log.e("response aaya for ",response);


                    if (response.equals("true")){

                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE, null, null)
                                .setTitleText("Success")
                                .setContentText("Subscription added")
                                .setConfirmText("Ok,cool !")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();

                                        Intent homeActivityIntent = new Intent(mContext, ActivityCalendar.class);
                                        mContext.startActivity(homeActivityIntent);

                                    }
                                }).show();




                    } else {

                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE, null, null)
                                    .setTitleText("Error!")
                                    .setContentText("Subscription Failed")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent intent = new Intent(mContext,PaymentsActivity.class);
                                            mContext.startActivity(intent);
                                        }
                                    }).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                    reqCount--;
                }
            }
            ) {
                @Override
                public byte[] getBody() {

                    /*
                    * {
	"deliveryDays": ["Friday", "Sunday", "Monday"],
	"productImage": "http://i.imgur.com/cB0IlaO.jpg",
	"productMaster": {
		"id": "1f45a603-0058-4637-b466-e4458935024d",
		"productQuantity": 0,
		"transientProductQuantity": 0
	},
	"productName": "SWAD URAD DAL",
	"productQuantity": 1,
	"productUnit": "2 lb",
	"productUnitCost": 3.45,
	"transientSubscriptionType": 7,
	"user": {
		"id": "27452ef0-a2c4-4774-a820-2206a06f009c"
	}
}*/

                    subscriptionMaster.setRecentlyChangedFlag(true);
                    subscriptionMaster.setActive(true);
                    subscriptionMaster.setVerifyStatus(1);
                    subscriptionMaster.setStartDate(start.get());
                    subscriptionMaster.setEndDate(end.get());
                    subscriptionMaster.setProductQuantity(productQuantity.get());
//                    if(isSingleDay.get())
//                        subscriptionMaster.setTransientSubscriptionType(4);
//                    else
                    subscriptionMaster.setTransientSubscriptionType(frequency.get());
//                    subscriptionMaster.setSubscriptionType(null);

                    Log.d("response-createnew",new Gson().toJson(subscriptionMaster, SubscriptionMaster.class));

                    return new Gson().toJson(subscriptionMaster, SubscriptionMaster.class).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(mContext, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

/*
    subscriptionMaster/deleteNew
    Data Description :
    String subcription id
    Data :
    ["subcription id 1", "subcription id 2", "subcription id 3"]


*/
    public View.OnClickListener getEndSubscription(){

        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE, null, null)
                        .setTitleText("End Subscription?")
                        .setContentText("Are you sure you want to end subscription ?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog){

                                endSubscription();

                            }
                        })
                        .show();

            }

        };


    }

    /*
    * 	{
		"status": "delete",
		"deliveryDay": "Friday",
		"subscriptionId": "",
		"productMasterId": "",
		"productUnitCost": 2.5,
		"productUnit": "",
		"userId": "",
		"frequency": 14,
		"quantity": 2

	}
    * */

    private void updateSubscription(){

        if(Util.isNetworkAvailable(mContext)) {

            Util.showDialog(mContext);

            final String url = Constants.API_PROD + "/subscriptionMaster/editNew";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("End Sub response",response);


                    if (response.equals("true")){


//                        saveSubscription();

                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE, null, null)
                                .setTitleText("Success")
                                .setContentText("Subscription Updated Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(mContext,ActivityCalendar.class);
                                        mContext.startActivity(intent);
                                    }
                                }).show();


                    } else {

                        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE, null, null)
                                .setTitleText("Error!")
                                .setContentText("Something went wrong")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(mContext,PaymentsActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                }).show();

                    }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.hideDialog();
                }
            }
            ) {
                @Override
                public byte[] getBody() {

//                    Log.e("end subscription",new Gson().toJson(subscriptionMaster.getSubscriptionIdList(), ArrayList.class));

                        /*
    * 	{
		"status": "delete",
		"deliveryDay": "Friday",
		"subscriptionId": "",
		"productMasterId": "",
		"productUnitCost": 2.5,
		"productUnit": "",
		"userId": "",
		"frequency": 14,
		"quantity": 2

	}
    * */

                    JSONArray jsonArray = new JSONArray();

                    for(String day: updateRemovedDays){

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("status","delete");
                            jsonObject.put("deliveryDay",day);
                            jsonObject.put("subscriptionId",getExistingSubscriptionId(day));
                            jsonObject.put("productMasterId",productMaster);
                            jsonObject.put("productUnit",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnit());
                            jsonObject.put("productUnitCost",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnitCost());
                            jsonObject.put("userId",Util.getUserId(mContext));
                            jsonObject.put("frequency",frequency.get());
                            jsonObject.put("quantity",productQuantity.get());
                            jsonObject.put("startDate",start.get());

                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                    for(String day: updateNewDays){

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("status","add");
                            jsonObject.put("deliveryDay",day);
                            jsonObject.put("subscriptionId","");
                            jsonObject.put("productMasterId",productMaster);
                            jsonObject.put("productUnit",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnit());
                            jsonObject.put("productUnitCost",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnitCost());
                            jsonObject.put("userId",Util.getUserId(mContext));
                            jsonObject.put("frequency",frequency.get());
                            jsonObject.put("quantity",productQuantity.get());
                            jsonObject.put("startDate",start.get());

                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    for(String day: updateAddedDays){

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("status","edit");
                            jsonObject.put("deliveryDay",day);
                            jsonObject.put("subscriptionId",getExistingSubscriptionId(day));
                            jsonObject.put("productMasterId",productMaster);
                            jsonObject.put("productUnit",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnit());
                            jsonObject.put("productUnitCost",subscriptionWrapper.getSubscriptionMasters().get(0).getProductUnitCost());
                            jsonObject.put("userId",Util.getUserId(mContext));
                            jsonObject.put("frequency",frequency.get());
                            jsonObject.put("quantity",productQuantity.get());
                            jsonObject.put("startDate",start.get());

                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    Log.e("body", jsonArray.toString());




                    return jsonArray.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(mContext, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }


    public String getExistingSubscriptionId(String day){

        for(SubscriptionMaster subscriptionMaster: subscriptionWrapper.getSubscriptionMasters()){

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(subscriptionMaster.getStartDate());

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

            if(day.equals(dayFormat.format(calendar.getTime()))){

                return subscriptionMaster.getId();

            }

        }

        return "";

    }


//    public void uncheckAll(){
//
//
//        binding.chkboxFri.setOnCheckedChangeListener(null);
//        binding.chkboxMon.setOnCheckedChangeListener(null);
//        binding.chkboxTue.setOnCheckedChangeListener(null);
//        binding.chkboxWed.setOnCheckedChangeListener(null);
//        binding.chkboxThu.setOnCheckedChangeListener(null);
//        binding.chkboxSat.setOnCheckedChangeListener(null);
//        binding.chkboxSun.setOnCheckedChangeListener(null);
//
//        binding.chkboxFri.setChecked(false);
//        binding.chkboxMon.setChecked(false);
//        binding.chkboxTue.setChecked(false);
//        binding.chkboxWed.setChecked(false);
//        binding.chkboxThu.setChecked(false);
//        binding.chkboxSat.setChecked(false);
//        binding.chkboxSun.setChecked(false);
//
//        binding.chkboxFri.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxMon.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxTue.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxWed.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxThu.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxSat.setOnCheckedChangeListener(getOnCheckChanged());
//        binding.chkboxSun.setOnCheckedChangeListener(getOnCheckChanged());
//
//
//    }

}
