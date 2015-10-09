package com.sayagodshala.dingdong.activities;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.ChooseAddressFragment;
import com.sayagodshala.dingdong.fragments.ChooseAddressFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class ChooseAddressActivity extends BaseActivity {

    private static final int LOCATION_REQUEST = 1001;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @AfterViews
    void init() {
        title.setText("Choose Address");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.inflateMenu(R.menu.choose_address);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_address:
                        UpdateCustomerAddressActivity_.intent(ChooseAddressActivity.this).address(null).startForResult(LOCATION_REQUEST);
                        return true;
                }
                return false;
            }
        });

        final ChooseAddressFragment fragment = ChooseAddressFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }

}
