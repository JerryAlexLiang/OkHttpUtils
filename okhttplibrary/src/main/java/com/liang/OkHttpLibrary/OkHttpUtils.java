package com.liang.OkHttpLibrary;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jerry on 2016/9/4.
 */
public class OkHttpUtils {

    private static OkHttpClient okHttpClient;

    /**
     * 初始化
     * 注意：官方建议OkHttpClient在一个APP中只实例化一次
     *
     * @return
     */
    public static OkHttpTask newInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return new OkHttpTask();
    }

    public static class OkHttpTask extends AsyncTask<String, Integer, String> {

        public static final String URL_REGEX = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        private IOKCallBack okCallBack;
        private FormBody formBody;


        public OkHttpTask start(String url) {

            //判断URL是否正确，使用正则表达式

            if (url == null) {
                throw new NullPointerException("url is null");
            }

            if (!url.matches(URL_REGEX)) {
                throw new IllegalArgumentException("url is error");
            }

            this.execute(url);
            return this;
        }

        public OkHttpTask post(Map<String, String> params) {

            Set<String> keySet = params.keySet();
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : keySet) {
                String value = params.get(key);
                builder.add(key, value);
            }
            formBody = builder.build();
            return this;
        }

        public void callback(IOKCallBack callBack) {
            this.okCallBack = callBack;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            //使用OkHttp请求网络
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
            /**
             * 同步请求
             * 创建一个Request对象，Request对象封装请求的地址，以及参数(POST请求)，还有一些请求的头部信息
             */
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            if (formBody != null) {
                builder.post(formBody);
            }
            Request request = builder.build();

            /**
             * 执行Request，并且返回Response对象
             * Response对象中包含Request请求返回的结果
             * 注意：此处请求网络没有开启新的线程，所以，此句代码不能在UI线程中执行
             */
            try {
                Response response = okHttpClient.newCall(request).execute();
//                String result = response.body().string();//只能获取一次结果数据
//                return result;
                return response.body().string();//返回Response对象,只能获取一次结果数据
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //接收到网络请求的结果
            if (okCallBack != null ) {
                okCallBack.success(s);
            }

        }

    }

    /**
     * 判断当前网络状态
     * @param activity
     * @return
     */
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
