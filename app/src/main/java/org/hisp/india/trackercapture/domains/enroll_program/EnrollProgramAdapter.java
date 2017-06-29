package org.hisp.india.trackercapture.domains.enroll_program;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.e_num.ValueType;
import org.hisp.india.trackercapture.models.storage.ROption;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.tmp.HeaderDateModel;
import org.hisp.india.trackercapture.models.tmp.ItemModel;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

import static org.hisp.india.trackercapture.models.tmp.ItemModel.ENROLLMENT_DATE;
import static org.hisp.india.trackercapture.models.tmp.ItemModel.FIELD_LIST;
import static org.hisp.india.trackercapture.models.tmp.ItemModel.INCIDENT_DATE;
import static org.hisp.india.trackercapture.models.tmp.ItemModel.REGISTER_BUTTON;

/**
 * Created by nhancao on 5/10/17.
 */

public class EnrollProgramAdapter extends BaseAdapter {
    private static final String TAG = EnrollProgramAdapter.class.getSimpleName();
    private String programName;
    private EnrollProgramActivity activity;
    private List<ItemModel> modelList;
    private List<BaseModel> organizationUnitList;
    private EnrollProgramCallBack enrollProgramCallBack;

    public EnrollProgramAdapter(EnrollProgramActivity activity, String programName,
                                EnrollProgramCallBack enrollProgramCallBack) {
        this.programName = programName;
        this.activity = activity;
        this.modelList = new ArrayList<>();
        this.organizationUnitList = new ArrayList<>();
        this.enrollProgramCallBack = enrollProgramCallBack;
    }

    public String getIncidentDateValue() {
        for (ItemModel itemModel : modelList) {
            if (itemModel.getType() == INCIDENT_DATE) {
                return itemModel.getHeaderDateModel().getValue();
            }
        }
        return null;
    }

    public String getEnrollmentDateValue() {
        for (ItemModel itemModel : modelList) {
            if (itemModel.getType() == ENROLLMENT_DATE) {
                return itemModel.getHeaderDateModel().getValue();
            }
        }
        return null;
    }

    public void setOrganizationUnitList(List<ROrganizationUnit> organizationUnitList) {
        if (organizationUnitList != null) {
            for (ROrganizationUnit rOrganizationUnit : organizationUnitList) {
                if (this.organizationUnitList.size() < 100) {
                    this.organizationUnitList
                            .add(new BaseModel(rOrganizationUnit.getId(), rOrganizationUnit.getDisplayName()));
                }
            }
        }
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public List<RProgramTrackedEntityAttribute> getProgramTrackedEntityAttributeList() {
        List<RProgramTrackedEntityAttribute> res = new ArrayList<>();
        for (ItemModel itemModel : modelList) {
            if (itemModel.getType() == FIELD_LIST) {
                res.add(itemModel.getProgramTrackedEntityAttribute());
            }
        }
        return res;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public ItemModel getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemModel itemModel = getItem(position);
        switch (itemModel.getType()) {
            case INCIDENT_DATE:
                convertView = handleHeaderDateViewHolder(position, convertView, parent);
                break;
            case ENROLLMENT_DATE:
                convertView = handleHeaderDateViewHolder(position, convertView, parent);
                break;
            case FIELD_LIST:
                convertView = handleFieldListViewHolder(position, convertView, parent);
                break;
            case REGISTER_BUTTON:
                convertView = handleButtonViewHolder(position, convertView, parent);
                break;
        }


        return convertView;
    }


    @NonNull
    private View handleButtonViewHolder(int position, View convertView, ViewGroup parent) {
        ButtonViewHolder holder;
        if (convertView == null) {
            holder = new ButtonViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_profile_button, null);
            holder.btRegister = (Button) convertView.findViewById(R.id.item_enroll_profile_btn_register);

            convertView.setTag(holder);
        } else {
            holder = (ButtonViewHolder) convertView.getTag();
        }
        holder.btRegister.setOnClickListener(v -> {
            if (enrollProgramCallBack != null) {
                enrollProgramCallBack.registerClick();
            }
        });

        return convertView;
    }

