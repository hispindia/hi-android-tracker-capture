package org.hisp.india.trackercapture.domains.enroll_program_stage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 5/10/17.
 */

public class EnrollProgramStageAdapter extends BaseAdapter {

    private List<RProgramStage> programStageList;
    private String enrollmentDate;

    public EnrollProgramStageAdapter() {
        this.programStageList = new ArrayList<>();
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public List<RProgramStage> getProgramStageList() {
        return programStageList;
    }

    public void setProgramStageList(List<RProgramStage> programStageList) {
        this.programStageList = programStageList;
        calculateDueDate();
        notifyDataSetChanged();
    }

    public void calculateDueDate() {
        DateTime dateTime = AppUtils.parseDate(enrollmentDate);
        for (int i = 0; i < programStageList.size(); i++) {
            programStageList.get(i).setDueDate(
                    AppUtils.formatDate(dateTime.plusDays(programStageList.get(i).getMinDaysFromStart())));
        }
    }

    public List<Event> getEventList() {
        List<Event> eventList = new ArrayList<>();
        for (RProgramStage rProgramStage : programStageList) {
            eventList.add(new Event(rProgramStage.getDueDate(), rProgramStage.getId()));
        }
        return eventList;
    }

    @Override
    public int getCount() {
        return programStageList.size();
    }

    @Override
    public RProgramStage getItem(int position) {
        return programStageList.get(position);
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
            convertView = View.inflate(parent.getContext(), R.layout.item_enroll_program_stage, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_enroll_program_stage_tv_title);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_enroll_program_stage_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RProgramStage item = getItem(position);

        holder.tvTitle.setText(item.getDisplayName());
        holder.tvValue.setText(item.getDisplayName());

        return convertView;
    }

    private class ViewHolder {
        private TextView tvTitle;
        private TextView tvValue;
    }

}
