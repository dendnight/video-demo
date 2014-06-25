package com.dendnight.video;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private MainViewManager mMainViewManager;
	private MainModel mMainModel;
	private IPlayerController mPlayerController;

	private PLAYER_MODE mMode = PLAYER_MODE.SURFACE_VIEW;

	private enum PLAYER_MODE {
		VIDEO_VIEW, SURFACE_VIEW
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindowFeature();
		setupController();
		setupModel();
		setupView();

		init();
	}

	private void initWindowFeature() {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void setupController() {
		switch (mMode) {
		case VIDEO_VIEW:
			mPlayerController = new VideoViewController(this);
			break;
		case SURFACE_VIEW:
			mPlayerController = new MediaPlayerController(this);
			break;
		}
		mPlayerController.onCreate();
	}

	private void setupModel() {
		mMainModel = MainModel.getInstance(this);
		mMainViewManager = (MainViewManager) mMainModel.getManager(MainModel.ManagerType.MAIN_VIEW_MANAGER);
	}

	private void setupView() {
		mPlayerController.setViewManager(mMainViewManager);
		mPlayerController.setupView();
		getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
	}

	private void init() {
		mPlayerController.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPlayerController.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlayerController.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
