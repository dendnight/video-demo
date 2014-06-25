package com.dendnight.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

//MediaPlayerControl handle key events
public class VideoViewController implements IPlayerController, View.OnClickListener {
	private static final String TAG = VideoViewController.class.getSimpleName();

	private static final int mPtr_PlayerControl = -1;
	// private String mMediaPath = "/storage/sda4/1080test.wmv";
	private static final String mMediaPath = "http://118.186.9.80/youku/65730D707ED308233899A3278A/0300020100517A44919913003E88038ACBA934-1F69-B3B1-C03C-3872493613D6.flv";
	// private static final String mMediaPath = "/storage/sdcard1/1080test.wmv";
	private Activity mMainController;
	private MainViewManager mMainViewManager;

	MediaPlayer.OnCompletionListener mCL = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.d(TAG, "onCompletion...");
			VideoView vv = getVideoView();
			PlayerControl pc = getPlayerControl();
			if (vv.isPlaying()) {
				pc.stop();
			}
			getVideoView().setVideoPath(mMediaPath);
			pc.start();
		}

	};

	public VideoViewController(Activity mainController) {
		mMainController = mainController;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prev:
			break;
		case R.id.next:
			break;
		}

	}

	@Override
	public void onCreate() {
		mMainController.setContentView(R.layout.video_view);
	}

	@Override
	public void setupView() {
		mMainViewManager.initViewById(mPtr_PlayerControl, new PlayerControl(mMainController));
		mMainViewManager.initViewById(R.id.vv, VideoView.class);
	}

	@Override
	public void setViewManager(MainViewManager mainViewManager) {
		mMainViewManager = mainViewManager;
	}

	@Override
	public void init() {
		initVideoView();
	}

	private void initVideoView() {
		VideoView vv = getVideoView();
		PlayerControl pc = getPlayerControl();
		vv.setMediaController(pc);
		pc.setMediaPlayer(vv);
		pc.setPrevNextListeners(this, this);
		getVideoView().setOnCompletionListener(mCL);
	}

	private PlayerControl getPlayerControl() {
		return (PlayerControl) mMainViewManager.get(mPtr_PlayerControl);
	}

	private VideoView getVideoView() {
		return (VideoView) mMainViewManager.get(R.id.vv);
	}

	@Override
	public void onResume() {
		getVideoView().setVideoPath(mMediaPath);
		getPlayerControl().start();
	}

	@Override
	public void onStop() {
		getPlayerControl().stop();
	}
}
