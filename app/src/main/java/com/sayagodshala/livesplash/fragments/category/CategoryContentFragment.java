/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sayagodshala.livesplash.fragments.category;

import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayagodshala.livesplash.BaseFragment;
import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.adapters.AppListAdapter;
import com.sayagodshala.livesplash.model.Feed;
import com.sayagodshala.livesplash.util.Util;


public class CategoryContentFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String KEY_TITLE = "title";
    private static final String CATEGORY_ID = "categoryId";

    private int categoryId, RemoveViewPos;

    private Context context;

    private boolean isScrollFirst;
    private View rootView;

    private GoogleMap googleMap;

    private ListView list_apps;

    private Toast toast;

    private boolean isAnotherViewOpen = false;
    private int index_of_item = 0;

    private ScrollView container1;
    private RelativeLayout container2;
    private LinearLayout section1;
    private LinearLayout section2;

    private String[] numbers = {"One", "Two", "Three"};
    private String[] fruits = {"Banana", "Mango", "Apple"};

    LayoutInflater layoutInflater;

    private AppListAdapter appListAdapter;

    public static CategoryContentFragment newInstance(CharSequence title, int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(CATEGORY_ID, categoryId);
        CategoryContentFragment fragment = new CategoryContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.categoy_fragment1, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        Bundle args = getArguments();
        if (args != null) {
            categoryId = args.getInt(CATEGORY_ID);
        }

        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initializeViews();
        defaultConfigs();

        if (categoryId == 0) {
            initContainer1();
        } else if (categoryId == 1) {
            initContainer3();
        } else if (categoryId == 2) {
            initContainer2();
        }

    }


    private void initializeViews() {
        container1 = Util.genericView(rootView, R.id.container1);
        container2 = Util.genericView(rootView, R.id.container2);
        section1 = Util.genericView(rootView, R.id.section1);
        section2 = Util.genericView(rootView, R.id.section2);
        list_apps = Util.genericView(rootView, R.id.list_apps);
    }

    private void defaultConfigs() {
        container1.setVisibility(View.GONE);
        container2.setVisibility(View.GONE);
        list_apps.setVisibility(View.GONE);

        if (section1.getChildCount() > 0)
            section1.removeAllViews();

        if (section2.getChildCount() > 0)
            section2.removeAllViews();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initContainer1() {
        container1.setVisibility(View.VISIBLE);
        for (int i = 0; i < 4; i++) {
            View itemView = layoutInflater.inflate(R.layout.list_item1,
                    section1, false);
            TextView title = Util.genericView(itemView, R.id.title);
            TextView subtitle = Util.genericView(itemView, R.id.subtitle);
            title.setText(numbers[randomNum() - 1] + " " + fruits[randomNum() - 1]);
            subtitle.setText("Cell Number " + (i + 1));
            section1.addView(itemView);
        }

        for (int i = 0; i < 3; i++) {
            View itemView = layoutInflater.inflate(R.layout.list_item1,
                    section2, false);
            TextView title = Util.genericView(itemView, R.id.title);
            TextView subtitle = Util.genericView(itemView, R.id.subtitle);
            title.setText(numbers[randomNum() - 1] + " " + fruits[randomNum() - 1]);
            subtitle.setText("Cell Number " + (i + 1));
            section2.addView(itemView);
        }
    }

    private int randomNum() {
        return 1 + (int) (Math.random() * ((3 - 1) + 1));
    }

    private void initContainer2() {
        container2.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initContainer3() {
        list_apps.setVisibility(View.VISIBLE);

        Feed feed = Util.getData(getActivity());

        if (feed != null) {
            appListAdapter = new AppListAdapter(getActivity(), feed.getEntry());
            list_apps.setAdapter(appListAdapter);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(false);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        setMarker();
    }

    private void setMarker() {
        MarkerOptions options;
        String latLng = Util.getLatLng(getActivity());

        if (!latLng.equalsIgnoreCase("")) {
            if (!latLng.equalsIgnoreCase("0,0") || !latLng.equalsIgnoreCase(",")) {
                LatLng position = new LatLng(Double.parseDouble(latLng.split(",")[0]), Double.parseDouble(latLng.split(",")[1]));
                options = new MarkerOptions();
                options.position(position);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_blue));
                googleMap.addMarker(options);

                CameraUpdate center = CameraUpdateFactory.newLatLng(position);
                googleMap.moveCamera(center);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                googleMap.animateCamera(zoom);
                googleMap.setOnMarkerClickListener(this);
            }
        }


    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        

        return false;
    }
}