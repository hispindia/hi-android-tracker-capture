package org.hisp.india.trackercapture.domains.tracked_entity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.response.QueryResponse;

/**
 * Created by nhancao on 5/10/17.
 */

public class TrackedEntityAdapter extends BaseAdapter {
    private static final String TAG = TrackedEntityAdapter.class.getSimpleName();

    private QueryResponse queryResponse;

    public TrackedEntityAdapter() {
    }

    public void setQueryResponse(QueryResponse queryResponse) {
        this.queryResponse = queryResponse;
    }

    @Override
    public int getCount() {
        try {
            return queryResponse.getRows().get(queryResponse.getPosition()).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        return queryResponse.getRows().get(queryResponse.getPosition()).get(position);
    }

    public String getLabelItem(int position) {
        return queryResponse.getHeaders().get(position).getColumn();
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
            convertView = View.inflate(parent.getContext(), R.layout.item_tracked_entity, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.item_tracked_entity_tv_label);
            holder.tvValue = (TextView) convertView.findViewById(R.id.item_tracked_entity_tv_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);

        holder.tvLabel.setText(getLabelItem(position));
        holder.tvValue.setText(item);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvLabel;
        private TextView tvValue;
    }

}
