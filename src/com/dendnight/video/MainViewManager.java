package com.dendnight.video;

import android.app.Activity;
import android.view.View;


public class MainViewManager implements IManager{

    private Activity mMainController;
    private View mRootView;
    private ViewMap<Integer, ViewRecord> mViewMap = new ViewMap<Integer, ViewRecord>();
    
    public MainViewManager(Activity mainController, View decorView) {
        mMainController = mainController;
        mRootView = decorView;
    }

    public void initViewById(int viewId, Class<? extends View> clazz) {
        if(mViewMap.get(viewId)==null ){
            mViewMap.put(viewId, new ViewRecord(viewId, mRootView.findViewById(viewId), clazz));
        }
    }

    public View get(int viewId) {
        return mViewMap.get(viewId).mySelf();
    }

    public void initViewById(int viewId, View view) {
        ViewRecord v;
        if((v= mViewMap.get(viewId))==null || !v.isMyself(view)){
            mViewMap.put(viewId, new ViewRecord(viewId, view, view.getClass()));
        }
    }

}
