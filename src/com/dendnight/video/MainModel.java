
package com.dendnight.video;

import android.app.Activity;
import android.util.SparseArray;

public class MainModel {

    private Activity mMainController;
    private static MainModel mInstance;

    public enum ManagerType {
        MAIN_VIEW_MANAGER
    }

    private SparseArray<IManager> mCache = new SparseArray<IManager>();

    private MainModel(Activity controller) {
        mMainController = controller;
    }

    public static MainModel getInstance(Activity mainActivity) {
        if (null == mInstance)
            mInstance = new MainModel(mainActivity);
        return mInstance;
    }

    public IManager getManager(ManagerType mType) {
        IManager mgr = null;
        switch (mType) {
            case MAIN_VIEW_MANAGER:
                mgr = mCache.get(mType.ordinal());
                if (null == mgr) {
                    mgr = new MainViewManager(mMainController, mMainController.getWindow()
                            .getDecorView());
                    mCache.put(mType.ordinal(), mgr);
                }
                break;

        }
        return mgr;
    }

}
