package org.hisp.india.trackercapture.widgets.option.tracked_entity_instance;

import android.app.AlertDialog;
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
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.ItemClickListener;
import org.hisp.india.trackercapture.widgets.NDialog;
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

        trackedEntityInstanceService.getTrackedEntityInstancesLocal(orgUnitId, houseHoldProgram.getId())
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> showLoading(false))
                .subscribe(rTrackedEntityInstances -> {
                    List<TrackedEntityInstance> trackedEntityInstances = new ArrayList<>();
                    for (RTrackedEntityInstance rTrackedEntityInstance : rTrackedEntityInstances) {
                        trackedEntityInstances.add(RMapping.from(rTrackedEntityInstance));
                    }
                    setModelList(trackedEntityInstances);
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

        adapter = new TrackedEntityInstanceAdapter(getContext(), R.layout.item_dialog_tei_option) {
            @Override
            protected void convert(BaseAdapterHelper helper, TrackedEntityInstance item) {
                TextView tvLabel1 = helper.getView(R.id.item_dialog_tei_option_label1);
                TextView tvLabel2 = helper.getView(R.id.item_dialog_tei_option_label2);
                TextView tvLabel3 = helper.getView(R.id.item_dialog_tei_option_label3);

                TextView tvAtt1 = helper.getView(R.id.item_dialog_tei_option_att1);
                TextView tvAtt2 = helper.getView(R.id.item_dialog_tei_option_att2);
                TextView tvAtt3 = helper.getView(R.id.item_dialog_tei_option_att3);

                List<Attribute> attributePreviewList = item.getAttributePreview();
                if (attributePreviewList.size() > 0) {
                    Attribute attribute = attributePreviewList.get(0);
                    tvLabel1.setText(attribute.getDisplayName());
                    tvAtt1.setText(AppUtils.highlightText(etSearch.getText().toString(),
                            attribute.getValue(),
                            Color.parseColor("#7A7986")));

                    tvLabel2.setVisibility(View.GONE);
                    tvAtt2.setVisibility(View.GONE);
                    tvLabel3.setVisibility(View.GONE);
                    tvAtt3.setVisibility(View.GONE);
                }
                if (attributePreviewList.size() > 1) {
                    tvLabel2.setVisibility(View.VISIBLE);
                    tvAtt2.setVisibility(View.VISIBLE);
                    Attribute attribute = attributePreviewList.get(1);
                    tvLabel2.setText(attribute.getDisplayName());
                    tvAtt2.setText(AppUtils.highlightText(etSearch.getText().toString(),
                            attribute.getValue(),
                            Color.parseColor("#7A7986")));

                    tvLabel3.setVisibility(View.GONE);
                    tvAtt3.setVisibility(View.GONE);
                }
                if (attributePreviewList.size() > 2) {
                    tvLabel3.setVisibility(View.VISIBLE);
                    tvAtt3.setVisibility(View.VISIBLE);
                    Attribute attribute = attributePreviewList.get(2);
                    tvLabel3.setText(attribute.getDisplayName());
                    tvAtt3.setText(AppUtils.highlightText(etSearch.getText().toString(),
                            attribute.getValue(),
                            Color.parseColor("#7A7986")));
                }

                helper.getView().setOnClickListener(view -> {
                    showDialogInfo(item);
                });
            }

            private void showDialogInfo(TrackedEntityInstance item) {
                AlertDialog.Builder builder = NDialog.newAlertBuilder(context, R.layout.dialog_info, view -> {
                    ListView lvInfoItem = view.findViewById(R.id.dialog_info_lv_item);
                    lvInfoItem.setAdapter(
                            new QuickAdapter<Attribute>(getContext(), R.layout.item_dialog_info, item.getAttributeList()) {
                                @Override
                                protected void convert(BaseAdapterHelper helper, Attribute item1) {
                                    helper.setText(R.id.item_dialog_info_title, item1.getDisplayName());
                                    helper.setText(R.id.item_dialog_info_value, item1.getValue());
                                }
                            });
                });

                builder
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(item);
                            }
                            dismiss();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> {
                            // do nothing
                        })
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
