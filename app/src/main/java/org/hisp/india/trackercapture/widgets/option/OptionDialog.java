package org.hisp.india.trackercapture.widgets.option;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.autocomplete.ItemClickListener;

import java.util.List;

/**
 * Created by nhancao on 4/25/17.
 */

@EFragment(R.layout.dialog_option)
public class OptionDialog<T extends Model> extends DialogFragment {
    private static final String TAG = OptionDialog.class.getSimpleName();

    @ViewById(R.id.dialog_option_et_search)
    protected EditText etSearch;
    @ViewById(R.id.dialog_option_lv_item)
    protected ListView lvItem;

    private ItemClickListener<T> onItemClickListener;
    private OptionAdapter<T> adapter;
    private List<T> modelList;

    public static <T extends Model> OptionDialog newInstance(List<T> modelList,
                                                             ItemClickListener<T> onItemClickListener) {
        OptionDialog<T> optionDialog = OptionDialog_.<T>builder().build();
        optionDialog.setModelList(modelList);
        optionDialog.setOnItemClickListener(onItemClickListener);
        return optionDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @AfterViews
    void init() {

        adapter = new OptionAdapter<T>(getContext(), R.layout.item_dialog_option) {
            @Override
            protected void convert(BaseAdapterHelper helper, T item) {
                TextView tvDisplay = helper.getView(R.id.item_dialog_option_title);
                tvDisplay.setText(AppUtils.highlightText(etSearch.getText().toString(), item.getDisplayName(),
                                                         Color.parseColor("#7A7986")));
            }
        };
        lvItem.setAdapter(adapter);
        if (modelList != null) {
            adapter.replaceAll(modelList);
        }

        etSearch.addTextChangedListener(new NTextChange(new NTextChange.TextListener() {
            @Override
            public void after(Editable editable) {
                String query = etSearch.getText().toString();
                adapter.getFilter().filter(query);
            }

            @Override
            public void before() {

            }
        }));

    }

    @ItemClick(R.id.dialog_option_lv_item)
    void lvItemClick(T model) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(model);
        }
        dismiss();
    }

    public void setOnItemClickListener(ItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setModelList(List<T> modelList) {
        this.modelList = modelList;
    }

    public void show(FragmentManager manager) {
        super.show(manager, OptionDialog.class.getSimpleName());
    }

}
