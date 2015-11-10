package com.sayagodshala.livesplash.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.sayagodshala.livesplash.BaseFragment;
import com.sayagodshala.livesplash.LocationClient;
import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.activities.CategoryNavigatorActivity_;
import com.sayagodshala.livesplash.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


/**
 * Created by sayagodshala on 8/7/2015.
 */

@EFragment(R.layout.fragment_splash)
public class SplashFragment extends BaseFragment implements LocationClient.LocationClientListener {

    private static final int MY_PERMISSION_FINE_LOCATION = 111;
    private LocationClient locationClient;
    private Toast toast;


    @AfterViews
    void init() {
        Context context = getActivity();
        Log.d("ID-SHA1", Util.getCertificateSHA1Fingerprint(context));
        Log.d("ID-KEYHASH", Util.getKeyHash(context));

        Util.clearSpecialPreferences(getActivity());


        showLoader();
        locationClient = new LocationClient(getActivity(), this);

    }


    @Override
    public void onLocationChanged(Location location) {
        hideLoader();
        if (location != null) {
            if (locationClient != null)
                locationClient.disconnect();
            locationClient = null;
            Util.saveLatLng(getActivity(), location.getLatitude() + "," + location.getLongitude());
            CategoryNavigatorActivity_.intent(this).start();
            getActivity().finish();
        } else {
            Util.intentCreateToast(getActivity(), toast, "Cannot fetch location", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        hideLoader();
        Util.intentCreateToast(getActivity(), toast, "Cannot fetch location", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        hideLoader();
        Util.intentCreateToast(getActivity(), toast, "Cannot fetch location", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationClient != null) {
            locationClient.connect();
        }
    }


}
