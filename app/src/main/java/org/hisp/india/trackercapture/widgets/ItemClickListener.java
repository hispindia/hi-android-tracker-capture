package org.hisp.india.trackercapture.widgets;

import org.hisp.india.trackercapture.models.base.Model;

/**
 * Created by nhancao on 4/25/17.
 */

public interface ItemClickListener<T extends Model> {

    void onItemClick(T model);

}
