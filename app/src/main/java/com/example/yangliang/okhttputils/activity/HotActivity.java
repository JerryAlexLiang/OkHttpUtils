package com.example.yangliang.okhttputils.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yangliang.okhttputils.R;
import com.liang.OkHttpLibrary.OkHttpUtils;


public class HotActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean netConnect;
    private WebView mWebView;
    private ProgressDialog progressDialog;
    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        //判断当前网络状态
        netConnect = OkHttpUtils.isNetworkConnected(this);
        if (!netConnect) {
            Toast.makeText(this, "当前网络未连接,请查看网络状态", Toast.LENGTH_SHORT).show();
        }
        //初始化视图
        mWebView = (WebView) findViewById(R.id.web_view);
        mBackBtn = (ImageView) findViewById(R.id.action_back);

        //监听事件
        mBackBtn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage("视频页面加载中...");

        // 加载url
        mWebView.loadUrl("file:///android_asset/demo03.html");
        // 初始化比例
        //mWebView.setInitialScale(50);
        WebSettings settings = mWebView.getSettings();
        // 支持脚本JavaScript
        settings.setJavaScriptEnabled(true);

        //设置可缩放
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        //设置出现缩放工具，其中settings.setBuiltInZoomControls(true)必须要加，不然缩放不起作用
        settings.setBuiltInZoomControls(true);
        //设置不显示WebView缩放按钮，但可缩放
        settings.setDisplayZoomControls(false);

//        //扩大比例的缩放,页面适应手机屏幕的分辨率，完整的显示在屏幕上
//        settings.setUseWideViewPort(true);
//        // setUseWideViewPort方法设置WebView推荐使用的窗口。setLoadWithOverviewMode方法是设置WebView加载的页面的模式。
//        settings.setLoadWithOverviewMode(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

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



    @Override
    public void onClick(View v) {
        //返回上一层
        finish();
    }
}
