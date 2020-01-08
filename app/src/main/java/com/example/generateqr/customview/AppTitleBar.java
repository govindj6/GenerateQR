package com.example.generateqr.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.generateqr.R;

import java.util.List;

public class AppTitleBar extends RelativeLayout {

    private View rootView;
    private TextView txtTitle;
    private ImageView imgTitle;
    private ImageView imgActionBack;
    private ImageView imgActionRight;
    private LinearLayout sectionMenu;

    public AppTitleBar(@NonNull Context context) {
        super(context);
        init();
    }

    public AppTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AppTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        rootView = inflater.inflate(R.layout.app_title_bar, this, true);
        txtTitle = rootView.findViewById(R.id.tv_title);
        imgActionBack = rootView.findViewById(R.id.iv_action_back);
        sectionMenu = rootView.findViewById(R.id.section_menu);
        imgTitle = rootView.findViewById(R.id.img_title);
    }

    public void setTextAlignment(int gravity) {
        txtTitle.setGravity(gravity);
    }

    public void setActionText(@Nullable String text) {
        if (text != null) {
            rootView.setVisibility(VISIBLE);
            if (text.trim().length() > 0) {
                txtTitle.setText(text);
                txtTitle.setVisibility(VISIBLE);
                imgTitle.setVisibility(GONE);
            } else {
                imgTitle.setVisibility(VISIBLE);
                txtTitle.setVisibility(GONE);
            }
        } else {
            rootView.setVisibility(GONE);
        }
    }

    public void setActionBackImage(@DrawableRes int resId) {
        imgActionBack.setImageResource(resId);
    }

    public void setActionBackImage(Drawable drawable) {
        imgActionBack.setImageDrawable(drawable);
    }

    public void setActionBackImage(Bitmap bitmap) {
        imgActionBack.setImageBitmap(bitmap);
    }

    public void setActionBackImage(boolean show) {
        imgActionBack.setVisibility(show ? VISIBLE : GONE);
    }

    public void setImageActionBackListener(OnClickListener listener) {
        imgActionBack.setOnClickListener(listener);
    }

    public void replaceMenuItems(List<View> menuItems) {
        clearMenu();
        for (View menuItem : menuItems) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            if (menuItem instanceof ImageView) {
                params.width = getResources().getDimensionPixelSize(R.dimen.vector_img_size);
                params.height = getResources().getDimensionPixelSize(R.dimen.vector_img_size);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.gap_medium);
            }
            sectionMenu.addView(menuItem, params);
        }
    }

    public void addMenuItem(View menuItem) {
        sectionMenu.addView(menuItem);
    }

    public void clearMenu() {
        sectionMenu.removeAllViews();
        sectionMenu.removeAllViewsInLayout();
    }
}
