package com.example.generateqr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.generateqr.R;
import com.example.generateqr.entities.QR;
import com.example.generateqr.fragments.main.DashboardFragment;
import com.example.generateqr.fragments.main.MainCallback;
import com.example.generateqr.fragments.main.QRCodeFragment;
import com.example.generateqr.fragments.main.ReferralFormFragment;
import com.example.generateqr.fragments.main.UplineFormFragment;
import com.example.generateqr.fragments.main.VoucherFormFragment;

public class MainActivity extends BaseActivity implements MainCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBackstackChangeListener();
        replaceFragment(DashboardFragment.newInstance());
    }

    @Override
    public void popLastFragment() {
    }

    @Override
    public void onReferralFormRequest() {
        addFragment(ReferralFormFragment.newInstance());
    }

    @Override
    public void onUplineFormRequest() {
        addFragment(UplineFormFragment.newInstance());
    }

    @Override
    public void onVoucherFormRequest(String type) {
        addFragment(VoucherFormFragment.newInstance(type));
    }

    @Override
    public void onQRGenerated(QR qr) {
        addFragment(QRCodeFragment.newInstance(qr));
    }
}
