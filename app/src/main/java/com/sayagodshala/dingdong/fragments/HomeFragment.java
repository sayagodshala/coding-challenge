package com.sayagodshala.dingdong.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.LocationClient;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.MyCartActivity_;
import com.sayagodshala.dingdong.activities.PickLocationActivity_;
import com.sayagodshala.dingdong.adapters.ProductListAdapter;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.LocationServed;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.CartUtils;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements LocationClient.LocationClientListener, ProductListAdapter.ProductListListener {

    private static final int LOCATION_CHANGED = 1001;
    APIService apiService;

    SupportMapFragment mapFragment;
    GoogleMap map;
    private Location myLocation;
    private LatLng location;
    private LatLng desLocation;
    private String latLng = "";

    private Dialog noLocationDialog;

    private LocationClient locationClient;

    private static final int SELECT_LOCATION_ACTIVITY_REQUEST = 5001;
    private Toast toast;

    @ViewById(R.id.current_location)
    TextView current_location;

    @ViewById(R.id.list_items)
    ListView list_items;
    ProductListAdapter productListAdapter;
    List<Product> listItems;

    @ViewById(R.id.container_location)
    RelativeLayout container_location;

    @ViewById(R.id.container_error)
    LinearLayout container_error;

    @ViewById(R.id.container_no_location)
    LinearLayout container_no_location;

    @ViewById(R.id.container_cart)
    RelativeLayout container_cart;

    @ViewById(R.id.container_no_internet)
    LinearLayout container_no_internet;

    @ViewById(R.id.text_total_price)
    TextView text_total_price;

    @ViewById(R.id.text_total_items)
    TextView text_total_items;

    @ViewById(R.id.button_enable_data)
    Button button_enable_data;


    private String currentAddress = "";

    private String metaMessage = "";

    private LocationServed locationServed;

    private boolean isInitiated = false;


    private void init() {
        showLoader();
        listItems = new ArrayList<>();
        apiService = APIClient.getAPIService();
        latLng = Util.getLatLng(getActivity());
        text_total_price.setText("");
        container_no_location.setVisibility(View.GONE);
        container_cart.setVisibility(View.GONE);
        container_location.setVisibility(View.GONE);
        container_no_internet.setVisibility(View.GONE);
        container_error.setVisibility(View.GONE);
        list_items.setVisibility(View.GONE);
        if (latLng.equalsIgnoreCase("") || latLng.equalsIgnoreCase("0,0")) {
            locationClient = new LocationClient(getActivity(), this);
            locationClient.connect();
        } else {
            setCurrentAddressAsTitle();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (locationClient != null)
                locationClient.disconnect();
            locationClient = null;
            Util.saveLatLng(getActivity(), location.getLatitude() + "," + location.getLongitude());
            setCurrentAddressAsTitle();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }

        Log.d("Fragment", "onpause");

    }

    @Click(R.id.button_enable_location)
    void enableLocation() {
        getActivity().startActivity(
                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Click(R.id.button_enable_data)
    void enableData() {
        getActivity().startActivity(
                new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
    }

    @Override
    public void onResume() {
        super.onResume();


        if (!Util.checkLocation(getActivity())) {

            if (isInitiated)
                isInitiated = false;
            container_no_location.setVisibility(View.VISIBLE);
            container_cart.setVisibility(View.GONE);
            container_error.setVisibility(View.GONE);
            container_location.setVisibility(View.GONE);
            container_no_internet.setVisibility(View.GONE);
            list_items.setVisibility(View.GONE);
        } else if (!Util.checkConnection(getActivity())) {

            if (isInitiated)
                isInitiated = false;

            container_no_location.setVisibility(View.GONE);
            container_cart.setVisibility(View.GONE);
            container_error.setVisibility(View.GONE);
            container_location.setVisibility(View.GONE);
            container_no_internet.setVisibility(View.VISIBLE);
            list_items.setVisibility(View.GONE);
        } else {
            if (!isInitiated) {
                isInitiated = true;
                init();
            }
            
            if (locationClient != null) {
                locationClient.connect();
            }
            refreshView();
        }

        Address myAddress = Util.getLatLngAddress(getActivity());

        if (myAddress != null) {
            if (myAddress.getLandmark() != null) {
                currentAddress = myAddress.getLandmark();
                current_location.setText(currentAddress);
            }
        }


//        if (!isInitiated) {
//            init();
//        }
//        isInitiated = true;
//
//
//        checkLocation();
//
//        if (noLocationDialog == null) {
//            if (locationClient != null) {
//                locationClient.connect();
//            }
//            refreshView();
//        }
//        Log.d("Fragment", "onresume");

    }

    @Click(R.id.container_location)
    void onLocationClick() {
        PickLocationActivity_.intent(this).purpose("changeLocation").startForResult(LOCATION_CHANGED);
    }

    @Click(R.id.container_cart)
    void onCartClick() {
        if (Util.isServiceOpen(getActivity())) {
            MyCartActivity_.intent(this).start();
        } else {
            Util.intentCreateToast(getActivity(), toast, metaMessage, Toast.LENGTH_SHORT);
        }
    }


    @Click(R.id.button_reload)
    void reload() {
        container_error.setVisibility(View.GONE);
        getProducts();
    }

    public void getProducts() {
        showLoader();

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        Call<APIResponse<List<Product>>> callBack = apiService.getProducts(customer.getuserId(), String.valueOf(hourOfDay));
        callBack.enqueue(new Callback<APIResponse<List<Product>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Product>>> response) {
                hideLoader();
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().isStatus()) {

                        if (response.body().getMeta() != null) {
                            boolean dowe = Util.doWeServeInDetectedArea(getActivity(), response.body().getMeta().getLocationServed());

                            Util.saveLocationServed(getActivity(), new Gson().toJson(response.body().getMeta().getLocationServed()));

                            metaMessage = !response.body().getMeta().getMessage().equalsIgnoreCase("") ? response.body().getMeta().getMessage() : "";
                            if (!dowe) {
                                metaMessage = "Sorry, we dont serve in this area!";
                                Util.intentCreateToast(getActivity(), toast, metaMessage, Toast.LENGTH_SHORT);
                            } else if (!response.body().getMeta().isOpen()) {
                                Util.intentCreateToast(getActivity(), toast, metaMessage, Toast.LENGTH_SHORT);
                            } else {
                                Util.setServiceOpen(getActivity());
                            }
                        }

                        if (response.body().getValues().size() > 0) {
                            listItems = mergeCartProductsAndServerProducts(response.body().getValues());
                            drawListItems();
                            updateCartView();
                        } else {
                            container_error.setVisibility(View.VISIBLE);
                        }
                    } else {
                        container_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    container_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                container_error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void drawListItems() {

        productListAdapter = new ProductListAdapter(getActivity(), listItems, this);
        list_items.setAdapter(productListAdapter);
        list_items.setVisibility(View.VISIBLE);
    }


    private void setCurrentAddressAsTitle() {
        if (getActivity() == null)
            return;


        Address myAddress = Util.getLatLngAddress(getActivity());

        if (myAddress != null)
            if (myAddress.getLandmark() != null)
                currentAddress = myAddress.getLandmark();

        Log.v("TAG", " Address " + currentAddress);
        if ((currentAddress.equalsIgnoreCase("") || currentAddress.equalsIgnoreCase("Cannot find address!"))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Util.saveLatLngAddress(getActivity(), Util.getAddressFromLatLng(getActivity()));
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Address myAddress = Util.getLatLngAddress(getActivity());
                            if (myAddress != null)
                                if (myAddress.getLandmark() != null)
                                    currentAddress = myAddress.getLandmark();

                            Log.v("TAG", " Address " + currentAddress);
                            if (!currentAddress.equalsIgnoreCase("") && !currentAddress.equalsIgnoreCase("Cannot find address!")) {
                                current_location.setText(currentAddress);
                                getProducts();
                            }
                        }
                    });
                }
            }).start();
        } else {
            current_location.setText(currentAddress);
            getProducts();
        }

        container_location.setVisibility(View.VISIBLE);

    }

    @Override
    public void onProductAdd(Product product, int index) {
        listItems.set(index, CartUtils.addItemInCart(getActivity(), listItems.get(index)));
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    @Override
    public void onProductIncrement(Product product, int index) {
        listItems.set(index, CartUtils.incrementProductIncart(getActivity(), listItems.get(index)));
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    @Override
    public void onProductDecrement(Product product, int index) {
        listItems.set(index, CartUtils.decrementProductIncart(getActivity(), listItems.get(index)));
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    private void updateCartView() {


        List<Product> cartProducts = CartUtils.getListOfItemsInCart(getActivity());
        if (cartProducts.size() > 0) {
            container_cart.setVisibility(View.VISIBLE);
        } else {
            container_cart.setVisibility(View.GONE);
        }
        int totalQuantity = 0;
        int topPrice = 0;
        for (int i = 0; i < cartProducts.size(); i++) {
            Product cartProduct = cartProducts.get(i);
            int quantity = cartProduct.getQuantity();
            totalQuantity += quantity;
            int price = Integer.parseInt(cartProduct.getPrice());
            int productPrice = quantity * price;
            topPrice += productPrice;
        }
        text_total_price.setText(topPrice + "");
        text_total_items.setText(totalQuantity + "");
    }

    private List<Product> mergeCartProductsAndServerProducts(List<Product> serverProducts) {

        List<Product> productlistincart = CartUtils.getListOfItemsInCart(getActivity());

        if (productlistincart.size() > 0) {
            for (Product product : serverProducts) {
                int quantity = 0;
                for (Product sp : productlistincart) {
                    if (sp.getProductId().equalsIgnoreCase(product.getProductId())) {
                        quantity += sp.getQuantity();
                    }
                }
                product.setQuantity(quantity);
            }
        }
        return serverProducts;
    }


    private void refreshView() {

        if (productListAdapter != null) {
            if (listItems.size() > 0) {
                List<Product> productlistincart = CartUtils.getListOfItemsInCart(getActivity());
                for (Product product : listItems) {
                    int quantity = 0;
                    for (Product sp : productlistincart) {
                        if (sp.getProductId().equalsIgnoreCase(product.getProductId())) {
                            if (sp.getQuantity() > 0) {
                                quantity += sp.getQuantity();
                            }
                        }
                    }
                    product.setQuantity(quantity);
                }
                productListAdapter.notifyDataSetChanged();
            }
            updateCartView();
        }
    }

    @OnActivityResult(LOCATION_CHANGED)
    void locationChangedByUser(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            Address address = Util.getLatLngAddress(getActivity());
            if (address != null) {
                drawListItems();
                currentAddress = address.getLandmark();
                current_location.setText(currentAddress);
            }
        }
    }

}
