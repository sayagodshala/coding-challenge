package com.sayagodshala.livesplash.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sayagodshala.livesplash.model.Customer;
import com.sayagodshala.livesplash.model.Feed;
import com.sayagodshala.livesplash.settings.AppSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

public class Util {


    public static String capWord(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0))
                    + words[0].subSequence(1, words[0].length()).toString()
                    .toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0))
                        + words[i].subSequence(1, words[i].length()).toString()
                        .toLowerCase());
            }
        }

        return sb.toString();

    }


    public static void intentRateUs(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + activity.getPackageName())));
        }
    }

    public static void intentShareApp(Activity activity) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Ding Dong - The best online on-demand condom delivery app. Download at " + Uri.parse("http://play.google.com/store/apps/details?id="
                        + activity.getPackageName()));
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);

    }

    public static void intentSendFeedBack(Activity activity, String[] emailIds,
                                          String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, emailIds);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        activity.startActivity(Intent.createChooser(intent, ""));
    }

    public static void intentCreateToast(Activity activity, Toast toast,
                                         String message, int duration) {

        if (activity == null)
            return;


        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(activity, message, duration);
        toast.show();

    }


    public static <AnyView extends View> AnyView genericView(View view, int id) {
        return (AnyView) view.findViewById(id);
    }

    public static <AnyView extends View> AnyView genericView(Activity activity, int id) {
        return (AnyView) activity.findViewById(id);
    }


    public static void setOnClickListener(Context act, View[] views) {

        for (int i = 0; i < views.length; i++) {

            views[i].setOnClickListener((View.OnClickListener) act);

        }
    }

    public static JSONObject parseFacebookUserData(JSONObject values) {

        JSONObject userObject = new JSONObject();

        try {

            userObject.put("firstName", values.getString("first_name"));
            userObject.put("lastName", values.getString("last_name"));
            userObject.put("gender", values.getString("gender"));
            userObject.put("email", values.getString("email"));
            userObject.put("facebookID", values.getString("id"));

            userObject.put("pictureURL", values.getJSONObject("picture")
                    .getJSONObject("data").getString("url"));
            userObject.put("coverURL",
                    values.getJSONObject("cover").getString("source"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userObject;
    }

    public static JSONObject parseUserInfoData(JSONObject values, String type) {

        JSONObject userObject = new JSONObject();

        try {

            if (type.equalsIgnoreCase("facebook")) {
                userObject.put("firstName", values.getString("first_name"));
                if (!values.isNull("last_name")) {
                    if (!values.getString("last_name").equalsIgnoreCase("")) {
                        userObject.put("lastName", values.getString("last_name"));
                    } else {
                        userObject.put("lastName", values.getString(""));
                    }
                }

                userObject.put("email", values.getString("email"));

                if (!values.isNull("birthday")) {
                    userObject.put("dateOfBirth", values.getString("birthday"));
                }

                if (!values.isNull("gender")) {
                    if (!values.getString("gender").equalsIgnoreCase("")) {
                        userObject.put("gender", values.getString("gender"));
                    } else {
                        userObject.put("gender", values.getString(""));
                    }
                }

                userObject.put("pictureURL", values.getJSONObject("picture")
                        .getJSONObject("data").getString("url"));
            } else {

                JSONObject user = values.getJSONObject("user");

                userObject.put("name", user.getString("name"));

                if (!user.isNull("gender")) {
                    userObject.put("gender", user.getString("gender"));
                }

                if (!user.isNull("pictureURL")) {
                    userObject.put("pictureURL", user.getString("pictureURL"));
                }

                if (!user.isNull("gender")) {
                    userObject.put("gender", user.getString("gender"));
                }

                if (!user.isNull("dateOfBirth")) {
                    userObject.put("dateOfBirth", user.getString("dateOfBirth"));
                }

                if (!user.isNull("sessionId")) {
                    userObject.put("sessionId", user.getString("sessionId"));
                }

                if (!user.isNull("radius")) {
                    userObject.put("radius", user.getString("radius"));
                }

                userObject.put("userId", user.getString("userId"));

            }
            Log.d("User Data", userObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userObject;
    }


    public static String getFormattedTime(String value) {
        String[] seperateTime = value.split(":");

        String getHoursStr = seperateTime[0];
        String getMinutesStr = seperateTime[1];

        String ampm = "";

        int hours = 0;
        int h = 0;

        if (getHoursStr.equalsIgnoreCase("00")) {
            h = 0;
        } else if (String.valueOf(getHoursStr.charAt(0)).equalsIgnoreCase("0")) {
            h = Integer.parseInt(String.valueOf(getHoursStr.charAt(0)));
        } else {
            h = Integer.parseInt(getHoursStr);
        }

        if (getMinutesStr.equalsIgnoreCase("0")) {
            getMinutesStr = "00";
        }

        if (getMinutesStr.length() == 1) {
            getMinutesStr = "0" + getMinutesStr;
        }

        if (h > 12) {
            hours = h - 12;
            ampm = "am";
        } else if (h == 0) {
            ampm = "pm";
            hours = 12;
        } else if (h < 12) {
            ampm = "pm";
            hours = h;
        } else if (h == 12) {
            ampm = "am";
            hours = h;
        }

        if (String.valueOf(hours).length() == 1) {

            return "0" + hours + ":" + getMinutesStr + " " + ampm;

        }

        return hours + ":" + getMinutesStr + " " + ampm;

    }

    public static String getFormattedTime1(String value) {
        String[] seperateTime = value.split(":");

        String getHoursStr = seperateTime[0];
        String getMinutesStr = seperateTime[1];

        String ampm = "";

        int hours = 0;
        int h = 0;

        if (getHoursStr.equalsIgnoreCase("00")) {
            h = 0;
        } else if (String.valueOf(getHoursStr.charAt(0)).equalsIgnoreCase("0")) {
            h = Integer.parseInt(String.valueOf(getHoursStr.charAt(0)));
        } else {
            h = Integer.parseInt(getHoursStr);
        }

        if (getMinutesStr.equalsIgnoreCase("0")) {
            getMinutesStr = "00";
        }

        if (getMinutesStr.length() == 1) {
            getMinutesStr = "0" + getMinutesStr;
        }

        if (h > 12) {
            hours = h - 12;
            ampm = "pm";
        } else if (h == 0) {
            ampm = "am";
            hours = 12;
        } else if (h < 12) {
            ampm = "am";
            hours = h;
        } else if (h == 12) {
            ampm = "pm";
            hours = h;
        }

        if (String.valueOf(hours).length() == 1) {

            return "0" + hours + ":" + getMinutesStr + " " + ampm;

        }

        return hours + ":" + getMinutesStr + " " + ampm;

    }

    public static void clearApplicationData(Context context) {
        AppSettings.clearAllPrefs(context);

        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG",
                            "**************** File /data/data/APP_PACKAGE/" + s
                                    + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void setTouchDeligate(final View touchview, final View rootview, final int left, final int right, final int top, final int bottom) {

        rootview.post(new Runnable() {
            public void run() {
                Rect delegateArea = new Rect();
                touchview.getHitRect(delegateArea);
                delegateArea.top += top;
                delegateArea.bottom += bottom;
                delegateArea.left += left;
                delegateArea.right += right;
                TouchDelegate expandedArea = new TouchDelegate(delegateArea,
                        touchview);

                if (View.class.isInstance(touchview.getParent())) {
                    ((View) touchview.getParent())
                            .setTouchDelegate(expandedArea);
                }
            }

            ;
        });
    }

    public static Customer getUserData(Context context) {
        Customer customer = null;
        String data = AppSettings.getValue(context, AppSettings.PREF_USER_DATA, "");
        if (!data.equalsIgnoreCase("")) {
            customer = new Gson().fromJson(data, Customer.class);
        }
        return customer;
    }

    public static void setUserData(Context context, String customer) {
        AppSettings.setValue(context, AppSettings.PREF_USER_DATA, customer);
    }

    public static void setMapMarker(GoogleMap googleMap, LatLng latLng, int id) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        options.icon(BitmapDescriptorFactory.fromResource(id));
        googleMap.addMarker(options);
    }

    public static String getMonth(int index) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[index];
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }

    }

    public static boolean textIsEmpty(String value) {
        boolean empty = false;

        String message = value.trim();

        if (TextUtils.isEmpty(message)) {
            empty = true;
        }

        boolean isWhitespace = message.matches("^\\s*$");

        if (isWhitespace) {
            empty = true;
        }

        return empty;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

//    public static Cloudinary cloudinaryInfo() {
//        HashMap config = new HashMap();
////        config.put("cloud_name", "sayagodshala");
////        config.put("api_key", "619779554471885");
////        config.put("api_secret", "4z5ddHn3nXJdxthNFEkVOxeYndk");
//        config.put("cloud_name", "dkiqsfnjt");
//        config.put("api_key", "897165184178136");
//        config.put("api_secret", "lOfU0fk6GzdlUhuCmBQqCg-eB-E");
//        Cloudinary cloudinary = new Cloudinary(config);
//        return cloudinary;
//    }
//
//    public static void saveLatLng(Context context, String latLng) {
//        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG, latLng);
//    }
//
//    public static String getLatLng(Context context) {
//        return AppSettings.getValue(context, AppSettings.PREF_LAT_LNG, "");
//    }
//
//    public static int getDifferenceDays(Date startDate, Date endDate) {
//        int daysdiff = 0;
//        long diff = endDate.getTime() - startDate.getTime();
//        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
//        daysdiff = (int) diffDays;
//        return daysdiff;
//    }
//
//    public static void openDirections(Context context, PropertyDetails propertyDetails) {
//
//        String latLng = Util.getLatLng(context);
//
//        if (!propertyDetails.getLatitude().equalsIgnoreCase("") && !propertyDetails.getLongitude().equalsIgnoreCase("")) {
//            String geoUriString = "http://maps.google.com/maps?" +
//                    "saddr=" + latLng + "&daddr=" + propertyDetails.getLatitude() + "," + propertyDetails.getLongitude();
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
//            context.startActivity(intent);
//        } else {
//
//            new SimpleAlertDialog(context, "Can't navigate to the location as Latitide and Longitude not mentioned for this project.", "", "Ok", new SimpleAlertDialog.SimpleAlertDialogListener() {
//                @Override
//                public void onButton1Action() {
//
//                }
//
//                @Override
//                public void onButton2Action() {
//
//                }
//            }).show();
//
//        }
//    }


    public static boolean checkConnection(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            System.out.println("true wifi");
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            System.out.println("true edge");
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            System.out.println("true net");
            return true;
        }
        System.out.println("false");
        return false;
    }

    public static boolean checkLocation(Context context) {
        final LocationManager manager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
////            if (noLocationDialog == null) {
////                noLocationDialog = Util.buildAlertDialogNoGps(getActivity());
////            }
////            noLocationDialog.show();
//        }
    }


    public static String getKeyHash(Context context) {

        String keyHash = "";

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return keyHash;
    }

    public static String getCertificateSHA1Fingerprint(Context context) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static void saveLatLng(Context context, String latLng) {
        if (context == null)
            return;

        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG, latLng);
        Log.d("Location Tracked", latLng);
    }


    public static void saveLatLngAddress(Context context, String address) {

        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG_ADDRESS, address);
    }

    public static com.sayagodshala.livesplash.model.Address getLatLngAddress(Context context) {
        com.sayagodshala.livesplash.model.Address myAddress = null;
        String raw = AppSettings.getValue(context, AppSettings.PREF_LAT_LNG_ADDRESS, "");
        if (!raw.equalsIgnoreCase("")) {
            myAddress = new Gson().fromJson(raw, com.sayagodshala.livesplash.model.Address.class);
        }
        return myAddress;
    }

    public static String getLatLng(Context context) {
        String location = AppSettings.getValue(context, AppSettings.PREF_LAT_LNG, "");
        Log.d("Location Tracked", location);
        return location;
    }


    public static void clearSpecialPreferences(Context context) {
        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG, "");
        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG_ADDRESS, "");
    }


    public static String getGcmToken(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_GCM_REGISTRATION_ID, "");
    }

    public static String uniqueDeviceID(Context context) {

        int hasPhoneStatePerm = -1;

        UUID deviceUuid;
        String tmDevice = "", tmSerial = "", androidId = "";
        String deviceIdStr = "";

        TelephonyManager telephonyManager = null;

        PackageManager packageManager = context.getPackageManager();

        hasPhoneStatePerm = packageManager.checkPermission(
                Manifest.permission.READ_PHONE_STATE,
                context.getPackageName());
        if (hasPhoneStatePerm == PackageManager.PERMISSION_GRANTED) {
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }

        try {
            if (telephonyManager != null) {
                tmDevice = "" + telephonyManager.getDeviceId();
                tmSerial = "" + telephonyManager.getSimSerialNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (androidId == null) {
            androidId = "";
        }


        String build = "";
        if (android.os.Build.SERIAL != null) {
            build = android.os.Build.SERIAL;
        }

        try {
            if (!tmDevice.equalsIgnoreCase("") && !tmSerial.equalsIgnoreCase("")) {
                deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                deviceIdStr = deviceUuid.toString();
            } else {
                deviceUuid = new UUID(androidId.hashCode(), build.hashCode());
                deviceIdStr = deviceUuid.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceIdStr;
    }


    public static JSONArray concatJSONArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

    public static Dialog buildAlertDialogNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Location is disabled, enable it?").setCancelable(false).
                setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        context.startActivity(
                                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        return builder.create();
    }

    public static String getDay(int index) {
        String day = "";
        try {
            String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            day = days[index - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }


    public static void saveData(Context context, String data) {
        AppSettings.setValue(context, AppSettings.PREF_DATA, data);
    }

    public static Feed getData(Context context) {
        Feed feed = null;
        String raw = AppSettings.getValue(context, AppSettings.PREF_DATA, "");
        if (!raw.equalsIgnoreCase("")) {
            Log.d("Feed Data", raw);
            try {
                feed = new Gson().fromJson(raw, Feed.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return feed;
    }


}
