
package com.hua.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hua.test.activity.R;
import com.hua.test.bean.VideoModle;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//@EViewGroup(R.layout.item_video)
public class VideoItemView extends LinearLayout {

//    @ViewById(R.id.video_img)
    protected ImageView videoView;
//    @ViewById(R.id.video_title)
    protected TextView videoTitle;
//    @ViewById(R.id.video_time)
    protected TextView videoTime;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;
    
    private View contentView;
    private Context mContext;

    public VideoItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public VideoItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 mContext = context;
		initView();
    
    }

	public VideoItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 mContext = context;
		initView();
	
	}

	public void initView(){
		
    	options = Options.getListOptions();
    	contentView = LayoutInflater.from(mContext).inflate(R.layout.item_video, null);
    	videoView = (ImageView) contentView.findViewById(R.id.video_img);
    	videoTitle = (TextView) contentView.findViewById(R.id.video_title);
    	videoTime = (TextView) contentView.findViewById(R.id.video_time);
    	
    	addView(contentView);
    	
    }
    
    
    public void setData(VideoModle videoModle) {
    	if(videoModle != null){
    		videoTime.setText(videoModle.getLength());
    		videoTitle.setText(videoModle.getTitle());
    		imageLoader.displayImage(videoModle.getCover(), videoView, options);
    	}
    }

}
