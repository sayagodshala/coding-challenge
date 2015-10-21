package com.sayagodshala.dingdong.activities;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.MobileNoVerificationFragment;
import com.sayagodshala.dingdong.fragments.MobileNoVerificationFragment_;
import com.sayagodshala.dingdong.model.Customer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class MobileNoVerificationActivity extends BaseActivity {

    @Extra
    Customer customer;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    private Context mContext;

    @AfterViews
    void init() {

        mContext = this;

        title.setText("");

        toolbar.setNavigationIcon(R.drawable.button_back1);

        Log.d("Verify1", new Gson().toJson(customer));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final MobileNoVerificationFragment fragment = MobileNoVerificationFragment_.builder().customer(customer).build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }

}
