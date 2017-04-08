package org.hisp.india.trackercapture.utils;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.trackercapture.models.UserResponse;

/**
 * Created by nhancao on 4/6/17.
 */

public class PrefManager {

    private static final String HOST = "HOST";
    private static final String HOST_DEFAULT = "http://localhost:8080/dhis/";
    private static final String API_TOKEN = "API_TOKEN";
    private static final String USER_INFO = "USER_INFO";

    public static boolean isLogin() {
        return getUserInfo() != null;
    }

    public static String getHost() {
        return Hawk.get(HOST, HOST_DEFAULT);
    }

    public static void setHost(String host) {
        Hawk.put(HOST, host);
    }

    public static String getApiToken() {
        return Hawk.get(API_TOKEN, null);
    }

    public static void setApiToken(String apiToken) {
        Hawk.put(API_TOKEN, apiToken);
    }

    public static UserResponse getUserInfo() {
        return Hawk.get(USER_INFO, null);
    }

    public static void setUserInfo(UserResponse userInfo) {
        Hawk.put(USER_INFO, userInfo);
    }
}
