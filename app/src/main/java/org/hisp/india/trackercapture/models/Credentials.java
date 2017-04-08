package org.hisp.india.trackercapture.models;

import java.io.UnsupportedEncodingException;

import okio.ByteString;

/**
 * Created by nhancao on 4/9/17.
 */

public class Credentials {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String genBasicToken() {
        try {
            String usernameAndPassword = username + ":" + password;
            byte[] bytes = usernameAndPassword.getBytes("ISO-8859-1");
            String encoded = ByteString.of(bytes).base64();
            return "Basic " + encoded;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
