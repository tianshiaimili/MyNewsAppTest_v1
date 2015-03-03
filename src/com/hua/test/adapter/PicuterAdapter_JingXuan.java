package com.hua.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.test.bean.NewModle;
import com.hua.test.bean.PicuterModle;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.PhotoItemView;

//@EBean
public class PicuterAdapter_JingXuan extends BaseAdapter {
	public List<PicuterModle> lists = new ArrayList<PicuterModle>();
	private static  PicuterAdapter_JingXuan mPicuterAdapter;
	private Context context;
	private int oldIndex = -1;
	private boolean isNeedUplistsModlesData;

//	public void appendList(List<PicuterModle> list) {
//		if (!lists.containsAll(list) && list != null && list.size() > 0) {
//			lists.addAll(list);
//		}
//		notifyDataSetChanged();
//	}

    public void appendList(List<PicuterModle> list,int newIndex) {
    	LogUtils2.d("list---"+list.size());
    	LogUtils2.i("newIndex = "+newIndex+"   oldIndex = "+oldIndex);
        if (!lists.contains(list.get(0)) && list != null && list.size() > 0 && newIndex != oldIndex) {
        	if (newIndex == 0 && lists.size() == 0) {
				lists.addAll(list);
				isNeedUplistsModlesData = true;
				oldIndex = -1;
			} else if (newIndex == 0 && lists.size() != 0) {

			} else {
				lists.addAll(list);
				if (newIndex == 0) {
					isNeedUplistsModlesData = true;
					oldIndex = -1;
				} else {
					isNeedUplistsModlesData = true;
					oldIndex = newIndex;
				}
				LogUtils2.e("*********lists.size==***== " + lists.size());
			}
        }
        notifyDataSetChanged();
    }
	
    
    public boolean isNeedUplistsModlesData(int newIndex){
    	return isNeedUplistsModlesData;
    }

    public static PicuterAdapter_JingXuan getPicuterAdapter(Context tempContext){
    	
    	if(mPicuterAdapter == null){
    		mPicuterAdapter = new PicuterAdapter_JingXuan(tempContext);
    	}
    	return mPicuterAdapter;
    	
    }
    
    public PicuterAdapter_JingXuan (Context tempContext){
    	if(tempContext != null){
    		context = tempContext;
    	}
    	
    	if(lists == null){
    		LogUtils2.i("***********NewAdapter.lists==null******");
    		 lists = new ArrayList<PicuterModle>();
    	}else {
    		LogUtils2.d("***********NewAdapter.lists != null******");
			lists = getLists();
		}
    	
    }

    
	public List<PicuterModle> getLists() {
		return lists;
	}


	public void setLists(List<PicuterModle> lists) {
		this.lists = lists;
	}


	public void clear() {
		lists.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PhotoItemView photoItemView;

		if (convertView == null) {
//			photoItemView = PhotoItemView_.build(context);
			photoItemView = new PhotoItemView(context);
		} else {
			photoItemView = (PhotoItemView) convertView;
		}

		PicuterModle picuterModle = lists.get(position);

		photoItemView.setData(picuterModle.getTitle(), picuterModle.getPic());

		return photoItemView;
	}
}
