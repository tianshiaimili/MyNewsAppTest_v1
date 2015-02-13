package com.hua.test.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hua.test.widget.swipeback.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

//@WindowFeature({
//        Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS
//})
//@Fullscreen
//@EActivity(R.layout.activity_play_videobuffer)
public class VideoPlayActivity extends BaseActivity implements
		OnInfoListener, OnBufferingUpdateListener, OnPreparedListener {

	// @ViewById(R.id.buffer)
	protected VideoView mVideoView;
	// @ViewById(R.id.probar)
	protected ProgressBar mProgressBar;
	// @ViewById(R.id.load_rate)
	protected TextView mLoadRate;
	// @ViewById(R.id.video_end)
	protected ImageView mVideoEnd;
	private Uri uri;
	private String playUrl;
	private String title;

	// @AfterInject
	public void init() {
		try {
			if (!LibsChecker.checkVitamioLibs(this))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @AfterViews
	public void initView() {
		playUrl = getIntent().getExtras().getString("playUrl");
		title = getIntent().getExtras().getString("filename");
		if ("".equals(playUrl) || playUrl == null) {
			showShortToast("请求地址错误");
			finish();
		}
		uri = Uri.parse(playUrl);
		
		mVideoView = (VideoView) findViewById(R.id.buffer);
		mProgressBar = (ProgressBar) findViewById(R.id.probar);
		mLoadRate = (TextView) findViewById(R.id.load_rate);
		mVideoEnd = (ImageView) findViewById(R.id.video_end);
		
		
		mVideoView.setVideoURI(uri);
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.requestFocus();
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnPreparedListener(this);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mLoadRate.setText(percent + "%");
		mVideoView.setFileName(title);
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		System.out.println(what);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				mProgressBar.setVisibility(View.VISIBLE);
				mLoadRate.setText("");
				mLoadRate.setVisibility(View.VISIBLE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// mVideoEnd.setVisibility(View.VISIBLE);
			mVideoView.start();
			mProgressBar.setVisibility(View.GONE);
			mLoadRate.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			break;
		}
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		mediaPlayer.setPlaybackSpeed(1.0f);
	}

	// /////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_play_videobuffer);
		init() ;
		initView();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
