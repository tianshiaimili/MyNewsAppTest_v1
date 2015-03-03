
package com.hua.test.fragment.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.hua.test.activity.BaseActivity;
import com.hua.test.activity.DetailsActivity;
import com.hua.test.activity.ImageDetailActivity;
import com.hua.test.activity.MainActivityPhone;
import com.hua.test.activity.R;
import com.hua.test.adapter.CardsAnimationAdapter;
import com.hua.test.adapter.news.TiYuAdapter;
import com.hua.test.bean.NewModle;
import com.hua.test.contants.Url;
import com.hua.test.fragment.BaseFragment;
import com.hua.test.initView.InitView;
import com.hua.test.network.http.json.HttpGetJsonUtil;
import com.hua.test.network.http.json.JacksonJsonUtil;
import com.hua.test.network.http.json.NewListJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.swipelistview.SwipeListView;
import com.hua.test.widget.viewimage.Animations.DescriptionAnimation;
import com.hua.test.widget.viewimage.Animations.SliderLayout;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.hua.test.widget.viewimage.SliderTypes.TextSliderView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.umeng.analytics.MobclickAgent;

//@EFragment(R.layout.activity_main)
@SuppressLint("ValidFragment")
public class TiYuFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnSliderClickListener {
    protected SliderLayout mDemoSlider;
//    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
//    @ViewById(R.id.listview)
    protected SwipeListView mSwipeListView;
//    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NewModle> newHashMap;

//    @Bean
    protected TiYuAdapter tiYuAdapter;
    protected List<NewModle> listsModles = new ArrayList<NewModle>();

	/** 全局的View */
	private View contentView;
	/***/
	private Context mContext;
	/** 标记获取了的页数下标 */
	private int index = 0;
	private boolean isRefresh = false;
	private static final int RESPONSE_OK = 0;
	private String cacheName = "TiYuFragment";// this.getClass().getSimpleName();
    
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			int what = message.what;
			int numchange = what;
			// LogUtils2.i("what==" + what);
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

    protected void init() {
        LogUtils2.d("what is the value of index = " + index);
		mContext = getActivity();
		// listsModles = new ArrayList<NewModle>();
		url_maps = new HashMap<String, String>();

		newHashMap = new HashMap<String, NewModle>();
    }

//    @AfterViews
    protected void initContentView(View tempContentView) {
//    	showProgressDialog();
    	
//		LogUtils2.i("the tabIndex = "+mTabIndex);
//		LogUtils2.d("the MainActivityPhone tabIndex = "+MainActivityPhone.getCurrentFragmentIndex());
//		if(mTabIndex == MainActivityPhone.getCurrentFragmentIndex()){
//			showProgressDialog();
//		}
    	
    	
		swipeLayout = (SwipeRefreshLayout) tempContentView
				.findViewById(R.id.swipe_container);
		mSwipeListView = (SwipeListView) tempContentView
				.findViewById(R.id.listview);
		 mProgressBar = (ProgressBar)
		 tempContentView.findViewById(R.id.progressBar);

		// LogUtils2.e("*******initView*************");
		LogUtils2.e("*******index*************== " + index);
		swipeLayout.setOnRefreshListener(this);
		InitView.instance().initSwipeRefreshLayout(swipeLayout);
		InitView.instance().initListView(mSwipeListView, getActivity());
		// /add HeadView
		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_item, null);
		mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
		mSwipeListView.addHeaderView(headView);

		// set the adapter of ListView
		// newAdapter = new NewAdapter(mContext);
		tiYuAdapter = TiYuAdapter.getYuLeAdapter(mContext);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(
				tiYuAdapter);
		animationAdapter.setAbsListView(mSwipeListView);
		mSwipeListView.setAdapter(animationAdapter);

		// /load data
		loadData(getCommonUrl(index + "", Url.TiYuId), cacheName);

