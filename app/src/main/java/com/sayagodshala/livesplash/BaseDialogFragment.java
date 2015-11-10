package com.sayagodshala.livesplash;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {
    private AsyncLoader asyncLoader;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FragmentDialog);
        asyncLoader = AsyncLoader.dialog(getActivity());
    }

    public void showLoader() {
        asyncLoader.show();
    }

    public void hideLoader() {
        asyncLoader.hide();
    }


}
