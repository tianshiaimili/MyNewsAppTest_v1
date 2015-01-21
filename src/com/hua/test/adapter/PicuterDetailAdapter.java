
package com.hua.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.test.bean.PicuterDetailModle;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.PhotoDetailView;

//@EBean
public class PicuterDetailAdapter extends BaseAdapter {

    public List<PicuterDetailModle> lists = new ArrayList<PicuterDetailModle>();
    private  Context context;


    public PicuterDetailAdapter (Context mContext){
    	context = mContext;
    }
    
    
    public void appendList(List<PicuterDetailModle> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
        	LogUtils2.i("the size of the PicuterDetailAdapter.list = "+list.size());
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    
    public List<PicuterDetailModle> getLists() {
		return lists;
	}



	public void setLists(List<PicuterDetailModle> lists) {
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
        PhotoDetailView photoItemView;
        if (convertView == null) {
//            photoItemView = PhotoDetailView_.build(context);
        	photoItemView = new PhotoDetailView(context);
        } else {
            photoItemView = (PhotoDetailView) convertView;
        }

        PicuterDetailModle picuterDetailModle = lists.get(position);

        photoItemView.setImage(lists.size(), position, picuterDetailModle.getAlt(),
                picuterDetailModle.getTitle(), picuterDetailModle.getPic());

        return photoItemView;
    }

}
