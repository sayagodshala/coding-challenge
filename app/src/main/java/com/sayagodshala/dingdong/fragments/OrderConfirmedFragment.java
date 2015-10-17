package com.sayagodshala.dingdong.fragments;

import android.widget.Toast;

import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.MyOrderActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_order_confirmed)
public class OrderConfirmedFragment extends BaseFragment {

    private Toast toast;

    void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Click(R.id.button_track_order)
    void trackOrder() {
        MyOrderActivity_.intent(this).start();
    }


}
