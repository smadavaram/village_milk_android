package com.villagemilk.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.villagemilk.R;
import com.freshdesk.hotline.Hotline;


public class ContactUsActivity extends BaseActivity {

	Button call,email,rate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);
		initSimpleToolbar();
	       call=(Button)findViewById(R.id.call);
        call.setTypeface(robotoBold);
		call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                //TODO change the number accordingly
                intent.setData(Uri.parse("tel:+919440494195"));
                startActivity(intent);
            }
        });
        rate = (Button)findViewById(R.id.RateUs);
        rate.setTypeface(robotoBold);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
        rate.setVisibility(View.GONE);
        email=(Button)findViewById(R.id.email);
        email.setTypeface(robotoBold);
        email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent email = new Intent(Intent.ACTION_SEND);
//                Map<String, String> customData = new HashMap<String, String>();
//                customData.put("useremail", ParseUser.getCurrentUser().getEmail());
//                Apptentive.showMessageCenter(ContactUsActivity.this);
                Hotline.showConversations(getApplicationContext());

                /*
                Sagar - this is old code
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "contact@dailyninja.in" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For DailyNinja (User:"+ ParseUser.getCurrentUser().getEmail()+")");
                //intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                */
            }
        });
        TextView tv = (TextView)findViewById(R.id.description);
        tv.setTypeface(robotoLight);
	}
}
