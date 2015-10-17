package com.sayagodshala.dingdong.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.OrderConfirmedActivity_;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.CartUtils;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_checkout)
public class CheckoutFragment extends BaseFragment {

    @FragmentArg
    Address address;

    APIService apiService;

    private Toast toast;

    @ViewById(R.id.container_no_internet)
    LinearLayout container_no_internet;

    @ViewById(R.id.button_pay)
    Button button_pay;

    private boolean isInitiated = false;

    @ViewById(R.id.text_total_price)
    TextView text_total_price;

    @ViewById(R.id.text_delivery_address)
    TextView text_delivery_address;

    @ViewById(R.id.scroll_content)
    ScrollView scroll_content;
    private List<Product> cartProducts;

    private String productIds;
    private String quantitys;

    void init() {

        cartProducts = CartUtils.getListOfItemsInCart(getActivity());

        apiService = APIClient.getAPIService();
        container_no_internet.setVisibility(View.GONE);
        button_pay.setVisibility(View.VISIBLE);
        scroll_content.setVisibility(View.VISIBLE);
        setTotalPrice();

        StringBuilder customerAddress = new StringBuilder();
        customerAddress.append(customer.getName() + "\n");
        customerAddress.append(address.getAddress() + "\n");
        customerAddress.append(address.getLandmark());

        text_delivery_address.setText(customerAddress.toString());

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Util.checkConnection(getActivity())) {
            if (isInitiated)
                isInitiated = false;
            container_no_internet.setVisibility(View.VISIBLE);
            button_pay.setVisibility(View.GONE);
            scroll_content.setVisibility(View.GONE);
        } else {
            if (!isInitiated) {
                isInitiated = true;
                init();
            }
        }
    }


    @Click(R.id.button_reload)
    void reload() {
        onResume();
    }

    private void setTotalPrice() {

        productIds = "";
        quantitys = "";

        int totalQuantity = 0;
        int topPrice = 0;
        for (int i = 0; i < cartProducts.size(); i++) {
            Product cartProduct = cartProducts.get(i);

            if (productIds.equalsIgnoreCase("")) {
                productIds = cartProduct.getProductId();
            } else {
                productIds += "," + cartProduct.getProductId();
            }

            if (quantitys.equalsIgnoreCase("")) {
                quantitys = String.valueOf(cartProduct.getQuantity());
            } else {
                quantitys += "," + String.valueOf(cartProduct.getQuantity());
            }


            int quantity = cartProduct.getQuantity();
            totalQuantity += quantity;
            int price = Integer.parseInt(cartProduct.getPrice());
            int productPrice = quantity * price;
            topPrice += productPrice;
        }
        text_total_price.setText(topPrice + "");
    }

    @Click(R.id.button_pay)
    void onPayClick() {
        postOrder();
    }

    private void postOrder() {
        showLoader();
        Call<APIResponse> callBack = apiService.postOrder(customer.getuserId(),
                address.getAddressId(),
                productIds,
                quantitys);
        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body() != null) {
                    if (response.body().isStatus()) {
//                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                        CartUtils.resetCart(getActivity());
                        OrderConfirmedActivity_.intent(getActivity()).start();
                    } else {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                } else {
                    Util.intentCreateToast(getActivity(), toast, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.intentCreateToast(getActivity(), toast, Constants.ERROR_MESSAGE1, Toast.LENGTH_SHORT);
            }
        });

    }


}
