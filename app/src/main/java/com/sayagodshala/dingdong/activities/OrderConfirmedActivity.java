package com.sayagodshala.dingdong.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.MainActivity_;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.OrderConfirmedFragment;
import com.sayagodshala.dingdong.fragments.OrderConfirmedFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class OrderConfirmedActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    private Context mContext;

    @AfterViews
    void init() {
        mContext = this;
        title.setText("Order Confirmed");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            }
        });

        final OrderConfirmedFragment fragment = OrderConfirmedFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }

}
