
package com.sayagodshala.dingdong;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.sayagodshala.dingdong.activities.SplashActivity_;
import com.sayagodshala.dingdong.notification.DDNotification;
import com.sayagodshala.dingdong.settings.AppSettings;
import com.sayagodshala.dingdong.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;


public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(Constants.GCM_SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.d("Device", "Device registered: regId = " + registrationId);
        AppSettings.setValue(context, AppSettings.PREF_GCM_REGISTRATION_ID, registrationId);
        AppSettings.setValue(context, AppSettings.PREF_GCM_REGISTRATION_STATUS, "true");
    }


    @Override
    protected void onMessage(Context context, Intent intent) {
        try {
            Log.d("Message Recieved : ", extrasToJSon(intent).toString());
            DDNotification ddNotification = new Gson().fromJson(extrasToJSon(intent).toString(), DDNotification.class);
            notifyMe(ddNotification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.d("Device", "unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
        } else {
            Log.d("Device", "Ignoring unregister callback");
        }
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d("Device", "Received error: " + errorId);
    }

    private static void generateNotification(Context context, String userid,
                                             String title, String mess) {

    }


    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;

    }


    private JSONObject extrasToJSon(Intent intent) {

        JSONObject json = new JSONObject();
        Set<String> keys = intent.getExtras().keySet();

        for (String key : keys) {
            try {
                json.put(key, intent.getExtras().get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }


    private void notifyMe(DDNotification box8Notification) {

        switch (box8Notification.getType()) {
            case DDNotification.TRANSACTIONAL_ORDER_PLACED:
            case DDNotification.TRANSACTIONAL_ORDER_DISPATCHED:
            case DDNotification.TRANSACTIONAL_ORDER_DELIVERED:
                transactional(box8Notification);
                break;
            case DDNotification.UPDATE:
                break;


        }
    }

    private void transactional(DDNotification ddNotification) {
        Intent notificationIntent;
        PendingIntent pendingIntent;

        Log.v("TAG", " When " + ddNotification.getTime());
        long when = 001;

        String message = ddNotification.getMessage();
        String title = ddNotification.getTitle();


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        notificationBuilder.setTicker(" : " + message);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setWhen(ddNotification.getTime() * 1000l);
        notificationBuilder.setSmallIcon(R.drawable.icon);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(
                Notification.DEFAULT_LIGHTS
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_SOUND);
        notificationBuilder.setStyle(
                new NotificationCompat.BigTextStyle().bigText(message));
        
        notificationIntent = new Intent(this, SplashActivity_.class);
//        notificationIntent.putExtra("notification_messsage", box8Notification.getMessage());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity_.class);
        stackBuilder.addNextIntent(notificationIntent);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify((int) when, notificationBuilder.build());
    }


}