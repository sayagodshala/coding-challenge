package com.sayagodshala.dingdong.activities;

import com.sayagodshala.dingdong.BaseActivity;
import com.sayagodshala.dingdong.Fragments;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.fragments.PickLocationFragment;
import com.sayagodshala.dingdong.fragments.PickLocationFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_pick_location)
public class PickLocationActivity extends BaseActivity {

    @Extra
    String purpose;

    @AfterViews
    void init() {
        final PickLocationFragment fragment = PickLocationFragment_.builder().purpose(purpose).build();
        Fragments.loadContentFragment(this, R.id.container, fragment);
    }

}
