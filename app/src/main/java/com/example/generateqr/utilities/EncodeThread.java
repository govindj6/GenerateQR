package com.example.generateqr.utilities;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.generateqr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


final class EncodeThread extends Thread {

    private static final String TAG = EncodeThread.class.getSimpleName();

    private final String contents;
    private final Handler handler;
    private final int pixelResolution;
    private final BarcodeFormat format;

    EncodeThread(String contents, Handler handler, int pixelResolution, BarcodeFormat format) {
        this.contents = contents;
        this.handler = handler;
        this.pixelResolution = pixelResolution;
        this.format = format;
    }

    @Override
    public void run() {
        try {
            Bitmap bitmap = QRCodeEncoder.encodeAsBitmap(contents, format, pixelResolution, pixelResolution);
            Message message = Message.obtain(handler, R.string.encode_succeeded);
            message.obj = bitmap;
            message.sendToTarget();
        } catch (WriterException e) {
            Log.e(TAG, "Could not encode barcode", e);
            Message message = Message.obtain(handler, R.string.encode_failed);
            message.sendToTarget();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Could not encode barcode", e);
            Message message = Message.obtain(handler, R.string.encode_failed);
            message.sendToTarget();
        }
    }
}