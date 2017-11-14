package com.villagemilk.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.beans.BillingItem;
import com.villagemilk.beans.BillingMaster;
import com.villagemilk.beans.Building;
import com.villagemilk.beans.Payment;
import com.villagemilk.beans.StatusBean;
import com.villagemilk.beans.User;
import com.villagemilk.dialogs.SweetAlertDialog;
import com.villagemilk.model.BillingAddress;
import com.villagemilk.util.Constants;
import com.villagemilk.util.DateOperations;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.villagemilk.util.Util;
import com.villagemilk.view.AcceptFragment;
import com.villagemilk.view.ActivityAuthorizeNet;
import com.villagemilk.view.ActivityBillingAddress;
import com.villagemilk.view.FragmentPaymentSuccess;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;
import net.authorize.aim.cardpresent.DeviceType;
import net.authorize.aim.cardpresent.MarketType;
import net.authorize.aim.emv.EMVErrorCode;
import net.authorize.aim.emv.EMVTransaction;
import net.authorize.aim.emv.EMVTransactionManager;
import net.authorize.aim.emv.EMVTransactionType;
import net.authorize.aim.emv.Result;
import net.authorize.auth.PasswordAuthentication;
import net.authorize.auth.SessionTokenAuthentication;
import net.authorize.data.Order;
import net.authorize.data.OrderItem;
import net.authorize.data.creditcard.CreditCard;
import net.authorize.data.reporting.ReportingDetails;
import net.authorize.mobile.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;


/**
 * Created by sagaryarnalkar on 18/07/15.
 */
public class PaymentsActivity extends BaseActivity implements PaymentResultListener, EncryptTransactionCallback {
    private static final String TAG = "Payments Activity";

    private static int PAYTM_PAYMENT_PROVIDER = 1;
    private static int MOBIKWKIK_PAYMENT_PROVIDER = 2;

    BillingAddress billingAddress;

    public static String THEME = "merchant";
    public static String WEBSITE = "dailyninja";
    public static String PAYTM_CHANNEL_ID = "WAP";
    public static String INDUSTRY_TYPE_ID = "Retail106";
    public static String PAYTM_CUSTOMER_ID = "ETYS13";
    public static String PAYTM_MERCHANT_kEY = "9h&F809NDdg682oB";
    public static String PAYTM_MERCHANT_ID = "dailyn93845776216227";
    public static String PAYTM_REBOOKING_URL = "http://test.etravelsmart.com/etsAPI/bus/paytmReBooking.htm?etsno=ETSNO";
    public static String PAYTM_BASE_URL = "https://pguat.paytm.com/oltp-web/processTransaction";
    public static String PAYTM_RETURN_URL="http://test.etravelsmart.com/etsAPI/payuresponse.jsp";
    public static String PAYTM_REFUND_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/REFUND";
    public static String PAYTM_RETURN_URL_FOR_REFUND = "http://test.etravelsmart.com/etsAPI/bus/processRefund.htm";
    public static String PAYTM_STATUS_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/TXNSTATUS";

    private int randomInt = 0;
    private PaytmPGService Service = null;

    private TextView RsSymbol;
    private TextView tvChangeBillingAddress;
    private EditText etAmount;
    private Button btn3000;
    private Button btn1000;
    private Button btn2000;

    private Button billingHistory;
    private Button payment_history;
    ScrollView scroll;

    private SharedPreferences sharedPreferences;

    TextView rupee_value;

    private BillingMaster billingMaster;
    private User user;
    double balance = 0.0;
    private LinearLayout llRecharge;
    private Payment payment = new Payment();

    int amount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        initSimpleToolbar();

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);

        String paytmRetailid = sharedPreferences.getString(Constants.paytm_retail_id_ref,null);

        if(paytmRetailid!=null && paytmRetailid.length()>0) {
            INDUSTRY_TYPE_ID = paytmRetailid;
        }

