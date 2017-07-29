package org.hisp.india.trackercapture.widgets.option.tracked_entity_instance;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.ItemClickListener;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.DefaultOptionAdapter;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.List;

/**
 * Created by YesKone on 29-Jul-17.
 */

@EFragment(R.layout.dialog_option)
public class TrackedEntityInstanceDialog  extends DialogFragment {

    @ViewById(R.id.dialog_option_lv_search)
    protected LinearLayout lvSearch;
    @ViewById(R.id.dialog_option_et_search)
    protected EditText etSearch;
    @ViewById(R.id.dialog_option_lv_item)
    protected ListView lvItem;

    private ItemClickListener<TrackedEntityInstance> onItemClickListener;
    private TrackedEntityInstanceAdapter adapter;
    private List<TrackedEntityInstance> modelList;

    public static  TrackedEntityInstanceDialog newInstance(List<TrackedEntityInstance> modelList,
                                                             ItemClickListener<TrackedEntityInstance> onItemClickListener) {
        TrackedEntityInstanceDialog dialog = TrackedEntityInstanceDialog_.builder().build();
        dialog.setModelList(modelList);
        dialog.setOnItemClickListener(onItemClickListener);
        return dialog;
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

        adapter = new TrackedEntityInstanceAdapter<TrackedEntityInstance>(getContext(), R.layout.item_dialog_option) {
            @Override
            protected void convert(BaseAdapterHelper helper, TrackedEntityInstance item) {
                TextView tvDisplay = helper.getView(R.id.item_dialog_option_title);
                tvDisplay.setText(AppUtils.highlightText(etSearch.getText().toString(), item.getDisplayName(),
                        Color.parseColor("#7A7986")));
            }
        };
        lvItem.setAdapter(adapter);
        if (modelList != null) {
            adapter.replaceAll(modelList);
            lvSearch.setVisibility((modelList.size() <= 5) ? View.GONE : View.VISIBLE);
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
    void lvItemClick(TrackedEntityInstance model) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(model);
        }
        dismiss();
    }

    public void setOnItemClickListener(ItemClickListener<TrackedEntityInstance> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setModelList(List<TrackedEntityInstance> modelList) {
        this.modelList = modelList;
    }

    public void show(FragmentManager manager) {
        super.show(manager, OptionDialog.class.getSimpleName());
    }





}
