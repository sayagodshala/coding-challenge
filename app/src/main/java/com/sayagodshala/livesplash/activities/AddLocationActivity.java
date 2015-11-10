package com.sayagodshala.livesplash.activities;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sayagodshala.livesplash.BaseActivity;
import com.sayagodshala.livesplash.Fragments;
import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.fragments.AddLocationFragment;
import com.sayagodshala.livesplash.fragments.AddLocationFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sayagodshala on 5/26/2015.
 */

@EActivity(R.layout.activity_container)
public class AddLocationActivity extends BaseActivity {


    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @AfterViews
    void init() {

        title.setText("Add Location");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final AddLocationFragment fragment = AddLocationFragment_.builder().build();
        Fragments.loadContentFragment(this, R.id.container, fragment);

    }


}