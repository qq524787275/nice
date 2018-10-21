package com.zhuzichu.library.action;

public class ActionUnreadCountChange {
    public int position;
    public int count;

    public ActionUnreadCountChange(int position, int count) {
        this.position = position;
        this.count = count;
    }
}
