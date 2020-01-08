package com.example.generateqr.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.generateqr.fragments.BaseFragment;


public abstract class BaseMainFragment extends BaseFragment {

    protected MainCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainCallback) {
            callback = (MainCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AuthCallback");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*View back = view.findViewById(R.id.ic_back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateBack();
                }
            });
        }*/
    }

    public void navigateBack() {
        callback.popLastFragment();
    }

    @Override
    public void refreshFragment() {
        super.refreshFragment();
        String title = getTitle();
        if (title != null) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    protected void showProgress() {
        callback.showProgress(null);
    }

    @Override
    protected void showProgress(String text) {
        callback.showProgress(text);
    }

    @Override
    protected void hideProgress() {
        callback.hideProgress();
    }
}
