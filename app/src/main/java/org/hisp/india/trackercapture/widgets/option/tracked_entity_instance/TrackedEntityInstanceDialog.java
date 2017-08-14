package org.hisp.india.trackercapture.widgets.option.tracked_entity_instance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import com.joanzapata.android.QuickAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.Attribute;
import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.ItemClickListener;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YesKone on 29-Jul-17.
 */

@EFragment(R.layout.dialog_tracked_entity_instance)
public class TrackedEntityInstanceDialog extends DialogFragment {
    private static final String TAG = TrackedEntityInstanceDialog.class.getSimpleName();

    @ViewById(R.id.dialog_tracked_entity_instance_lv_search)
    protected LinearLayout lvSearch;
    @ViewById(R.id.dialog_tracked_entity_instance_et_search)
    protected EditText etSearch;
    @ViewById(R.id.dialog_tracked_entity_instance_lv_item)
    protected ListView lvItem;
    @ViewById(R.id.dialog_tracked_entity_instance_loading_indicator)
    protected AVLoadingIndicatorView vLoadingIndicator;
    @ViewById(R.id.dialog_tracked_entity_instance_tv_empty)
    protected TextView tvEmpty;
    @FragmentArg
    protected String orgUnitId;
    private ItemClickListener<TrackedEntityInstance> onItemClickListener;
    private TrackedEntityInstanceAdapter adapter;
    private TrackedEntityInstanceService trackedEntityInstanceService;

    public static TrackedEntityInstanceDialog newInstance(String orgUnitId,
                                                          TrackedEntityInstanceService trackedEntityInstanceService,
                                                          ItemClickListener<TrackedEntityInstance> onItemClickListener) {
        TrackedEntityInstanceDialog dialog = TrackedEntityInstanceDialog_.builder()
                                                                         .orgUnitId(orgUnitId)
                                                                         .build();
        dialog.setOnItemClickListener(onItemClickListener);
        dialog.setTrackedEntityInstanceService(trackedEntityInstanceService);
        return dialog;
    }

    public void loadData() {
        RProgram houseHoldProgram = ProgramQuery.getProgramByName("Household");
        if (houseHoldProgram == null) return;

        showLoading(true);
        trackedEntityInstanceService.getTrackedEntityInstances(orgUnitId, houseHoldProgram.getId())
                                    .compose(RxScheduler.applyIoSchedulers())
                                    .doOnTerminate(() -> {
                                        showLoading(false);
                                    })
                                    .subscribe(trackedEntityInstancesResponse -> {
                                        setModelList(trackedEntityInstancesResponse.getTrackedEntityInstances());
                                    });
    }

    public void showLoading(boolean show) {
        if (vLoadingIndicator == null) return;
        if (show) {
            vLoadingIndicator.smoothToShow();
        } else {
            vLoadingIndicator.smoothToHide();
        }
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

        adapter = new TrackedEntityInstanceAdapter(getContext(), R.layout.item_dialog_option) {
            @Override
            protected void convert(BaseAdapterHelper helper, TrackedEntityInstance item) {
                TextView tvDisplay = helper.getView(R.id.item_dialog_option_title);

                String display = "";
                for (Attribute attribute : item.getAttributePreview()) {
                    display += String.format("%s: %s\n", attribute.getDisplayName(), attribute.getValue());
                }

                tvDisplay.setText(AppUtils.highlightText(etSearch.getText().toString(), display,
                                                         Color.parseColor("#7A7986")));

                helper.getView().setOnClickListener(view -> {
                    showDialogInfo(item);
                });
            }

            private void showDialogInfo(TrackedEntityInstance item) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }

                View view = View.inflate(getContext(), R.layout.dialog_info, null);

                ListView lvInfoItem = view.findViewById(R.id.dialog_info_lv_item);
                lvInfoItem.setAdapter(
                        new QuickAdapter<Attribute>(getContext(), R.layout.item_dialog_info, item.getAttributeList()) {
                            @Override
                            protected void convert(BaseAdapterHelper helper, Attribute item) {
                                helper.setText(R.id.item_dialog_info_title, item.getDisplayName());
                                helper.setText(R.id.item_dialog_info_value, item.getValue());
                            }
                        });

                builder.setTitle("Info")
                       .setView(view)
                       .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                           if (onItemClickListener != null) {
                               onItemClickListener.onItemClick(item);
                           }
                           dismiss();
                       })
                       .setNegativeButton(android.R.string.no, (dialog, which) -> {
                           // do nothing
                       })
                       .setIcon(android.R.drawable.ic_dialog_info)
                       .show();
            }
        };
        lvItem.setAdapter(adapter);

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
        loadData();
    }

    public void setOnItemClickListener(ItemClickListener<TrackedEntityInstance> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Attribute getMainAttribute(TrackedEntityInstance trackedEntityInstance) {
        for (Attribute attribute : trackedEntityInstance.getAttributeList()) {
            if (attribute.getDisplayName().contains("House number")) {
                return attribute;
            }
        }
        return null;
    }

    public void setModelList(List<TrackedEntityInstance> _modelList) {
        List<TrackedEntityInstance> modelList = new ArrayList<>();
        for (TrackedEntityInstance trackedEntityInstance : _modelList) {
            Attribute attribute = getMainAttribute(trackedEntityInstance);
            if (attribute != null) {
                trackedEntityInstance.setDisplayName(attribute.getDisplayName());
                trackedEntityInstance.setValue(attribute.getValue());
                modelList.add(trackedEntityInstance);
            }
            trackedEntityInstance.initAttributePreview();
        }
        if (modelList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvSearch.setVisibility(View.GONE);
        } else if (adapter != null && lvSearch != null) {
            tvEmpty.setVisibility(View.GONE);
            adapter.replaceAll(modelList);
            lvSearch.setVisibility((modelList.size() <= 5) ? View.GONE : View.VISIBLE);
        }
    }

    public void setTrackedEntityInstanceService(TrackedEntityInstanceService trackedEntityInstanceService) {
        this.trackedEntityInstanceService = trackedEntityInstanceService;
    }

    public void show(FragmentManager manager) {
        super.show(manager, OptionDialog.class.getSimpleName());
    }

}
