package org.hisp.india.trackercapture.widgets.autocomplete;


import android.content.Context;
import android.util.AttributeSet;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.Model;

import java.util.List;

/**
 * Created by nhancao on 4/23/17.
 */

public class NAutoCompleteTextView<T extends Model> extends android.widget.AutoCompleteTextView {

    private DefaultAutoCompleteAdapter<T> adapter;
    private T selected;

    public NAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        adapter = new DefaultAutoCompleteAdapter<>(getContext(), R.layout.item_autocomplete);
        setAdapter(adapter);
        setThreshold(1);
        setOnItemClickListener((parent, view, position, id) -> {
            selected = adapter.getItem(position);
            if (selected != null) {
                setText(selected.getDisplayName());
            }
        });
    }

    @Override
    public DefaultAutoCompleteAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(DefaultAutoCompleteAdapter<T> adapter) {
        this.adapter = adapter;
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
