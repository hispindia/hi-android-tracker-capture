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

import com.nhancv.ntask.RTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;

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

    public static SyncQueueDialog newInstance(RTask rTask) {
        return SyncQueueDialog_.builder().taskId(rTask.getId()).detail(rTask.getItemContent()).build();
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
    void rlCloseClick() {
        dismiss();
    }

    @Click(R.id.dialog_sync_queue_rl_delete)
    void rlDeleteClick() {
        if (dialogInterface != null) {
            dialogInterface.onDeleteClick(this, taskId);
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
        void onDeleteClick(DialogFragment dialogFragment, String taskId);
    }

}

