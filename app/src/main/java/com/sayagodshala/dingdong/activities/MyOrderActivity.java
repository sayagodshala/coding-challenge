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
import com.sayagodshala.dingdong.fragments.MyOrderFragment;
import com.sayagodshala.dingdong.fragments.MyOrderFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class MyOrderActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;
    private Context mContext;

    @AfterViews
    void init() {
        mContext = this;
        title.setText("My Orders");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            }
        });

        final MyOrderFragment fragment = MyOrderFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

}
