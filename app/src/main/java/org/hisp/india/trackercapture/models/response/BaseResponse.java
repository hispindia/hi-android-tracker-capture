package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhancao on 5/9/17.
 */

public class BaseResponse {
    @SerializedName("httpStatus")
    private String httpStatus;
    @SerializedName("httpStatusCode")
    private int httpStatusCode;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
