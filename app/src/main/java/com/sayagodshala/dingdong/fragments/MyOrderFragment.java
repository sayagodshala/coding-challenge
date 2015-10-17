package com.sayagodshala.dingdong.fragments;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.MainActivity;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.adapters.MyOrderListAdapter;
import com.sayagodshala.dingdong.model.Order;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_my_order)
public class MyOrderFragment extends BaseFragment implements MyOrderListAdapter.MyOrderListListener {

    APIService apiService;

    private Toast toast;

    @ViewById(R.id.container_empty)
    LinearLayout container_empty;

    @ViewById(R.id.list_items)
    ListView list_items;

    @ViewById(R.id.container_no_internet)
    LinearLayout container_no_internet;

    @ViewById(R.id.container_error)
    LinearLayout container_error;

    private List<Order> listItems;

    private MyOrderListAdapter myOrderListAdapter;
    private boolean isInitiated = false;

    @ViewById(R.id.text_error)
    TextView text_error;
    private int selectedOrderIndex;

    void init() {

        listItems = new ArrayList<>();
        apiService = APIClient.getAPIService();
        container_empty.setVisibility(View.GONE);
        container_no_internet.setVisibility(View.GONE);
        container_error.setVisibility(View.GONE);
        list_items.setVisibility(View.GONE);
        getMyOrders();

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
            list_items.setVisibility(View.GONE);
        } else {
            if (!isInitiated) {
                isInitiated = true;
                init();
            }
        }
    }


    @Click(R.id.button_ordernow)
    void onOrderNowClick() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    @Click(R.id.button_reload)
    void reload() {
        container_error.setVisibility(View.GONE);
        getMyOrders();

    }

    private void getMyOrders() {

        showLoader();

        Call<APIResponse<List<Order>>> callBack = apiService.getMyOrders(customer.getuserId());
        callBack.enqueue(new Callback<APIResponse<List<Order>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Order>>> response) {
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
                    text_error.setText(Constants.ERROR_MESSAGE);
                    container_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                text_error.setText(Constants.ERROR_MESSAGE);
                container_error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void cancelOrder(String orderId) {

        showLoader();

        Call<APIResponse> callBack = apiService.cancelOrder(customer.getuserId(), orderId);
        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        listItems.get(selectedOrderIndex).setStatus("canceled");
                        myOrderListAdapter.notifyDataSetChanged();
                    } else {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                } else {
                    Util.intentCreateToast(getActivity(), toast, Constants.ERROR_MESSAGE + " while canceling order", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.intentCreateToast(getActivity(), toast, Constants.ERROR_MESSAGE1 + " while canceling order", Toast.LENGTH_SHORT);
            }
        });
    }


    private void drawItems() {
        myOrderListAdapter = new MyOrderListAdapter(getActivity(), listItems, this);
        list_items.setAdapter(myOrderListAdapter);

        if (listItems.size() > 0) {
            container_empty.setVisibility(View.GONE);
            container_no_internet.setVisibility(View.GONE);
            list_items.setVisibility(View.VISIBLE);
        } else {
            container_empty.setVisibility(View.VISIBLE);
            container_no_internet.setVisibility(View.GONE);
            list_items.setVisibility(View.GONE);
        }

    }

    @Override
    public void onOrderSelected(Order order, int index) {

    }

    @Override
    public void onOrderCanceled(Order order, int index) {
        selectedOrderIndex = index;
        cancelOrder(order.getOrderId());
    }
}
