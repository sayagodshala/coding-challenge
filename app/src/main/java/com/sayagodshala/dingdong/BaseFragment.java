package com.sayagodshala.dingdong;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.util.Util;

public class BaseFragment extends Fragment {
    private AsyncLoader asyncLoader;

    public Customer customer;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncLoader = AsyncLoader.dialog(getActivity());
        customer = Util.getUserData(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showLoader() {
        try {
            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean onBackPressed() {
        return false;
    }


    protected void onPostCreateView(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
