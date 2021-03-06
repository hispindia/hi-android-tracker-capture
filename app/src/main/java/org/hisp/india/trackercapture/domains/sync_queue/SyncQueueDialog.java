package org.hisp.india.trackercapture.domains.sync_queue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;

/**
 * Created by nhancao on 7/13/17.
 */
@EFragment(R.layout.dialog_sync_queue)
public class SyncQueueDialog extends android.support.v4.app.DialogFragment {
    private static final String TAG = SyncQueueDialog.class.getSimpleName();

    @ViewById(R.id.dialog_sync_queue_tv_info)
    protected TextView tvInfo;
    @FragmentArg
    protected String taskId;
    @FragmentArg
    protected String detail;

    private DialogInterface dialogInterface;

    public static SyncQueueDialog newInstance(RTaskRequest rTask) {
        return SyncQueueDialog_.builder()
                               .taskId(rTask.getUuid())
                               .detail(rTask.toString())
                               .build();
    }

    @AfterViews
    protected void init() {
        tvInfo.setText(detail);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
        // request a window without the title
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onResume() {
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }

    @Click(R.id.dialog_sync_queue_rl_close)
    protected void rlCloseClick() {
        dismiss();
    }

    @Click(R.id.dialog_sync_queue_rl_sync)
    void rlSyncClick() {
        if (dialogInterface != null) {
            dialogInterface.onSyncClick(this, taskId);
            dismiss();
        }
    }

    @Click(R.id.dialog_sync_queue_rl_edit)
    protected void rlEditClick() {
        if (dialogInterface != null) {
            dialogInterface.onEditClick(this, taskId);
            dismiss();
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public SyncQueueDialog setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
        return this;
    }

    public interface DialogInterface {
        void onSyncClick(DialogFragment dialogFragment, String taskId);

        void onEditClick(DialogFragment dialogFragment, String taskId);
    }

}

