package org.hisp.india.trackercapture.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by nhancao on 6/1/17.
 */


public class NonFocusScrollView extends ScrollView {

    public NonFocusScrollView(Context context) {
        super(context);
    }

    public NonFocusScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonFocusScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    @Override
    public ArrayList<View> getFocusables(int direction) {
        return new ArrayList<>();
    }
}
