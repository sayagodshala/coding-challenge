
package com.sayagodshala.dingdong;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
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


}