		mSwipeListView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPagte++;
				index = index + 20;
				LogUtils2.i("onButtomListener  the index is " + index);
				loadData(getCommonUrl(index + "", Url.TiYuId), cacheName);
			}
		});

		mSwipeListView.setOnItemClickListener(new MyTiYuListViewItemListener());

		mSwipeListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				LogUtils2.i("******onScrollStateChanged**********");
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				} else {


				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
    }


	/** 加载数据 */
	public void loadData(String url, String cacheFragmentName) {
		// LogUtils2.e("commentUrl = "+this.getClass().getSimpleName()+"   cacheName ="+cacheName);
		if (getMyActivity().hasNetWork()) {
			loadNewList(url);
		} else {
			mSwipeListView.onBottomComplete();
//			dismissProgressDialog();
			 mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr(
					cacheFragmentName + currentPagte);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}


	/**
	 * 第一次进来时 把banner部分初始化处理
	 * 
	 * @param newModles
	 */
	private void initSliderLayout(List<NewModle> newModles) {

		if (!isNullString(newModles.get(0).getImgsrc()))
			newHashMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
		if (!isNullString(newModles.get(1).getImgsrc()))
			newHashMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
		if (!isNullString(newModles.get(2).getImgsrc()))
			newHashMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
		if (!isNullString(newModles.get(3).getImgsrc()))
			newHashMap.put(newModles.get(3).getImgsrc(), newModles.get(3));

		if (!isNullString(newModles.get(0).getImgsrc()))
			url_maps.put(newModles.get(0).getTitle(), newModles.get(0)
					.getImgsrc());
		if (!isNullString(newModles.get(1).getImgsrc()))
			url_maps.put(newModles.get(1).getTitle(), newModles.get(1)
					.getImgsrc());
		if (!isNullString(newModles.get(2).getImgsrc()))
			url_maps.put(newModles.get(2).getTitle(), newModles.get(2)
					.getImgsrc());
		if (!isNullString(newModles.get(3).getImgsrc()))
			url_maps.put(newModles.get(3).getTitle(), newModles.get(3)
					.getImgsrc());

		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(getActivity());
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(name).image(url_maps.get(name));

			textSliderView.getBundle().putString("extra", name);
			mDemoSlider.addSlider(textSliderView);
		}

		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
		mDemoSlider
				.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());
		LogUtils2.i("*****mViewFlowAdapter.setAdapterData********");
		// mViewFlowAdapter.setAdapterData(newHashMap, url_maps);
		LogUtils2.e("");
		tiYuAdapter.appendList(newModles, index);
	}


	public void getResult(String result) {
		if (result != null) {

			LogUtils2.e("getMyActivity = "+getMyActivity());
			getMyActivity().setCacheStr(cacheName + currentPagte, result);
			if (isRefresh) {
				isRefresh = false;
				tiYuAdapter.clear();
				listsModles.clear();
			}
			swipeLayout.setRefreshing(false);
//			List<NewModle> list = NewListJson.instance(getActivity())
//					.readJsonNewModles(result, Url.TiYuId);
			
    		//这是JsonObject来解析json数据
//    		List<NewModle> list =
//    				NewListJson.instance(getActivity()).readJsonNewModles(result,
//    						Url.TopId);

    		//这是用jackSon 来解析数据 这个更加快捷 但是 有点麻烦
    		List<NewModle> list =JacksonJsonUtil.readJson2NewModles(result, Url.TiYuId);
    		/////
			
			mProgressBar.setVisibility(View.GONE);
			if (index == 0) {
				// LogUtils2.i("is first come in************");
				initSliderLayout(list);
			} else {
				// LogUtils2.i("add data to the listView************");
				tiYuAdapter.appendList(list, index);
			}

//			dismissProgressDialog();

			if (tiYuAdapter.isNeedUplistsModlesData(index)) {
				listsModles.addAll(list);
			}
			mSwipeListView.onBottomComplete();
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
			// LogUtils2.i("get data from network result == " + result);
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
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				currentPagte = 1;
				isRefresh = true;
				index = 0;
				loadData(getCommonUrl(0 + "", Url.TiYuId), cacheName);
				url_maps.clear();
				mDemoSlider.removeAllSliders();
			}
		}, 2000);
	}
	
	
	// @Background
	public void loadNewList(String url) {
		LogUtils2.i("loadNewList.url = " + url);
		String result;
		try {
	    	  //这是android原生的单线程获取数据 不怎么好用
//          new GetDataTask().execute(url);
    	  //下面是使用async-android-http 开源框架来加载数据
      	
    	HttpGetJsonUtil.get(url, new AsyncHttpResponseHandler() { 
    		
			
			@Override
			public void onSuccess(int statueCode, Header[] arg1, byte[] result) {
				LogUtils2.e("___________result________________= "+result);
				Message msg = new Message();
				msg.obj = new String(result);
				msg.what = RESPONSE_OK;
				mHandler.sendMessage(msg);
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				LogUtils2.e("___________error_________________");
			}
		});
    	
    	/////////////////
			// getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils2.e("loadNewList error -----");
		}
	}


	// //////////////////////////////////////

	class MyTiYuListViewItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// LogUtils2.e("in the onItemClick the position = "+position);
			// Toast.makeText(mContext, "  pos== "+position, 300).show();
			if(mProgressBar.getVisibility() == View.VISIBLE){
				return;
			}
			
			NewModle newModle = listsModles.get(position - 1);
			enterDetailActivity(newModle);
		}

	}
	

	// open the head bar pic
	@Override
	public void onSliderClick(BaseSliderView slider) {
		NewModle newModle = newHashMap.get(slider.getUrl());
		enterDetailActivity(newModle);
	}

	public void enterDetailActivity(NewModle newModle) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("newModle", newModle);
		Class<?> class1;
		if (newModle.getImagesModle() != null
				&& newModle.getImagesModle().getImgList().size() > 1) {
			class1 = ImageDetailActivity.class;
		} else {
			class1 = DetailsActivity.class;
		}
		((BaseActivity) getActivity()).openActivity(class1, bundle, 0);
	}

	// ///////////////oncreate ...///////////////////////

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			LogUtils2.i("***onCreate***");
			mContext = getActivity();

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			LogUtils2.i("***onCreateView***");
			contentView = inflater.inflate(R.layout.news_activity_main, null);
			init();
			// LayoutUtils.init(getActivity(),listsModles,url_maps,newHashMap);
			initContentView(contentView);
			return contentView;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			LogUtils2.i("***onViewCreated***");
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			LogUtils2.i("***onActivityCreated***");
		}

		@Override
		public void onStart() {
			super.onStart();
			LogUtils2.i("***onStart***");
		}

		@Override
		public void onResume() {
			super.onResume();
			LogUtils2.i("***onResume***");
			MobclickAgent.onPageStart("MainScreen"); // 统计页面
		}

		@Override
		public void onPause() {
			super.onPause();
			MobclickAgent.onPageEnd("MainScreen");
		}

		@Override
		public void onStop() {
			super.onStop();
			LogUtils2.w("***onStop***");
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			LogUtils2.w("***onDestroy***");
			// yuLeAdapter.getLists().clear();
		}
}
