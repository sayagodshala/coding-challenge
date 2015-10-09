package com.sayagodshala.dingdong.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.UpdateCustomerAddressFragment;
import com.sayagodshala.dingdong.fragments.UpdateCustomerAddressFragment_;
import com.sayagodshala.dingdong.model.Address;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class UpdateCustomerAddressActivity extends BaseActivity {

    @Extra
    Address address;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @AfterViews
    void init() {

        if (address != null) {
            title.setText("Update Address");
        } else {
            title.setText("Add Address");
        }

        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final UpdateCustomerAddressFragment fragment = UpdateCustomerAddressFragment_.builder().address(address).build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }

}
