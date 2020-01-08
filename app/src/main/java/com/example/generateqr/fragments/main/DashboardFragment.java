package com.example.generateqr.fragments.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.generateqr.R;
import com.example.generateqr.entities.QR;

public class DashboardFragment extends BaseMainFragment {

    private Button btnReferrerQR;
    private Button btnUplineQR;
    private Button btnGCQR;
    private Button btnCashQR;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void bindViews(View root) {
        btnReferrerQR = root.findViewById(R.id.btn_referrer_qr);
        btnUplineQR = root.findViewById(R.id.btn_upline_qr);
        btnGCQR = root.findViewById(R.id.btn_gc_qr);
        btnCashQR = root.findViewById(R.id.btn_cash_qr);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnReferrerQR.setOnClickListener(v-> callback.onReferralFormRequest());
        btnUplineQR.setOnClickListener(v-> callback.onUplineFormRequest());
        btnGCQR.setOnClickListener(v-> callback.onVoucherFormRequest(QR.TYPE_GC));
        btnCashQR.setOnClickListener(v-> callback.onVoucherFormRequest(QR.TYPE_CASH));
    }
}
