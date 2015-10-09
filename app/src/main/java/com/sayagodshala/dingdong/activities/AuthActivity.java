package com.sayagodshala.dingdong.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.AuthFragment;
import com.sayagodshala.dingdong.fragments.AuthFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_container)
public class AuthActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void init() {

        toolbar.setVisibility(View.GONE);

        final AuthFragment fragment = AuthFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }


}
