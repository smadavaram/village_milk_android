package com.villagemilk.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.ProductMaster;
import com.villagemilk.beans.StatusBean;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.beans.SubscriptionMasterSmall;
import com.villagemilk.beans.User;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.villagemilk.view.ActivityCalendar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SubscriptionActivity extends BaseActivity {
    private static final String TAG = "Subscription Activity";

    private NumberPicker aNumberPicker;
    private Button pick;
    private final Context context = this;
    private AlertDialog.Builder alertBw;
    private AlertDialog alertDw;
    private Calendar calendar;
    private Button startDateView;
    private int year, month, day;

    private TextView productName;
    private TextView productPrice;
    private TextView productUnit;
    private ImageView productImage;
    private RadioGroup subscriptionTypeRadioGroup;

    private String current_product_id = "";

    public boolean isUpdateRequest = false;
    public boolean isFutureUpdateRequest = false;

    private int productType;
    private Button endDateView;
    private Boolean radioGroupToBeVisible = false;
    private Boolean isFirstLoop = true;
    private TextView tvEveryday;
    private TextView tvAltDay;
    private TextView tvalt2day;
    private TextView tvOnlyTomorrow;
    private TextView tvQuantity;
    private RadioButton radioTomorrow;
    private RadioButton radioEveryday;
    private RadioButton radioAlternateDay;
    private RadioButton radioEvery2Day;
    private Button oneDayDate;
    private View btnMinus;
    private View btnPlus;
    private int quantity = 1;

    private SweetAlertDialog sDialog;

    private static final String QUANT_REF = "QNT_REF";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String ONE_DATE = "ONE_DATE";

    private LinearLayout lStartDateLayout;

    int sub_type_init_value;//Initial values of Qt and Sub type to check if changed
    int quantity_init_value;
    private TextView tvStartDate;
    private TextView tvEndDate;

    private TextView abEnd;

    private Date endDate;
    private Date startDate;
    private Date oneDayDateObj;

    private SubscriptionMasterSmall subscriptionMasterSmall;
    private ProductMaster productMaster;

    private boolean endSubscription;

    private SharedPreferences sharedPreferences;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        //Initialize dynamic view components
        initSimpleToolbar();

        abEnd = (TextView) findViewById(R.id.abEnd);
        abEnd.setTypeface(robotoBold);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        initializeViews();

//        if(getIntent().getExtras() != null && getIntent().getExtras().getString("intent_source").equalsIgnoreCase("UpdateSubscription")) {
//            TextView tv = (TextView) findViewById(R.id.abTitle);
//            tv.setText("Update Subscription");
//            abEnd.setVisibility(View.VISIBLE);
//        } else {
//            abEnd.setVisibility(View.GONE);
//        }

        findViewById(R.id.ivInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(SubscriptionActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                        .setTitleText("Subscription Info")
                        .setContentText(getString(R.string.subscription_info))
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        abEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(SubscriptionActivity.this, SweetAlertDialog.WARNING_TYPE, robotoBold, robotoLight)
                        .setTitleText("End Subscription?")
                        .setContentText("Are you sure you want to end the subscription ?")
                        .setConfirmText("Yes :(")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog2) {
                                sDialog = sDialog2;
                                endSubscription();
                            }
                        }).show();
            }
        });

        if(savedInstanceState != null) {
            quantity = savedInstanceState.getInt(QUANT_REF,1);
            startDate = (Date)savedInstanceState.getSerializable(START_DATE);
            endDate = (Date)savedInstanceState.getSerializable(END_DATE);
            oneDayDateObj = (Date)savedInstanceState.getSerializable(ONE_DATE);
        }

        productType = getIntent().getIntExtra("product_type", 0);
        Log.d(TAG, "prodType" + productType);

        showStartDate(year, month + 1, day);
        showEndDate(year, month + 1, day);
        showOneDayDate(year, month + 1, day);

        setAlert();
    }

    private void initializeViews(){
        productName =(TextView) findViewById(R.id.tv_productName);
        productName.setTypeface(robotoBold);
        productPrice =(TextView) findViewById(R.id.tv_productPrice);
        productPrice.setTypeface(robotoBold);
        productUnit =(TextView) findViewById(R.id.tv_productUnit);
        productUnit.setTypeface(robotoLight);
        productImage =(ImageView) findViewById(R.id.im_productImage);
        productImage.setImageDrawable(context.getResources().getDrawable(R.drawable.product_default));
        subscriptionTypeRadioGroup = (RadioGroup)findViewById(R.id.rg_subscriptionType);
        tvEveryday = (TextView)findViewById(R.id.tvEveryday);
        tvEveryday.setTypeface(robotoLight);
        tvAltDay = (TextView)findViewById(R.id.tvAltDay);
        tvAltDay.setTypeface(robotoLight);
        tvalt2day = (TextView)findViewById(R.id.tvalt2day);
        tvalt2day.setTypeface(robotoLight);
        tvOnlyTomorrow = (TextView)findViewById(R.id.tvOnlyTomorrow);
        tvOnlyTomorrow.setTypeface(robotoLight);
        tvStartDate=(TextView)findViewById(R.id.start_date_textview);
        tvEndDate=(TextView)findViewById(R.id.end_date_text_view);
        oneDayDate = (Button)findViewById(R.id.oneDayDate);
        oneDayDate.setVisibility(View.GONE);
        oneDayDate.setTypeface(robotoBold);
        oneDayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(500);
            }
        });

        radioTomorrow = (RadioButton)findViewById(R.id.radioTomorrow);
        radioTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartDate.setVisibility(View.GONE);
                tvEndDate.setVisibility(View.GONE);
                startDateView.setVisibility(View.GONE);
                endDateView.setVisibility(View.GONE);
                oneDayDate.setVisibility(View.VISIBLE);
            }
        });

        radioEveryday = (RadioButton)findViewById(R.id.radioEveryDay);
        radioEveryday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartDate.setVisibility(View.VISIBLE);
                tvEndDate.setVisibility(View.VISIBLE);
                startDateView.setVisibility(View.VISIBLE);
                endDateView.setVisibility(View.VISIBLE);
                oneDayDate.setVisibility(View.GONE);
            }
        });

        radioAlternateDay = (RadioButton)findViewById(R.id.radioAlternateDay);
        radioAlternateDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartDate.setVisibility(View.VISIBLE);
                tvEndDate.setVisibility(View.VISIBLE);
                startDateView.setVisibility(View.VISIBLE);
                endDateView.setVisibility(View.VISIBLE);
                oneDayDate.setVisibility(View.GONE);
            }
        });

        radioEvery2Day = (RadioButton)findViewById(R.id.radioEvery2Day);
        radioEvery2Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartDate.setVisibility(View.VISIBLE);
                tvEndDate.setVisibility(View.VISIBLE);
                startDateView.setVisibility(View.VISIBLE);
                endDateView.setVisibility(View.VISIBLE);
                oneDayDate.setVisibility(View.GONE);
            }
        });

        pick = (Button) findViewById(R.id.quantity_button);

        startDateView = (Button) findViewById(R.id.start_date_button);
        startDateView.setTypeface(robotoBold);
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);//for setting start date
            }
        });

        endDateView = (Button)findViewById(R.id.end_date_button);
        endDateView.setTypeface(robotoBold);
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(100);//for setting end date
            }
        });

        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.DATE,1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        day = calendar.get(Calendar.DAY_OF_MONTH);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        tvQuantity.setTypeface(robotoBold);
        btnMinus = findViewById(R.id.btnMinus);
        lStartDateLayout = (LinearLayout)findViewById(R.id.start_date_layout2);

        btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                tvQuantity.setText(quantity + "");
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    tvQuantity.setText(quantity + "");
                } else {
                    alert("You cannot have less than 1 items");
                }
            }
        });

        Button submit = (Button) findViewById(R.id.button_add_subscription);
        submit.setTypeface(robotoBold);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DateOperations.getStringToDate(endDateView.getText().toString()).before(DateOperations.getStringToDate(startDateView.getText().toString()))){
                    Toast.makeText(SubscriptionActivity.this,"End date cannot be before start date.",Toast.LENGTH_SHORT).show();
                    return;
                } else if(!isUpdateRequest && DateOperations.getStringToDate(startDateView.getText().toString()).before(DateOperations.getTodayEndDate())){
                    Toast.makeText(SubscriptionActivity.this,"Start date cannot be before tomorrow's date.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (oneDayDate.getVisibility() != View.GONE && DateOperations.getStringToDate(oneDayDate.getText().toString()).before(DateOperations.getTodayEndDate())) {
                    alert("Only future dates can be selected.");
                    return;
                }

                new SweetAlertDialog(SubscriptionActivity.this, SweetAlertDialog.WARNING_TYPE, robotoBold, robotoLight)
                        .setTitleText("Confirm Subscription?")
                        .setContentText("Are you sure you want to subscribe ?")
                        .setConfirmText("Yes, lets do it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog2)
                            {
                                sDialog = sDialog2;
                                addSubscription();
                            }
                        })
                        .show();

            }
        });

        if(getIntent().getExtras().getString("intent_source") != null) {
            if(getIntent().getExtras().getString("intent_source").equalsIgnoreCase("CalendarActivity")) {//Hardcoded
                radioGroupToBeVisible = true;
                radioEveryday.setChecked(false);
                radioAlternateDay.setChecked(false);
                radioEvery2Day.setChecked(false);
                radioTomorrow.setChecked(true);
                oneDayDate.setVisibility(View.VISIBLE);
                tvStartDate.setVisibility(View.GONE);
                tvEndDate.setVisibility(View.GONE);
                startDateView.setVisibility(View.GONE);
                endDateView.setVisibility(View.GONE);
                Log.d(TAG, "Calendar date : " + getIntent().getExtras().getString("source_calendar_date"));

                String productMasterGson = getIntent().getExtras().getString("product_master");

                if(productMasterGson != null){
                    productMaster = new Gson().fromJson(productMasterGson, ProductMaster.class);
                    current_product_id = productMaster.getId();
                    if(productMaster.getProductImage() != null){
                        imageLoader.displayImage(productMaster.getProductImage(), productImage);
                    }
                    productName.setText(productMaster.getProductName());
                    productPrice.setText("₹" + String.valueOf(productMaster.getProductUnitPrice()));
                    productUnit.setText(productMaster.getProductUnitSize());

                }

                String endTime = getIntent().getExtras().getString("source_calendar_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                try {
                    Date dateEnd = dateFormat.parse(endTime);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
                    String endDateString = sdf2.format(dateEnd);
                    startDateView.setText(endDateString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(getIntent().getExtras().getString("intent_source").equalsIgnoreCase("HomeActivity") || getIntent().getExtras().getString("intent_source").equalsIgnoreCase("ProductSearchActivity")) {
                radioGroupToBeVisible = true;

                String productMasterGson = getIntent().getExtras().getString("product_master");

                if(productMasterGson != null){
                    productMaster = new Gson().fromJson(productMasterGson, ProductMaster.class);
                    current_product_id = productMaster.getId();
                    if(productMaster.getProductImage() != null){
                        imageLoader.displayImage(productMaster.getProductImage(), productImage);
                    };
                    productName.setText(productMaster.getProductName());
                    productPrice.setText("₹" + String.valueOf(productMaster.getProductUnitPrice()));
                    productUnit.setText(productMaster.getProductUnitSize());

                }
            } else if(getIntent().getExtras().getString("intent_source").equalsIgnoreCase("UpdateSubscription")){
                String subscriptionMasterGson = getIntent().getExtras().getString("subscription_master");

                if (subscriptionMasterGson != null){
                    isUpdateRequest = true;
                    subscriptionMasterSmall = new Gson().fromJson(subscriptionMasterGson, SubscriptionMasterSmall.class);

                    productMaster = new ProductMaster(subscriptionMasterSmall);

                    current_product_id = productMaster.getId();

                    if(subscriptionMasterSmall.getProductQuantity() != 0) {
                        tvQuantity.setText("" + subscriptionMasterSmall.getProductQuantity());
                        quantity = subscriptionMasterSmall.getProductQuantity();
                    }

                    if(productMaster.getProductImage() != null){
                        imageLoader.displayImage(productMaster.getProductImage(), productImage);
                    }
                    productName.setText(subscriptionMasterSmall.getProductName());
                    productPrice.setText("₹" + String.valueOf(subscriptionMasterSmall.getProductUnitCost()));
                    productUnit.setText(subscriptionMasterSmall.getProductUnitSize());

                    //Called for update work
                    radioGroupToBeVisible = true;
                    Log.d(TAG, "Subscription start date : " + DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate()).toString());
                    if(new Date().after(DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate()))){
                        lStartDateLayout.setVisibility(View.GONE);
                        isFutureUpdateRequest = false;
                    } else{
                        isFutureUpdateRequest = true;
                    }

                    initializeUpdateViews();
                    lStartDateLayout.setVisibility(View.GONE);
                    isFutureUpdateRequest = false;
                } else {
                    isFutureUpdateRequest = true;
                }
            }

            if (!radioGroupToBeVisible) {//Make radio group and text invisible if source is calendar 'Add Event' click
                subscriptionTypeRadioGroup.setVisibility(View.GONE);
                tvEveryday.setVisibility(View.GONE);
                tvAltDay.setVisibility(View.GONE);
                tvalt2day.setVisibility(View.GONE);
            }
        }
    }

    private void initializeUpdateViews() {
        startDateView.setText(DateOperations.getDateString(DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate())));
        endDateView.setText(DateOperations.getDateString(DateOperations.getDateFromLong(subscriptionMasterSmall.getEndDate())));
        int subscriptionType = subscriptionMasterSmall.getSubscriptionType();
        if(subscriptionType == 1){
            radioEveryday.setChecked(true);
            radioAlternateDay.setChecked(false);
            radioEvery2Day.setChecked(false);
            radioTomorrow.setChecked(false);
        } else if(subscriptionType == 2){
            radioEveryday.setChecked(false);
            radioAlternateDay.setChecked(true);
            radioEvery2Day.setChecked(false);
            radioTomorrow.setChecked(false);
        } else if(subscriptionType == 3){
            radioEveryday.setChecked(false);
            radioAlternateDay.setChecked(false);
            radioEvery2Day.setChecked(true);
            radioTomorrow.setChecked(false);
        } else if(subscriptionType == 4){
            radioEveryday.setChecked(false);
            radioAlternateDay.setChecked(false);
            radioEvery2Day.setChecked(false);
            radioTomorrow.setChecked(true);
            oneDayDate.setVisibility(View.VISIBLE);
            oneDayDate.setText(DateOperations.getDateString(DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate())));
            tvStartDate.setVisibility(View.GONE);
            tvEndDate.setVisibility(View.GONE);
            startDateView.setVisibility(View.GONE);
            endDateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(QUANT_REF, quantity);
        savedInstanceState.putSerializable(START_DATE, startDate);
        savedInstanceState.putSerializable(END_DATE,endDate);
        savedInstanceState.putSerializable(ONE_DATE,oneDayDateObj);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    public void setStartDate(View view) {
        showDialog(999);//for setting start date
    }
    @SuppressWarnings("deprecation")
    public void setEndDate(View view) {
        showDialog(100);//for setting end date
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999){//Start date
            return new DatePickerDialog(this, myStartDateListener, year, month, day);
        }
        if(id == 100){//End date
            return new DatePickerDialog(this, myEndDateListener, year, month, day);
        }
        if(id == 500)//One day date
            return new DatePickerDialog(this, oneDayDateListener, year, month, day);

        return null;
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showStartDate(arg1, arg2 + 1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener myEndDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showEndDate(arg1, arg2 + 1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener oneDayDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

//            showOneDayDate(arg1, arg2 + 1, arg3);
            oneDayDate.setText(new StringBuilder().append(arg3).append("/").append(arg2+1).append("/").append(arg1));
        }
    };

    private void showStartDate(int year, int month, int day) {
        if(radioGroupToBeVisible) {//radio = true, visible, called from default add page
            startDateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        } else{//radio = false, invisible, called from calendar page
            String startTime = getIntent().getExtras().getString("source_calendar_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//yyyy-MM-dd HH:mm:ss
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                Date dateStart = dateFormat.parse(startTime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
                String startDateString = sdf2.format(dateStart);
                startDateView.setText(startDateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void showEndDate(int year, int month, int day) {
        if(radioGroupToBeVisible && isFirstLoop) {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("IST"));
            c.set(Calendar.YEAR,2035);
            c.set(Calendar.DAY_OF_YEAR,1);
            endDate = c.getTime();
//            c.set(Calendar.YEAR,2035);
            endDateView.setText("01/01/2035");//Hardcoded
            isFirstLoop = false;
        }
        else if(radioGroupToBeVisible) {//radio = true, visible
            endDateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
            isFirstLoop = false;
        }else{//radio = false, invisible
            String endTime = getIntent().getExtras().getString("source_calendar_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                Date dateEnd = dateFormat.parse(endTime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
                String endDateString = sdf2.format(dateEnd);
                endDateView.setText(endDateString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        radioGroupToBeVisible=true;//For next iteration, make it true so that date changes are visible
    }

    private void showOneDayDate(int year, int month, int day) {
        if(getIntent().getExtras().getString("intent_source").equalsIgnoreCase("CalendarActivity")) {
            String endTime = getIntent().getExtras().getString("source_calendar_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                Date dateEnd = dateFormat.parse(endTime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
                String endDateString = sdf2.format(dateEnd);
                oneDayDate.setText(endDateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            oneDayDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    public void setAlert(){
        RelativeLayout linearLayout=new RelativeLayout(context);
        aNumberPicker=new NumberPicker(context);
        aNumberPicker.setMaxValue(100);
        aNumberPicker.setMinValue(1);
        final int number=aNumberPicker.getValue();
        aNumberPicker.setWrapSelectorWheel(false);
        aNumberPicker.setClickable(false);
        aNumberPicker.setEnabled(true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50,50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker,numPicerParams);
        linearLayout.isClickable();

        aNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
            }
        });

        alertBw=new AlertDialog.Builder(context);
        alertBw.setTitle("Select the Quantity you want to purchase...");
        alertBw.setView(linearLayout);

        alertBw.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBw.setNeutralButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
//                selected_product_quantity = aNumberPicker.getValue();
                pick.setText(aNumberPicker.getValue()+"");
                Log.d("Value is", aNumberPicker.getValue() + "");
                dialog.dismiss();
            }
        });
        alertDw=alertBw.create();
    }

    public void addSubscription() {
        createOrUpdateSubscription(quantity);
    }

    private void createOrUpdateSubscription(final int selected_product_quantity)
    {
        boolean isUpdate = false;
        if (getIntent().getExtras().getString("intent_source") != null) {
            int radioButtonID = subscriptionTypeRadioGroup.getCheckedRadioButtonId();
            View radioButton = subscriptionTypeRadioGroup.findViewById(radioButtonID);
            int idx = subscriptionTypeRadioGroup.indexOfChild(radioButton) + 1;//Add 1 cos it starts with zero

            if (getIntent().getExtras().getString("intent_source").equalsIgnoreCase("UpdateSubscription")) {
                boolean quantityChanged = subscriptionMasterSmall.getProductQuantity() != selected_product_quantity ? true : false;
                boolean subscriptionTypeChanged = getIntent().getExtras().getInt("subscription_sub_type") != idx ? true : false;
                boolean startDateChanged = !DateOperations.getDateString(DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate())).equalsIgnoreCase(startDateView.getText().toString()) ? true : false;
                boolean endDateChanged = !DateOperations.getDateString(DateOperations.getDateFromLong(subscriptionMasterSmall.getEndDate())).equalsIgnoreCase(endDateView.getText().toString()) ? true : false;

                if(quantityChanged || subscriptionTypeChanged || startDateChanged || endDateChanged ){
                    isUpdate = true;
                } else {
                    isUpdate = false;
                }
            }
        }

        if (sDialog != null && sDialog.isShowing())
        {
            sDialog.getProgressHelper().setBarColor(Color.parseColor("#FBB81D"));
            sDialog.setCancelable(false);
            sDialog.setTitleText("Doing some magic....")
                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        }



        if (isUpdate && subscriptionMasterSmall != null) {


            if(isFutureUpdateRequest) {
                onlyUpdateExistingSubscription(selected_product_quantity, false);
            } else {
                try{
                    createNewAndUpdateExistingSubscription(selected_product_quantity);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(!isUpdate && subscriptionMasterSmall != null)
        {
            if (sDialog != null && sDialog.isShowing())
            {
                sDialog
                        .setTitleText("Added Subscription!")
                        .setContentText(getString(R.string.thanks_subscription_normal))
                        .setConfirmText("Ok,cool !")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                BaseApplication.reloadHomePage = true;
//                                Intent homeActivityIntent = new Intent(SubscriptionActivity.this, CalendarActivity.class);
//                                startActivity(homeActivityIntent);
                                finish();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }

        else {
            SubscriptionMaster subscriptionMaster = new SubscriptionMaster();
            subscriptionMaster.setProductMaster(productMaster);
            subscriptionMaster.setProductQuantity(selected_product_quantity);
            subscriptionMaster.setRecentlyChangedFlag(true);
            subscriptionMaster.setActive(true);
            subscriptionMaster.setVerifyStatus(1);
            /* Get date */
            Date start_date = new Date();
            Date end_date3 = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                if (oneDayDate.getVisibility() == View.GONE) {//Check if tomorrow radio is checked
                    start_date = format.parse(startDateView.getText().toString());
                    end_date3 = format.parse(endDateView.getText().toString());

                } else {
                    start_date = format.parse(oneDayDate.getText().toString());
                    end_date3 = start_date;
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Before Conversion : Start of the date : " + start_date.toString());
            start_date = DateOperations.getStartOfDate(start_date);
            Log.d(TAG, "After Conversion : Start of the date : " + start_date.toString());
            end_date3 = DateOperations.getEndOfDate(end_date3);
            subscriptionMaster.setStartDate(start_date.getTime());
            subscriptionMaster.setEndDate(end_date3.getTime());
            int radioButtonID = subscriptionTypeRadioGroup.getCheckedRadioButtonId();
            View radioButton = subscriptionTypeRadioGroup.findViewById(radioButtonID);
            int idx = subscriptionTypeRadioGroup.indexOfChild(radioButton) + 1;//Add 1 cos it starts with zero
            subscriptionMaster.setTransientSubscriptionType(idx);

            User user = new User();
            user.setId(sharedPreferences.getString(Constants.USER_ID, ""));

            subscriptionMaster.setUser(user);

            saveSubscription(subscriptionMaster);
        }
    }

    private void createNewAndUpdateExistingSubscription(int selected_product_quantity) throws java.text.ParseException {
        onlyUpdateExistingSubscription(selected_product_quantity, true);

        //End Date object
        Date end_date = new Date();

        SubscriptionMaster newSubscriptionMaster = new SubscriptionMaster();
        ProductMaster productMaster = new ProductMaster();
        productMaster.setId(subscriptionMasterSmall.getProductMasterId());
        newSubscriptionMaster.setProductMaster(productMaster);
        newSubscriptionMaster.setProductQuantity(selected_product_quantity);
        newSubscriptionMaster.setRecentlyChangedFlag(true);
        newSubscriptionMaster.setActive(true);
        newSubscriptionMaster.setVerifyStatus(3);
        if(oneDayDate.getVisibility() == View.GONE) { //Check if tomorrow radio is checked
            if(!isFutureUpdateRequest){
                newSubscriptionMaster.setStartDate(DateOperations.getStartOfDate(DateOperations.getTomorrowStartDate()).getTime());
            } else{
                newSubscriptionMaster.setStartDate(DateOperations.getStartOfDate(DateOperations.getStringToDate(startDateView.getText().toString())).getTime());
            }

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            format2.setTimeZone(TimeZone.getTimeZone("IST"));
            end_date = format2.parse(endDateView.getText().toString());
            end_date = DateOperations.getEndOfDate(end_date);
            newSubscriptionMaster.setEndDate(end_date.getTime());
        } else{
            newSubscriptionMaster.setStartDate(DateOperations.getStringToDate(oneDayDate.getText().toString()).getTime());
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            format2.setTimeZone(TimeZone.getTimeZone("IST"));
            end_date = format2.parse(oneDayDate.getText().toString());
            end_date = DateOperations.getEndOfDate(end_date);
            newSubscriptionMaster.setEndDate(end_date.getTime());
        }

        //Get subscription_type from RadioButtonGroup
        int radioButtonID = subscriptionTypeRadioGroup.getCheckedRadioButtonId();
        View radioButton = subscriptionTypeRadioGroup.findViewById(radioButtonID);
        int idx = subscriptionTypeRadioGroup.indexOfChild(radioButton) + 1;//Add 1 cos it starts with zero
        newSubscriptionMaster.setTransientSubscriptionType(idx);
        newSubscriptionMaster.setProductQuantity(selected_product_quantity);

        User user = new User();
        user.setId(sharedPreferences.getString(Constants.USER_ID, ""));

        newSubscriptionMaster.setUser(user);
        saveSubscription(newSubscriptionMaster);
    }

    private void onlyUpdateExistingSubscription(int selected_product_quantity, boolean endToday) {
        Date start_date1 = new Date();
        Date end_date1 = new Date();

        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        format2.setTimeZone(TimeZone.getTimeZone("IST")); //To save date in Indian Standard time
        try {
            if (oneDayDate.getVisibility() == View.GONE) {//Check if tomorrow radio is checked
                start_date1 = format2.parse(startDateView.getText().toString());
                if(endToday){
                    //if end today i.e. called from createNewAndUpdate
                    end_date1 = DateOperations.getTodayEndDate();
                } else{
                    //Only update
                    end_date1 = format2.parse(endDateView.getText().toString());
                    end_date1 = DateOperations.getEndOfDate(end_date1);
                }

            } else {
                if(!endToday) {
                    start_date1 = format2.parse(oneDayDate.getText().toString());
                    end_date1 = start_date1;
                    end_date1 = DateOperations.getEndOfDate(end_date1);
                }
            }
        } catch (java.text.ParseException e4) {
            e4.printStackTrace();
        }

        int radioButtonID = subscriptionTypeRadioGroup.getCheckedRadioButtonId();
        View radioButton = subscriptionTypeRadioGroup.findViewById(radioButtonID);
        int idx = subscriptionTypeRadioGroup.indexOfChild(radioButton) + 1;//Add 1 cos it starts with zero

        if(!endToday) {
            subscriptionMasterSmall.setTransientSubscriptionType(idx);
            subscriptionMasterSmall.setProductQuantity(selected_product_quantity);
            subscriptionMasterSmall.setStartDate(start_date1.getTime());
        }
        if(subscriptionMasterSmall.getSubscriptionType() !=null)
            subscriptionMasterSmall.setTransientSubscriptionType(subscriptionMasterSmall.getSubscriptionType());
        subscriptionMasterSmall.setEndDate(end_date1.getTime());

        SubscriptionMaster newSubscriptionMaster = new SubscriptionMaster(subscriptionMasterSmall, sharedPreferences.getString(Constants.USER_ID, ""));
        updateSubscription(newSubscriptionMaster);
    }

    public void endSubscription() {
        endSubscription = true;
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.getProgressHelper().setBarColor(Color.parseColor("#FBB81D"));
            sDialog.setCancelable(false);
            sDialog.setTitleText("Doing some magic....")
                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        }

        if(subscriptionMasterSmall != null) {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("IST"));
            Date today_date = new Date();
            Log.i("*****", "this is end sub");

            if(4 == subscriptionMasterSmall.getSubscriptionType() || DateOperations.getDateFromLong(subscriptionMasterSmall.getStartDate()).after(DateOperations.getTodayEndDate())) {
                //endSubscription = true;
                subscriptionMasterSmall.setTransientSubscriptionType(subscriptionMasterSmall.getSubscriptionType());
                subscriptionMasterSmall.setEndDate(DateOperations.getTodayEndDate().getTime());

                SubscriptionMaster newSubscriptionMaster = new SubscriptionMaster(subscriptionMasterSmall, sharedPreferences.getString(Constants.USER_ID, ""));
                updateSubscription(newSubscriptionMaster);
            } else {
                subscriptionMasterSmall.setTransientSubscriptionType(subscriptionMasterSmall.getSubscriptionType());
                subscriptionMasterSmall.setEndDate(DateOperations.getTodayEndDate().getTime());

                SubscriptionMaster newSubscriptionMaster = new SubscriptionMaster(subscriptionMasterSmall, sharedPreferences.getString(Constants.USER_ID, ""));
                updateSubscription(newSubscriptionMaster);
            }
        } else {
            //endSubscription = true;
        }
    }

    private void saveSubscription(final SubscriptionMaster subscriptionMaster) {
        if(isNetworkAvailable()) {
            final String url = Constants.API_PROD + "/subscriptionMaster/create";
            Log.d(TAG, "Url : " + url);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    StatusBean statusBean = new Gson().fromJson(response, StatusBean.class);

                    if (statusBean != null && statusBean.getStatus() == 1){
                        BaseApplication.reloadHomePage = true;

                        if (sDialog != null && sDialog.isShowing()) {
                            if(endSubscription) {
                                sDialog
                                        .setTitleText("Subscription Ended!").setContentText("Your Subscription for the following product ended");
                            } else {
                                sDialog
                                        .setTitleText("Added Subscription!").setContentText(getString((R.string.thanks_subscription_normal)));
                            }

                            sDialog.setConfirmText("Ok,cool !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            Intent homeActivityIntent = new Intent(SubscriptionActivity.this, ActivityCalendar.class);
                                            startActivity(homeActivityIntent);
                                            finish();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    } else {
                        sDialog.dismissWithAnimation();
                        if (statusBean != null && statusBean.getMessage() != null) {
//                            Toast.makeText(SubscriptionActivity.this, statusBean.getMessage(), Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(SubscriptionActivity.this, SweetAlertDialog.ERROR_TYPE, robotoBold, robotoLight)
                                    .setTitleText("Error!")
                                    .setContentText(statusBean.getMessage())
                                    .setConfirmText("Recharge Now")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog2) {
                                            sDialog2.dismissWithAnimation();
                                            Intent intent = new Intent(SubscriptionActivity.this,PaymentsActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                        } else {
                            Toast.makeText(SubscriptionActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    if (sDialog != null && sDialog.isShowing()) {
                        sDialog.dismissWithAnimation();
                        alert("Connection Error");
                    }
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    subscriptionMaster.getUser().setId(sharedPreferences.getString(Constants.USER_ID, ""));
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
            Toast.makeText(SubscriptionActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSubscription(final SubscriptionMaster subscriptionMaster){
        if(isNetworkAvailable()) {
            final String url = Constants.API_PROD + "/subscriptionMaster/edit";
            Log.d(TAG, "Url : " + url);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    StatusBean statusBean = new Gson().fromJson(response, StatusBean.class);

                    if (statusBean != null && statusBean.getStatus() == 1){
                        BaseApplication.reloadHomePage = true;

                        if (sDialog != null && sDialog.isShowing()){
                            if(endSubscription) {
                                sDialog
                                        .setTitleText("Subscription Ended!").setContentText("Your Subscription for the following product ended");
                            } else {
                                sDialog
                                        .setTitleText("Subscription Updated!").setContentText(getString((R.string.thanks_subscription_normal)));
                            }

                            sDialog.setConfirmText("Ok,cool !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();

                                            Intent homeActivityIntent = new Intent(SubscriptionActivity.this, ActivityCalendar.class);
                                            startActivity(homeActivityIntent);
                                            finish();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }

                    } else {
                        if (statusBean != null && statusBean.getMessage() != null) {
                            Toast.makeText(SubscriptionActivity.this, statusBean.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubscriptionActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    if (sDialog != null && sDialog.isShowing()) {
                        sDialog.dismissWithAnimation();
                        alert("Connection Error");
                    }
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    subscriptionMaster.getUser().setId(sharedPreferences.getString(Constants.USER_ID, ""));
                    subscriptionMaster.setSubscriptionType(null);
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
            Toast.makeText(SubscriptionActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isNetworkAvailable()){
            finish();
        }
    }
}