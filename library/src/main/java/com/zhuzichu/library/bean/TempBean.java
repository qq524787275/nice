package com.zhuzichu.library.bean;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public class TempBean<T> {
    private int index;
    private T data;

    public TempBean(int index, T data) {
        this.index = index;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
