package com.example.generateqr.fragments.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.generateqr.R;
import com.example.generateqr.entities.QR;
import com.example.generateqr.utilities.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class QRCodeFragment extends BaseMainFragment {

    private static final String ARG_QR = "qr";
    private static final int MENU_ID_SHARE_QR = 1;
    private static final int MENU_ID_SHARE_URL = 2;

    private static final int REQUEST_PERMISSION_QR = 1;
    private static final int QR_EXPIRY_MINUTES = 5;
    private static final long MAX_QR_TIME = QR_EXPIRY_MINUTES * 60 * 1000L;
    private static final int QR_SIZE = 500;
    private static final long TIMER_TICK_DELAY = 1000L;

    private ImageView ivQRCode;
    private boolean clicked = false;

    private long remainingTime = 0;
    private Timer timer;

    private QR qr;

    public QRCodeFragment() {
    }

    public static QRCodeFragment newInstance(QR qr) {
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QR, qr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qr = (QR) getArguments().getSerializable(ARG_QR);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.qr_code_fragment;
    }

    @Override
    protected void bindViews(View root) {
        ivQRCode = root.findViewById(R.id.iv_qr_code);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateNewQr();
    }

    private void generateNewQr() {
        if (getContext() != null) {
            getActivity().runOnUiThread(() -> {
                qr.setExpiry(getNewExpiryDate());
                Bitmap bitmap = getQRCode(qr.encode());
                if (bitmap != null) {
                    ivQRCode.setImageBitmap(bitmap);
                    startQRExpiryTimer();
                }
            });
        }
    }

    private long getNewExpiryDate() {
        long currentMillis = System.currentTimeMillis();
        currentMillis += QR_EXPIRY_MINUTES * 60 * 1000L;
        return currentMillis;
    }

    private static final long MIN_QR_EXPIRY_DURATION = 3 * 60 * 1000; //3 minute
    private static final long MAX_QR_EXPIRY_DURATION_PROFILE = 60 * 60 * 1000; //60 minute

    private boolean isValidQRTime(Date expiryTime) {
        Date now = new Date();
        if (expiryTime.getTime() - now.getTime() < MIN_QR_EXPIRY_DURATION) {
            showToast(getString(R.string.err_invalid_duration_min));
            return false;
        }
        if (QR.TYPE_UPLINE_SELECTION.equals(qr.getQrType()) || QR.TYPE_REFERRAL_PROFILE.equals(qr.getQrType())) {
            if (expiryTime.getTime() - now.getTime() > MAX_QR_EXPIRY_DURATION_PROFILE) {
                showToast(getString(R.string.err_invalid_duration_max));
                return false;
            }
        }
        return true;
    }

    private Bitmap getQRCode(String data) {
        try {
            return QRCodeEncoder.encodeAsBitmap(
                    data, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE
            );
        } catch (WriterException e) {
            e.printStackTrace();
            showToast("QRCode error 1: [" + e.getMessage() + "]");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showToast("QRCode error 2: [" + e.getMessage() + "]");
        } catch (Exception e) {
            e.printStackTrace();
            showToast("QRCode error 3: [" + e.getMessage() + "]");
        }
        return null;
    }

    private static Uri getBitmapUri(Context context, Bitmap bitmap) {
        Uri uri = null;
        if (bitmap != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "qr_" + System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream os = context.getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    if (os != null) {
                        os.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_QR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void updateRemainingTimeText() {
        if (clicked != true) {
            if (getContext() != null) {
                //getActivity().runOnUiThread(() -> tvQRExpireTime.setText(getString(R.string.txt_qr_expiry, (remainingTime / 1000) + "")));
            }
        } else {

        }
    }

    private void startQRExpiryTimer() {
        remainingTime = MAX_QR_TIME;
        stopQRExpiryTime();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                remainingTime -= TIMER_TICK_DELAY;
                if (remainingTime <= 0) {
                    stopQRExpiryTime();
                    generateNewQr();
                } else {
                    updateRemainingTimeText();
                }
            }
        }, 0, TIMER_TICK_DELAY);
    }

    private void stopQRExpiryTime() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        stopQRExpiryTime();
        super.onDestroy();
    }

    @Override
    public String getTitle() {
        return getString(R.string.qr_code);
    }
}
