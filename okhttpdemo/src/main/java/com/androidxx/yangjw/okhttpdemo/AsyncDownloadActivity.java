package com.androidxx.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 创建日期：2018/11/9 on 下午2:19
 * 描述: 异步下载
 * 作者: liangyang
 */
public class AsyncDownloadActivity extends AppCompatActivity {

    private Button buttonDownload;
    private ImageView imageView;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_download);
        initView();
        downAsyncFile();
    }

    private void downAsyncFile() {
        /*
     String url = "";
     Request request = new Request.Builder().url(url).build();
     mOkHttpClient.newCall(request).enqueue(new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
         }
         @Override
         public void onResponse(Call call, Response response) {
             InputStream inputStream = response.body().byteStream();
             FileOutputStream fileOutputStream = null;
             try {
                 fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                 byte[] buffer = new byte[2048];
                 int len = 0;
                 while ((len = inputStream.read(buffer)) != -1) {
                     fileOutputStream.write(buffer, 0, len);
                 }
                 fileOutputStream.flush();
             } catch (IOException e) {
                 Log.i("wangshu", "IOException");
                 e.printStackTrace();
            }
            Log.d("wangshu", "文件下载成功");
        }
    });
         */
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder()
                .url(url)
                .build();
//        okHttpClient.newCall(request).e
    }

    private void initView() {
        buttonDownload = (Button) findViewById(R.id.btn_async_download_image);
        imageView = (ImageView) findViewById(R.id.iv_image_view);
    }
}
