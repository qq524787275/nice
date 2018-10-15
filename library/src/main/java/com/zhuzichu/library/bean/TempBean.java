package com.zhuzichu.library.bean;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public class TempBean<T> {
    public int index;
    public T data;

    public TempBean(int index, T data) {
        this.index = index;
        this.data = data;
    }
}
