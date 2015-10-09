package com.sayagodshala.dingdong;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.util.Util;

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
//        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        FlurryAgent.onStartSession(this);
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


}
