package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by dell1 on 2016/12/15.
 */

public class Xutil {

//    public static String URL = "http://121.43.101.148:5601/forward-service/api";

    // 正汇正式环境
    public static String URL = "http://139.224.200.54:5601/forward-service/api";

    public void post(final String code, String json, final XUtils3CallBackPost backPost){
        RequestParams params = new RequestParams(URL);
        params.addBodyParameter("code", code);
        params.addBodyParameter("json", json);

        System.out.println("lei_http,code="+code);
        System.out.println("lei_http,json="+json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getString("errorCode").equals("0")){

                        if(result.indexOf("xn0000")!= -1){
                            backPost.onSuccess("indexOutOf");
                        }else{
                            backPost.onSuccess(object.getString("data"));
                            System.out.println("code="+code+",onSuccess="+result);
                        }

                    }else if(object.getString("errorCode").equals("3")){

                        System.out.println("code="+code+",onTip="+result);
                            backPost.onTip(object.getString("errorInfo"));

                    }else{
                        System.out.println("code="+code+"请求失败，errorCode="+object.getString("errorCode")+",errorInfo:"+object.getString("errorInfo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException){// 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();

                    System.out.println("code="+code+".onError=网络错误");
                }
                System.out.println("code="+code+", onError="+ex.getMessage());
//                backPost.onError(ex.getMessage(),isOnCallback);
                backPost.onTip("code="+code+", onError="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void get(final String code,String url, final XUtils3CallBackGet backGet){
        RequestParams params = new RequestParams(URL + url);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                System.out.println("x.http().get().onCache="+result);
                return false;
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getString("errorCode").equals("0")){

                        backGet.onSuccess(object.getString("data"));
                        System.out.println("code="+code+",onSuccess="+result);

                    }else if(object.getString("errorCode").equals("3")){

                        System.out.println("code="+code+",onTip="+result);
                        backGet.onTip(object.getString("errorInfo"));

                    }else{
                        System.out.println("code="+code+"请求失败，errorCode="+object.getString("errorCode")+",errorInfo:"+object.getString("errorInfo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("x.http().get().onError="+ex.getMessage());
                if (ex instanceof HttpException){// 网络错误

                }
                backGet.onError(ex.getMessage(),isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("x.http().get().onCancelled");
            }

            @Override
            public void onFinished() {
                System.out.println("x.http().get().onFinished");
            }
        });
    }


    public interface XUtils3CallBackPost {

        void onSuccess(String result);

        void onTip(String tip);

        void onError(String error, boolean isOnCallback);
    }

    public interface XUtils3CallBackGet {

        void onSuccess(String result);

        void onTip(String tip);

        void onError(String error, boolean isOnCallback);
    }
}
