package com.example.generateqr.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.generateqr.R;
import com.example.generateqr.customview.AppTitleBar;
import com.example.generateqr.fragments.BaseFragment;

import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private AppTitleBar toolbar;
    private View progressBar;
    private TextView txtProgress;

    protected final void addActivity(Class<?> cls, @Nullable Bundle data) {
        Intent intent = new Intent(this, cls);
        /*
        Intent currentIntent = getIntent();
        if (currentIntent != null) {
            if (currentIntent.getAction() != null) {
                intent.setAction(currentIntent.getAction());
            }
            if (currentIntent.getData() != null) {
                intent.setData(currentIntent.getData());
            }
        }
        */
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    protected void setupActionbar(AppTitleBar toolbar) {
        this.toolbar = toolbar;
        this.toolbar.setImageActionBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBackStackEntry()) {
                    onBackPressed();
                } else {
                    openSideDrawer();
                }
            }
        });
        setupBackstackChangeListener();
        refreshActionIcon();
    }

    protected void setupBackstackChangeListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onContentViewSet();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onContentViewSet();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onContentViewSet();
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
        onContentViewSet();
    }

    private void onContentViewSet() {
        /*progressBar = findViewById(R.id.progress_bar);
        txtProgress = findViewById(R.id.txt_progress);
        hideProgress();*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        //String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            String path = data.getPath();
            if (path != null && path.contains("/qr/")) {
                /*PreferencesUtils.savePendingAction(FrontRowApp.EVENT_DEEPLINK_QR);
                PreferencesUtils.savePendingActionData(path.split("qr/")[1]);*/
            }
        }
        //System.out.println("----## handleIntent ##----");
        //System.out.println("---- action:[" + action + "] ----");
        //System.out.println("---- data:[" + data + "] ----");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void openSideDrawer() {
    }

    protected final void replaceActivity(Class<?> cls, @Nullable Bundle data) {
        addActivity(cls, data);
        finish();
    }

    protected void popLastFragment() {
        getSupportFragmentManager().popBackStack();
    }

    protected void popAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    protected void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    protected void addFragmentWithAnimation(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack("image", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack("image")
                .commit();

        /*getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement))
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();*/
    }

    protected void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    protected void replaceFragment(Fragment fragment, View sharedElement) {
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement))
                .replace(R.id.container, fragment)
                .commit();
    }

    public void setActionBarTitle(String title) {
        toolbar.setActionText(title);
    }

    protected final void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackStackChanged() {
        if (this.toolbar != null) {
            refreshActionIcon();
            try {
                Fragment fragment = getTopFragment();
                ((BaseFragment) fragment).refreshFragment();
                replaceMenuItems(((BaseFragment) fragment).getMenuItems());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hideKeyboard();
    }

    public void refreshMenuItems() {
        if (this.toolbar != null) {
            try {
                Fragment fragment = getTopFragment();
                replaceMenuItems(((BaseFragment) fragment).getMenuItems());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void replaceMenuItems(List<View> menuItems) {
        this.toolbar.replaceMenuItems(menuItems);
    }

    private void refreshActionIcon() {
        if (hasBackStackEntry()) {
            this.toolbar.setActionBackImage(true);
            //this.toolbar.setActionBackImage(R.drawable.back);
        } else {
            this.toolbar.setActionBackImage(false);
            //this.toolbar.setActionBackImage(R.drawable.ic_vc_drwaer_menu);
        }
        this.toolbar.clearMenu();
    }

    private boolean hasBackStackEntry() {
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            return count > 0;
        } catch (Exception e) {
            return true;
        }
    }

    public Fragment getTopFragment() {
        List<Fragment> fragentList = getSupportFragmentManager().getFragments();
        Fragment top = null;
        for (int i = fragentList.size() - 1; i >= 0; i--) {
            top = fragentList.get(i);
            if (top != null) {
                return top;
            }
        }
        return top;
    }

    public void showProgress(String text) {
        if (txtProgress != null) {
            if (text != null) {
                txtProgress.setText(text);
            } else {
                txtProgress.setText("");
            }
        }
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected final void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
