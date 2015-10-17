package com.sayagodshala.dingdong.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.UpdateCustomerAddressActivity_;
import com.sayagodshala.dingdong.adapters.CustomerAddressListAdapter;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_customer_addres_list)
public class CustomerAddressFragment extends BaseFragment implements CustomerAddressListAdapter.CustomerAddressListAdapterListener {

    private static final int LOCATION_REQUEST = 1001;

    APIService apiService;

    private Toast toast;

    @ViewById(R.id.list_views)
    ListView list_views;

    @ViewById(R.id.container_empty)
    LinearLayout container_empty;

    @ViewById(R.id.container_no_internet)
    LinearLayout container_no_internet;

    @ViewById(R.id.container_error)
    LinearLayout container_error;

    @ViewById(R.id.text_error)
    TextView text_error;

    private CustomerAddressListAdapter customerAddressListAdapter;
    private List<Address> listItems;

    private Address address;
    private boolean isInitiated = false;

    private int selectedAddress = -1;

    @AfterViews
    void init() {
        container_no_internet.setVisibility(View.GONE);
        container_empty.setVisibility(View.GONE);
        container_error.setVisibility(View.GONE);
        listItems = new ArrayList<>();
        apiService = APIClient.getAPIService();
        container_empty.setVisibility(View.GONE);
        getUserAddresses();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Util.checkConnection(getActivity())) {
            if (isInitiated)
                isInitiated = false;
            container_error.setVisibility(View.GONE);
            container_empty.setVisibility(View.GONE);
            container_no_internet.setVisibility(View.VISIBLE);
            list_views.setVisibility(View.GONE);
        } else {
            if (!isInitiated) {
                isInitiated = true;
                init();
            }
        }

    }

    @Click(R.id.button_add)
    void onAddAddress() {
        UpdateCustomerAddressActivity_.intent(this).address(null).start();
    }

    @Click(R.id.button_reload)
    void reload() {
        container_error.setVisibility(View.GONE);
        getUserAddresses();
    }

    private void getUserAddresses() {
        showLoader();
        Call<APIResponse<List<Address>>> callBack = apiService.getUserAddresses(customer.getuserId());
        callBack.enqueue(new Callback<APIResponse<List<Address>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Address>>> response) {
                hideLoader();
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        if (response.body().getValues().size() > 0) {
                            listItems = response.body().getValues();
                        } else {
                            container_empty.setVisibility(View.GONE);
                        }
                        drawItems();
                    } else {
                        text_error.setText(response.body().getMessage());
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

    private void deleteAddress() {
        showLoader();
        Call<APIResponse> callBack = apiService.deleteAddress(customer.getuserId(), address.getAddressId());
        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                try {
                    hideLoader();
                    if (response.body().isStatus()) {
                        for (int i = 0; i < listItems.size(); i++) {
                            if (listItems.get(i).getAddressId().equalsIgnoreCase(address.getAddressId())) {
                                listItems.remove(i);
                                address = null;
                            }
                        }
                        customerAddressListAdapter.notifyDataSetChanged();

                        if (listItems.size() == 0) {
                            container_empty.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    Util.intentCreateToast(getActivity(), toast, "Oops, Something went wrong!", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.intentCreateToast(getActivity(), toast, t.getMessage(), Toast.LENGTH_SHORT);
            }
        });


    }

    private void drawItems() {
        customerAddressListAdapter = new CustomerAddressListAdapter(getActivity(), listItems, this);
        list_views.setAdapter(customerAddressListAdapter);

        if (listItems.size() > 0) {
            container_empty.setVisibility(View.GONE);
            list_views.setVisibility(View.VISIBLE);
        } else {
            container_empty.setVisibility(View.VISIBLE);
            list_views.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAddressEdited(Address address) {
        UpdateCustomerAddressActivity_.intent(this).address(address).startForResult(LOCATION_REQUEST);
    }

    @Override
    public void onAddressDeleted(Address address) {
        this.address = address;
        deleteAddress();
    }

    @OnActivityResult(LOCATION_REQUEST)
    void locationRequest(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (data.hasExtra("address")) {

                Address address = data.getExtras().getParcelable("address");
                if (data.hasExtra("update")) {
                    for (int i = 0; i < listItems.size(); i++) {
                        Address ad = listItems.get(i);
                        if (ad.getAddressId().equalsIgnoreCase(address.getAddressId())) {
                            listItems.set(i, address);
                            customerAddressListAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } else {
                    if (listItems.size() > 1) {
                        listItems.add(address);
                        customerAddressListAdapter.notifyDataSetChanged();
                    } else {
                        listItems.add(address);
                        drawItems();
                    }
                }


            }
        }
    }
}
