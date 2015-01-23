
package com.hua.test.fragment;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hua.test.activity.BaseActivity;
import com.hua.test.contants.Url;
import com.hua.test.utils.DialogUtil;
import com.hua.test.utils.StringUtils;

public class BaseFragment extends Fragment {
	
	  private Dialog progressDialog;
	
    public View mView;
    /**
     * 当前页
     */
    public int currentPagte = 1;

    public BaseActivity getMyActivity() {
        return (BaseActivity) getActivity();
    }

    public String getNewUrl(String index) {
        String urlString = Url.TopUrl + Url.TopId + "/" + index + Url.endUrl;
        return urlString;
    }

    public String getCommonUrl(String index, String itemId) {
        String urlString = Url.CommonUrl + itemId + "/" + index + Url.endUrl;
        return urlString;
    }

    public String getLocalUrl(String index, String itemId) {
        String urlString = Url.Local + itemId + "/" + index + Url.endUrl;
        return urlString;
    }

    public String getFangUrl(String index, String itemId) {
        String urlString = Url.FangChan + itemId + "/" + index + Url.endUrl;
        return urlString;
    }

    public String getPhotosUrl(String index) {
        String urlString = Url.TuJi + index + Url.TuJiEnd;
        return urlString;
    }

    public String getReDianPicsUrl(String index) {
        String urlString = Url.TuPianReDian + index + Url.TuJiEnd;
        return urlString;
    }

    public String getDuJiaPicsUrl(String index) {
        String urlString = Url.TuPianDuJia + index + Url.TuJiEnd;
        return urlString;
    }

    public String getMingXingPicsUrl(String index) {
        String urlString = Url.TuPianMingXing + index + Url.TuJiEnd;
        return urlString;
    }

    public String getTiTanPicsUrl(String index) {
        String urlString = Url.TuPianTiTan + index + Url.TuJiEnd;
        return urlString;
    }

    public String getMeiTuPicsUrl(String index) {
        String urlString = Url.TuPianMeiTu + index + Url.TuJiEnd;
        return urlString;
    }

    public String getSinaJingXuan(String index) {
        String urlString = Url.JINGXUAN_ID + index;
        return urlString;
    }

    public String getSinaQuTu(String index) {
        String urlString = Url.QUTU_ID + index;
        return urlString;
    }

    public String getSinaMeiTu(String index) {
        String urlString = Url.MEITU_ID + index;
        return urlString;
    }

    public String getSinaGuShi(String index) {
        String urlString = Url.GUSHI_ID + index;
        return urlString;
    }

    // ��Ƶ http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
    public String getVideoUrl(String index, String videoId) {
        String urlString = Url.Video + videoId + Url.VideoCenter + index + Url.videoEndUrl;
        return urlString;
    }

    public boolean isNullString(String imgUrl) {

        if (StringUtils.isEmpty(imgUrl)) {
            return true;
        }
        return false;
    }
    
    
    /**
     * 显示dialog
     * 
     * @param msg 显示内容
     */
    public void showProgressDialog() {
        try {

            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(getActivity());

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示dialog2
     * 
     * @param msg 显示内容
     */
    public void showProgressDialog2() {
        try {

            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog2(getActivity());

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 隐藏dialog
     */
    public void dismissProgressDialog() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
}
