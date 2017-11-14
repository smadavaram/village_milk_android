package com.villagemilk.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.villagemilk.R;

/**
 * Created by akash.mercer on 19-01-2016.
 */
public class AboutUsActivity extends BaseActivity {
    private static final String TAG = "About Us Activity";

    private Button buttonTermsOfUse;
    private Button buttonPrivacyPolicy;
    private Button buttonCancellationPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initSimpleToolbar();

        buttonCancellationPolicy=(Button)findViewById(R.id.buttonCancellationPolicy);
        buttonCancellationPolicy.setTypeface(robotoBold);
        buttonCancellationPolicy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://villagemilk.in/refundcancellation-policy/"));
                startActivity(browserIntent);
            }
        });

        buttonTermsOfUse = (Button)findViewById(R.id.buttonTermsOfUse);
        buttonTermsOfUse.setTypeface(robotoBold);
        buttonTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://villagemilk.in/terms-conditions/"));
                startActivity(browserIntent);
            }
        });

        buttonPrivacyPolicy=(Button)findViewById(R.id.buttonPrivacyPolicy);
        buttonPrivacyPolicy.setTypeface(robotoBold);
        buttonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://villagemilk.in/privacy-policy/"));
                startActivity(browserIntent);
            }
        });
    }
}
