package com.example.yangliang.okhttputils.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.bean.StartFloatAllBean;
import com.example.yangliang.okhttputils.bean.StartFloatBean;
import com.example.yangliang.okhttputils.constant.Url;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.liang.OkHttpLibrary.IOKCallBack;
import com.liang.OkHttpLibrary.OkHttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private boolean netConnect;
    private ImageView mInageView;
    private TextView mContentTv;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //判断当前网络状态
        netConnect = OkHttpUtils.isNetworkConnected(this);
        if (!netConnect) {
            Toast.makeText(this, "当前网络未连接,请查看网络状态", Toast.LENGTH_SHORT).show();
        }
        //初始化视图
        mInageView = (ImageView) findViewById(R.id.img_icon);
        title = (TextView) findViewById(R.id.title_tv);
        mContentTv = (TextView) findViewById(R.id.content_tv);
        //下载数据
        downloadData();
    }

    private void downloadData() {
        OkHttpUtils.newInstance()
                .start(Url.BASE_URL)
                .callback(new IOKCallBack() {

                    @Override
                    public void success(String result) {

                        String startFloatData = "{\"type\":1,\"data\":{\"userId\":\"20170106\",\"account\":\"技术中心\",\"type\":\"2\",\"unitNo\":\"科大讯飞股份有限公司\"}}";

                        //测试类型一：不写Javabean类解析数据
//                        Gson gson = new Gson();
//                        JsonObject jsonObject = gson.fromJson(startFloatData, JsonObject.class);
//                        String type = jsonObject.get("type").toString();
//
//                        JsonObject data = jsonObject.get("data").getAsJsonObject();
//
//                        //way1：不写Javabean类解析数据
//                        String userId = data.get("userId").toString();
//                        String account = data.get("account").toString();
//                        String type1 = data.get("type").toString();
//                        String unitNo = data.get("unitNo").toString();
//
////                        //way2：通过Javabean类解析数据
////                        StartFloatBean startFloatBean = gson.fromJson(data, StartFloatBean.class);
////                        String userId = startFloatBean.getUserId();
////                        String account = startFloatBean.getAccount();
////                        String type1 = startFloatBean.getType();
////                        String unitNo = startFloatBean.getUnitNo();
//
//                        System.out.println("====" + "type=" + type + " " +
//                                "id=" + userId + " " + "account=" + account + " "
//                                + "type1=" + type1 + " " + "unitNo=" + unitNo);


                        //测试类型二：通过Javabean类解析数据,GsonFormat方法
                        Gson gson = new Gson();
                        StartFloatAllBean startFloatAllBean = gson.fromJson(startFloatData, StartFloatAllBean.class);
                        int type = startFloatAllBean.getType();
                        StartFloatAllBean.DataBean data = startFloatAllBean.getData();
                        String userId = data.getUserId();
                        String account = data.getAccount();
                        String type1 = data.getType();
                        String unitNo = data.getUnitNo();

                        System.out.println("====" + "type=" + type + " " +
                                "id=" + userId + " " + "account=" + account + " "
                                + "type1=" + type1 + " " + "unitNo=" + unitNo);

                    }
                });

    }
}
