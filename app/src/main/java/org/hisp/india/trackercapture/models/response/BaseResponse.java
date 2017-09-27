package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nhancao on 5/9/17.
 */

public class BaseResponse {
    @Expose
    @SerializedName("httpStatus")
    private String httpStatus;
    @Expose
    @SerializedName("httpStatusCode")
    private int httpStatusCode;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("response")
    private Response response;
    @Expose
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
        @Expose
        @SerializedName("responseType")
        private String responseType;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("reference")
        private String reference;
        @Expose
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

        @Expose
        @SerializedName("imported")
        private int imported;
        @Expose
        @SerializedName("updated")
        private int updated;
        @Expose
        @SerializedName("ignored")
        private int ignored;
        @Expose
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
