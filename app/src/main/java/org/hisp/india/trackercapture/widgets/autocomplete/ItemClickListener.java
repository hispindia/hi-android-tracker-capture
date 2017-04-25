package org.hisp.india.trackercapture.widgets.autocomplete;

import android.view.View;
import android.widget.AdapterView;

import org.hisp.india.trackercapture.models.base.Model;

/**
 * Created by nhancao on 4/25/17.
 */

public interface ItemClickListener<T extends Model> {

    void onItemClick(AdapterView<?> parent, View view, int position, long id, T model);

}
