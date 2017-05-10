package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhancao on 5/10/17.
 */

public class PageResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("total")
    private int total;
    @SerializedName("pageSize")
    private int pageSize;
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
