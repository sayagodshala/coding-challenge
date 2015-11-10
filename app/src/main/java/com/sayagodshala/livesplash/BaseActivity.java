package com.sayagodshala.livesplash;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.sayagodshala.livesplash.model.Customer;
import com.sayagodshala.livesplash.util.Util;

public class BaseActivity extends ActionBarActivity {
    private AsyncLoader asyncLoader;

    public Customer customer;
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncLoader = AsyncLoader.dialog(this);

        customer = Util.getUserData(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showLoader() {
        try {
            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (customer == null)
            customer = Util.getUserData(this);

    }
}
