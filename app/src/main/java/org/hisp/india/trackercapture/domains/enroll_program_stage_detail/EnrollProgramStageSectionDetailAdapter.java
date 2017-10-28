package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.enroll_program_stage_detail.EnrollProgramStageDetailActivity;
import org.hisp.india.trackercapture.models.base.Option;
import org.hisp.india.trackercapture.models.base.ProgramStageSection;
import org.hisp.india.trackercapture.models.e_num.ValueType;
import org.hisp.india.trackercapture.models.program_rules.TMPEvaluateProgramRule;
import org.hisp.india.trackercapture.models.storage.RDataElement;
import org.hisp.india.trackercapture.models.storage.ROption;
import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;
import org.hisp.india.trackercapture.models.storage.RProgramStageSection;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NTextChange;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ahmed on 9/22/2017.
 */

public class EnrollProgramStageSectionDetailAdapter extends BaseAdapter{
    private EnrollProgramStageDetailActivity activity;
    private List<RProgramStageDataElement> programStageDataElementList;
    private RProgramStageSection programStageSection;
    private List<RProgramStageDataElement> programStageDataElementListRefined;
    private String programId;

    public EnrollProgramStageSectionDetailAdapter(EnrollProgramStageDetailActivity activity,String programId) {
        this.activity = activity;
        this.programStageDataElementList = new ArrayList<>();
        this.programStageDataElementListRefined = new ArrayList<>();
        this.programId = programId;
    }


    @Override
    public int getCount() {
        return programStageDataElementListRefined.size();
    }

    @Override
    public RProgramStageDataElement getItem(int position) {
        return programStageDataElementListRefined.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EnrollProgramStageSectionDetailAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new EnrollProgramStageSectionDetailAdapter.ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_stage, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_label);
            holder.etValue = (EditText) convertView.findViewById(R.id.item_enroll_profile_et_value);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_profile_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (EnrollProgramStageSectionDetailAdapter.ViewHolder) convertView.getTag();
        }
        holder.ref = position;

        RProgramStageDataElement programStageDataElement = getItem(holder.ref);
        RDataElement item = programStageDataElement.getDataElement();

        String label = item.getDisplayName() + (programStageDataElement.isCompulsory() ? "*" : "");
        SpannableString completedString = new SpannableString(label);
        if (programStageDataElement.isCompulsory()) {
            completedString.setSpan(new ForegroundColorSpan(Color.RED), label.length() - 1, label.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tvLabel.setText(completedString);

        if (item.isOptionSetValue()
                || item.getValueType() == ValueType.BOOLEAN
                || item.getValueType() == ValueType.DATE
                ) {
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.etValue.setVisibility(View.GONE);

            holder.tvValue.setText(programStageDataElement.getValueDisplay());
        } else {
            holder.etValue.setVisibility(View.VISIBLE);
            holder.tvValue.setVisibility(View.GONE);

            holder.etValue.setText(programStageDataElement.getValueDisplay());
            holder.etValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                @Override
                public void after(Editable editable) {
                    RProgramStageDataElement itemModel = getItem(holder.ref);
                    itemModel.setValue(editable.toString());
                    itemModel.setValueDisplay(editable.toString());
                }
            }));
        }

        if (item.isOptionSetValue()) {
            List<Option> modelList = new ArrayList<>();
            for (ROption option : item.getOptionSet().getOptions()) {
                modelList.add(new Option(option.getId(), option.getDisplayName(), option.getCode()));
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
                    List<Option> modelList = new ArrayList<Option>() {
                        {
                            add(new Option("0", "No", "false"));
                            add(new Option("1", "Yes", "true"));
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




    private void refineList(){

        if(programStageDataElementList!=null && programStageSection !=null){
            String id = programStageSection.getId();
            programStageDataElementListRefined.clear();
            for(RProgramStageDataElement programStageDataElement:getProgramStageDataElementList()){
                if(programStageSection.getProgramStageDataElements().contains(programStageDataElement)){
                    programStageDataElementListRefined.add(programStageDataElement);
                }
            }
        }
        if(programStageSection==null || programStageSection.getProgramStageDataElements().size()==0){
           programStageDataElementListRefined.addAll(programStageDataElementList);
        }
        applyProgramRules();
        activity.refreshList();
    }

    private void applyProgramRules(){
        List<String> fieldsToHide = TMPEvaluateProgramRule.fieldsToHide(programStageDataElementListRefined,programId);
        /*
        List<RProgramStageDataElement> elementsToRemove = new ArrayList<>();
        for(RProgramStageDataElement dataElement:programStageDataElementListRefined){
            for(String key:fieldsToHide) {
                if(dataElement.getId().equals(key)){
                    elementsToRemove.add(dataElement);
                    break;
                }
            }
        }

        for(RProgramStageDataElement element:elementsToRemove){
            programStageDataElementListRefined.remove(element);
        }*/
        Iterator<RProgramStageDataElement> programStageDataElementIterator = programStageDataElementListRefined.iterator();
        while(programStageDataElementIterator.hasNext()){
            RProgramStageDataElement element = programStageDataElementIterator.next();
            if(fieldsToHide.contains(element.getId())){
                programStageDataElementIterator.remove();
            }
        }


    }


    private void showOptionDialog(EnrollProgramStageSectionDetailAdapter.ViewHolder holder, List<Option> modelList) {
        OptionDialog.newInstance(modelList, model -> {
            holder.tvValue.setText(model.getDisplayName());

            RProgramStageDataElement itemModel = getItem(holder.ref);
            itemModel.setValue(model.getCode());
            itemModel.setValueDisplay(holder.tvValue.getText().toString());
            refineList();//added to check programe rules temp
        }).show(activity.getSupportFragmentManager());
    }

    private void showDatePickerDialog(EnrollProgramStageSectionDetailAdapter.ViewHolder holder, RProgramStageDataElement item) {
        DatePickerDialog
                datePicker = DatePickerDialog
                .newInstance(item.isAllowFutureDate());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            holder.tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));

            RProgramStageDataElement itemModel = getItem(holder.ref);
            itemModel.setValue(holder.tvValue.getText().toString());
            itemModel.setValueDisplay(holder.tvValue.getText().toString());

        });
        datePicker.show(activity.getSupportFragmentManager());
    }

    public List<RProgramStageDataElement> getProgramStageDataElementList() {
        return programStageDataElementList;
    }

    public void setProgramStageDataElementList(
            List<RProgramStageDataElement> programStageDataElementList) {
        this.programStageDataElementList = programStageDataElementList;
        refineList();
        notifyDataSetChanged();
    }

    public EnrollProgramStageDetailActivity getActivity() {
        return activity;
    }

    public void setActivity(EnrollProgramStageDetailActivity activity) {
        this.activity = activity;
    }

    public RProgramStageSection getProgramStageSection() {
        return programStageSection;
    }

    public void setProgramStageSection(RProgramStageSection programStageSection) {
        this.programStageSection = programStageSection;
        refineList();
    }

    public List<RProgramStageDataElement> getProgramStageDataElementListRefined() {
        return programStageDataElementListRefined;
    }

    public void setProgramStageDataElementListRefined(List<RProgramStageDataElement> programStageDataElementListRefined) {
        this.programStageDataElementListRefined = programStageDataElementListRefined;

    }


    private class ViewHolder {
        private TextView tvLabel;
        private EditText etValue;
        private TextView tvValue;
        private int ref;
    }
}