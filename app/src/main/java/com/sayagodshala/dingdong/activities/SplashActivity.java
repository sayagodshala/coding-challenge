package com.sayagodshala.dingdong.activities;

import android.view.View;
import android.support.v7.widget.Toolbar;

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

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void init() {

        toolbar.setVisibility(View.GONE);

        final SplashFragment splashFragment = SplashFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, splashFragment);
    }


}