    @NonNull
    private View handleHeaderDateViewHolder(int position, View convertView, ViewGroup parent) {
        FieldListViewHolder holder;
        if (convertView == null) {
            holder = new FieldListViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_profile, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_label);
            holder.tvMandatory = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_mandatory);
            holder.etValue = (EditText) convertView.findViewById(R.id.item_enroll_profile_et_value);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (FieldListViewHolder) convertView.getTag();
        }
        holder.ref = position;
        holder.tvValue.setVisibility(View.VISIBLE);
        holder.etValue.setVisibility(View.GONE);

        HeaderDateModel model = getItem(holder.ref).getHeaderDateModel();

        holder.tvLabel.setText(model.getLabel());
        holder.tvMandatory.setVisibility(View.VISIBLE);
        holder.tvValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
            @Override
            public void after(Editable editable) {
                getItem(holder.ref).getHeaderDateModel().setValue(editable.toString());
            }
        }));
        holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.clearFocus();
                AppUtils.hideKeyBoard(v);
                //Show date picker
                DatePickerDialog datePicker = DatePickerDialog.newInstance(model.isAllowFutureDate());
                datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    holder.tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
                    getItem(holder.ref).getHeaderDateModel().setValue(holder.tvValue.getText().toString());
                });
                datePicker.show(activity.getSupportFragmentManager());
            }
        });
        return convertView;
    }

    @NonNull
    private View handleFieldListViewHolder(int position, View convertView, ViewGroup parent) {
        FieldListViewHolder holder;
        if (convertView == null) {
            holder = new FieldListViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_profile, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_label);
            holder.tvMandatory = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_mandatory);
            holder.etValue = (EditText) convertView.findViewById(R.id.item_enroll_profile_et_value);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (FieldListViewHolder) convertView.getTag();
        }
        holder.ref = position;

        RProgramTrackedEntityAttribute item = getItem(holder.ref).getProgramTrackedEntityAttribute();

        holder.tvLabel.setText(item.getDisplayName().replace(programName + " ", ""));
        holder.tvMandatory.setVisibility(item.isMandatory() ? View.VISIBLE : View.GONE);

        if (item.getTrackedEntityAttribute().isOptionSetValue()
            || item.getValueType() == ValueType.BOOLEAN
            || item.getValueType() == ValueType.DATE
            || item.getValueType() == ValueType.ORGANISATION_UNIT
                ) {
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.etValue.setVisibility(View.GONE);

            holder.etValue.setText(item.getValue());
            holder.tvValue.setText(item.getValue());
            holder.tvValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                @Override
                public void after(Editable editable) {
                    getItem(holder.ref).getProgramTrackedEntityAttribute().setValue(editable.toString());
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
                    getItem(holder.ref).getProgramTrackedEntityAttribute().setValue(editable.toString());
                }
            }));
        }

        if (item.getTrackedEntityAttribute().isOptionSetValue()) {
            List<BaseModel> modelList = new ArrayList<>();
            for (ROption option : item.getTrackedEntityAttribute().getOptionSet().getOptions()) {
                modelList.add(new BaseModel(option.getId(), option.getDisplayName()));
            }

            holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.clearFocus();
                    AppUtils.hideKeyBoard(v);
                    showOptionDialog(holder, modelList);
                }
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
                    holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
                            v.clearFocus();
                            AppUtils.hideKeyBoard(v);
                            showDatePickerDialog(holder, item);
                        }
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
                    List<BaseModel> modelList = new ArrayList<BaseModel>() {
                        {
                            add(new BaseModel("0", "Yes"));
                            add(new BaseModel("1", "No"));
                        }
                    };
                    holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
                            v.clearFocus();
                            AppUtils.hideKeyBoard(v);
                            showOptionDialog(holder, modelList);
                        }
                    });
                    break;
                case ORGANISATION_UNIT:
                    holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
                            v.clearFocus();
                            AppUtils.hideKeyBoard(v);
                            showOptionDialog(holder, organizationUnitList);
                        }
                    });
                    break;
                default:
                    holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }
        return convertView;
    }

    private void showOptionDialog(FieldListViewHolder holder, List<BaseModel> modelList) {
        OptionDialog.newInstance(modelList, model -> {
            holder.tvValue.setText(model.getDisplayName());
            getItem(holder.ref).getProgramTrackedEntityAttribute().setValue(holder.tvValue.getText().toString());
        }).show(activity.getSupportFragmentManager());
    }

    private void showDatePickerDialog(FieldListViewHolder holder, RProgramTrackedEntityAttribute item) {
        DatePickerDialog
                datePicker = DatePickerDialog
                .newInstance(item.isAllowFutureDate());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            holder.tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
            getItem(holder.ref).getProgramTrackedEntityAttribute().setValue(holder.tvValue.getText().toString());
        });
        datePicker.show(activity.getSupportFragmentManager());
    }

    public interface EnrollProgramCallBack {
        void registerClick();
    }

    private class FieldListViewHolder {
        private TextView tvLabel;
        private TextView tvMandatory;
        private EditText etValue;
        private TextView tvValue;
        private int ref;
    }

    private class ButtonViewHolder {
        private Button btRegister;
    }

}
