package com.sayagodshala.dingdong.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.LocationClient;
import com.sayagodshala.dingdong.MainActivity_;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.AuthActivity_;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoredWhenDetached;
import org.androidannotations.annotations.UiThread;


/**
 * Created by sayagodshala on 8/7/2015.
 */


@EFragment(R.layout.fragment_splash)
public class SplashFragment extends BaseFragment implements LocationClient.LocationClientListener {

    private LocationClient locationClient;

    @AfterViews
    void init() {
        Context context = getActivity();
        Log.d("ID-SHA1", Util.getCertificateSHA1Fingerprint(context));
        Log.d("ID-KEYHASH", Util.getKeyHash(context));

        Util.clearSpecialPreferences(getActivity());
        Util.registerDevice(getActivity());

        locationClient = new LocationClient(getActivity(), this);

        waitOnSplashFor(3 * 1000);

    }

    @Background
    void waitOnSplashFor(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashWaitCompleted();
    }

    @IgnoredWhenDetached
    @UiThread
    void splashWaitCompleted() {
        navigateHome();
    }

    @IgnoredWhenDetached
    public void navigateHome() {

        if (Util.isUserLoggedIn(getActivity())) {
            MainActivity_.intent(this).start();
        } else {
            AuthActivity_.intent(this).start();
        }
        getActivity().finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            if (locationClient != null)
                locationClient.disconnect();
            locationClient = null;

            Util.saveLatLng(getActivity(), location.getLatitude() + "," + location.getLongitude());

        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }

        Log.d("Fragment", "onpause");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment", "onresume");
        if (locationClient != null) {
            locationClient.connect();
        }
    }
}
