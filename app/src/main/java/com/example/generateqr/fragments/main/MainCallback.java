package com.example.generateqr.fragments.main;

import com.example.generateqr.entities.QR;

public interface MainCallback {

    void showProgress(String text);

    void hideProgress();

    void popLastFragment();

    void onReferralFormRequest();

    void onUplineFormRequest();

    void onVoucherFormRequest(String type);

    void onQRGenerated(QR qr);


}
