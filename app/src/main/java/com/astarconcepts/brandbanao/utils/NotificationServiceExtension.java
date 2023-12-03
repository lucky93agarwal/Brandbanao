package com.astarconcepts.brandbanao.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.ui.activities.CustomSplashActivity;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

@SuppressWarnings("unused")
public class NotificationServiceExtension implements OneSignal.OSRemoteNotificationReceivedHandler {

    public static final String CHANNEL_ID = "brandbanao_channel";
    public static final int NOTIFICATION_ID = 1;
    public String catId = "";
    public String catType = "";
    public String catName = "";
    public String externalLink = "";
    public Context context;
    PreferenceManager preferenceManager;

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent osNotificationReceivedEvent) {
        this.context = context;
        preferenceManager = new PreferenceManager(context);

        OSNotification notification = osNotificationReceivedEvent.getNotification();
        Util.showLog("NOTIFICATION: " + notification.toString());

        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, notification.getTitle().length(), 0);
            builder.setContentTitle(spannableTitle);
            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE), 0, notification.getBody().length(), 0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        JSONObject data = notification.getAdditionalData();

        Util.showLog(data.toString());
        try {
            catType = data.getString("type");
            if (catType.contains(Constant.CATEGORY) || catType.contains(Constant.FESTIVAL)) {

                catId = data.getString("id");
                catName = data.getString("name");

            } else if (catType.contains(Constant.SUBS_PLAN)) {

                catId = data.getString("id");
                catName = data.getString("subscriptionPlan");

            } else if (catType.contains(Constant.EXTERNAL)) {
                externalLink = data.getString("external_link");
            }


            Log.i("onesignal error", "onesignal:: " + catId + catName);

        } catch (JSONException e) {
            Util.showErrorLog(e.getMessage(), e);
        }
        osNotificationReceivedEvent.complete(null);

        if (preferenceManager.getBoolean(Constant.NOTIFICATION_ENABLED)) {
            sendNotification(notification);
        }

    }

    private void sendNotification(OSNotification notification) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;

        preferenceManager.setBoolean(Constant.IS_NOT, true);
        preferenceManager.setString(Constant.PRF_ID, catId);
        preferenceManager.setString(Constant.PRF_NAME, catName);
        preferenceManager.setString(Constant.PRF_TYPE, catType);
        preferenceManager.setString(Constant.PRF_LINK, externalLink);
        preferenceManager.setBoolean(Constant.INTENT_VIDEO, false);

        intent = new Intent(context, CustomSplashActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, catType);
        intent.putExtra(Constant.INTENT_IS_FROM_NOTIFICATION, true);
        intent.putExtra(Constant.INTENT_FEST_ID, catId);
        intent.putExtra(Constant.INTENT_FEST_NAME, catName);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BrandBanao";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(uri)
                .setAutoCancel(true)
                .setLights(Color.RED, 800, 800)
                .setContentText(notification.getBody())
                .setChannelId(CHANNEL_ID);

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
        try {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "errror large- " + e.getMessage(), Toast.LENGTH_LONG).show();
            preferenceManager.setBoolean(Constant.IS_NOT, false);
        }

        if (notification.getTitle().trim().isEmpty()) {
            mBuilder.setContentTitle(context.getString(R.string.app_name));
            mBuilder.setTicker(context.getString(R.string.app_name));
        } else {
            mBuilder.setContentTitle(notification.getTitle());
            mBuilder.setTicker(notification.getTitle());
        }

        if (notification.getBigPicture() != null) {
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(MyUtils.getBitmapFromURLImage(notification.getBigPicture())));
        }

        mBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
            return R.mipmap.ic_launcher;
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    private int getColour() {
        return 0x8b5630;
    }


}
