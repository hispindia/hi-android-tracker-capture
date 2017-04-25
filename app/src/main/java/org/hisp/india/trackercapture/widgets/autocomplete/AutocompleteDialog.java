package org.hisp.india.trackercapture.widgets.autocomplete;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.Window;
import android.widget.AutoCompleteTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.Model;

import java.util.List;

/**
 * Created by nhancao on 4/25/17.
 */

@EFragment(R.layout.dialog_autocomplete)
public class AutocompleteDialog<T extends Model> extends DialogFragment {

    @ViewById(R.id.dialog_autocomplete_et_search)
    AutoCompleteTextView etSearch;

    private List<T> modelList;
    private ItemClickListener<T> onItemClickListener;

    public static <T extends Model> AutocompleteDialog newInstance(List<T> modelList,
                                                                   ItemClickListener<T> onItemClickListener) {
        AutocompleteDialog<T> autocompleteDialog = AutocompleteDialog_.<T>builder().build();
        autocompleteDialog.setModelList(modelList);
        autocompleteDialog.setOnItemClickListener(onItemClickListener);
        return autocompleteDialog;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(Gravity.TOP);
        }
    }

    @AfterViews
    void init() {
        NAutoCompleteTextView<T> tAutoCompleteTextView = new NAutoCompleteTextView<>(etSearch);
        tAutoCompleteTextView.setModelList(modelList);
        tAutoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }

    public void show(FragmentManager manager) {
        super.show(manager, AutocompleteDialog.class.getSimpleName());
    }

    public void setModelList(List<T> modelList) {
        this.modelList = modelList;
    }

    public void setOnItemClickListener(ItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
