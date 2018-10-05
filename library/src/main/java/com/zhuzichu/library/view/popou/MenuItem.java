package com.zhuzichu.library.view.popou;

public class MenuItem {
    private int icon = -1;
    private String title;
    MenuPopup.onMenuItemClickListener listener;

    public MenuItem() {
    }

    public MenuItem(String title) {
        this.title = title;
    }

    public MenuItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
