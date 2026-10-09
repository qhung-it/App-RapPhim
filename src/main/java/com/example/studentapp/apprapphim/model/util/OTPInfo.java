package com.example.studentapp.apprapphim.model.util;

public class OTPInfo {

    private final String otp;
    private final long expireTime;

    public OTPInfo(String otp, long expireTime) {
        this.otp = otp;
        this.expireTime = expireTime;
    }

    public String getOtp() {
        return otp;
    }

    public long getExpireTime() {
        return expireTime;
    }
}