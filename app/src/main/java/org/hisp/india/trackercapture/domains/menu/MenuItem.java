package org.hisp.india.trackercapture.domains.menu;

import android.support.annotation.DrawableRes;

import org.hisp.india.trackercapture.R;

/**
 * Created by nhancao on 4/10/17.
 */

public enum MenuItem {

    ENROLL("Enroll", R.drawable.ic_enroll),
    SETTINGS("Settings", R.drawable.ic_settings),
    INFO("Information", R.drawable.ic_info),
    SPACE("", 0),
    LOGOUT("Logout", R.drawable.ic_logout);

    private String title;
    private int icon;

    MenuItem(String title, @DrawableRes int icon) {
        this.title = title;
        this.icon = icon;
    }

    public static MenuItem getCode(int position) {
        for (MenuItem menuItem : MenuItem.values()) {
            if (position == menuItem.ordinal()) {
                return menuItem;
            }
        }
        return SPACE;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
