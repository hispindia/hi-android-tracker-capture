package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nhancao on 5/10/17.
 */

public class PageResponse {

    @Expose
    @SerializedName("page")
    private int page;
    @Expose
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("pageSize")
    private int pageSize;
    @Expose
    @SerializedName("pageCount")
    private int pageCount;

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }
}
