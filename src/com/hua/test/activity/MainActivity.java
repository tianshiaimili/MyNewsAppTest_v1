package com.hua.test.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import com.hua.tes.slidingmenu.BaseSlidingFragmentActivity;
import com.hua.tes.slidingmenu.SlidingMenu;

public class MainActivity extends BaseSlidingFragmentActivity {

	private SlidingMenu mSlidingMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_main);
		initSlidingMenu() ;
	}


	private void initSlidingMenu() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// ��ȡ��Ļ�ֱ��ʿ��
		setBehindContentView(R.layout.main_left_layout);// ������˵����������һ�����ʾ�����Ĳ˵�
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftContentFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// �������󻬻����һ����������Ҷ����Ի������������Ҷ����Ի�
		mSlidingMenu.setShadowWidth(mScreenWidth / 50);// ������Ӱ��� ���ǻ���ʱҳ�ߵ���Ӱ
		mSlidingMenu.setShadowDrawable(R.drawable.shadow_left);// ������˵���ӰͼƬ
		mSlidingMenu.setBehindOffset(mScreenWidth / 5);// ���ò˵����
		mSlidingMenu.setFadeDegree(0.35f);// ���õ��뵭���ı���
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//���û�����ģʽ
//		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);// �����Ҳ˵���ӰͼƬ
		mSlidingMenu.setFadeEnabled(true);// ���û���ʱ�˵����Ƿ��뵭��
		mSlidingMenu.setBehindScrollScale(0.333f);// ���û���ʱ��קЧ��
	}

	
}
