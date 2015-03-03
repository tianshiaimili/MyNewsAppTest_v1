package com.hua.test.network.http.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hua.test.bean.ImagesModle;
import com.hua.test.bean.NewModle;
import com.hua.test.utils.LogUtils2;

public class JacksonJsonUtil {
	private static ObjectMapper mapper;
	
	/**
	 * 获取ObjectMapper实例
	 * @param createNew 方式：true，新实例；false,存在的mapper实例
	 * @return
	 */
	public static synchronized ObjectMapper getMapperInstance(boolean createNew) {   
        if (createNew) {   
            return new ObjectMapper();   
        } else if (mapper == null) {   
            mapper = new ObjectMapper();
        }   
        return mapper;   
    } 
	
	/**
	 * 将java对象转换成json字符串
	 * @param obj 准备转换的对象
	 * @return json字符串
	 * @throws Exception 
	 */
	public static String beanToJson(Object obj) throws Exception {
		try {
			ObjectMapper objectMapper = getMapperInstance(false);
			String json =objectMapper.writeValueAsString(obj);
			return json;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}		
	}
	
	/**
	 * 将java对象转换成json字符串
	 * @param obj 准备转换的对象
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return json字符串
	 * @throws Exception
	 */
	public static String beanToJson(Object obj,Boolean createNew) throws Exception {
		try {
			ObjectMapper objectMapper = getMapperInstance(createNew);
			String json =objectMapper.writeValueAsString(obj);
			return json;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}		
	}
	
	/**
	 * 将json字符串转换成java对象
	 * @param json 准备转换的json字符串
	 * @param cls  准备转换的类
	 * @return 
	 * @throws Exception 
	 */
	public static Object jsonToBean(String json, Class<?> cls) throws Exception {
		try {
		ObjectMapper objectMapper = getMapperInstance(false);
		Object vo = objectMapper.readValue(json, cls);
		return vo;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}	
	}
	
	/**
	 * 将json字符串转换成java对象
	 * @param json 准备转换的json字符串
	 * @param cls  准备转换的类
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return
	 * @throws Exception
	 */
	public static Object jsonToBean(String json, Class<?> cls,Boolean createNew) throws Exception {
		try {
		ObjectMapper objectMapper = getMapperInstance(createNew);
		Object vo = objectMapper.readValue(json, cls);
		return vo;
		} catch (Exception e) {
			LogUtils2.e("error == "+e.getMessage());
			throw new Exception(e.getMessage());
		}	
	}
	
	
	
	/**
	 * 将json字符串转换成java对象
	 * @param json 准备转换的json字符串
	 * @param cls  准备转换的类
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return
	 * @throws Exception
	 */
	public static  Object[] jsonArrayToBean(String json, Class<?> cls,Boolean createNew){
		try {
		ObjectMapper objectMapper = getMapperInstance(createNew);
		Object[] vo =null;// objectMapper.readValue(json,GXJSON[].class);
		return vo;
		} catch (Exception e) {
			LogUtils2.e("error == "+e.getMessage());
//			throw new Exception(e.getMessage());
		}	
		
		return null;
	}
	
