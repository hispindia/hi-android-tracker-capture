package org.hisp.india.trackercapture.widgets.option;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import com.joanzapata.android.QuickAdapter;

import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YesKone on 29-Jul-17.
 */

public abstract class OptionAdapter<T> extends QuickAdapter<T> implements Filterable {

    protected List<T> originalList;

    public OptionAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        originalList = new ArrayList<>();
    }

    @Override
    public void replaceAll(List<T> elem) {
        super.replaceAll(elem);
        originalList = elem;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
