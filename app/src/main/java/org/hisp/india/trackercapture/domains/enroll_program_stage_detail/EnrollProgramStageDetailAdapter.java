package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.models.e_num.ValueType;
import org.hisp.india.trackercapture.models.storage.RDataElement;
import org.hisp.india.trackercapture.models.storage.ROption;
import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 5/10/17.
 */

public class EnrollProgramStageDetailAdapter extends BaseAdapter {

    private EnrollProgramStageDetailActivity activity;
    private List<RProgramStageDataElement> programStageDataElementList;

    public EnrollProgramStageDetailAdapter(EnrollProgramStageDetailActivity activity) {
        this.activity = activity;
        this.programStageDataElementList = new ArrayList<>();
    }

    public void setProgramStageDataElementList(
            List<RProgramStageDataElement> programStageDataElementList) {
        this.programStageDataElementList = programStageDataElementList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return programStageDataElementList.size();
    }

    @Override
    public RProgramStageDataElement getItem(int position) {
        return programStageDataElementList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_stage, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_label);
            holder.tvMandatory = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_mandatory);
            holder.etValue = (EditText) convertView.findViewById(R.id.item_enroll_profile_et_value);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;

        RProgramStageDataElement programStageDataElement = getItem(holder.ref);

        RDataElement item = programStageDataElement.getDataElement();

        holder.tvLabel.setText(item.getDisplayName());
        holder.tvMandatory.setVisibility(programStageDataElement.isCompulsory() ? View.VISIBLE : View.GONE);

        if (item.isOptionSetValue()
            || item.getValueType() == ValueType.BOOLEAN
            || item.getValueType() == ValueType.DATE
                ) {
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.etValue.setVisibility(View.GONE);

            holder.etValue.setText(item.getValue());
            holder.tvValue.setText(item.getValue());
            holder.tvValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                @Override
                public void after(Editable editable) {
                    getItem(holder.ref).getDataElement().setValue(editable.toString());
                }
            }));
        } else {
            holder.etValue.setVisibility(View.VISIBLE);
            holder.tvValue.setVisibility(View.GONE);

            holder.etValue.setText(item.getValue());
            holder.tvValue.setText(item.getValue());
            holder.etValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                @Override
                public void after(Editable editable) {
                    getItem(holder.ref).getDataElement().setValue(editable.toString());
                }
            }));
        }

        if (item.isOptionSetValue()) {
            List<Model> modelList = new ArrayList<>();
            for (ROption option : item.getOptionSet().getOptions()) {
                modelList.add(new BaseModel(option.getId(), option.getDisplayName()));
            }

            holder.tvValue.setOnClickListener((v) -> {
                AppUtils.hideKeyBoard(v);
                showOptionDialog(holder, modelList);
            });
        } else {
            switch (item.getValueType()) {
                case TEXT:
                case LONG_TEXT:
                case LETTER:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case NUMBER:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case DATE:
                    holder.tvValue.setOnClickListener((v) -> {
                        AppUtils.hideKeyBoard(v);
                        showDatePickerDialog(holder, programStageDataElement);
                    });
                    break;
                case DATE_TIME:
                case TIME:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_DATETIME);
                    break;
                case PHONE_NUMBER:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case BOOLEAN:
                    List<Model> modelList = new ArrayList<Model>() {
                        {
                            add(new BaseModel("0", "Yes"));
                            add(new BaseModel("1", "No"));
                        }
                    };
                    holder.tvValue.setOnClickListener((v) -> {
                        AppUtils.hideKeyBoard(v);
                        showOptionDialog(holder, modelList);
                    });
                    break;
                default:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }

        return convertView;
    }

    private void showOptionDialog(ViewHolder holder, List<Model> modelList) {
        OptionDialog.newInstance(modelList, model -> {
            holder.tvValue.setText(model.getDisplayName());
        }).show(activity.getSupportFragmentManager());
    }

    private void showDatePickerDialog(ViewHolder holder, RProgramStageDataElement item) {
        DatePickerDialog
                datePicker = DatePickerDialog
                .newInstance(item.isAllowFutureDate());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            holder.tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(activity.getSupportFragmentManager());
    }

    private class ViewHolder {
        private TextView tvLabel;
        private TextView tvMandatory;
        private EditText etValue;
        private TextView tvValue;
        private int ref;
    }
}
