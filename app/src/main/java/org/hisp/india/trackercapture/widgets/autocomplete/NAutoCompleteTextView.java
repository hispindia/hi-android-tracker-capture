package org.hisp.india.trackercapture.widgets.autocomplete;


import android.widget.AutoCompleteTextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.Model;

import java.util.List;

/**
 * Created by nhancao on 4/23/17.
 */

public class NAutoCompleteTextView<T extends Model> {
    private static final String TAG = NAutoCompleteTextView.class.getSimpleName();

    private AutoCompleteTextView autoCompleteTextView;
    private DefaultAutoCompleteAdapter<T> adapter;
    private T selected;

    public NAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
        init();
    }

    public void init() {
        adapter = new DefaultAutoCompleteAdapter<>(autoCompleteTextView.getContext(), R.layout.item_autocomplete);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            selected = adapter.getItem(position);
            if (selected != null) {
                autoCompleteTextView.setText(selected.getDisplayName());
            }
        });
    }

    public DefaultAutoCompleteAdapter<T> getAdapter() {
        return adapter;
    }

    public void setModelList(List<T> modelList) {
        adapter.setModelList(modelList);
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }
}
