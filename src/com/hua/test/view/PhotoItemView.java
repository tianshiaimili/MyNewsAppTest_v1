
package com.hua.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hua.test.activity.R;
import com.hua.test.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//@EViewGroup(R.layout.item_photo)
public class PhotoItemView extends RelativeLayout {

//    @ViewById(R.id.photo_img)
    protected ImageView photoImg;

//    @ViewById(R.id.photo_title)
    protected TextView photoTitle;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;
    private Context mContext ;
    private View contentView;

    public PhotoItemView(Context context) {
        super(context);
        initView(context);
    }

    
    public PhotoItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
    }

	public PhotoItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}





	public void initView(Context context) {
    	this.mContext = context;
    	options = Options.getListOptions();
    	contentView = LayoutInflater.from(context).inflate(R.layout.item_photo, null);
    	photoImg = (ImageView) contentView.findViewById(R.id.photo_img);
    	photoTitle = (TextView) contentView.findViewById(R.id.photo_title);
        addView(contentView);
    }
    
    
    public void setData(String title, String picUrl) {

        picUrl = picUrl.replace("auto", "854x480x75x0x0x3");

        photoTitle.setText(title);
        imageLoader.displayImage(picUrl, photoImg, options);
    }

}
