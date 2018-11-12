package com.androidxx.yangjw.okhttpdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import cn.krvision.navigation.utils.CustomProgressDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注意：OkHttpClient在一个APP中建议只有一个。（OkHttpClient对象是单例）
 * OkHttp的Post请求方式
 * 1、同步 execute
 * 2、异步 enqueue
 * 使用步骤：
 * 1、创建OkHttpClient对象
 * 2、创建一个Request对象
 * 3、执行Request，并且获得Responce对象
 */
public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URL = "http://www.1688wan.com/majax.action?method=searchGift";
    private Button mRequestBodyJsonBtn;
    private TextView mShowResultTxt;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Button btnClearText;
    private Button mFormBodyKeyValueBtn;
    private Button mFormBodyHashmapBtn;
    private Button mRequestBodyUploadFileBtn;
    private CustomProgressDialog progressDialog;
    private Button mRequestBodyUploadImageBtn;
    private Button mMultipartBodyUploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
    }

    /**
     * post和get的不同在于对Request请求的构造不同(因为post需要携带参数)，
     * post方式中的Request需要传递一个RequestBody作为post的参数。
     * RequestBody有两个子类：FormBody和MultipartBody
     */
    private void initView() {

        progressDialog = new CustomProgressDialog(this, "Loading~", false);

        mRequestBodyJsonBtn = (Button) findViewById(R.id.okhttp_requestBody_json_btn);
        mFormBodyKeyValueBtn = (Button) findViewById(R.id.okhttp_fromBody_key_value_btn);
        mFormBodyHashmapBtn = (Button) findViewById(R.id.okhttp_formBody_hashmap_btn);
        mRequestBodyUploadFileBtn = (Button) findViewById(R.id.okhttp_requestBody_uploadFile_btn);
        mRequestBodyUploadImageBtn = (Button) findViewById(R.id.okhttp_requestBody_uploadImage_btn);
        mMultipartBodyUploadBtn = (Button) findViewById(R.id.okhttp_multipartBody_upload_btn);

        btnClearText = (Button) findViewById(R.id.btn_clear_text);
        mShowResultTxt = (TextView) findViewById(R.id.okhttp_post_show_result_tv);

        mRequestBodyJsonBtn.setOnClickListener(this);
        mFormBodyKeyValueBtn.setOnClickListener(this);
        mFormBodyHashmapBtn.setOnClickListener(this);
        mRequestBodyUploadFileBtn.setOnClickListener(this);
        mRequestBodyUploadImageBtn.setOnClickListener(this);
        mMultipartBodyUploadBtn.setOnClickListener(this);
        btnClearText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okhttp_requestBody_json_btn:
                //POST请求-RequestBody--json数据提交
                String url = "http://www.1688wan.com/majax.action?method=searchGift";
                String json = "{key,热血}";
                progressDialog.show();
                postRequestBodyJsonRequest(url, json);
                break;


            case R.id.okhttp_fromBody_key_value_btn:
                //POST请求-FromBody--键值对数据提交
                progressDialog.show();
                postFormBodyKeyValueRequest();
                break;

            case R.id.okhttp_formBody_hashmap_btn:
                //POST请求-HashMap--数据提交
                progressDialog.show();
                postFormBodyHashMapRequest();
                break;

            case R.id.okhttp_requestBody_uploadFile_btn:
                //异步上传文件
                progressDialog.show();
                postAsyncFile();
                break;

            case R.id.okhttp_requestBody_uploadImage_btn:
                //POST-异步上传图片到服务器
                progressDialog.show();
                postAsyncImage();
                break;

            case R.id.okhttp_multipartBody_upload_btn:
                //POST请求-异步上传Multipart复杂请求体文件
                progressDialog.show();
                postAsyncMultipartBody();
                break;

            case R.id.btn_clear_text:
                mShowResultTxt.setText("");
                break;

            default:
                break;
        }
    }

    /**
     * POST请求-异步上传Multipart复杂请求体文件
     * 这种场景很常用，有时会上传文件同时还需要传其他类型的字段，
     * OkHttp3实现起来很简单，需要注意的是没有服务器接收我这个Multipart文件，
     * 所以这里只是举个例子，具体的应用还要结合实际工作中对应的服务器。
     */
    private void postAsyncMultipartBody() {
        //首先定义上传文件类型：
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        //图片RequestBody
        File file = new File(Environment.getExternalStorageDirectory(), "image2018.jpg");
        RequestBody body = RequestBody.create(MEDIA_TYPE_PNG, file);
        //创建MultipartBody
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "admin")
                .addFormDataPart("image", "image2018.jpg", body)
                .build();
        //创建Request
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(multipartBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
                LogUtils.e("POST-异步上传多文件成功~", result);
            }
        });

    }

    /**
     * POST-异步上传图片到服务器
     */
    private void postAsyncImage() {
        //首先定义上传文件的类型
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        File file = new File(Environment.getExternalStorageDirectory(), "image2018.jpg");
        //创建RequestBody
        RequestBody body = RequestBody.create(MEDIA_TYPE_PNG, file);
        //创建Request（备注：注意的是没有服务器接收这个图片文件，这里只是举个例子，具体的应用还要结合实际工作中对应的服务器。）
        final Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
                LogUtils.e("POST-异步上传文件成功~", result);
            }
        });

    }

    /**
     * POST-异步上传文件-方法同JSON上传
     */
    private void postAsyncFile() {
        //首先定义上传文件类型：
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=utf-8");
        //将sdcard根目录的wangshu.txt文件上传到服务器上：
        File file = new File(Environment.getExternalStorageDirectory(), "当虹人像.txt");
        //创建RequestBody
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
        //创建Request
        final Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
                LogUtils.e("POST-异步上传文件成功~", result);
            }
        });

    }

    /**
     * POST请求-HashMap--数据提交
     */
    private void postFormBodyHashMapRequest() {
        //把参数传进Map中
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", "热血");
        FormBody.Builder builder = new FormBody.Builder();
        //追加表单信息
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        //创建RequestBody
        FormBody formBody = builder.build();
        //创建Request对象
        Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                LogUtils.e("POST请求-HashMap--数据提交", result);
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mShowResultTxt.setText(result);
                    }
                });
            }
        });

    }

    /**
     * POST请求-FromBody--键值对数据提交
     */
    private void postFormBodyKeyValueRequest() {
        //post : RequestBody
        //封装Post请求的参数
        FormBody formBody = new FormBody.Builder()
                .add("key", "热血")//添加Post请求的参数
                .build();

        //创建一个Request对象
        Request request = new Request.Builder()
                .url(URL) //网络请求地址
                .post(formBody) //Post请求，并且将Post请求需要的参数封装到FormBody中
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                LogUtils.e("POST请求-FromBody--键值对数据提交", result);

                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mShowResultTxt.setText(result);
                    }
                });
            }
        });
    }

    private void postRequestBodyJsonRequest(String url, String json) {
        MediaType mediaTypeJson = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(mediaTypeJson, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("POST请求失败", e.getMessage());
                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                LogUtils.e("post-json", result);

                if (progressDialog.isShow()) {
                    progressDialog.dismiss();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mShowResultTxt.setText(result);
                    }
                });
            }
        });

    }
}
