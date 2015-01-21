package com.hua.test.activity;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hua.test.adapter.PicuterDetailAdapter;
import com.hua.test.bean.PicuterDetailModle;
import com.hua.test.contants.Url;
import com.hua.test.network.http.json.PicuterSinaJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.flipview.FlipView;
import com.hua.test.widget.flipview.FlipView.OnFlipListener;
import com.hua.test.widget.flipview.FlipView.OnOverFlipListener;
import com.hua.test.widget.flipview.OverFlipMode;
import com.hua.test.widget.swipeback.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

//@EActivity(R.layout.activity_photo)
public class PicuterDetailActivity extends SwipeBackActivity implements
		OnFlipListener, OnOverFlipListener {
	// @ViewById(R.id.flip_view)
	protected FlipView mFlipView;

	// @Bean
	protected PicuterDetailAdapter picuterDetailAdapter;

	private String imgUrl;
	
	private Context mContext;
	
	private static final int RESPONSE_OK = 0;

    
	Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			int what = message.what;
			int numchange = what;
//			LogUtils2.i("what==" + what);
			switch (what) {
			case RESPONSE_OK:
				String result = (String) message.obj;
				getResult(result);
				// mHandler.obtainMessage(ShowFootView).sendToTarget();
				break;
			default:
				break;
			}

		};
	};
	
	// @AfterInject
	public void init() {
		try {
			if (getIntent().getExtras().getString("pic_id") != null) {
				imgUrl = getIntent().getExtras().getString("pic_id");
				showProgressDialog();
				loadData(Url.JINGXUANDETAIL_ID + imgUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @AfterViews
	public void initView() {
		// imageAdapter.appendList(imgList);
		try {
			mFlipView = (FlipView) findViewById(R.id.flip_view);
			mFlipView.setOnFlipListener(this);
			picuterDetailAdapter = new PicuterDetailAdapter(mContext);
			mFlipView.setAdapter(picuterDetailAdapter);
			mFlipView.peakNext(false);
			mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
			mFlipView.setOnOverFlipListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadData(String url) {
		if (hasNetWork()) {
			loadPhotoList(url);
		} else {
			dismissProgressDialog();
			showShortToast(getString(R.string.not_network));
			String result = getCacheStr(imgUrl);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	// @Background
	void loadPhotoList(String url) {
		String result;
		try {
			
			LogUtils2.i("the url === "+url);
			 new GetDataTask().execute(url);
//			getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @UiThread
	public void getResult(String result) {
		LogUtils2.d("whaht is the pictureresult = "+result);
		setCacheStr(imgUrl, result);
		dismissProgressDialog();
		try {
			List<PicuterDetailModle> list = PicuterSinaJson.instance(this)
					.readJsonPicuterModle(result);
			picuterDetailAdapter.appendList(list);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils2.e("read json data error ");
		}
	}

	
    private class GetDataTask extends AsyncTask<String, Void, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtils2.e("GetDataTask onPreExecute ----");
		}
		// 后台处理部分
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = HttpUtil.getByHttpClient(mContext, params[0]);

			} catch (Exception e) {
				e.printStackTrace();
				LogUtils2.e("GetDataTask get Data error ----");
			}
			LogUtils2.i("get data from network result == " + result);
			return result;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String result) {
			// 在头部增加新添内容
			// Toast.makeText(getActivity(), "lal", 300).show();
			super.onPostExecute(result);// 这句是必有的，AsyncTask规定的格式
			// if (mPullRefreshListView != null) {
			Message msg = new Message();
			msg.obj = result;
			msg.what = RESPONSE_OK;
			mHandler.sendMessage(msg);
			// mPullRefreshListView.onRefreshComplete();
			// }

		}
	}
    

	
	
	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {

	}
///////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.activity_photo);
		initView();
		init() ;
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
