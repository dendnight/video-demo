package com.dendnight.video;

import android.view.View;

public class ViewRecord {

    private int mViewId;
    private Class<? extends View> mClazz;
    private View mySelf;
    
    
    public ViewRecord(int viewId, View view, Class<? extends View> clazz) {
        mViewId = viewId;
        mySelf = view;
        mClazz = clazz;
    }


    public boolean isClazzEqual(Class<? extends View> clazz) {
        return mClazz == clazz;
    }


    public View mySelf() {
        return mySelf;
    }


    public boolean isMyself(View view) {
        return mySelf == view  ;
    }
}

