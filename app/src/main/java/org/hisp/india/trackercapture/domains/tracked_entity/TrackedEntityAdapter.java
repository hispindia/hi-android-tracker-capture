package org.hisp.india.trackercapture.domains.tracked_entity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.base.RowModel;

/**
 * Created by nhancao on 5/10/17.
 */

public class TrackedEntityAdapter extends BaseAdapter {
    private static final String TAG = TrackedEntityAdapter.class.getSimpleName();

    private RowModel rowModel;

    public void setQueryResponse(RowModel rowModel) {
        this.rowModel = rowModel;
    }

    @Override
    public int getCount() {
        try {
            return rowModel.getRows().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        try {
            return rowModel.getRows().get(position);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLabelItem(int position) {
        try {
            return rowModel.getHeaders().get(position).getColumn();
        } catch (Exception e) {
            return "";
        }
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

        holder.tvLabel.setText(getLabelItem(position));
        holder.tvValue.setText(getItem(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView tvLabel;
        private TextView tvValue;
    }

}
