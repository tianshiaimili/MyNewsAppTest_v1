
package com.hua.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.test.bean.VideoModle;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.VideoItemView;

//@EBean
public class VideoGaoXiaoAdapter extends BaseAdapter {
    public List<VideoModle> lists = new ArrayList<VideoModle>();
    private Context mContext;
    private static VideoGaoXiaoAdapter mVideoAdapter;
    private int oldIndex = -1;
    private boolean isNeedUplistsModlesData;

//    public void appendList(List<VideoModle> list) {
//        if (!lists.containsAll(list) && list != null && list.size() > 0) {
//            lists.addAll(list);
//        }
//        notifyDataSetChanged();
//    }

    
    public void appendList(List<VideoModle> list,int newIndex) {
//    	LogUtils2.d("list---"+list.size());
//    	LogUtils2.i("newIndex = "+newIndex+"   oldIndex = "+oldIndex);
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
//            LogUtils2.e("*********lists.size==***== " +lists.size());
        }
        notifyDataSetChanged();
    }

    
    
//    @RootContext
    public static  VideoGaoXiaoAdapter getVideoAdapter(Context tempContext){
    	if(mVideoAdapter == null){
    		mVideoAdapter = new VideoGaoXiaoAdapter(tempContext);
    	}
    	return mVideoAdapter;
    }

    
    public VideoGaoXiaoAdapter (Context tempContext){
    	if(tempContext != null){
    		mContext = tempContext;
    	}
    	
    	if(lists == null){
    		 lists = new ArrayList<VideoModle>();
    	}else {
			lists = getLists();
		}
    	
    }
    
    public List<VideoModle> getLists() {
		return lists;
	}

	public void setLists(List<VideoModle> lists) {
		this.lists = lists;
	}

	public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

	public boolean isNeedUplistsModlesData(int index){
		return isNeedUplistsModlesData;
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

        VideoItemView videoItemView;

        if (convertView == null) {
//            videoItemView = VideoItemView_.build(mContext);
        	videoItemView = new VideoItemView(mContext);
        } else {
            videoItemView = (VideoItemView) convertView;
        }

        VideoModle videoModle = lists.get(position);
        videoItemView.setData(videoModle);

        return videoItemView;
    }
}
