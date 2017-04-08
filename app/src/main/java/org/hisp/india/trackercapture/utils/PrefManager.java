package org.hisp.india.trackercapture.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by nhancao on 4/6/17.
 */

public class PrefManager {

    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String HOST = "HOST";

    public static boolean isLogin() {
        return Hawk.get(IS_LOGIN, false);
    }

    public static void setIsLogin(boolean isLogin) {
        Hawk.put(IS_LOGIN, isLogin);
    }

    public static boolean getHost() {
        return Hawk.get(HOST, false);
    }

    public static void setHost(String host) {
        Hawk.put(HOST, host);
    }

}
