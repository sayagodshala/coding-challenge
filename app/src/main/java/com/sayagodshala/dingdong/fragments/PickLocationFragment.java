package com.sayagodshala.dingdong.fragments;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.adapters.LocationSearchListAdapter;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.GooglePrediction;
import com.sayagodshala.dingdong.network.GoogleAPIClient;
import com.sayagodshala.dingdong.network.GoogleAPIResponse;
import com.sayagodshala.dingdong.network.GoogleAPIService;
import com.sayagodshala.dingdong.settings.AppSettings;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_pick_location)
public class PickLocationFragment extends BaseFragment implements LocationSearchListAdapter.LocationSearchListAdapterEventListener {

    @FragmentArg
    String purpose;

    GoogleAPIService googleAPIService;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.linear_sublocality)
    LinearLayout linear_sublocality;

    @ViewById(R.id.text_sublocality)
    TextView text_sublocality;

    private List<GooglePrediction> currentLocation;
    private GooglePrediction locationObject;

    @ViewById(R.id.edit_search)
    EditText edit_search;

    @ViewById(R.id.linear_location_check)
    LinearLayout linear_location_check;

    @ViewById(R.id.linear_no_results)
    LinearLayout linear_no_results;

    @ViewById(R.id.list_views)
    ListView list_views;

    private String currentLatLng;
    private LocationSearchListAdapter locationSearchListAdapter;


    private static final int ASYNC_GET_PLACES = 1001;
    private static final int ASYNC_GET_LAT_LNG_FROM_PLACE = 1002;

    private String subLocalityLatLng = "";

    private String temporaryLocation = "", sublocality_latlong;

    @ViewById(R.id.progress_spinner)
    ProgressWheel progress_spinner;
    private Toast toast;

    @AfterViews
    void init() {
        googleAPIService = GoogleAPIClient.getAPIService();
        linear_sublocality.setVisibility(View.GONE);
        progress_spinner.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        linear_location_check.setVisibility(View.GONE);
        linear_no_results.setVisibility(View.GONE);

        currentLatLng = Util.getLatLng(getActivity());
        currentLocation = new ArrayList<>();

        if (!currentLatLng.equalsIgnoreCase("")) {
            locationObject = new GooglePrediction();
            try {
//                locationObject.put("description", "Use my location");
//                locationObject.put("latLng", currentLatLng);

                locationObject.setDescription("Use my location");
                locationObject.setLatlng(currentLatLng);

                currentLocation.add(locationObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (purpose.equalsIgnoreCase("updateLandmark")) {
            edit_search.setHint("Enter Landmark");
        } else {
            edit_search.setHint("Enter Location");
        }

        drawSavedLocationList();

    }

    @TextChange(R.id.edit_search)
    void onLocationSearch(TextView tv, CharSequence text) {
        if (text.length() > 0) {
            linear_no_results.setVisibility(View.VISIBLE);
            getGooglePlaces();
        } else {
            progress_spinner.setVisibility(View.GONE);
            linear_no_results.setVisibility(View.INVISIBLE);
            drawSavedLocationList();
        }


    }

    @Click(R.id.linear_sublocality)
    void onSublocalityClicked() {
        if (linear_sublocality.getVisibility() == View.VISIBLE) {
            linear_sublocality.setVisibility(View.GONE);
            edit_search.setText("");
            edit_search.setHint("Enter Location");
            drawSavedLocationList();
        }
    }

    public void getGooglePlaces() {

        progress_spinner.setVisibility(View.VISIBLE);

        Call<GoogleAPIResponse<List<GooglePrediction>>> callBack = null;

        try {
            callBack = googleAPIService.getGooglePlacesByLocation("false", "en", Constants.GOOGLE_APP_API_BROWSER_KEY, URLEncoder.encode(edit_search.getText().toString(), "utf8"), currentLatLng, 3000, "country:in");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callBack.enqueue(new Callback<GoogleAPIResponse<List<GooglePrediction>>>() {
            @Override
            public void onResponse(Response<GoogleAPIResponse<List<GooglePrediction>>> response) {

                progress_spinner.setVisibility(View.GONE);
                Log.d("Retrofit Response", new Gson().toJson(response.body()));

                List<GooglePrediction> tempPredictions = new ArrayList<GooglePrediction>();
                List<GooglePrediction> predictions = new ArrayList<GooglePrediction>();

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        predictions = response.body().getPredictions();
                        if (predictions.size() > 0) {
                            if (!currentLatLng.equalsIgnoreCase("")) {
                                tempPredictions = new ArrayList<GooglePrediction>();
                                tempPredictions.add(locationObject);
                                for (int i = 0; i < predictions.size(); i++) {
                                    tempPredictions.add(predictions.get(i));
                                }
                            }
                        }


                    }

                    drawSearchedLocationList(tempPredictions);


//                    JSONArray temparray = new JSONArray();
//
//                    try {
//                        if (googleResponse.has("status")) {
//                            if (googleResponse.getString("status").equalsIgnoreCase("ok") || googleResponse.getString("status").equalsIgnoreCase("zero_results")) {
//
//                                temparray = googleResponse.getJSONArray("predictions");
//                                if (!currentLatLng.isEmpty()) {
//                                    temparray = new JSONArray();
//                                    temparray.put(0, locationObject);
//                                    if (googleResponse.getJSONArray("predictions") != null) {
//                                        for (int i = 0; i < googleResponse.getJSONArray("predictions").length(); i++) {
//                                            temparray.put(googleResponse.getJSONArray("predictions").getJSONObject(i));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        drawSearchedLocationList(temparray);
//
//                    } catch (JSONException e) {
//                        drawSearchedLocationList(new JSONArray());
//                        e.printStackTrace();
//                    }
                } else {
                    drawSearchedLocationList(predictions);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progress_spinner.setVisibility(View.GONE);
                Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });
    }

    private void drawSearchedLocationList(List<GooglePrediction> values) {
        if (values != null && values.size() == 0)
            linear_no_results.setVisibility(View.VISIBLE);
        else {
            linear_no_results.setVisibility(View.GONE);
        }
        locationSearchListAdapter = new LocationSearchListAdapter(getActivity(), values, this);
        list_views.setAdapter(locationSearchListAdapter);
    }


//    @Override
//    public void onLocationSearchedClicked(JSONObject item) {
//        try {
//            if (item.getString("description").equalsIgnoreCase("use my location")) {
////                AppSettings.setValue(getActivity(), AppSettings.PREF_LAT_LNG_ADDRESS, "");
////                AppSettings.setValue(getActivity(), AppSettings.PREF_LAT_LNG, "");
////                onBackPressed();
//            } else {
//                if (item.has("type")) {
//                    if (item.getString("type").equalsIgnoreCase("saved")) {
//                        Log.d("Saved Location", item.toString());
//                        temporaryLocation = item.getString("description");
////                        getLatLngFromPlace(temporaryLocation);
//                    }
//                } else {
//                    String type = item.getJSONArray("types").getString(0);
//                    Log.v("TAG", " Type is " + type);
//                    if (type != null && type.equals("sublocality_level_1")) {
//                        linear_sublocality.setVisibility(View.VISIBLE);
//                        String place = !item.getString("description").isEmpty() ?
//                                item.getString("description").indexOf(",") > 0 ? item.getString("description").substring(0, item.getString("description").indexOf(",")) : item.getString("description") : "";
//                        text_sublocality.setText(place);
//                        edit_search.setText("");
//                        edit_search.setHint("Where in " + place);
//                    } else {
//                        temporaryLocation = item.getString("description");
////                        getLatLngFromPlace(temporaryLocation);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onLocationSearchedClicked(GooglePrediction item) {

        if (item.getDescription().toLowerCase().equalsIgnoreCase("use my location")) {

        } else {

            if (item.getType() != null) {
                if (item.getType().toLowerCase().equalsIgnoreCase("saved")) {

                }
            } else {

                String type = item.getTypes()[0];
                if (type != null && type.equalsIgnoreCase("sublocality_level_1")) {
                    linear_sublocality.setVisibility(View.VISIBLE);
                    String place = !item.getDescription().isEmpty() ?
                            item.getDescription().indexOf(",") > 0 ? item.getDescription().substring(0, item.getDescription().indexOf(",")) : item.getDescription() : "";
                    text_sublocality.setText(place);
                    edit_search.setText("");
                    edit_search.setHint("Where in " + place);
                } else {
                    temporaryLocation = item.getDescription();
                    Log.d("temporaryLocation", temporaryLocation);
                    getLatlongFromAddress();
                }

            }
        }


    }

    private void drawSavedLocationList() {
        String savedLocationStr = AppSettings.getValue(getActivity(), AppSettings.PREF_SEARCHED_SELECTED_LOCATION, "");
        List<GooglePrediction> savedLocations = new ArrayList<>();

        try {
            if (!savedLocationStr.equalsIgnoreCase("")) {
                savedLocations = new Gson().fromJson(savedLocationStr, new TypeToken<List<GooglePrediction>>() {
                }.getType());

            }

            for (int i = 0; i < savedLocations.size(); i++) {
                currentLocation.add(savedLocations.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        locationSearchListAdapter = new LocationSearchListAdapter(getActivity(), currentLocation, this);
        list_views.setAdapter(locationSearchListAdapter);
    }

    private void getLatlongFromAddress() {

        showLoader();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Address result = Util.getLatlongFromAddress(getActivity(), temporaryLocation);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();

                        if (purpose.equalsIgnoreCase("changeLocation")) {
                            boolean dowe = Util.doWeServeInDetectedArea1(getActivity(), result);
                            if (dowe) {
                                Util.saveLatLng(getActivity(), result.getLatlng());
                                Util.saveLatLngAddress(getActivity(), new Gson().toJson(result));
                                Util.setServiceOpen(getActivity());
                                getActivity().finish();
                            } else {
                                Util.intentCreateToast(getActivity(), toast, "it seems we dont serve this area!", Toast.LENGTH_SHORT);
                            }
                        }
                        if (purpose.equalsIgnoreCase("updatelandmark")) {
                            Intent intent = new Intent();
                            intent.putExtra("address", result);
                            intent.putExtra("type", "updatelandmark");
                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        } else if (purpose.equalsIgnoreCase("addlandmark")) {
                            Intent intent = new Intent();
                            intent.putExtra("address", result);
                            intent.putExtra("type", "addlandmark");
                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        }


                    }
                });
            }
        }).start();
    }

    private void handleLocationSave(GooglePrediction googlePrediction) {
        JSONArray newLocations = new JSONArray();
        String savedLocationStr = AppSettings.getValue(getActivity(), AppSettings.PREF_SEARCHED_SELECTED_LOCATION, "");
        List<GooglePrediction> savedLocations = new ArrayList<>();
        if (!savedLocationStr.isEmpty()) {
            try {
                savedLocations = new Gson().fromJson(savedLocationStr, new TypeToken<List<GooglePrediction>>() {
                }.getType());
                newLocations.put(googlePrediction);
                if (savedLocations != null && savedLocations.size() > 0) {
                    for (int i = 0; i < 2; i++) {
                        if (savedLocations.get(i).getLatlng().equalsIgnoreCase(googlePrediction.getLatlng())) {
                        } else {
                            newLocations.put(savedLocations.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newLocations.put(googlePrediction);
        }
        AppSettings.setValue(getActivity(), AppSettings.PREF_SEARCHED_SELECTED_LOCATION, newLocations.toString());
    }


    private void saveLocality(String lat, String lng) throws JSONException {
        GooglePrediction googlePrediction = new GooglePrediction();
        googlePrediction.setDescription(temporaryLocation);
        googlePrediction.setLatlng(lat + "," + lng);
        googlePrediction.setType("saved");
        handleLocationSave(googlePrediction);
    }

}
