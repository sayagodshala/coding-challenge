package com.sayagodshala.livesplash.fragments;

import android.widget.EditText;
import android.widget.Toast;

import com.sayagodshala.livesplash.BaseFragment;
import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.util.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_add_location)
public class AddLocationFragment extends BaseFragment {

    @ViewById(R.id.latlng)
    EditText latlng;

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.description)
    EditText description;
    private Toast toast;

    @AfterViews
    void init() {
        latlng.setText(Util.getLatLng(getActivity()));
    }

    private boolean isValid() {
        String validationMessage = "";
        if (Util.textIsEmpty(name.getText().toString())) {
            name.requestFocus();
            validationMessage = "Pease enter name";
        } else if (Util.textIsEmpty(description.getText().toString())) {
            description.requestFocus();
            validationMessage = "Pease enter description";
        } else if (Util.textIsEmpty(latlng.getText().toString())) {
            latlng.requestFocus();
            validationMessage = "Pease enter latlng";
        }

        if (!validationMessage.equalsIgnoreCase("")) {
            Util.intentCreateToast(getActivity(), toast, validationMessage, Toast.LENGTH_LONG);
        }

        return validationMessage.length() == 0;
    }


    @Click(R.id.button_submit)
    void click() {
        if (isValid()) {
            getActivity().finish();
        }

    }

}
