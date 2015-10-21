package com.sayagodshala.dingdong.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.PickLocationActivity_;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_update_user_addres)
public class UpdateCustomerAddressFragment extends BaseFragment {


    private static final int LOCATION_REQUEST = 1001;
    @FragmentArg
    Address address;

    @ViewById(R.id.rl_address)
    RelativeLayout rl_address;

    @ViewById(R.id.rl_landmark)
    RelativeLayout rl_landmark;

    @ViewById(R.id.relative_current_location)
    RelativeLayout relative_current_location;

    @ViewById(R.id.text_address)
    TextView text_address;

    @ViewById(R.id.text_current_location)
    TextView text_current_location;

    @ViewById(R.id.text_nearest_landmark)
    TextView text_nearest_landmark;

    @ViewById(R.id.edit_address)
    EditText edit_address;

    @ViewById(R.id.edit_nearest_landmark)
    TextView edit_nearest_landmark;

    @ViewById(R.id.button_update)
    Button button_update;
    private Toast toast;

    private APIService apiService;

    @AfterViews
    void init() {

        apiService = APIClient.getAPIService();

        if (address != null) {
            edit_address.setText(address.getAddress());
            edit_nearest_landmark.setText(address.getLandmark());
            edit_nearest_landmark.setTextColor(Color.parseColor("#373737"));
            text_nearest_landmark.setVisibility(View.VISIBLE);
        } else {
            address = new Address();
            button_update.setText("ADD");
        }

        setCurrentAddressAsTitle();
    }

    @Click(R.id.button_update)
    void onUpdateClick() {
        if (isValid()) {
            if (button_update.getText().toString().equalsIgnoreCase("update")) {

                updateAddress();

            } else {
                postAddress();
            }
        }
    }

    private void updateAddress() {

        showLoader();

        address.setAddress(edit_address.getText().toString());
        address.setLandmark(edit_nearest_landmark.getText().toString());

        Call<APIResponse> callBack = apiService.updateAddress(customer.getuserId(), address.getAddressId(),
                address.getAddress(),
                address.getLatlng(),
                address.getLandmark());

        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();

                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                        Intent intent = new Intent();
                        intent.putExtra("address", address);
                        intent.putExtra("update", "blabla");

                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
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

    private void postAddress() {

        showLoader();

        address.setAddress(edit_address.getText().toString());

        Call<APIResponse<Address>> callBack = apiService.postAddress(customer.getuserId(),
                address.getAddress(),
                address.getLatlng(),
                address.getLandmark());

        callBack.enqueue(new Callback<APIResponse<Address>>() {
            @Override
            public void onResponse(Response<APIResponse<Address>> response) {
                hideLoader();

                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                        Intent intent = new Intent();
                        intent.putExtra("address", response.body().getValues());
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
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


    private boolean isValid() {
        String validationMessage = "";
        if (Util.textIsEmpty(edit_address.getText().toString())) {
            edit_address.requestFocus();
            validationMessage = "Please Enter Address";
        } else if (Util.textIsEmpty(edit_nearest_landmark.getText().toString())) {
            edit_nearest_landmark.requestFocus();
            validationMessage = "Please Enter Nearest Landmark";
        }

        if (!validationMessage.equalsIgnoreCase("")) {
            Util.intentCreateToast(getActivity(), toast, validationMessage, Toast.LENGTH_LONG);
        }

        return validationMessage.length() == 0;
    }
    
    @Click(R.id.rl_landmark)
    void onLandMarkClick() {
        if (address != null) {
            if (address.getAddressId() != null) {
                PickLocationActivity_.intent(this).purpose("updateLandmark").startForResult(LOCATION_REQUEST);
            } else {
                PickLocationActivity_.intent(this).purpose("addLandmark").startForResult(LOCATION_REQUEST);
            }

        } else {
            PickLocationActivity_.intent(this).purpose("addLandmark").startForResult(LOCATION_REQUEST);
        }
    }

    @TextChange(R.id.edit_address)
    void onAddressChange(TextView tv, CharSequence text) {
        if (text.length() > 0) {
            text_address.setVisibility(View.GONE);
        } else {
            text_address.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentAddressAsTitle() {

        Address myAddress = Util.getLatLngAddress(getActivity());

        String address = "";

        if (myAddress != null)
            if (myAddress.getLandmark() != null)
                address = myAddress.getLandmark();

        relative_current_location.setVisibility(View.VISIBLE);
        if (address.isEmpty() || address.equalsIgnoreCase("Cannot find address!")) {
            relative_current_location.setVisibility(View.GONE);
        } else {
            text_current_location.setText(address);
        }
        text_nearest_landmark.setVisibility(View.GONE);
    }

    @OnActivityResult(LOCATION_REQUEST)
    void locationRequest(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (data.hasExtra("address")) {

                Address tempAddress = data.getExtras().getParcelable("address");

                address.setLandmark(tempAddress.getLandmark());
                address.setLatlng(tempAddress.getLatlng());

                edit_nearest_landmark.setText(address.getLandmark());
                edit_nearest_landmark.setTextColor(Color.parseColor("#373737"));
                text_nearest_landmark.setVisibility(View.VISIBLE);
            }

        }
    }

}
