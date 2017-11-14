package com.villagemilk.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.customviews.CartBottomView;
import com.villagemilk.dialogs.SweetAlertDialog;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.cardpresent.DeviceType;
import net.authorize.aim.cardpresent.MarketType;
import net.authorize.auth.PasswordAuthentication;
import net.authorize.auth.SessionTokenAuthentication;
import net.authorize.data.Order;
import net.authorize.data.OrderItem;
import net.authorize.data.creditcard.CreditCard;
import net.authorize.mobile.Transaction;

import java.math.BigDecimal;

/**
 * Created by arsh on 20/6/17.
 */

public class ActivityAuthorizeNet extends BaseActivity {

    EditText etCNo;
    EditText etCName;
    EditText etCExpiry;
    EditText etCCVV;
    Button btnPay;

    Double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorize_net);

        etCNo = (EditText)findViewById(R.id.etCno);
        etCName = (EditText)findViewById(R.id.etCName);
        etCExpiry = (EditText)findViewById(R.id.etCExpiry);
        etCCVV = (EditText)findViewById(R.id.etCCVV);
        btnPay = (Button) findViewById(R.id.btnPay);

        amount = getIntent().getExtras().getDouble("amount");

        btnPay.setText("PAY $" + amount);

        setUpToolbar();

        btnPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startNonEmvPayments();

            }

        });

    }

    public void setUpToolbar(){

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Authorize.net");



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }


    public void startNonEmvPayments(){
        if(TextUtils.isEmpty(etCNo.getText())){
            Toast.makeText(this,"Enter Card Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etCName.getText())){
            Toast.makeText(this,"Enter Card Holder's Name",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(etCCVV.getText())){
            Toast.makeText(this,"Enter CVV",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etCExpiry.getText())){
            Toast.makeText(this,"Enter Expiry",Toast.LENGTH_SHORT).show();
            return;
        }

        PasswordAuthentication passAuth = PasswordAuthentication.createMerchantAuthentication("4Zgm7C4G", "64TfV5YU5q36EuUw", "InpersonSDK-Android-test");
        Merchant testMerchant = Merchant.createMerchant(Environment.SANDBOX, passAuth);
        testMerchant.setDeviceType(DeviceType.WIRELESS_POS);
        testMerchant.setMarketType(MarketType.RETAIL);

        net.authorize.aim.Transaction transaction = net.authorize.aim.Transaction.createTransaction(testMerchant, TransactionType.AUTH_CAPTURE, new BigDecimal(1.0));
        net.authorize.aim.Result loginResult = (net.authorize.aim.Result)testMerchant.postTransaction(transaction);

//        net.authorize.aim.Transaction transaction = Transaction.createTransaction(testMerchant, net.authorize.aim.TransactionTransactionType.MOBILE_DEVICE_LOGIN);
//        net.authorize.mobile.Result loginResult = (net.authorize.mobile.Result)testMerchant.postTransaction(transaction);
//if login succeeded, populate session token in the Merchant object
        SessionTokenAuthentication sessionTokenAuthentication = SessionTokenAuthentication.createMerchantAuthentication(testMerchant.getMerchantAuthentication().getName(), loginResult.getSessionToken(), "Test EMV Android");

        if ((loginResult.getSessionToken() != null) && (sessionTokenAuthentication != null)) {
            testMerchant.setMerchantAuthentication(sessionTokenAuthentication);
        }

        String expiry = etCExpiry.getText().toString();
//create new credit card object with required fields
        CreditCard creditCard = CreditCard.createCreditCard();
        creditCard.setCreditCardNumber(etCNo.getText().toString());
        creditCard.setExpirationMonth(expiry.substring(0,2));
        creditCard.setExpirationYear("20"+expiry.substring(2));
        creditCard.setCardCode(etCCVV.getText().toString());

//create order item and add to transaction object
        Order order =  Order.createOrder();
        OrderItem oi =  OrderItem.createOrderItem();
        oi.setItemId("001");
        oi.setItemName("testItem");
        oi.setItemDescription("Goods");
        oi.setItemPrice(new BigDecimal(amount));
        oi.setItemQuantity("1");
        oi.setItemTaxable(false);
        order.addOrderItem(oi);
        order.setTotalAmount(new BigDecimal(amount));



//post the transaction to Gateway
        net.authorize.aim.Result authCaptureResult = (net.authorize.aim.Result) testMerchant.postTransaction(transaction);

        if(authCaptureResult.isOk()){
            new SweetAlertDialog(ActivityAuthorizeNet.this, SweetAlertDialog.SUCCESS_TYPE, robotoBold, robotoLight)
                    .setTitleText("Payment Successful")
                    .setContentText(getString((R.string.thanks_subscription_normal)))
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            setResult(1);
                        }
                    })
                    .show();


        }else {

            new SweetAlertDialog(ActivityAuthorizeNet.this, SweetAlertDialog.ERROR_TYPE, robotoBold, robotoLight)
                    .setTitleText("Payment Failure")
                    .setContentText(authCaptureResult.getXmlResponse())
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
//                            setResult(2);
                        }
                    })
                    .show();

        }

//        net.authorize.reporting.Result result = null;
//        net.authorize.reporting.Transaction t = testMerchant.createReportingTransaction(net.authorize.reporting.TransactionType.GET_UNSETTLED_TRANSACTION_LIST);
//        t.setReportingDetails(ReportingDetails.createReportingDetails());
//        result = (net.authorize.reporting.Result) AnetHelper.merchant.postTransaction(t);

    }

}
