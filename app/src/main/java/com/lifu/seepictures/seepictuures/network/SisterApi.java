package com.lifu.seepictures.seepictuures.network;

import android.util.Log;

import com.lifu.seepictures.seepictuures.bean.Sister;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 描述：网络请求处理相关类
 * Created by PC-LI on 2018/2/28.
 */

public class SisterApi {
    private static final String TAG="Network";
    private static final String BASE_URL="http://gank.io/api/data/福利/";

    public ArrayList<Sister> fetchSister(int count, int page)  {
        String fetchUrl=BASE_URL+count+"/"+page;
        ArrayList<Sister> sisters=new ArrayList<>();
        try {
            URL url=new URL(fetchUrl);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            int code=conn.getResponseCode();
            if (code==200){
                InputStream in=conn.getInputStream();
                byte[] data=readFromInput(in);
                String result=new String(data,"UTF-8");
                sisters=parseSister(result);
            }else {
                Log.e(TAG, "请求失败: "+code );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }

    private ArrayList<Sister> parseSister(String content) throws Exception {
        ArrayList<Sister> sisters=new ArrayList<>();
        JSONObject object=new JSONObject(content);
        JSONArray array=object.getJSONArray("results");
        for (int i=0;i<array.length();i++){
            JSONObject results= (JSONObject) array.get(i);
            Sister sister=new Sister();
            sister.set_id(results.getString("_id"));
            sister.setCreateAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used"));
            sister.setWho(results.getString("who"));
            sisters.add(sister);
        }
        return sisters;
    }

    //读取数据中方法
    private byte[] readFromInput(InputStream in) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] bytes=new byte[1024];
        int len;
        while ((len=in.read(bytes))!=-1){
            out.write(bytes,0,len);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
}
