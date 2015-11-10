package com.sayagodshala.livesplash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayagodshala.livesplash.activities.SplashActivity_;
import com.sayagodshala.livesplash.fragments.HomeFragment;
import com.sayagodshala.livesplash.fragments.HomeFragment_;
import com.sayagodshala.livesplash.network.APIClient;
import com.sayagodshala.livesplash.network.APIService;
import com.sayagodshala.livesplash.settings.AppSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.main)
public class MainActivity extends BaseActivity {
    public static final String NAVIGATION_ITEM_TAG = "NAVIGATION_ITEM_TAG";
    private static final int LOCATION_REQUEST = 1001;

    @ViewById(R.id.drawer)
    DrawerLayout drawer;
    @ViewById(R.id.navigation)
    NavigationView navigation;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @ViewById(R.id.navigation_profile_image)
    ImageView image;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.email)
    TextView email;

    @ViewById(R.id.phone)
    TextView phone;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.login)
    TextView login;

    private MenuItem menuItem;

    @ViewById(R.id.container)
    FrameLayout container;

    private APIService apiService;

//    @ViewById(R.id.relative_cart)
//    RelativeLayout relative_cart;
//
//    @ViewById(R.id.text_total_items)
//    TextView text_total_items;

    private boolean isHomeFragmentActive = false;
    private Toast toast;

    private String whichFragment = "";

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

//        relative_cart.setVisibility(View.GONE);


        if (title.getText().toString().equalsIgnoreCase("ding dong")) {
            exitAppAlert();
        } else {
            whichFragment = "";
            toolbar.getMenu().clear();
            title.setText("Ding Dong");
            container.setVisibility(View.VISIBLE);
            homePresenter();

            for (int i = 0; i < navigation.getMenu().size(); i++) {

                MenuItem menuItem = navigation.getMenu().getItem(i);
                menuItem.setChecked(false);
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        menuItem.setChecked(true);
                        break;
                }

            }


        }


    }

    private BaseFragment currentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(NAVIGATION_ITEM_TAG);
    }

    private void exitAppAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Exit application?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @AfterViews
    void init() {
        apiService = APIClient.getAPIService();
//        relative_cart.setVisibility(View.GONE);

        setupNavigationView();
        homePresenter();
    }

    @AfterViews
    void initHeader() {
//        toolbar.inflateMenu(R.menu.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.navMenuClicked(view);
            }
        });
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem1) {
//                return MainActivity.this.navMenuItemClicked(menuItem1);
//            }
//        });
    }


    private boolean navMenuItemClicked(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.my_cart:
                break;
//            case R.id.logout:
//                logoutAlert();
//                break;
        }

        return false;
    }


    private void resetNavigateMenu() {
        for (int i = 0; i < navigation.getMenu().size(); i++) {
            navigation.getMenu().getItem(i).setChecked(false);
        }

        navigation.getMenu().getItem(navigation.getMenu().size() - 1).setChecked(false);

    }


    private void navMenuClicked(View view) {
        toggleDrawer();
    }

    private void setupNavigationView() {
        drawerToggle =
                new ActionBarDrawerToggle(this, drawer, null, R.string.app_name, R.string.app_name);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem1) {
                return MainActivity.this.navigationItemSelected(menuItem1);
            }
        });
    }

    private boolean navigationItemSelected(final MenuItem menuItem) {
        whichFragment = "";
        menuItem.setChecked(false);
        switchFragment(menuItem);
        toggleDrawer();
        return false;
    }

    private void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void switchFragment(final MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.home:
                toolbar.getMenu().clear();
                title.setText("Ding Dong");
                homePresenter();
                menuItem.setChecked(true);
                break;

        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void homePresenter() {
        isHomeFragmentActive = true;
        final HomeFragment homeFragment = HomeFragment_.builder().build();
//        new HomePresenter(homeFragment, ridesModel());
        Fragments.replaceContentFragment(this, container.getId(), homeFragment, NAVIGATION_ITEM_TAG);
    }


    private void blankPresenter() {
        Fragments.replaceContentFragment(this, R.id.container, new Fragment(), NAVIGATION_ITEM_TAG);
    }

    private void replaceFragment(final Fragment fragment) {
        Fragments.replaceContentFragment(this, container.getId(), fragment, NAVIGATION_ITEM_TAG);
    }


    private void clearAllUserDataAndFinish() {
        SharedPreferences settings = this.getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        SplashActivity_.intent(this).start();
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }




    @Override
    protected void onResume() {
        super.onResume();

    }


}
