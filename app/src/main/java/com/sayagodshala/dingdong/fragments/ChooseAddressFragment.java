package com.sayagodshala.dingdong.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.UpdateCustomerAddressActivity_;
import com.sayagodshala.dingdong.adapters.ChooseAddressListAdapter;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.CartUtils;
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

@EFragment(R.layout.fragment_customer_choose_addres_list)
public class ChooseAddressFragment extends BaseFragment implements ChooseAddressListAdapter.CustomerChooseAddressListAdapterListener {

    private static final int LOCATION_REQUEST = 1001;
    APIService apiService;

    private Toast toast;

    @ViewById(R.id.container_submit)
    RelativeLayout container_submit;

    @ViewById(R.id.list_views)
    ListView list_views;

    @ViewById(R.id.container_empty)
    LinearLayout container_empty;

    @ViewById(R.id.container_no_internet)
    LinearLayout container_no_internet;

    @ViewById(R.id.container_error)
    LinearLayout container_error;

    private ChooseAddressListAdapter chooseAddressListAdapter;
    private List<Address> listItems;

    private Address address;

    @ViewById(R.id.text_total_price)
    TextView text_total_price;

    @ViewById(R.id.text_error)
    TextView text_error;
    private boolean isInitiated = false;

    @AfterViews
    void init() {

        listItems = new ArrayList<>();

        apiService = APIClient.getAPIService();
        container_empty.setVisibility(View.GONE);
        container_no_internet.setVisibility(View.GONE);
        container_error.setVisibility(View.GONE);
//        container_submit.setVisibility(View.GONE);
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

    @Click(R.id.container_submit)
    void onSubmitClick() {
        if (listItems.size() > 0) {
            if (address != null) {

            } else {
                Util.intentCreateToast(getActivity(), toast, "Please select delivery address", Toast.LENGTH_SHORT);
            }
        } else {
            Util.intentCreateToast(getActivity(), toast, "Please add and then select delivery address", Toast.LENGTH_SHORT);
        }

    }

    @Click(R.id.button_add)
    void onAddAddress() {
        UpdateCustomerAddressActivity_.intent(this).address(null).startForResult(LOCATION_REQUEST);
    }

    @Click(R.id.button_reload)
    void reload() {
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
                            container_empty.setVisibility(View.VISIBLE);
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
                        chooseAddressListAdapter.notifyDataSetChanged();

                        if (listItems.size() == 0) {
                            container_empty.setVisibility(View.VISIBLE);
//                            container_submit.setVisibility(View.GONE);
                        }

                    } else {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
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
        chooseAddressListAdapter = new ChooseAddressListAdapter(getActivity(), listItems, this);
        list_views.setAdapter(chooseAddressListAdapter);

        if (listItems.size() > 0) {
            container_empty.setVisibility(View.GONE);
            list_views.setVisibility(View.VISIBLE);
//            container_submit.setVisibility(View.VISIBLE);
            updateCartView();
        } else {
            container_empty.setVisibility(View.VISIBLE);
            list_views.setVisibility(View.GONE);
//            container_submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAddressChoosed(Address address) {
        this.address = address;

        if (this.address != null) {
            container_submit.setVisibility(View.VISIBLE);
        } else {
            container_submit.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAddressEdited(Address address) {

    }

    @Override
    public void onAddressDeleted(Address address) {
        this.address = address;

        deleteAddress();
    }

    private void updateCartView() {
        List<Product> cartProducts = CartUtils.getListOfItemsInCart(getActivity());
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
    }

    @OnActivityResult(LOCATION_REQUEST)
    void locationRequest(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (data.hasExtra("address")) {
                Address address = data.getExtras().getParcelable("address");
                if (listItems.size() > 0) {
                    listItems.add(address);
                    chooseAddressListAdapter.notifyDataSetChanged();
                } else {
                    listItems.add(address);
                    drawItems();
                }
            }
        }
    }


}
