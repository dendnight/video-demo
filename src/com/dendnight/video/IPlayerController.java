package com.dendnight.video;

import com.dendnight.video.MainViewManager;

public interface IPlayerController {
    void onCreate();

    void setViewManager(MainViewManager mMainViewManager);

    void setupView();

    void init();

    void onResume();

    void onStop();

}
