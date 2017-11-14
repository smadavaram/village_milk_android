package com.villagemilk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.villagemilk.R;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class ReferralCodeDialog extends Dialog {
    private static final String TAG = "ReferralCodeDialog";

    private Context context;

    private EditText etReferralCode;

    private TextView tvSubmit;

    public ReferralCodeDialog(Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_referral_code);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);

        // Touching outside of the dialog will dismiss it
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        etReferralCode = (EditText) findViewById(R.id.etReferralCode);

        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

        initViews();
    }

    private void initViews(){
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etReferralCode.getText().toString() != null && etReferralCode.getText().toString().length() > 0){
//                    ((ReferralActivity) context).submitReferralCode(etReferralCode.getText().toString());
                } else {
                    Toast.makeText(context, "Please enter valid referral code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }
}
