package com.zhuzichu.library.utils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CompositeDisposableUtils {

    private static Map<String,CompositeDisposable> map=new HashMap<>();

    public static synchronized void setTaskTag(String taskTag,Disposable d){
        CompositeDisposable task=map.get(taskTag);
        if(task==null){
            task=new CompositeDisposable();
            map.put(taskTag,task);
        }
        task.add(d);
        map.put(taskTag,task);
    }

    public static synchronized void endTaskByTag(String taskTag){
        CompositeDisposable task=map.get(taskTag);
        if(task!=null){
            task.dispose();
            map.remove(taskTag);
        }
    }

}
