package org.hisp.india.trackercapture.domains.sync_queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 5/10/17.
 */

public class SyncQueueAdapter extends BaseSwipeAdapter {
    private static final String TAG = SyncQueueAdapter.class.getSimpleName();

    private List<RTaskRequest> taskList;
    private SyncQueueAdapterCallback callback;

    public SyncQueueAdapter(SyncQueueAdapterCallback callback) {
        this.taskList = new ArrayList<>();
        this.callback = callback;
    }

    public void setTaskList(List<RTaskRequest> _taskList) {
        this.taskList = _taskList;
        notifyDataSetChanged();
    }

    public void removeTask(RTaskRequest task) {
        for (int i = 0; i < taskList.size(); i++) {
            RTaskRequest queueItem = taskList.get(i);
            if (queueItem.getUuid().equals(task.getUuid())) {
                taskList.remove(i);
                task.delete();
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
    public RTaskRequest getItem(int position) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_queue, parent, false);
    }

    @Override
    public void fillValues(int position, View convertView) {
        RTaskRequest item = getItem(position);

        SwipeLayout swipeLayout = convertView.findViewById(R.id.item_room_swipe);
        View closeRoom = convertView.findViewById(R.id.bottom_wrapper);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, closeRoom);

        closeRoom.setOnClickListener(v -> {
            callback.removeTask(item);
            swipeLayout.close();
        });

        View llItem = convertView.findViewById(R.id.item_tracked_entity_ll_item);

        TextView tvValueId = convertView.findViewById(R.id.item_tracked_entity_tv_value_id);
        TextView tvValueEnrollDate = convertView.findViewById(R.id.item_tracked_entity_tv_value_enroll_date);
        TextView tvValueIncidentDate = convertView.findViewById(R.id.item_tracked_entity_tv_value_incident_date);

        ImageView ivNeedSync = convertView.findViewById(R.id.item_tracked_entity_iv_need_sync);

        RTaskEnrollment taskEnrollment = item.getEnrollment();
        if (taskEnrollment != null) {
            tvValueId.setText(taskEnrollment.getEnrollmentId() == null ? "n/a" : taskEnrollment.getEnrollmentId());
            tvValueEnrollDate.setText(taskEnrollment.getEnrollmentDate());
            tvValueIncidentDate.setText(taskEnrollment.getIncidentDate());
        }
        if (item.isNeedSync()) {
            ivNeedSync.setVisibility(View.VISIBLE);
            ivNeedSync.setBackgroundResource(item.isLastSyncStatus() ? R.drawable.ic_sync : R.drawable.ic_sync_red);
        } else {
            ivNeedSync.setVisibility(View.INVISIBLE);
        }
        llItem.setOnClickListener(v -> callback.onClick(item));
    }

    public interface SyncQueueAdapterCallback {
        void removeTask(RTaskRequest rTask);

        void onClick(RTaskRequest rTask);
    }

}
