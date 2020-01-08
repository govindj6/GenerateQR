package com.example.generateqr.fragments.main;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.generateqr.R;
import com.example.generateqr.entities.QR;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ReferralFormFragment extends BaseMainFragment {

    private EditText edtName;
    private EditText edtReferralId;
    private EditText edtExpiryDate;
    private Button btnGenerate;

    private long expiry;

    public static ReferralFormFragment newInstance() {
        ReferralFormFragment fragment = new ReferralFormFragment();
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_referral_form;
    }

    @Override
    protected void bindViews(View root) {
        edtName = root.findViewById(R.id.edt_person_name);
        edtReferralId = root.findViewById(R.id.edt_referral_id);
        edtExpiryDate = root.findViewById(R.id.edt_expiry_date);
        btnGenerate = root.findViewById(R.id.btn_generate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtExpiryDate.setOnClickListener(view1 -> datePickerDialog());

        btnGenerate.setOnClickListener(view1 -> handleGenerateButton());

    }

    private void datePickerDialog(){

        Calendar calendar =Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                edtExpiryDate.setText(day+"-"+(month+1)+"-"+year);
                Calendar c = Calendar.getInstance();
                c.set(year,month+1,day);
                expiry = c.getTimeInMillis();
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }

    private void handleGenerateButton(){
        if (edtName.getText().toString().trim().length() == 0){
            edtName.setError("Please enter name");
            edtName.requestFocus();
            return;
        }
        if (edtReferralId.getText().toString().trim().length() == 0){
            edtReferralId.setError("Please enter referral id");
            edtReferralId.requestFocus();
            return;
        }
        if (expiry == 0){
            edtExpiryDate.setError("Please select date");
            return;
        }

        QR qr = new QR();
        qr.setPersonName(edtName.getText().toString().trim());
        qr.setReferrer(edtReferralId.getText().toString().trim());
        qr.setExpiry(expiry);
        callback.onQRGenerated(qr);
    }
}
