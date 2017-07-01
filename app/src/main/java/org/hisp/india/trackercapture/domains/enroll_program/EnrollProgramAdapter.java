package org.hisp.india.trackercapture.domains.enroll_program;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by nhancao on 7/1/17.
 */

public class EnrollProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        try {
            switch (viewType) {
                case INCIDENT_DATE:
                    view = LayoutInflater.from(parent.getContext())
                                         .inflate(R.layout.item_enroll_profile, parent, false);
                    return new FieldListViewHolder(view);
                case ENROLLMENT_DATE:
                    view = LayoutInflater.from(parent.getContext())
                                         .inflate(R.layout.item_enroll_profile, parent, false);
                    return new FieldListViewHolder(view);
                case FIELD_LIST:
                    view = LayoutInflater.from(parent.getContext())
                                         .inflate(R.layout.item_enroll_profile, parent, false);
                    return new FieldListViewHolder(view);
                case REGISTER_BUTTON:
                    view = LayoutInflater.from(parent.getContext())
                                         .inflate(R.layout.item_enroll_profile_button, parent, false);
                    return new ButtonViewHolder(view);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            switch (getItemViewType(position)) {
                case INCIDENT_DATE:
                    handleHeaderDateViewHolder(position, (FieldListViewHolder) holder);
                    break;
                case ENROLLMENT_DATE:
                    handleHeaderDateViewHolder(position, (FieldListViewHolder) holder);
                    break;
                case FIELD_LIST:
                    handleFieldListViewHolder(position, (FieldListViewHolder) holder);
                    break;
                case REGISTER_BUTTON:
                    handleButtonViewHolder(position, (ButtonViewHolder) holder);
                    break;
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    public ItemModel getItem(int position) {
        return modelList.get(position);
    }

    private void handleButtonViewHolder(int position, ButtonViewHolder holder) {
        holder.btRegister.setOnClickListener(v -> {
            if (enrollProgramCallBack != null) {
                enrollProgramCallBack.registerClick();
            }
        });
    }

    private void handleHeaderDateViewHolder(int position, FieldListViewHolder holder) {
        holder.ref = position;
        holder.tvValue.setVisibility(View.VISIBLE);
        holder.etValue.setVisibility(View.GONE);

        HeaderDateModel model = getItem(holder.ref).getHeaderDateModel();

        String label = model.getLabel() + "*";
        SpannableString completedString = new SpannableString(label);
        completedString.setSpan(new ForegroundColorSpan(Color.RED), label.length() - 1, label.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvLabel.setText(completedString);
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
    }

    private void handleFieldListViewHolder(int position, FieldListViewHolder holder) {
        holder.ref = position;

        RProgramTrackedEntityAttribute item = getItem(holder.ref).getProgramTrackedEntityAttribute();

        String label = item.getDisplayName().replace(programName + " ", "") + (item.isMandatory() ? "*" : "");
        SpannableString completedString = new SpannableString(label);
        if (item.isMandatory()) {
            completedString.setSpan(new ForegroundColorSpan(Color.RED), label.length() - 1, label.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tvLabel.setText(completedString);

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
            List<BaseModel> optionList = new ArrayList<>();
            for (ROption option : item.getTrackedEntityAttribute().getOptionSet().getOptions()) {
                optionList.add(new BaseModel(option.getId(), option.getDisplayName()));
            }

            holder.tvValue.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.clearFocus();
                    AppUtils.hideKeyBoard(v);
                    showOptionDialog(holder, optionList);
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
                            add(new BaseModel("0", "No"));
                            add(new BaseModel("1", "Yes"));
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
    }

    private void showOptionDialog(FieldListViewHolder holder, List<BaseModel> optionList) {
        OptionDialog.newInstance(optionList, model -> {
            holder.tvValue.setText(model.getDisplayName());

            ItemModel itemModel = getItem(holder.ref);
            if (!TextUtils.isEmpty(model.getDisplayName())) {
                itemModel.getProgramTrackedEntityAttribute().setValue(model.getDisplayName());
            } else {
                itemModel.getProgramTrackedEntityAttribute().setValue(holder.tvValue.getText().toString());
            }
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

    private static final class FieldListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private EditText etValue;
        private TextView tvValue;
        private int ref;

        public FieldListViewHolder(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.item_enroll_profile_tv_label);
            etValue = (EditText) itemView.findViewById(R.id.item_enroll_profile_et_value);
            tvValue = (TextView) itemView.findViewById(R.id.item_enroll_profile_tv_value);
        }
    }

    private static final class ButtonViewHolder extends RecyclerView.ViewHolder {
        private Button btRegister;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            btRegister = (Button) itemView.findViewById(R.id.item_enroll_profile_btn_register);

        }
    }

}
