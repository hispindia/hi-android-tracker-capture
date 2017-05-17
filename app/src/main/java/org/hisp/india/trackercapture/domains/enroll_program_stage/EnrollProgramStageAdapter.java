package org.hisp.india.trackercapture.domains.enroll_program_stage;

import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.models.storage.ROption;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

import static org.hisp.india.trackercapture.models.e_num.ValueType.BOOLEAN;
import static org.hisp.india.trackercapture.models.e_num.ValueType.DATE;

/**
 * Created by nhancao on 5/10/17.
 */

public class EnrollProgramStageAdapter extends BaseAdapter {

    public String programName;
    public EnrollProgramStageActivity activity;
    public List<RProgramTrackedEntityAttribute> programTrackedEntityAttributeList;

    public EnrollProgramStageAdapter(EnrollProgramStageActivity activity, String programName) {
        this.programName = programName;
        this.activity = activity;
        this.programTrackedEntityAttributeList = new ArrayList<>();
    }

    public List<RProgramTrackedEntityAttribute> getProgramTrackedEntityAttributeList() {
        return programTrackedEntityAttributeList;
    }

    public void setProgramTrackedEntityAttributeList(
            List<RProgramTrackedEntityAttribute> programTrackedEntityAttributeList) {
        this.programTrackedEntityAttributeList = programTrackedEntityAttributeList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return programTrackedEntityAttributeList.size();
    }

    @Override
    public RProgramTrackedEntityAttribute getItem(int position) {
        return programTrackedEntityAttributeList.get(position);
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
            convertView = LayoutInflater.from(activity)
                                        .inflate(R.layout.item_enroll_profile, parent, false);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_label);
            holder.tvMandatory = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_mandatory);
            holder.etValue = (EditText) convertView.findViewById(R.id.item_enroll_profile_et_value);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;

        RProgramTrackedEntityAttribute item = getItem(holder.ref);

        holder.tvLabel.setText(item.getDisplayName().replace(programName + " ", ""));
        holder.tvMandatory.setVisibility(item.isMandatory() ? View.VISIBLE : View.GONE);

        if (item.getTrackedEntityAttribute().isOptionSetValue()
            || item.getValueType() == BOOLEAN
            || item.getValueType() == DATE
                ) {
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.etValue.setVisibility(View.GONE);

            holder.etValue.setText(item.getValue());
            holder.tvValue.setText(item.getValue());
            holder.tvValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                @Override
                public void after(Editable editable) {
                    getItem(holder.ref).setValue(editable.toString());
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
                    getItem(holder.ref).setValue(editable.toString());
                }
            }));
        }

        if (item.getTrackedEntityAttribute().isOptionSetValue()) {
            holder.tvValue.setOnClickListener(v -> {
                List<Model> modelList = new ArrayList<>();
                for (ROption option : item.getTrackedEntityAttribute().getOptionSet().getOptions()) {
                    modelList.add(new BaseModel(option.getId(), option.getDisplayName()));
                }

                OptionDialog.newInstance(modelList, model -> {
                    holder.tvValue.setText(model.getDisplayName());
                }).show(activity.getSupportFragmentManager());
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
                    holder.tvValue.setOnClickListener(v -> {
                        DatePickerDialog datePicker = DatePickerDialog
                                .newInstance(item.isAllowFutureDate());
                        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                            holder.tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
                        });
                        datePicker.show(activity.getSupportFragmentManager());
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
                    holder.tvValue.setOnClickListener(v -> {
                        List<Model> modelList = new ArrayList<Model>() {
                            {
                                add(new BaseModel("0", "Yes"));
                                add(new BaseModel("1", "No"));
                            }
                        };
                        OptionDialog.newInstance(modelList, model -> {
                            holder.tvValue.setText(model.getDisplayName());
                        }).show(activity.getSupportFragmentManager());
                    });
                    break;
                default:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }


        return convertView;
    }

    private class ViewHolder {
        private TextView tvLabel;
        private TextView tvMandatory;
        private EditText etValue;
        private TextView tvValue;
        private int ref;
    }

}