	/**
	 * 将json字符串转换成java对象
	 * @param json 准备转换的jList bean字符串
	 * @param cls  准备转换的类
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return
	 * @throws Exception
	 */
	public static  Object[] jsonArrayToNewModleBean(String json, Class<?> cls,Boolean createNew){
		try {
		ObjectMapper objectMapper = getMapperInstance(createNew);
		Object[] vo = objectMapper.readValue(json,NewModle[].class);
		return vo;
		} catch (Exception e) {
			LogUtils2.e("error == "+e.getMessage());
//			throw new Exception(e.getMessage());
		}	
		
		return null;
	}
	
    
    /**
     * <b>function:</b>json字符串转换Map集合
     * @author hoojo
     * @createDate Nov 27, 2010 3:00:06 PM
     */
    public static List<NewModle>  readJson2NewModles(String res,String value) {
        String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                    "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
        
        String json2 = "{\"T1348647909107\":[{\"Address\": \"北海拉\",\"Name\":\"haha2\",\"id\":2,\"email\":\"email2\"},{\"address\":\"福成拉\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}"
        		+ ",{\"address\":\"福成拉123\",\"name\":[{\"Test1\": \"北海拉\",\"Test2\":\"haha2\"}],\"id\":1,\"email\":\"email\"}]}";
        
        newModles = new ArrayList<NewModle>();
        try {
        	 NewModle newModle = null;
            Map<String, Map<String, Object>> maps = getMapperInstance(false).readValue(res, Map.class);
            System.out.println(maps.size());
            Set<String> key = maps.keySet();
            
            Iterator<String> iter = key.iterator();
//            while (iter.hasNext()) {
                String field = iter.next();
                System.out.println(field + ":" + maps.get(field));
                LogUtils2.d("-----"+maps.get(field));
                
                ArrayList<Map<String, Object>> resultMaps = (ArrayList<Map<String, Object>>) maps.get(value);
                for(int i = 0;i<resultMaps.size();i++){
                	LogUtils2.e("---------------");
                	 newModle = new NewModle();
                	 Map<String, Object> resultMap = resultMaps.get(i);
                     if (resultMap.get("skipType") != null && ((String)resultMap.get("skipType")).equals("special")) {
                         continue;
                     }
                	///
                     if (resultMap.get("TAGS") != null && resultMap.get("TAGS") == null) {
                         continue;
                     }
                     //////
                     if (resultMap.get("imgextra") != null) {
//                    	 LogUtils2.d("=====================");
                         newModle.setTitle(getStringByJackSon("title", resultMap));
//                         LogUtils2.d("===========title =========="+getStringByJackSon("title", resultMap));
                         newModle.setDocid(getStringByJackSon("docid", resultMap));
                         ImagesModle imagesModle = new ImagesModle();
                         List<String> list;
                         list = readImgListByJackson(resultMap.get("imgextra"));
                         list.add(getStringByJackSon("imgsrc", resultMap));
                         imagesModle.setImgList(list);
                         newModle.setImagesModle(imagesModle);
                     } else {
                         newModle = read2NewModleByJackson(resultMap);
                     }
                     newModles.add(newModle);
                     
                }
                
//            }
        } catch (JsonParseException e) {
        	LogUtils2.e("error =="+e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
        	LogUtils2.e("error =="+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
        	LogUtils2.e("error =="+e.getMessage());
            e.printStackTrace();
        }finally {
            System.gc();
        }
//        LogUtils2.v("***************");
        return newModles;
    }
	
    public static  String getStringByJackSon(String key,Map<String, Object> map){
//    	LogUtils2.v("****getStringByJackSon");
    	if(map.get(key) != null && !((String)map.get(key)).equals("")){
    			String keyValue = (String) map.get(key);
//    			LogUtils2.v("****keyValue =="+keyValue);
    			return keyValue;
    	}
    	return "";
    	
    }
    
    /**
     * 解析图片集
     * 返回图片的url集合
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public static List<String> readImgListByJackson(Object object) throws Exception {
        List<Map<String, Object>> imgList = (List<Map<String, Object>>) object;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < imgList.size(); i++) {
        	
        	Map<String, Object> map = imgList.get(i);
        	
        	list.add(getStringByJackSon("imgsrc", map));
        }
//        LogUtils2.v("------list.size=="+list.size());
        return list;
    }
    
    
    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static NewModle read2NewModleByJackson(Map<String, Object> resultMap) throws Exception {
        NewModle newModle = null;

        String docid = "";
        String title = "";
        String digest = "";
        String imgsrc = "";
        String source = "";
        String ptime = "";
        String tag = "";

        docid = getStringByJackSon("docid", resultMap);
        title = getStringByJackSon("title", resultMap);
        digest = getStringByJackSon("digest", resultMap);
        imgsrc = getStringByJackSon("imgsrc", resultMap);
        source = getStringByJackSon("source", resultMap);
        ptime = getStringByJackSon("ptime", resultMap);
        tag = getStringByJackSon("TAG", resultMap);

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
     * 解析获取的新闻json数据
     * @param res
     * @param value
     * @return
     */
    public static List<NewModle> newModles;
    public static List<NewModle> readJsonNewModles(String res, String value) {
        newModles = new ArrayList<NewModle>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            NewModle newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            
            LogUtils2.i("jsonObject == "+jsonObject.toString());
            LogUtils2.i("value == "+jsonArray.toString());
            LogUtils2.i("value == "+jsonArray.length());
            
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
     * @param key
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static String getString(String key, JSONObject jsonObject) throws Exception {
        String res = "";
        if (jsonObject.has(key)) {
            if (key == null) {
                return "";
            }
            res = jsonObject.getString(key);
        }
        return res;
    }
    
    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static NewModle readNewModle(JSONObject jsonObject) throws Exception {
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
     * 解析图片集
     * 返回图片的url集合
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public static List<String> readImgList(JSONArray jsonArray) throws Exception {
        List<String> imgList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            imgList.add(getString("imgsrc", jsonArray.getJSONObject(i)));
        }

        return imgList;
    }
}
