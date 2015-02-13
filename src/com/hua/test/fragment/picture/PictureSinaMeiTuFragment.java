package com.hua.test.fragment.picture;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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

import com.hua.test.activity.BaseActivity;
import com.hua.test.activity.DetailsActivity;
import com.hua.test.activity.ImageDetailActivity;
import com.hua.test.activity.MainActivityPhone;
import com.hua.test.activity.PictureDetailActivity;
import com.hua.test.activity.PictureSinaActivity;
import com.hua.test.activity.R;
import com.hua.test.adapter.CardsAnimationAdapter;
import com.hua.test.adapter.PicuterAdapter_MeiTu;
import com.hua.test.bean.NewModle;
import com.hua.test.bean.PicuterModle;
import com.hua.test.fragment.BaseFragment;
import com.hua.test.initView.InitView;
import com.hua.test.network.http.json.PicuterSinaJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.swipelistview.SwipeListView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("ValidFragment")
public class PictureSinaMeiTuFragment extends BaseFragment implements
SwipeRefreshLayout.OnRefreshListener{

	private Context mContext;
    /**全局的View*/
    private View contentView;
//    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
//    @ViewById(R.id.listview)
    protected SwipeListView mSwipeListView;
//    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;

//    public int index = 1;
    /**标记获取了的页数下标*/
    private int index = 0;
    
    protected PicuterAdapter_MeiTu mPicuterAdapter;
    protected List<PicuterModle> listsModles;
    private boolean isRefresh = false;
    private static final int RESPONSE_OK = 0;
    private String cacheName ="PictureSinaMeiTuFragment";
    private boolean isCanClick;

	//当前fragment 在viewpage中的第几个页面下的index
	private int mTabIndex;
    
	public PictureSinaMeiTuFragment(int tabIndex) {
		super();
		this.mTabIndex = tabIndex;
	}
    
	public PictureSinaMeiTuFragment() {
		super();
	}
	
    
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
    

    public void getResult(String result) {
    	if(result != null){
    		
    		
    		getMyActivity().setCacheStr(cacheName + currentPagte, result);
    		if (isRefresh) {
    			isRefresh = false;
    			mPicuterAdapter.clear();
    			listsModles.clear();
    		}
//    		dismissProgressDialog();
    		swipeLayout.setRefreshing(false);
    		List<PicuterModle> list = PicuterSinaJson.instance(getActivity()).readJsonPhotoListModles(
    				result);

    		mProgressBar.setVisibility(View.GONE);
    		mPicuterAdapter.appendList(list,index);
    		
    		if(mPicuterAdapter.isNeedUplistsModlesData(index)){
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
    

	
    public void loadNewList(String url) {
  	LogUtils2.i("loadNewList.url = "+url);
      String result;
      try {
//          result = HttpUtil.getByHttpClient(getActivity(), url);
          new GetDataTask().execute(url);
//          getResult(result);
      } catch (Exception e) {
          e.printStackTrace();
          LogUtils2.e("loadNewList error -----");
      }
  }

   
	
	/**加载数据*/
	public void loadData(String url,String cacheFragmentName){
        if (getMyActivity().hasNetWork()) {
            loadNewList(url);
        } else {
            mSwipeListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
//            dismissProgressDialog();
            getMyActivity().showShortToast(getString(R.string.not_network));
            String result = getMyActivity().getCacheStr(cacheFragmentName + currentPagte);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
	}

	
	////SwipeRefreshLayout.OnRefreshListener
	@Override
	public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                index = 0;
                loadData(getSinaMeiTu(index + ""),cacheName);
            }
        }, 2000);
	}

	
    protected void init() {
        listsModles = new ArrayList<PicuterModle>();
    }
	
	public void initContentView(View tempContentView){
		
//		showProgressDialog();
//		if(mTabIndex == PictureSinaActivity.getCurrentFragmentIndex()){
//			showProgressDialog();
//		}
		
		swipeLayout = (SwipeRefreshLayout) tempContentView.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		mSwipeListView = (SwipeListView) tempContentView.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) tempContentView.findViewById(R.id.progressBar);
//		mProgressBar.setVisibility(View.GONE);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mSwipeListView, getActivity());
        // mSwipeListView.addHeaderView(headView);
        mPicuterAdapter = PicuterAdapter_MeiTu.getPicuterAdapter(mContext);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mPicuterAdapter);
        animationAdapter.setAbsListView(mSwipeListView);
        mSwipeListView.setAdapter(animationAdapter);
        mSwipeListView.setOnItemClickListener(new MyMeiTuListViewItemListener());
        
        loadData(getSinaMeiTu(index + ""),cacheName);
        

        mSwipeListView.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPagte++;
                index = index + 1;
                loadData(getSinaMeiTu(index + ""),cacheName);
            }
        });
		
	};
	
	////////////////////////////////

    
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
//		LayoutUtils.init(getActivity(),listsModles,url_maps,newHashMap);
		init();
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
		LogUtils2.w("***onResume***");
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
//    	photoAdapter.getLists().clear();
    }
    
	class MyMeiTuListViewItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(mProgressBar.getVisibility() == View.VISIBLE){
				return;
			}
			
	        PicuterModle photoModle = listsModles.get(position);
	        Bundle bundle = new Bundle();
	        bundle.putString("pic_id", photoModle.getId());
	        ((BaseActivity) getActivity()).openActivity(PictureDetailActivity.class,
	                bundle, 0);
			
		}
		
	}
	
}
