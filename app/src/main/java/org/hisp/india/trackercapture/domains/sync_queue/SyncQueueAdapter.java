package org.hisp.india.trackercapture.domains.sync_queue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhancv.ntask.RTask;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by nhancao on 5/10/17.
 */

public class SyncQueueAdapter extends BaseAdapter {
    private static final String TAG = SyncQueueAdapter.class.getSimpleName();

    private List<QueueItem> taskList;
    private rx.Subscription subscription;

    public SyncQueueAdapter() {
        taskList = new ArrayList<>();
    }

    public void setTaskList(List<RTask> _taskList) {
        RxScheduler.onStop(subscription);
        subscription = Observable.unsafeCreate((Observable.OnSubscribe<List<QueueItem>>) subscriber -> {
            List<QueueItem> res = new ArrayList<>();
            for (RTask task : _taskList) {
                String id = task.getId().substring(task.getId().length() / 4);
                QueueItem queueItem = new QueueItem(id,
                                                    "Status: " + task.getStatus() + " - Retry: " + task.getRetryTime());
                res.add(queueItem);
            }

            subscriber.onNext(res);
        }).subscribe(o -> {
            taskList = o;
            notifyDataSetChanged();
        });

    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public QueueItem getItem(int position) {
        return taskList.get(position);
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

        QueueItem task = getItem(position);

        String hashId = task.getId().substring(
                task.getId().length() - ((task.getId().length() > 12) ? 12 : task.getId().length()));
        holder.tvLabel.setText(String.format("Task: %s", hashId));
        holder.tvValue.setText(task.getValue());

        return convertView;
    }

    private class ViewHolder {
        private TextView tvLabel;
        private TextView tvValue;
    }

    private class QueueItem {
        private String id;
        private String value;

        QueueItem(String id, String value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }


}
