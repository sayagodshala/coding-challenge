package com.sayagodshala.dingdong.activities;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.MyCartFragment;
import com.sayagodshala.dingdong.fragments.MyCartFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_my_cart)
public class MyCartActivity extends BaseActivity {

    @AfterViews
    void init() {
        final MyCartFragment fragment = MyCartFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);
    }

}
