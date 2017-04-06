package org.hisp.india.trackercapture.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by nhancao on 4/6/17.
 */

public class PrefManager {

    private static final String IS_LOGIN = "IS_LOGIN";

    public static boolean isLogin() {
        return Hawk.get(IS_LOGIN, false);
    }

    public static void setIsLogin(boolean isLogin) {
        Hawk.put(IS_LOGIN, isLogin);
    }

}
