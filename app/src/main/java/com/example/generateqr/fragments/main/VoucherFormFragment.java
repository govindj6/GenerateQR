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

import java.util.Calendar;

public class VoucherFormFragment extends BaseMainFragment{

    private static final String ARG_TYPE = "type";

    private EditText edtName;
    private EditText edtAmount;
    private EditText edtExpiryDate;
    private Button btnGenerate;

    private String type;

    private long expiry;

    public static VoucherFormFragment newInstance(String type) {
        VoucherFormFragment fragment = new VoucherFormFragment();
        Bundle args= new Bundle();
        args.putString(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_gc_form;
    }

    @Override
    protected void bindViews(View root) {
        edtName = root.findViewById(R.id.edt_name);
        edtAmount = root.findViewById(R.id.edt_amount);
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
        if (edtAmount.getText().toString().trim().length() == 0){
            edtAmount.setError("Please enter amount");
            edtAmount.requestFocus();
            return;
        }
        if (expiry == 0){
            edtExpiryDate.setError("Please select date");
            return;
        }

        QR qr = new QR();
        if (type.equalsIgnoreCase(QR.TYPE_CASH)){
            qr.setQrType(QR.TYPE_CASH);
        }else {
            qr.setQrType(QR.TYPE_GC);
        }
        qr.setPersonName(edtName.getText().toString().trim());
        qr.setValue(Double.parseDouble(edtAmount.getText().toString().trim()));
        qr.setExpiry(expiry);
        callback.onQRGenerated(qr);
    }
}
