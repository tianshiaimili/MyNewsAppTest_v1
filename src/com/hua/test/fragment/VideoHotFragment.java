package com.hua.test.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.hua.test.activity.R;
import com.hua.test.adapter.CardsAnimationAdapter;
import com.hua.test.adapter.VideoAdapter;
import com.hua.test.bean.NewModle;
import com.hua.test.bean.VideoModle;
import com.hua.test.contants.Url;
import com.hua.test.initView.InitView;
import com.hua.test.network.http.json.ViedoListJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.swipelistview.SwipeListView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.umeng.analytics.MobclickAgent;

//@EFragment(R.layout.activity_main)
public class VideoHotFragment extends BaseFragment implements
		SwipeRefreshLayout.OnRefreshListener {
	// @ViewById(R.id.swipe_container)
	protected SwipeRefreshLayout swipeLayout;
	// @ViewById(R.id.listview)
	protected SwipeListView mSwipeListView;
	// @ViewById(R.id.progressBar)
	protected ProgressBar mProgressBar;

	/** 全局的View */
	private View contentView;
	/***/
	private Context mContext;

	// @Bean
	protected VideoAdapter videoAdapter;
	protected List<VideoModle> listsModles = new ArrayList<VideoModle>();;
	private int index = 0;
	private boolean isRefresh = false;
	private static final int RESPONSE_OK = 0;
	private String cacheName =this.getClass().getSimpleName();

	
    
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
	protected void init() {
//		listsModles = new ArrayList<VideoModle>();
	}

	// @AfterViews
	protected void initContentView(View tempContentView) {
		swipeLayout = (SwipeRefreshLayout) tempContentView.findViewById(R.id.swipe_container);
		mSwipeListView = (SwipeListView) tempContentView.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) tempContentView.findViewById(R.id.progressBar);
		
		swipeLayout.setOnRefreshListener(this);
		InitView.instance().initSwipeRefreshLayout(swipeLayout);
		InitView.instance().initListView(mSwipeListView, getActivity());
		
		videoAdapter = VideoAdapter.getVideoAdapter(mContext);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(
				videoAdapter);
		animationAdapter.setAbsListView(mSwipeListView);
		mSwipeListView.setAdapter(animationAdapter);
		
		loadData(getVideoUrl(index + "", Url.VideoReDianId),cacheName);
		
		mSwipeListView.setOnItemClickListener(new MyVideoListViewItemListener());
		
		mSwipeListView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPagte++;
				index = index + 10;
				loadData(getVideoUrl(index + "", Url.VideoReDianId),cacheName);
			}
		});
	}

	private void loadData(String url,String cacheName) {
		if (getMyActivity().hasNetWork()) {
			loadNewList(url);
		} else {
			mSwipeListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr(
					cacheName + currentPagte);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				currentPagte = 1;
				isRefresh = true;
				index = 0;
				loadData(getVideoUrl(index + "", Url.VideoReDianId),cacheName);
			}
		}, 2000);
	}

	public void enterDetailActivity(VideoModle videoModle) {
		Bundle bundle = new Bundle();
		bundle.putString("playUrl", videoModle.getMp4Hd_url());
		bundle.putString("filename", videoModle.getTitle());
		// ((BaseActivity) getActivity()).openActivity(VideoPlayActivity_.class,
		// bundle, 0);
	}

	// @Background
	void loadNewList(String url) {
		String result;
		try {
//			result = HttpUtil.getByHttpClient(getActivity(), url, null);
//			getResult(result);
			new GetDataTask().execute(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @UiThread
	public void getResult(String result) {
		getMyActivity().setCacheStr(cacheName + currentPagte,
				result);
		if (isRefresh) {
			isRefresh = false;
			videoAdapter.clear();
			listsModles.clear();
		}
		mProgressBar.setVisibility(View.GONE);
		swipeLayout.setRefreshing(false);

		List<VideoModle> list = ViedoListJson.instance(getActivity())
				.readJsonVideoModles(result, Url.VideoReDianId);
		
		videoAdapter.appendList(list,index);
		
		if(videoAdapter.isNeedUplistsModlesData(index)){
			listsModles.addAll(list);
		}
		mSwipeListView.onBottomComplete();
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
    
    
    
	class MyVideoListViewItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			VideoModle videoModle = listsModles.get(position);
			enterDetailActivity(videoModle);
		}
		
	}

	
	///////////////////////////////////////////////
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils2.w("***onCreate***");
		mContext = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils2.i("***onCreateView***");
		contentView = inflater.inflate(R.layout.news_activity_main, null);
//		init();
		// LayoutUtils.init(getActivity(),listsModles,url_maps,newHashMap);
		initContentView(contentView);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils2.w("***onViewCreated***");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils2.w("***onActivityCreated***");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils2.w("***onStart***");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
