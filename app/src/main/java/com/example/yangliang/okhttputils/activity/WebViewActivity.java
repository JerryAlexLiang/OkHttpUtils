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

    public static final String VIDEO_URL = "http://218.23.170.98:8098/bziflytek-web/spzb/mobile/index.do";

    private boolean netConnect;
    private WebView mWebView;
    private ProgressDialog progressDialog;
    private ImageView mBackBtn;
    private TextView mHot;
    private TextView title;

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
        title = (TextView) findViewById(R.id.run_content_tv_name);

        //监听事件
        mBackBtn.setOnClickListener(this);
        mHot.setOnClickListener(this);
        title.setOnClickListener(this);

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
        // 支持脚本JavaScript
        settings.setJavaScriptEnabled(true);

        //设置可缩放
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        //设置出现缩放工具，其中settings.setBuiltInZoomControls(true)必须要加，不然缩放不起作用
        settings.setBuiltInZoomControls(true);
        //设置不显示WebView缩放按钮，但可缩放
        settings.setDisplayZoomControls(true);

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
                //跳转到热度页面
                Intent intent = new Intent(WebViewActivity.this, HotActivity.class);
                startActivity(intent);
                break;

            case R.id.run_content_tv_name:
                Intent intent1 = new Intent(WebViewActivity.this,TestActivity.class);
                startActivity(intent1);
                break;
        }

    }

}
