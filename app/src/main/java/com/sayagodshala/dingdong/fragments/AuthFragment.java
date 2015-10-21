package com.sayagodshala.dingdong.fragments;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.MainActivity_;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.MobileNoVerificationActivity_;
import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.network.APIClient;
import com.sayagodshala.dingdong.network.APIResponse;
import com.sayagodshala.dingdong.network.APIService;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.fragment_auth)
public class AuthFragment extends BaseFragment {

    APIService apiService;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.phone_number)
    EditText phoneNumber;

    @ViewById(R.id.choose_password)
    EditText choosePassword;
    @ViewById(R.id.username)
    EditText username;
    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.auth_container)
    ViewAnimator authContainer;


    @ViewById(R.id.signup_container)
    View signUpContainer;
    @ViewById(R.id.signin_container)
    View signInContainer;
    private Toast toast;


    @AfterViews
    void init() {

        apiService = APIClient.getAPIService();

        signInContainer();
    }

    @Click(R.id.signup_container)
    void signUpContainer() {
        authContainer.setDisplayedChild(0);
        selectAuthContainer(signUpContainer);
    }

    @Click(R.id.signin_container)
    void signInContainer() {
        authContainer.setDisplayedChild(1);
        selectAuthContainer(signInContainer);
    }

    private void selectAuthContainer(View view) {
        signUpContainer.setSelected(false);
        signInContainer.setSelected(false);
        view.setSelected(true);
    }

    @Click(R.id.signup)
    void signup() {
        if (isSignUpValid())
            generateOtp();
    }

    @Click(R.id.signin)
    void signin() {
        if (isSignInValid())
            loginUser();
    }

    @Click(R.id.reset_password)
    void resetPassword() {

    }

    private boolean isSignInValid() {


        String validationMessage = "";
        if (Util.textIsEmpty(username.getText().toString())) {
            username.requestFocus();
            validationMessage = Constants.WARNING_USERNAME;
        } else if (Util.textIsEmpty(password.getText().toString())) {
            password.requestFocus();
            validationMessage = Constants.WARNING_PASSWORD1;
        }

        if (!validationMessage.equalsIgnoreCase("")) {
            Util.intentCreateToast(getActivity(), toast, validationMessage, Toast.LENGTH_LONG);
        }

        return validationMessage.length() == 0;
    }

    private boolean isSignUpValid() {


        String validationMessage = "";
        if (Util.textIsEmpty(name.getText().toString())) {
            name.requestFocus();
            validationMessage = Constants.WARNING_NAME;
        } else if (!Util.isValidEmail(email.getText().toString())) {
            email.requestFocus();
            validationMessage = Constants.WARNING_EMAILID;
        } else if (Util.textIsEmpty(phoneNumber.getText().toString()) || phoneNumber.getText().toString().length() < 10) {
            phoneNumber.requestFocus();
            validationMessage = Constants.WARNING_MOBILENO;
        } else if (Util.textIsEmpty(choosePassword.getText().toString()) || choosePassword.getText().toString().length() < 6) {
            choosePassword.requestFocus();
            validationMessage = Constants.WARNING_PASSWORD;
        }
        if (!validationMessage.equalsIgnoreCase("")) {
            Util.intentCreateToast(getActivity(), toast, validationMessage, Toast.LENGTH_LONG);
        }

        return validationMessage.length() == 0;
    }

//    public void registerUser() {
//
//        String gcmToken = Util.getGcmToken(getActivity());
//        String deviceId = Util.uniqueDeviceID(getActivity());
//
//        showLoader();
//
//
//        Call<APIResponse<Customer>> callBack = apiService.registerUser(name.getText().toString().trim(),
//                email.getText().toString().trim(),
//                choosePassword.getText().toString(), gcmToken, deviceId, "android", phoneNumber.getText().toString().trim(), "user");
//
//        callBack.enqueue(new Callback<APIResponse<Customer>>() {
//            @Override
//            public void onResponse(Response<APIResponse<Customer>> response) {
//                Log.d("Retrofit Response", new Gson().toJson(response.body()));
//
//                if (response.body() != null) {
//                    if (response.body().isStatus()) {
//                        Util.setUserData(getActivity(), new Gson().toJson(response.body().getValues()));
////                        Customer customer = Util.getUserData(getActivity());
//                        Log.d("Customer", new Gson().toJson(Util.getUserData(getActivity())));
//                        Util.setUserLogIn(getActivity());
//                        MainActivity_.intent(getActivity()).start();
//                        getActivity().finish();
//                    } else {
//                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
//                    }
//                }
//                hideLoader();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                hideLoader();
//                Log.d("Retrofit Response", new Gson().toJson(t));
//            }
//        });
//
//
//    }

    public void generateOtp() {

        showLoader();

        Call<APIResponse> callBack = apiService.generateOtp(phoneNumber.getText().toString().trim(),
                email.getText().toString().trim(), name.getText().toString().trim());


        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                hideLoader();
                if (response.body() != null) {
                    if (response.body().isStatus()) {


                        Customer customer = new Customer();
                        customer.setEmail(email.getText().toString().trim());
                        customer.setName(name.getText().toString().trim());
                        customer.setmobileNo(phoneNumber.getText().toString().trim());
                        customer.setPassword(choosePassword.getText().toString());
                        Log.d("generate", new Gson().toJson(customer));
                        MobileNoVerificationActivity_.intent(getActivity()).customer(customer).start();

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

    public void loginUser() {

        String gcmToken = Util.getGcmToken(getActivity());
        String deviceId = Util.uniqueDeviceID(getActivity());

        showLoader();

        Call<APIResponse<Customer>> callBack = apiService.loginUser(username.getText().toString().trim(),
                password.getText().toString(), gcmToken, deviceId, "android");

        callBack.enqueue(new Callback<APIResponse<Customer>>() {
            @Override
            public void onResponse(Response<APIResponse<Customer>> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        Util.setUserData(getActivity(), new Gson().toJson(response.body().getValues()));
//                        Customer customer = Util.getUserData(getActivity());
                        Log.d("Customer", new Gson().toJson(Util.getUserData(getActivity())));
                        Util.setUserLogIn(getActivity());
                        MainActivity_.intent(getActivity()).start();
                        getActivity().finish();
                    } else {
                        Util.intentCreateToast(getActivity(), toast, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                }
                hideLoader();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });


    }


}
