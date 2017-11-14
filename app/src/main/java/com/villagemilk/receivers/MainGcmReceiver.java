package com.villagemilk.receivers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.activities.SplashActivity;
import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineNotificationConfig;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sagaryarnalkar on 10/05/16.
 */
public class MainGcmReceiver extends BroadcastReceiver
{



    /**
     * All pushes received from Localytics will contain an 'll' string extra which can be parsed into
     * a JSON object. This JSON object contains performance tracking information, such as a campaign
     * ID. Any push received containing this 'll' string extra, should be passed on to the Localytics
     * PushReceiver. Any other push can be handled as you see fit.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Hotline instance = Hotline.getInstance(context);

        /*****************************Registration block to get the registration ID*********************************************/
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION"))
        {
            String registrationId = intent.getStringExtra("registration_id");
//            if (!TextUtils.isEmpty(registrationId))
//            {
//                Localytics.setPushRegistrationId(registrationId);
//            }
        }

        /******************See Whether the Message contains the key mp_message********************/
        else if (intent.getExtras().containsKey("mp_message"))
        {
//            new PushReceiver().onReceive(context, intent);
            String mp_message = intent.getExtras().getString("mp_message");

            if(intent.getExtras().containsKey("bigpic"))
            {
                new generatePictureStyleNotification(context,intent.getExtras().getString("title"), mp_message, intent.getExtras().getString("bigpic"),intent.getExtras().getString("tickertext"),intent.getExtras().getString("summarytext")).execute();
            }
            else
            {
                showNormalNotification(context, mp_message);
            }
        }
        else if (instance.isHotlineNotification(intent))
        {
            HotlineNotificationConfig notificationConfig = new HotlineNotificationConfig()
                    .setNotificationSoundEnabled(true)
                    .setSmallIcon(R.drawable.ic_launch)
                    .setLargeIcon(R.mipmap.ic_launcher)
                    .launchDeepLinkTargetOnNotificationClick(true)
                    .launchActivityOnFinish(HomeActivity.class.getName())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            Hotline.getInstance(context.getApplicationContext()).setNotificationConfig(notificationConfig);
            instance.handleGcmMessage(intent);
            return;
        }

        else
        {

            String message = intent.getStringExtra("message");
            String title = intent.getStringExtra("title");
            if(title==null || title.length()==0)
                showNormalNotification(context,message);
            else
                showNormalNotification(context,message,title);


        }
    }


    /*This class generate picture style notification and also extends AyncTask*/
    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap>
    {

        private Context mContext;
        private String title, message, imageUrl, ticker, summary;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl, String ticker, String summary) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.ticker = ticker;
            this.summary = summary;
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result)
        {
            super.onPostExecute(result);

            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.putExtra("key", "value");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);
            if(title==null)
                title = "Dailyninja";
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if(title!=null && title.length()>0)
            {
                Notification.BigPictureStyle nbps = new Notification.BigPictureStyle().bigPicture(result);
                if(summary!=null)
                {

                    nbps.setSummaryText(summary);
                }

                Uri sound = null;
                try
                {
                    sound = Uri.parse("android.resource://com.dailyninjadev/" + R.raw.notifsound);
                }
                catch(Exception exp)
                {

                }

                Notification.Builder nb = new Notification.Builder(mContext)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_notify_icon)
                        .setLargeIcon(result)
                        .setStyle(nbps);
                if(ticker!=null)
                {
                    nb.setTicker(ticker);
                }

                if(sound!=null)
                    nb = nb.setSound(sound);


                Notification notif = nb.build();
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(1, notif);
            }
            else
            {
//                Notification.BigPictureStyle nbps = new Notification.BigPictureStyle().bigPicture(result);
//                if(summary!=null)
//                {
//                    nbps.setSummaryText(summary);
//                }
//                Notification.Builder nb = new Notification.Builder(mContext)
//                        .setContentIntent(pendingIntent)
//                        .setContentText(message)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setLargeIcon(result)
//                        .setStyle(nbps);
//                if(ticker!=null)
//                {
//                    nb.setTicker(ticker);
//                }
//                Notification notif = nb.build();
//                notif.flags |= Notification.FLAG_AUTO_CANCEL;
//                notificationManager.notify(1, notif);

//                int icon = R.mipmap.ic_launcher;
//                long when = System.currentTimeMillis();
//                Notification notification = new Notification(icon, "Custom Notification", when);
//
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    RemoteViews views;
//                    views = new RemoteViews(mContext.getPackageName(), R.layout.notif_layout);
//                    views.setImageViewBitmap(R.id.big_picture, result);
//                    views.setImageViewBitmap(R.id.big_icon, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
//                    views.setTextViewText(R.id.title, message);
//                    notification.bigContentView = views;
//                }
//
////                NotificationManager notificationManager = (NotificationManager)
////                        mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                Random randomGenerator = new Random();
//                int randomInt = randomGenerator.nextInt(100);
//                notificationManager.notify(randomInt, notification);

                // Using RemoteViews to bind custom layouts into Notification
                RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                        R.layout.notif_layout);

                // Set Notification Title
//                String strtitle = mContext.getString(R.string.customnotificationtitle);
                // Set Notification Text
//                String strtext = getString(R.string.customnotificationtext);

                // Open NotificationView Class on Notification Click
                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri sound = null;
                try {
                    sound = Uri.parse("android.resource://com.dailyninjadev/" + R.raw.notifsound);
                }catch(Exception exp){}

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                        // Set Icon
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // Set Ticker Message
                        .setTicker(message)
                        // Dismiss Notification
                        .setAutoCancel(true)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                        .setContent(remoteViews);

                if(sound!=null)
                    builder = builder.setSound(sound);

                // Locate and set the Image into customnotificationtext.xml ImageViews
                remoteViews.setImageViewBitmap(R.id.big_picture,result);
                remoteViews.setImageViewResource(R.id.big_icon,R.mipmap.ic_launcher);

                // Locate and set the Text into customnotificationtext.xml TextViews
                remoteViews.setTextViewText(R.id.title,message);
//                remoteViews.setTextViewText(R.id.text,getString(R.string.customnotificationtext));

                // Create Notification Manager
//                NotificationManager notificationmanager = (NotificationManager) mCgetSystemService(NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationManager.notify(0, builder.build());


            }

        }
    }

    private void showNormalNotification(Context context, String message)
    {
        if (!TextUtils.isEmpty(message))
        {
            Intent mainIntent = new Intent(context, SplashActivity.class);
            PendingIntent launchIntent = PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri sound = null;
            try {
                sound = Uri.parse("android.resource://com.dailyninjadev/" + R.raw.notifsound);
            }catch(Exception exp){}
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notify_icon)
                    .setContentTitle("Dailyninja")
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(launchIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            if(sound!=null)
                builder=builder.setSound(sound);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }

    private void showNormalNotification(Context context, String message, String title)
    {
        if (!TextUtils.isEmpty(message))
        {
            Intent mainIntent = new Intent(context, SplashActivity.class);
            mainIntent.putExtra("serverNotif", true);
            PendingIntent launchIntent = PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri sound = null;
            try {
                sound = Uri.parse("android.resource://com.dailyninjadev/" + R.raw.notifsound);
            }catch(Exception exp){}

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notify_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(launchIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            if(sound!=null)
                builder=builder.setSound(sound);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }

}
