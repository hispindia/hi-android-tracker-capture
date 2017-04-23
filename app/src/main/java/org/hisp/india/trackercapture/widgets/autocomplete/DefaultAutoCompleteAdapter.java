package org.hisp.india.trackercapture.widgets.autocomplete;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.Model;
import org.hisp.india.trackercapture.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 4/23/17.
 */

public class DefaultAutoCompleteAdapter<T extends Model> extends ArrayAdapter<T> implements Filterable {
    private static final String TAG = DefaultAutoCompleteAdapter.class.getSimpleName();

    @LayoutRes
    private int resource;
    private List<T> originalList;
    private List<T> resultList;
    private String searchText;

    public DefaultAutoCompleteAdapter(
            @NonNull
                    Context context,
            @LayoutRes
                    int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public T getItem(int position) {
        return resultList.get(position);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                searchText = (String) constraint;
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    List<Model> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString();
                    for (Model item : originalList) {
                        if (AppUtils.isContainText(searchStr, item.getDisplayName())) {
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
                    resultList = originalList;
                } else {
                    resultList = (List<T>) results.values;
                }
                notifyDataSetChanged();
            }
        };

    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable
                                View convertView,
                        @NonNull
                                ViewGroup parent) {
        Model model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        String displayName = model != null ? model.getDisplayName() : "";

        TextView tvDisplay = (TextView) convertView.findViewById(R.id.item_autocomplete_tv_display);
        tvDisplay.setText(AppUtils.highlightText(searchText, displayName));
        return convertView;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        if (resultList != null)
            return resultList.size();
        return 0;
    }

    public void setResultList(List<T> resultList) {
        this.originalList = resultList;
        this.resultList = resultList;
        notifyDataSetChanged();
    }


}
