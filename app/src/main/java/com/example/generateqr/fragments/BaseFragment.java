package com.example.generateqr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    private boolean viewCreated = false;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        bindViews(view);
        view.setClickable(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }

    public final boolean isViewCreated() {
        return this.viewCreated;
    }

    protected final void showToast(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    protected final void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected abstract void showProgress();

    protected abstract void showProgress(String text);

    protected abstract void hideProgress();

    protected abstract @LayoutRes
    int getLayout();

    public void refreshFragment() {
    }

    public String getTitle() {
        return "";
    }

    /*protected ImageView getImageMenuItem(@DrawableRes int resId, View.OnClickListener onClickListener) {
        ImageView imageMenuItem = new ImageView(getContext());
        imageMenuItem.setImageResource(resId);
        imageMenuItem.setOnClickListener(onClickListener);
        imageMenuItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageMenuItem.setAdjustViewBounds(true);
        return imageMenuItem;
    }

    protected View getImageMenuItem(@DrawableRes int resId, int quantity, View.OnClickListener onClickListener) {
        View group = LayoutInflater.from(getContext()).inflate(R.layout.item_menu_width_badge, null, false);
        ((ImageView) group.findViewById(R.id.img_main)).setImageResource(resId);
        ((Button) group.findViewById(R.id.btn_badge)).setText(String.valueOf(quantity));
        group.findViewById(R.id.btn_badge).setVisibility(quantity > 0 ? View.VISIBLE : View.GONE);
        group.findViewById(R.id.btn_badge).setOnClickListener(onClickListener);
        group.setOnClickListener(onClickListener);
        return group;
    }

    protected CircleImageView getCircleImageMenuItem(@DrawableRes int resId, View.OnClickListener onClickListener) {
        CircleImageView imageMenuItem = new CircleImageView(getContext());
        if (resId != 0) {
            imageMenuItem.setImageResource(resId);
        }
        imageMenuItem.setOnClickListener(onClickListener);
        return imageMenuItem;
    }

    protected CircleImageView getCircleImageMenuItem(Bitmap bitmap, View.OnClickListener onClickListener) {
        CircleImageView imageMenuItem = new CircleImageView(getContext());
        imageMenuItem.setImageBitmap(bitmap);
        imageMenuItem.setOnClickListener(onClickListener);
        return imageMenuItem;
    }

    protected TextView getTextMenuItem(@StringRes int resId, int textSize, View.OnClickListener onClickListener) {
        return getTextMenuItem(getString(resId), textSize, onClickListener);
    }

    protected TextView getTextMenuItem(String text, int textSize, View.OnClickListener onClickListener) {
        TextView textMenuItem = new TextView(getContext());
        textMenuItem.setTextColor(getResources().getColor(R.color.blue));
        textMenuItem.setText(text);
        textMenuItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textMenuItem.setOnClickListener(onClickListener);
        return textMenuItem;
    }*/

    protected void hideKeyboard() {
        if (getActivity() != null) {
            Object service = getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (service != null) {
                InputMethodManager imm = (InputMethodManager) service;
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public List<View> getMenuItems() {
        return new ArrayList<>();
    }

    protected abstract void bindViews(View root);
}
