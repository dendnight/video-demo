package com.dendnight.video;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.dendnight.video.PlayerControl.OnBtnNotifier;
import com.dendnight.video.PlayerControl.OnHideShowL;

//import com.aq.tools.gl.GlModeManager;

public class MediaPlayerController implements IPlayerController, OnBtnNotifier, OnPreparedListener, OnHideShowL {
	private static final String TAG = MediaPlayerController.class.getSimpleName();

	private static final int mPtr_PlayerControl = -2;

	private MainActivity mMainController;
	private MainViewManager mMainViewManager;

	private MediaPlayer mMediaPlayer;

	private SurfaceHolder.Callback2 mSurfaceHolerCB2 = new SurfaceHolder.Callback2() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mMediaPlayer != null)
				mMediaPlayer.release();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			if (mMediaPlayer == null)
				initMediaPlayer();
			mMediaPlayer.setDisplay(getSurfaceView().getHolder());
		}

		@Override
		public void surfaceRedrawNeeded(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}
	};

	private MediaPlayerControl mMediaController = new MediaController.MediaPlayerControl() {

		@Override
		public void start() {
			if (mMediaPlayer == null)
				return;
			mMediaPlayer.start();
		}

		@Override
		public void seekTo(int pos) {
			if (mMediaPlayer == null)
				return;
			mMediaPlayer.seekTo(pos);
		}

		@Override
		public void pause() {
			if (mMediaPlayer == null)
				return;
			mMediaPlayer.pause();
		}

		@Override
		public boolean isPlaying() {
			if (mMediaPlayer == null)
				return false;
			return mMediaPlayer.isPlaying();
		}

		@Override
		public int getDuration() {
			if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
				return 0;
			return mMediaPlayer.getDuration();
		}

		@Override
		public int getCurrentPosition() {
			if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
				return 0;
			return mMediaPlayer.getCurrentPosition();
		}

		@Override
		public int getBufferPercentage() {
			return -1;
		}

		@Override
		public boolean canSeekForward() {
			return false;
		}

		@Override
		public boolean canSeekBackward() {
			return false;
		}

		@Override
		public boolean canPause() {
			return false;
		}

	};

	private OnClickListener mSVOnClickL = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			getPlayerControl().show();
		}
	};

	public MediaPlayerController(Activity mainActivity) {
		mMainController = (MainActivity) mainActivity;
	}

	@Override
	public void onCreate() {
		mMainController.setContentView(R.layout.surface_view);
	}

	@Override
	public void setViewManager(MainViewManager mainViewManager) {
		mMainViewManager = mainViewManager;
	}

	@Override
	public void setupView() {
		mMainViewManager.initViewById(mPtr_PlayerControl, new PlayerControl(mMainController));
		mMainViewManager.initViewById(R.id.sv, SurfaceView.class);
		getSurfaceView().setOnClickListener(mSVOnClickL);
		getSurfaceView().setOnSystemUiVisibilityChangeListener(mSysUiVisL);
	}

	@Override
	public void init() {
		initSurfaceHolder();
		if (mMediaPlayer == null)
			initMediaPlayer();
		initPlayerControl();
	}

	private void initPlayerControl() {
		PlayerControl pc = getPlayerControl();
		pc.setMediaPlayer(mMediaController);
		pc.setAnchorView(mMainController.getWindow().getDecorView());
		pc.setOnBtnNotifier(this);
	}

	private PlayerControl getPlayerControl() {
		return (PlayerControl) mMainViewManager.get(mPtr_PlayerControl);
	}

	private void initSurfaceHolder() {
		getSurfaceView().getHolder().addCallback(mSurfaceHolerCB2);
	}

	private void initMediaPlayer() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(this);
	}

	private SurfaceView getSurfaceView() {
		return (SurfaceView) mMainViewManager.get(R.id.sv);
	}

	@Override
	public void onResume() {
		AssetFileDescriptor afd = null;
		FileDescriptor fd = null;
		Log.d(TAG, "onResume");
		try {
			afd = mMainController.getAssets().openFd("1080test.mp4");
			fd = afd.getFileDescriptor();
		} catch (IOException e1) {
			Log.e(TAG, "assets openFd error", e1);
		}
		try {
			mMediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
			mMediaPlayer.prepareAsync();
		} catch (IOException e) {
			Log.e(TAG, "mediaplayer set data source error", e);
		}
		mMediaPlayer.setLooping(true);
	}

	@Override
	public void onStop() {
		mMediaController.pause();
	}

	@Override
	public void onBtnClick(View v) {
		if (v.getId() != R.id.fullscr)
			return;
		View vv = getSurfaceView();

		ViewGroup.LayoutParams lp = vv.getLayoutParams();
		if (lp.height != 1080) {
			lp.height = 1080;
			lp.width = 1920;
			// Toast.makeText(mMainController,
			// "surface 1080 mode"+vv.getTranslationX()+","+vv.getTranslationY()+","+vv.getWidth()+","+vv.getHeight(),
			// Toast.LENGTH_LONG).show();
		} else {
			lp.height = 720;
			lp.width = 1080;
			// Toast.makeText(mMainController,
			// "surface 720 mode"+vv.getTranslationX()+","+vv.getTranslationY()+","+vv.getWidth()+","+vv.getHeight(),
			// Toast.LENGTH_LONG).show();
		}
		vv.requestLayout();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "on Prepared");
		mp.start();
		getPlayerControl().setOnHideL(this);
		showSystemUi(false);
	}

	@Override
	public void onHide() {
		showSystemUi(false);
	}

	@Override
	public void onShow() {
		showSystemUi(true);
	}

	private void showSystemUi(boolean visible) {
		mMainController.getWindow().getDecorView()
				.setSystemUiVisibility(visible ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	private View.OnSystemUiVisibilityChangeListener mSysUiVisL = new View.OnSystemUiVisibilityChangeListener() {
		@Override
		public void onSystemUiVisibilityChange(int visibility) {
			Log.d(TAG, "visibility" + visibility);
			if (visibility == 0) {
				getPlayerControl().show();
			}
		}
	};
}
