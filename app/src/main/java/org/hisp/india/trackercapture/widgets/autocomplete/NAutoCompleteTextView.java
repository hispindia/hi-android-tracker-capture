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
    private ItemClickListener<T> onItemClickListener;

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
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(selected);
                }
            }
        });

        //Config auto show dialog
        autoCompleteTextView.setOnClickListener(v -> {
            autoCompleteTextView.showDropDown();
        });
    }

    public DefaultAutoCompleteAdapter<T> getAdapter() {
        return adapter;
    }

    public void setModelList(List<T> modelList) {
        adapter.setModelList(modelList);
        autoCompleteTextView.post(() -> autoCompleteTextView.showDropDown());
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public void setOnItemClickListener(ItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
