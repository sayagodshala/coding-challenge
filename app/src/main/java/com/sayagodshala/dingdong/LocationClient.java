package com.sayagodshala.dingdong;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by sayagodshala on 5/25/2015.
 */

public class LocationClient implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public interface LocationClientListener {

        public void onLocationChanged(Location location);

        public void onConnected(Bundle bundle);

        public void onConnectionSuspended(int i);

        public void onConnectionFailed(ConnectionResult connectionResult);

    }

    private static LocationClient instance = null;

    private LocationClientListener mListener;

    private GoogleApiClient googleApiClient;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(8000)         // n seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    public LocationClient(Context context, LocationClientListener listener) {
        Log.d("LocationClient", "init");
        mListener = listener;
        googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    public static LocationClient init(Context context, LocationClientListener listener) {
        if (instance == null) {
            instance = new LocationClient(context, listener);
        }
        return instance;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null)
            return;
        mListener.onLocationChanged(location);
        Log.d("LocationClient", location.getLatitude() + " : " + location.getLongitude());
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, REQUEST, this);

        Log.d("LocationClient", "Location Connected");
        mListener.onConnected(bundle);

        if (LocationServices.FusedLocationApi.getLastLocation(googleApiClient) != null) {
            Log.d("LocationClient", "Last Location");
            mListener.onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        mListener.onConnectionSuspended(i);
        Log.d("LocationClient", "Location Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mListener.onConnectionFailed(connectionResult);
        Log.d("LocationClient", "Location Failed");
    }

    public void connect() {
        googleApiClient.connect();
        Log.d("LocationClient", "Location Connect");
    }

    public void disconnect() {
        googleApiClient.disconnect();
        Log.d("LocationClient", "Location Disconnect");
    }
}