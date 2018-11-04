package com.androidxx.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注意：OkHttpClient在一个APP中建议只有一个。（OkHttpClient对象是单例）
 * OkHttp的Get请求方式
 * 分2种：
 * 1、同步请求  execute方法就是同步请求
 * 2、异步请求 enqueue方法就是异步请求，开启的新的线程执行请求，并且子线程是有线程池管理
 */
public class GetActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URL = "http://www.1688wan.com/majax.action?method=bdxqs&pageNo=0";
    private static final String TAG = "GetActivity";
    private TextView mShowResultTxt;
    private Button mAsyncBtn;
    private Button mSyncBtn;

    /**
     * 1、创建一个OkHttpClient对象
     * 2、创建一个Request请求对象
     * 3、执行Request,返回一个Response对象。
     * Request封装的是需要传递给服务器的数据
     * response封装的是服务返回的结果数据
     */
    private OkHttpClient mOkClient = new OkHttpClient();
    private Button mClearBtn;
    private Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        initView();
    }

    private void initView() {

        //创建Request请求对象
        request = new Request.Builder()
                .url(URL)//请求的网络地址
                .get()//表示Get请求
                .build();

        mShowResultTxt = (TextView) findViewById(R.id.okhttp_get_show_result_tv);
        mAsyncBtn = (Button) findViewById(R.id.okhttp_get_async_btn);
        mSyncBtn = (Button) findViewById(R.id.okhttp_get_sync_btn);
        mClearBtn = (Button) findViewById(R.id.btn_clear);
        mAsyncBtn.setOnClickListener(this);
        mSyncBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okhttp_get_sync_btn:
                runOnThread();
                break;

            case R.id.okhttp_get_async_btn:
                asyncRequest();
                break;

            case R.id.btn_clear:
                mShowResultTxt.setText("");
                break;

            default:
                break;

        }
    }


    private void runOnThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = syncRequest();
//                Log.e(TAG, "同步请求" + result);
                LogUtils.e("同步请求", result);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mShowResultTxt.setText(result);
                    }
                });
            }
        }).start();
    }

    /**
     * 同步请求
     */
    private String syncRequest() {
//        //创建Request请求对象
//        Request request = new Request.Builder()
//                .url(URL)//请求的网络地址
//                .get()//表示Get请求
//                .build();
        //执行请求
        try {
            //response是网络请求成功之后，服务器返回的数据的封装对象
//            Call call = mOkClient.newCall(request);
//            Response response = call.execute();
            Response response = mOkClient.newCall(request).execute();
            //获得请求的结果
            //注意：response.body().string()执行获取一次，再次获取为空
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get异步请求
     */
    private void asyncRequest() {
//        //默认是Get请求
//        Request request = new Request.Builder()
//                .url(URL)
//                .build();

//        Call call = mOkClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });

        //enqueue方法会开启一个子线程执行此次请求
        mOkClient.newCall(request).enqueue(new Callback() {
            /**
             * 请求失败
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                //执行在子线程中
                LogUtils.e("onFailure", e.getMessage());
                e.printStackTrace();
            }

            /**
             * 请求成功
             * @param call 请求服务器的对象
             * @param response 请求成功后的返回结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //执行在子线程中
                //获得请求的结果
                //注意：response.body().string()执行获取一次，再次获取为空
                final String result = response.body().string();
//                Log.e(TAG, "异步请求： " + result);
                LogUtils.e("异步请求", result);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mShowResultTxt.setText(result);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Call call = mOkClient.newCall(request);
        call.cancel();
        Log.e(TAG, "取消网络请求：" + call.isCanceled());
    }
}
