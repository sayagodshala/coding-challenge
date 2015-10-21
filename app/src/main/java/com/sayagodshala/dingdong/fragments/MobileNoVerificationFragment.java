package com.sayagodshala.dingdong.fragments;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.MainActivity_;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_mobileno_verification)
public class MobileNoVerificationFragment extends BaseFragment {

    @FragmentArg
    Customer customer;

    APIService apiService;

    private Toast toast;

    @ViewById(R.id.linear_phoneno)
    LinearLayout linear_phoneno;

    @ViewById(R.id.edit_code)
    EditText edit_code;

    @ViewById(R.id.edit_phoneno)
    EditText edit_phoneno;


    @AfterViews
    void init() {

        apiService = APIClient.getAPIService();
        Log.d("Verify2", new Gson().toJson(customer));
        edit_phoneno.setText(customer.getmobileNo());

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void registerUser() {
        String gcmToken = Util.getGcmToken(getActivity());
        String deviceId = Util.uniqueDeviceID(getActivity());

        showLoader();

        Call<APIResponse<Customer>> callBack = apiService.registerUser(customer.getName(),
                customer.getEmail(),
                customer.getPassword(), gcmToken, deviceId, "android", customer.getmobileNo(), "user", edit_code.getText().toString());

        callBack.enqueue(new Callback<APIResponse<Customer>>() {
            @Override
            public void onResponse(Response<APIResponse<Customer>> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                hideLoader();
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        Util.setUserData(getActivity(), new Gson().toJson(response.body().getValues()));
                        Log.d("Customer", new Gson().toJson(Util.getUserData(getActivity())));
                        Util.setUserLogIn(getActivity());
                        MainActivity_.intent(getActivity()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
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
                Util.intentCreateToast(getActivity(), toast, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT);
            }
        });


    }

    @Click(R.id.button_verify)
    void verify() {
        if (!Util.textIsEmpty(edit_code.getText().toString())) {
            if (edit_code.getText().toString().length() == 4) {
                registerUser();
            } else {
                Util.intentCreateToast(getActivity(), toast, "CODE must be 4 digits", Toast.LENGTH_SHORT);
            }
        } else {
            Util.intentCreateToast(getActivity(), toast, "Please enter OTP.", Toast.LENGTH_SHORT);
        }
    }

    @Click(R.id.button_resend)
    void resendOtp() {
        if (!Util.textIsEmpty(edit_phoneno.getText().toString())) {

            if (edit_phoneno.getText().toString().length() == 10) {

                generateOtp();

            } else {
                Util.intentCreateToast(getActivity(), toast, "Mobile No should be 10 digits long.", Toast.LENGTH_SHORT);
            }

        } else {
            Util.intentCreateToast(getActivity(), toast, "Please enter Mobile No.", Toast.LENGTH_SHORT);
        }
    }

    public void generateOtp() {

        showLoader();

        Call<APIResponse> callBack = apiService.generateOtp(edit_phoneno.getText().toString(),
                customer.getEmail(), customer.getName());

        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                hideLoader();
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        customer.setmobileNo(edit_phoneno.getText().toString());
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
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
                Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });

    }

}
