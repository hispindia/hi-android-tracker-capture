package org.hisp.india.trackercapture.models.base;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.trackercapture.utils.Constants;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import okio.ByteString;

/**
 * Created by nhancao on 4/9/17.
 */

public class Credentials implements Serializable {

    private String host;
    private String apiToken;
    private boolean loginSuccess;

    public Credentials() {
        host = Constants.HOST_DEFAULT;
    }

    private String genBasicToken(String username, String password) {
        try {
            String usernameAndPassword = username + ":" + password;
            byte[] bytes = usernameAndPassword.getBytes("ISO-8859-1");
            String encoded = ByteString.of(bytes).base64();
            return "Basic " + encoded;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String username, String password) {
        apiToken = genBasicToken(username, password);
        savePref();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        savePref();
    }

    public void clear() {
        apiToken = null;
        loginSuccess = false;
        savePref();
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
        savePref();
    }

    private void savePref() {
        Hawk.put(Constants.CREDENTIALS, this);
    }
}
