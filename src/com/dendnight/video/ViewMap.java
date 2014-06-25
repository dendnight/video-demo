package com.dendnight.video;

import android.util.SparseArray;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class ViewMap<T,E> {

    private Set<T> mIdSet = new HashSet<T>();
    private SparseArray<E> mViewMap = new SparseArray<E>();
    
    public E get(int viewId) {
        return mViewMap.get(viewId);
    }


    public void put(int viewId, E e) {
        mViewMap.put(viewId, e);
    }
    
}
