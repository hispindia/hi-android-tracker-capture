package org.hisp.india.trackercapture.domains.sync_queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.nhancv.ntask.NTaskManager;
import com.nhancv.ntask.RTask;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by nhancao on 5/10/17.
 */

public class SyncQueueAdapter extends BaseSwipeAdapter {
    private static final String TAG = SyncQueueAdapter.class.getSimpleName();

    private List<QueueItem> taskList;
    private rx.Subscription subscription;
    private SyncQueueAdapterCallback callback;

    public SyncQueueAdapter(SyncQueueAdapterCallback callback) {
        this.taskList = new ArrayList<>();
        this.callback = callback;
    }

    public void setTaskList(List<RTask> _taskList) {
        RxScheduler.onStop(subscription);
        subscription = Observable.unsafeCreate((Observable.OnSubscribe<List<QueueItem>>) subscriber -> {
            List<QueueItem> res = new ArrayList<>();
            for (RTask task : _taskList) {
                String id = task.getId().substring(task.getId().length() / 4);
                QueueItem queueItem = new QueueItem(id,
                                                    "Status: " + task.getStatus() + " - Retry: " + task.getRetryTime(),
                                                    task);
                res.add(queueItem);
            }

            subscriber.onNext(res);
        }).subscribe(o -> {
            taskList = o;
            notifyDataSetChanged();
        });

    }

    public RTask getTask(String taskId) {
        for (QueueItem queueItem : taskList) {
            if (queueItem.getTask().getId().equals(taskId)) {
                return queueItem.getTask();
            }
        }
        return null;
    }

    public void completeTask(RTask task) {
        for (int i = 0; i < taskList.size(); i++) {
            QueueItem queueItem = taskList.get(i);
            if (queueItem.getTask().getId().equals(task.getId())) {
                NTaskManager.completeTask(task);
                taskList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.item_room_swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracked_entity, parent, false);
    }

    @Override
    public void fillValues(int position, View convertView) {
        QueueItem item = getItem(position);

        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.item_room_swipe);
        View closeRoom = convertView.findViewById(R.id.bottom_wrapper);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, closeRoom);

        closeRoom.setOnClickListener(v -> {
            callback.removeTask(item.getTask());
            swipeLayout.close();
        });
        LinearLayout llItem = (LinearLayout) convertView.findViewById(R.id.item_tracked_entity_ll_item);
        TextView tvLabel = (TextView) convertView.findViewById(R.id.item_tracked_entity_tv_label);
        TextView tvValue = (TextView) convertView.findViewById(R.id.item_tracked_entity_tv_value);

        QueueItem task = getItem(position);

        String hashId = task.getId().substring(
                task.getId().length() - ((task.getId().length() > 12) ? 12 : task.getId().length()));
        tvLabel.setText(String.format("Task: %s", hashId));
        tvValue.setText(task.getValue());

        llItem.setOnClickListener(v -> {
            callback.onClick(item.getTask());
        });
    }

    public interface SyncQueueAdapterCallback {
        void removeTask(RTask rTask);

        void onClick(RTask rTask);
    }

    private class QueueItem {
        private String id;
        private String value;
        private RTask task;

        QueueItem(String id, String value, RTask task) {
            this.id = id;
            this.value = value;
            this.task = task;
        }

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public RTask getTask() {
            return task;
        }
    }

}
