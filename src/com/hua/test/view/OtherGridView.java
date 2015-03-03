package com.hua.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 这是针对 添加频道页面的 其他 频道的 Gridview
 * @author zero
 *
 */
public class OtherGridView extends GridView {

	public OtherGridView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
