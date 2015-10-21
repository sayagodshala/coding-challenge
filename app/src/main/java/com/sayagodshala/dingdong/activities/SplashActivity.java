package com.sayagodshala.dingdong.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.SplashFragment;
import com.sayagodshala.dingdong.fragments.SplashFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class SplashActivity extends BaseActivity {
    
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void init() {
        toolbar.setVisibility(View.GONE);
        requestPermission();
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            final SplashFragment splashFragment = SplashFragment_.builder().build();
            Fragments.loadContentFragment(this, R.id.container, splashFragment);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (permissions.length > 0) {
            Log.d("Permissions", permissions[0] + " : " + grantResults[0] + MY_PERMISSION_FINE_LOCATION);
        } else {
            Log.d("Permissions", "--------------" + MY_PERMISSION_FINE_LOCATION);
        }


        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    final SplashFragment splashFragment = SplashFragment_.builder().build();
                    Fragments.loadContentFragment(this, R.id.container, splashFragment);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
