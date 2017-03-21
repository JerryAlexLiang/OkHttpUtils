package com.example.yangliang.okhttputils.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangliang.okhttputils.R;
import com.liang.OkHttpLibrary.OkHttpUtils;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIDEO_URL = "http://60.174.83.217:8091/wjbzh5/spzb/spzbIndex.do?";

    private boolean netConnect;
    private WebView mWebView;
    private ProgressDialog progressDialog;
    private ImageView mBackBtn;
    private TextView mHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //判断当前网络状态
        netConnect = OkHttpUtils.isNetworkConnected(this);
        if (!netConnect) {
            Toast.makeText(this, "当前网络未连接,请查看网络状态", Toast.LENGTH_SHORT).show();
        }
        //初始化视图
        mWebView = (WebView) findViewById(R.id.web_view);
        mBackBtn = (ImageView) findViewById(R.id.action_back);
        mHot = (TextView) findViewById(R.id.tv_hot);

        //监听事件
        mBackBtn.setOnClickListener(this);
        mHot.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage("视频页面加载中...");

        // 加载url
        mWebView.loadUrl(VIDEO_URL);
        // 初始化比例
        //mWebView.setInitialScale(50);
        WebSettings settings = mWebView.getSettings();
        // 支持使用接口,可任意比例缩放
        settings.setUseWideViewPort(true);
        // setUseWideViewPort方法设置WebView推荐使用的窗口。setLoadWithOverviewMode方法是设置WebView加载的页面的模式。
        settings.setLoadWithOverviewMode(true);
        // 支持脚本JavaScript
        settings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    /**
     * 主要是把WebView所持用的资源销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("about:blank");
        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.destroy();
        mWebView = null;
    }

    /**
     * 点击监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.action_back:
                //返回上一层
                finish();
                break;

            case R.id.tv_hot:
                //跳转到搜索大厅页面
                Intent intent = new Intent(WebViewActivity.this, HotActivity.class);
                startActivity(intent);
                break;
        }

    }

}