//        try {
//            mixpanel.track("Payment Screen");
//        } catch (Exception e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }

        billingHistory = (Button)findViewById(R.id.billing_history);
        payment_history = (Button)findViewById(R.id.payment_history);
        tvChangeBillingAddress = (TextView) findViewById(R.id.tvChangeBillingAddress);

        tvChangeBillingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Building building = BaseApplication.getInstance().getUser().getBuilding();
                Intent intent = new Intent(PaymentsActivity.this, ActivityBillingAddress.class);
                intent.putExtra(Constants.BUILDING_ID, building.getId());
                intent.putExtra(Constants.BUILDING_NAME, building.getBuildingName());
                intent.putExtra("fromPaymentScreen", true);
                startActivity(intent);



            }
        });

        billingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent billingIntent = new Intent(PaymentsActivity.this, BillingHistoryActivity.class);
                startActivity(billingIntent);
            }
        });

        payment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent billingIntent = new Intent(PaymentsActivity.this, BillingListActivity.class);
                startActivity(billingIntent);
            }
        });

       // scroll = (ScrollView) findViewById(R.id.scrollView);

        //scroll.scrollTo(0,100);

        initViews();
        getCurrentBill();
        getBillingAddress();
    }

    public String getFormattedDate(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        try {
            String formatedString = dateFormat.format(d);
            return formatedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private void initViews(){

        Button btnPay = (Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                payNow();


                if(TextUtils.isEmpty(etAmount.getText())){

                    Toast.makeText(PaymentsActivity.this,"Enter Amount first",Toast.LENGTH_LONG).show();
                    return;

                }
                amount = Integer.valueOf(etAmount.getText().toString());

                launchAcceptFragment();

            }
        });
        btnPay.setTypeface(robotoBold);

        Button btnContact = (Button) findViewById(R.id.btnContact);
        btnContact.setTypeface(robotoBold);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityCalendar = new Intent(PaymentsActivity.this, ContactUsActivity.class);
                startActivity(activityCalendar);
            }
        });

        findViewById(R.id.ivInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(PaymentsActivity.this, SweetAlertDialog.NORMAL_TYPE, robotoBold, robotoLight)
                        .setTitleText("Wallet Info")
                        .setContentText(getString(R.string.bill_info))
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

        llRecharge = (LinearLayout)findViewById(R.id.llmoneyOption);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etAmount.setTypeface(robotoLight);
        RsSymbol = (TextView)findViewById(R.id.RsSymbol);
        RsSymbol.setTypeface(robotoLight);
        RsSymbol.setText("$");
        btn3000 = (Button)findViewById(R.id.btn3000);
        btn3000.setTypeface(robotoBold);
        btn3000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementAmount(20);
            }
        });
        btn1000 = (Button)findViewById(R.id.btn1000);
        btn1000.setTypeface(robotoBold);
        btn1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementAmount(5);
            }
        });
        btn2000 = (Button)findViewById(R.id.btn2000);
        btn2000.setTypeface(robotoBold);
        btn2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementAmount(10);
            }
        });

    }

    AcceptFragment checkoutFragment;

    private void launchAcceptFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        checkoutFragment =
                (AcceptFragment) fragmentManager.findFragmentByTag("");
        if (checkoutFragment == null) {
            checkoutFragment = AcceptFragment.newInstance(amount);
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, checkoutFragment, "checkout").addToBackStack(null)
                    .commit();
        }
    }


    private void incrementAmount(int amount) {
        String amt = etAmount.getText().toString();
        if(amt != null && amt.length() >0) {
            int amnt = Integer.parseInt(amt);
            etAmount.setText((amnt + amount) + "");
        } else {
            etAmount.setText(amount + "");
    }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super. onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 303) {
        }

        if(requestCode == 1){

            Payment payment = new Payment();
            payment.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
            payment.setAmount((double)amount);
            payment.setPaymentType(3);
            payment.setPaymentProvider(MOBIKWKIK_PAYMENT_PROVIDER);
            payment.setBankTxnId("");
            payment.setTransactionId("");
            payment.setOrderId("");

            makePayment(payment);

        }
    }

    public void makeAuthorizePayment(EncryptTransactionResponse response){

        getSupportFragmentManager().beginTransaction().remove(checkoutFragment).commit();

        Payment payment = new Payment();
        payment.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
        payment.setAmount((double)amount);
        payment.setPaymentType(3);
        payment.setPaymentProvider(MOBIKWKIK_PAYMENT_PROVIDER);
        payment.setBankTxnId(response.getDataValue());
        payment.setTransactionId(response.getDataValue());
        payment.setOrderId(response.getDataValue());

        makePayment(payment);

    }
    private void getCurrentBill() {
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/billingMaster/findById";
            Log.d(TAG, "Url : " + url);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Response : " + response.toString());

                    billingMaster = new Gson().fromJson(String.valueOf(response), BillingMaster.class);
                    if (billingMaster != null && billingMaster.isCurrent()) {   //isCurrent = true,endDate & paymentDate should be null
                        user = billingMaster.getUser();
                        if (billingMaster.getStartDate() != 0) {
                            double bill = billingMaster.getAmount();
                            int temp = (int)bill;
                            BillingItem ei;
                            if(temp<0)
                            {
                                  ei = new BillingItem("Current Balance",
                                        "$ " + billingMaster.getAmount(),
                                        "From " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getStartDate())),
                                        R.drawable.green_dot, billingMaster.getAmount(), true);

                            }
                            else
                            {
                                  ei = new BillingItem("Current Balance",
                                        "$ " + billingMaster.getAmount(),
                                        "From " + getFormattedDate(DateOperations.getDateFromLong(billingMaster.getStartDate())),
                                        R.drawable.big_red_dot, billingMaster.getAmount(), true);

                            }

                            final TextView paid_date=(TextView)findViewById(R.id.paid_date);
                            paid_date.setTypeface(robotoLight);
                            rupee_value = (TextView)findViewById(R.id.ruppes_value);
                            paid_date.setTypeface(robotoBold);
                            final TextView date=(TextView)findViewById(R.id.start_end_date);
                            paid_date.setTypeface(robotoLight);
                            final ImageView item_image=(ImageView)findViewById((R.id.imageView1));

                            if (paid_date != null){
                                paid_date.setText(ei.paid_date);
                            }

                            if(rupee_value != null) {
                                rupee_value.setTextColor(getResources().getColor(R.color.black));
                                if(ei.isCurrent && ei.amount > 0 ) {
                                    rupee_value.setTextColor(getResources().getColor(R.color.red_btn_bg_pressed_color));
                                    rupee_value.setText("$ -" + String.format("%.2f", ei.amount));
                                    double amnt = 500 - (ei.amount % 100);
                                   // etAmount.setText("$" + "");
                                } else if(ei.isCurrent && ei.amount < 0) {
                                    rupee_value.setText("$ "+String.format("%.2f", (-1 * ei.amount)));
                                } else{
                                    rupee_value.setText("$ 0.00");
                                }

                                balance = ei.amount;
                            }
                            if(date != null) {
                                date.setText(ei.start_end_date);
                            }

                            if(item_image!=null){
                                item_image.setImageResource(ei.imageid);
                            }
                        }
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                    finish();
                }
            }
            ) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", BaseApplication.getInstance().getUser().getBillingMasterId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Body : " + jsonObject.toString());
                    return jsonObject.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(PaymentsActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }


    private void getBillingAddress() {

        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/user/getBillingAddress/" + BaseApplication.getInstance().getUser().getId();
            Log.d(TAG, "Url : " + url);

            final StringRequest stringRequest = new StringRequest(url,new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    Gson gson = new Gson();

                    billingAddress = gson.fromJson(response,BillingAddress.class);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                    finish();
                }
            }
            ) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(PaymentsActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    public void makePayment(final Payment payment){
        if(isNetworkAvailable()){
            showDialog();

            String url = Constants.API_PROD + "/makeBillPayment";
            Log.d(TAG, "Url : " + url);

            final StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "MakePayment Response : " + response.toString());

                    hideDialog();

                    etAmount.setText("");

                    StatusBean statusBean = new Gson().fromJson(response, StatusBean.class);

                    if(statusBean.getStatus() == 1){
                        alert("Transaction complete !!!");

                        try {
                            JSONObject extraInfo = new JSONObject();
                            extraInfo.put("OrderID", payment.getOrderId());
//                            extraInfo.put("Ordernote", "CashonDelivery");
//                            AppviralityAPI.saveConversionEvent("Transaction", ""+payment.getAmount(), "Rs", extraInfo);
                        }catch (Exception exp){}

                        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.INTENT_SOURCE)){
                            if(Constants.NEW_CART_ACTIVITY.equals(getIntent().getExtras().getString(Constants.INTENT_SOURCE))){


                                finish();
                            }
                        } else {
                            getCurrentBill();
                        }
                    } else {

                        alert("Transaction failed !!!");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "MakePayment Error : " + error.toString());
                    alert("Connection Error");
                    hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    return new Gson().toJson(payment).getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(PaymentsActivity.this, R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        BaseApplication.reloadHomePage = true;
//        getCurrentBill();
    }

    @Override
    public void onRestart() {
        super.onRestart();

//        BaseApplication.reloadHomePage = true;
//        getCurrentBill();
    }

    @Override
    public void onPaymentSuccess(String s) {

        Payment payment = new Payment();
        payment.setUserId(sharedPreferences.getString(Constants.USER_ID, ""));
        payment.setAmount((double) amount);
        payment.setPaymentType(3);
        payment.setPaymentProvider(3);
        payment.setBankTxnId(s);
        payment.setTransactionId(s);
        payment.setOrderId(s);

        makePayment(payment);

        etAmount.setText("");

//        Toast.makeText(this,"Payment Successfull " + s,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this,"Payment Failure",Toast.LENGTH_LONG).show();

    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void onErrorReceived(ErrorTransactionResponse error) {
        checkoutFragment.dismissDialog();
        Toast.makeText(PaymentsActivity.this,"Payment Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncryptionFinished(EncryptTransactionResponse response) {

        checkoutFragment.dismissDialog();

        sendPaymentRequest(response);
    }

    private void sendPaymentRequest(final EncryptTransactionResponse transactionResponse) {

        if (Util.isNetworkAvailable(this)) {
            Util.showDialog(this);

            String url = Constants.API_PROD + "/billingMaster/creditCardProcessing";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getSupportFragmentManager().popBackStack();

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getString("status").equals("success")) {
                            Toast.makeText(PaymentsActivity.this,"Payment Success", Toast.LENGTH_SHORT).show();

                            String transactionId = jsonObject.getString("transactionId");

//                            FragmentPaymentSuccess fragmentPaymentSuccess = FragmentPaymentSuccess.newInstance(transactionId,(double)amount,billingAddress);
//
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.frameLayout, fragmentPaymentSuccess).addToBackStack(null)
//                                    .commit();

                            amount += -balance;
                            rupee_value.setText("$ " + String.format("%.2f", (double)amount));



                        }else {
                            Toast.makeText(PaymentsActivity.this,"Payment Failed", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Util.hideDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Util.alert(PaymentsActivity.this,"Connection Error");

                    Util.hideDialog();
                }
            }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("amount", amount);
                        jsonObject.put("userId", sharedPreferences.getString(Constants.USER_ID, ""));
                        jsonObject.put("dataDescriptor", transactionResponse.getDataDescriptor());
                        jsonObject.put("dataValue", transactionResponse.getDataValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return jsonObject.toString().getBytes();
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
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

}
