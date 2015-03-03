package com.hua.test.activity;

import com.igexin.sdk.PushManager;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * 刚打开时显示的页面，后台在加载数据    啦啦啦德玛西亚   啦啦啦啦啦啦的玛西亚
 * 提交ok？lalalallalas大三大四的
 * @author Hua
 *
 */
public class WelcomeActivity2 extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 初始化接口，应用启动的时候调用
		// 参数：appId, appSecret, 调试模式
//		AdManager.getInstance(this).init("85aa56a59eac8b3d", "a14006f66f58d5d7", false);
//		AdManager.getInstance(getApplicationContext()).init("217849af92c2b80f", "31eced554a9c8e43", false);
		initYouMI();
		
		///初始化 个推
		PushManager.getInstance().initialize(this.getApplicationContext());
		
		// 如果仅仅使用开屏，需要取消注释以下注释，如果使用了开屏和插屏，则不需要。
		SpotManager.getInstance(this).loadSplashSpotAds();

		// 开屏的两种调用方式：请根据使用情况选择其中一种调用方式。
		// 1.可自定义化调用：
		// 此方式能够将开屏适应一些应用的特殊场景进行使用。
		// 传入需要跳转的activity
		SplashView splashView = new SplashView(this, MainActivityPhone.class);

		// 开屏也可以作为控件加入到界面中。
		setContentView(splashView.getSplashView());

		SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
				new SpotDialogListener() {

					@Override
					public void onShowSuccess() {
						Log.i("YoumiAdDemo", "开屏展示成功");
					}

					@Override
					public void onShowFailed() {
						Log.i("YoumiAdDemo", "开屏展示失败。");
					}

					@Override
					public void onSpotClosed() {
						Log.i("YoumiAdDemo", "开屏关闭。");
					}
				});

		
		/**预加载插屏广告数据*/
		SpotManager.getInstance(this).loadSpotAds();
		SpotManager.getInstance(this).setSpotOrientation(
	            SpotManager.ORIENTATION_PORTRAIT);
		
		// 2.简单调用方式
		// 如果没有特殊要求，简单使用此句即可实现插屏的展示
		// SpotManager.getInstance(this).showSplashSpotAds(this,
		// MainActivity.class);

	}

	
	   /**初始化 有米广告*/
 private void initYouMI() {
		AdManager.getInstance(getApplicationContext()).init("217849af92c2b80f", "31eced554a9c8e43", false);
		OffersManager.getInstance(getApplicationContext());
	}
	
	
	// 请务必加上词句，否则进入网页广告后无法进去原sdk
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 10045) {
			Intent intent = new Intent(WelcomeActivity2.this, MainActivityPhone.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// 取消后退键
	}

	@Override
	protected void onResume() {

		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}
	
}
