package org.hisp.india.trackercapture.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.HashMap;

/**
 * Created by nhancao on 4/7/17.
 */

public class NKeyboard implements ViewTreeObserver.OnGlobalLayoutListener {

    private static HashMap<NKeyboardListener, NKeyboard> listenerMap = new HashMap<>();
    private NKeyboardListener callback;
    private View rootView;
    private boolean wasOpened;
    private int keyBoardVisibleThreshold;

    private NKeyboard(Activity activity, NKeyboardListener listener) {
        callback = listener;

        rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        keyBoardVisibleThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                                                                   activity.getResources().getDisplayMetrics());
    }

    public static void addListener(Activity act, NKeyboardListener listener) {
        removeListener(listener);
        listenerMap.put(listener, new NKeyboard(act, listener));
    }

    public static void removeListener(NKeyboardListener listener) {
        if (listenerMap.containsKey(listener)) {
            NKeyboard k = listenerMap.get(listener);
            k.removeListener();
            listenerMap.remove(listener);
        }
    }

    public static void removeAllListeners() {
        for (NKeyboardListener l : listenerMap.keySet()) {
            listenerMap.get(l).removeListener();
        }
        listenerMap.clear();
    }

    /**
     * Determine if keyboard is visible
     *
     * @param activity Activity
     * @return Whether keyboard is visible or not
     */
    public static boolean isKeyboardVisible(Activity activity) {
        Rect r = new Rect();

        View activityRoot = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        int visibleThreshold = Math
                .round((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                                                       activity.getResources().getDisplayMetrics()));

        activityRoot.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRoot.getRootView().getHeight() - r.height();

        return heightDiff > visibleThreshold;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);

        int screenHeight = rootView.getHeight();
        int keyboardHeight = screenHeight - (rect.bottom - rect.top);

        boolean isOpen = keyboardHeight > keyBoardVisibleThreshold;
        if (isOpen == wasOpened) {
            // keyboard state has not changed
            return;
        }
        wasOpened = isOpen;

        if (callback != null) {
            callback.onToggleSoftKeyboard(isOpen, screenHeight, keyboardHeight);
        }

    }

    private void removeListener() {
        callback = null;
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public interface NKeyboardListener {
        void onToggleSoftKeyboard(boolean isVisible, int screenHeight, int keyboardHeight);
    }


}
