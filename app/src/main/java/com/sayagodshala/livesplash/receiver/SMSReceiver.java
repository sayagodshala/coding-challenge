
package com.sayagodshala.livesplash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    private boolean once = true;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        Log.d("MSG RCVD", "Recived");

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d("MSG RCVD", senderNum + " , " + message);

                    if (senderNum.toLowerCase().contains("didong")) {

                        if (once) {
                            once = false;
                            Intent in = new Intent("SmsMessage.intent.MAIN");
                            in.putExtra("getMessage", message);
                            in.putExtra("getSenderName", message);
                            context.sendBroadcast(in);
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }
}