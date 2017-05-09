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
    @SerializedName("response")
    private Response response;

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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        @SerializedName("responseType")
        private String responseType;
        @SerializedName("status")
        private String status;
        @SerializedName("reference")
        private String reference;

        public String getResponseType() {
            return responseType;
        }

        public void setResponseType(String responseType) {
            this.responseType = responseType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        @Override
        public String toString() {
            return "Response{" +
                   "responseType='" + responseType + '\'' +
                   ", status='" + status + '\'' +
                   ", reference='" + reference + '\'' +
                   '}';
        }
    }

}
