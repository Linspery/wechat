package com.linspery.bean;

/**
 * Created by Linspery on 15/12/1.
 */
public class AccessToken {
    private static String accesstoken;
    private static int expiresin;

    public static String getAccesstoken() {
        return accesstoken;
    }

    public static void setAccesstoken(String accesstoken) {
        AccessToken.accesstoken = accesstoken;
    }

    public static int getExpiresin() {
        return expiresin;
    }

    public static void setExpiresin(int expiresin) {
        AccessToken.expiresin = expiresin;
    }
}
