package org.hisp.india.trackercapture.widgets.option.tracked_entity_instance;

import android.content.Context;
import android.widget.Filter;

import org.hisp.india.trackercapture.models.base.Attribute;
import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.option.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 4/26/17.
 */

public abstract class TrackedEntityInstanceAdapter extends OptionAdapter<TrackedEntityInstance> {

    public TrackedEntityInstanceAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    List<TrackedEntityInstance> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString();


                    for (TrackedEntityInstance item : originalList) {
                        String search = "";
                        for (Attribute attribute : item.getAttributePreview()) {
                            search += String.format("%s ", attribute.getValue());
                        }

                        if (AppUtils.isContainText(searchStr, search)) {
                            resultsData.add(item);
                        }
                    }
                    results.count = resultsData.size();
                    results.values = resultsData;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint == null || constraint.length() == 0) {
                    replaceAll(originalList);
                } else {
                    replaceAll((List<TrackedEntityInstance>) results.values);
                }
            }
        };
    }

}
