package com.example.generateqr.entities;

import androidx.annotation.DrawableRes;

import com.example.generateqr.utilities.EncryptionUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class QR implements Serializable {

    public static final String TYPE_GC = "type_gc";
    public static final String TYPE_CASH = "type_cash";
    public static final String TYPE_REFERRAL_PROFILE = "type_profile";
    public static final String TYPE_UPLINE_SELECTION = "type_referral";

    private String qrType;
    private String personName;
    private String referrer;
    private String upline;
    private String uplineDirection;
    private @DrawableRes
    int personImage;
    private double value;
    private long expiry;

    private @DrawableRes
    int uplineImage;
    private String uplineName;

    public QR() {

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getQrType() {
        return qrType;
    }

    public void setQrType(String qrType) {
        this.qrType = qrType;
    }

    public int getPersonImage() {
        return personImage;
    }

    public void setPersonImage(int personImage) {
        this.personImage = personImage;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getExpiryInDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(expiry);
        return calendar.getTime();
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUpline() {
        return upline;
    }

    public void setUpline(String upline) {
        this.upline = upline;
    }

    public int getUplineImage() {
        return uplineImage;
    }

    public void setUplineImage(int uplineImage) {
        this.uplineImage = uplineImage;
    }

    public String getUplineName() {
        return uplineName;
    }

    public void setUplineName(String uplineName) {
        this.uplineName = uplineName;
    }

    public String getUplineDirection() {
        return uplineDirection;
    }

    public void setUplineDirection(String uplineDirection) {
        this.uplineDirection = uplineDirection;
    }

    public String encode() {
        return EncryptionUtils.getInstance().encrypt(new Gson().toJson(this));
    }

    public static QR decode(String json) {
        try {
            return new Gson().fromJson(EncryptionUtils.getInstance().decrypt(json), QR.class);
        } catch (Exception e) {
            return null;
        }
    }
}
