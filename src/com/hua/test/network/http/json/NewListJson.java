
package com.hua.test.network.http.json;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.hua.test.bean.ImagesModle;
import com.hua.test.bean.NewModle;
import com.hua.test.utils.LogUtils2;

public class NewListJson extends JsonPacket {

    public static NewListJson newListJson;

    public List<NewModle> newModles;
    
    public List<SoftReference<NewModle>> newModles2;

    public NewListJson(Context context) {
        super(context);
    }

    public static NewListJson instance(Context context) {
        if (newListJson == null) {
            newListJson = new NewListJson(context);
        }
        return newListJson;
    }

    /**
     * 解析获取的新闻json数据
     * @param res
     * @param value
     * @return
     */
    public List<NewModle> readJsonNewModles(String res, String value) {
        newModles = new ArrayList<NewModle>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            NewModle newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            // if (isFirst) {
            // for (int i = 0; i < 4; i++) {
            // JSONObject js = jsonArray.getJSONObject(i);
            // newModle = readNewModle(js);
            // newModles.add(newModle);
            // }
            // }

            for (int i = 1; i < jsonArray.length(); i++) {
                newModle = new NewModle();
                JSONObject js = jsonArray.getJSONObject(i);
                if (js.has("skipType") && js.getString("skipType").equals("special")) {
                    continue;
                }
                if (js.has("TAGS") && !js.has("TAG")) {
                    continue;
                }
                if (js.has("imgextra")) {
                    newModle.setTitle(getString("title", js));
                    newModle.setDocid(getString("docid", js));
                    ImagesModle imagesModle = new ImagesModle();
                    List<String> list;
                    list = readImgList(js.getJSONArray("imgextra"));
                    list.add(getString("imgsrc", js));
                    imagesModle.setImgList(list);
                    newModle.setImagesModle(imagesModle);
                } else {
                    newModle = readNewModle(js);
                }
                newModles.add(newModle);
            }
        } catch (Exception e) {

        } finally {
            System.gc();
        }
        return newModles;
    }

    
    /**
     * 解析获取的新闻json数据 采用弱引用 试试
     * @param res
     * @param value
     * @return
     */
    public List<SoftReference<NewModle>> readJsonNewModles2(String res, String value) {
    	
    	LogUtils2.e("******readJsonNewModles2*****");
    	
        newModles2 = new ArrayList<SoftReference<NewModle>>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            NewModle mNewModle = null;
            SoftReference<NewModle> newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            // if (isFirst) {
            // for (int i = 0; i < 4; i++) {
            // JSONObject js = jsonArray.getJSONObject(i);
            // newModle = readNewModle(js);
            // newModles.add(newModle);
            // }
            // }

            for (int i = 1; i < jsonArray.length(); i++) {
            	mNewModle = new NewModle();
            	 newModle = new SoftReference<NewModle>(mNewModle);
            	 LogUtils2.e("******WeakRe1111ference*****");
                JSONObject js = jsonArray.getJSONObject(i);
                if (js.has("skipType") && js.getString("skipType").equals("special")) {
                    continue;
                }
                if (js.has("TAGS") && !js.has("TAG")) {
                    continue;
                }
                if (js.has("imgextra")) {
                    newModle.get().setTitle(getString("title", js));
                    newModle.get().setDocid(getString("docid", js));
                    ImagesModle imagesModle = new ImagesModle();
                    List<String> list;
                    list = readImgList(js.getJSONArray("imgextra"));
                    list.add(getString("imgsrc", js));
                    imagesModle.setImgList(list);
                    newModle.get().setImagesModle(imagesModle);
                } else {
                    newModle = readNewModle2(js);
                }
                LogUtils2.e("******SoftReference*****");
                LogUtils2.e("******SoftReference.NewModel = "+newModle.get());
                
                newModles2.add(newModle);
            }
        } catch (Exception e) {

        	LogUtils2.e("******SoftReference.error*****"+e.getMessage());
        	
        } finally {
            System.gc();
        }
        LogUtils2.e("the newModel = "+newModles2.get(0).get());
        return newModles2;
    }
    
    
    /**
     * 解析图片集
     * 返回图片的url集合
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public List<String> readImgList(JSONArray jsonArray) throws Exception {
        List<String> imgList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            imgList.add(getString("imgsrc", jsonArray.getJSONObject(i)));
        }

        return imgList;
    }

    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public NewModle readNewModle(JSONObject jsonObject) throws Exception {
        NewModle newModle = null;

        String docid = "";
        String title = "";
        String digest = "";
        String imgsrc = "";
        String source = "";
        String ptime = "";
        String tag = "";

        docid = getString("docid", jsonObject);
        title = getString("title", jsonObject);
        digest = getString("digest", jsonObject);
        imgsrc = getString("imgsrc", jsonObject);
        source = getString("source", jsonObject);
        ptime = getString("ptime", jsonObject);
        tag = getString("TAG", jsonObject);

        newModle = new NewModle();

        newModle.setDigest(digest);
        newModle.setDocid(docid);
        newModle.setImgsrc(imgsrc);
        newModle.setTitle(title);
        newModle.setPtime(ptime);
        newModle.setSource(source);
        newModle.setTag(tag);

        return newModle;
    }

    
    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public SoftReference<NewModle> readNewModle2(JSONObject jsonObject) throws Exception {
    	NewModle mNewModle = null;
    	SoftReference<NewModle>  newModle = null;

        String docid = "";
        String title = "";
        String digest = "";
        String imgsrc = "";
        String source = "";
        String ptime = "";
        String tag = "";

        docid = getString("docid", jsonObject);
        title = getString("title", jsonObject);
        digest = getString("digest", jsonObject);
        imgsrc = getString("imgsrc", jsonObject);
        source = getString("source", jsonObject);
        ptime = getString("ptime", jsonObject);
        tag = getString("TAG", jsonObject);
        
        mNewModle = new NewModle();
        newModle = new SoftReference<NewModle>(mNewModle);

        newModle.get().setDigest(digest);
        newModle.get().setDocid(docid);
        newModle.get().setImgsrc(imgsrc);
        newModle.get().setTitle(title);
        newModle.get().setPtime(ptime);
        newModle.get().setSource(source);
        newModle.get().setTag(tag);

        return newModle;
    }
    
}
