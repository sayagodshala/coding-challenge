package com.sayagodshala.livesplash.fragments;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.sayagodshala.livesplash.BaseFragment;
import com.sayagodshala.livesplash.LocationClient;
import com.sayagodshala.livesplash.R;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements LocationClient.LocationClientListener {


    @Override
    public void onLocationChanged(Location location) {

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
}
