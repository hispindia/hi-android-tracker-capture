package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("importCount")
    private ImportCount importCount;

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

    @Override
    public String toString() {
        return "BaseResponse{" +
               "httpStatus='" + httpStatus + '\'' +
               ", httpStatusCode=" + httpStatusCode +
               ", status='" + status + '\'' +
               ", message='" + message + '\'' +
               ", response=" + response +
               '}';
    }

    public class Response {
        @SerializedName("responseType")
        private String responseType;
        @SerializedName("status")
        private String status;
        @SerializedName("reference")
        private String reference;
        @SerializedName("importSummaries")
        private List<Response> importSummaries;

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

        public List<Response> getImportSummaries() {
            return importSummaries;
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

    public class ImportCount {

        @SerializedName("imported")
        private int imported;
        @SerializedName("updated")
        private int updated;
        @SerializedName("ignored")
        private int ignored;
        @SerializedName("deleted")
        private int deleted;

        public int getImported() {
            return imported;
        }

        public int getUpdated() {
            return updated;
        }

        public int getIgnored() {
            return ignored;
        }

        public int getDeleted() {
            return deleted;
        }
    }


}
