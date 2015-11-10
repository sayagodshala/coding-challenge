package com.sayagodshala.livesplash.activities;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sayagodshala.livesplash.BaseActivity;
import com.sayagodshala.livesplash.Fragments;
import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.fragments.category.CategorySlidingTabsFragment;
import com.sayagodshala.livesplash.model.Feed;
import com.sayagodshala.livesplash.network.APIClient;
import com.sayagodshala.livesplash.network.APIResponse;
import com.sayagodshala.livesplash.network.APIService;
import com.sayagodshala.livesplash.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by sayagodshala on 5/26/2015.
 */

@EActivity(R.layout.activity_category_navigator)
public class CategoryNavigatorActivity extends BaseActivity {

    APIService apiService;

    private static final String CATEGORY_ID = "categoryId";
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;
    private Toast toast;

    @AfterViews
    void init() {

        apiService = APIClient.getAPIService();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CategorySlidingTabsFragment fragment = new CategorySlidingTabsFragment();
        Fragments.loadContentFragment(this, R.id.fragment_tabs, fragment);

        title.setText("Live Splash");

        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Util.getData(this) == null) {
            showLoader();
            loadData();
        }
    }

    private void loadData() {
        Call<APIResponse<Feed>> callBack = apiService.loadData();
        callBack.enqueue(new Callback<APIResponse<Feed>>() {
            @Override
            public void onResponse(Response<APIResponse<Feed>> response) {
                hideLoader();

                Log.d("Feed Data Loaded", new Gson().toJson(response.body()));

                Util.saveData(CategoryNavigatorActivity.this, new Gson().toJson(response.body().getFeed()));
                Util.intentCreateToast(CategoryNavigatorActivity.this, toast, "Data Loaded", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.intentCreateToast(CategoryNavigatorActivity.this, toast, "Error while loading data", Toast.LENGTH_SHORT);
            }
        });

    }

}