package com.example.yangliang.okhttputils.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.adapter.HomeLvAdapter;
import com.example.yangliang.okhttputils.bean.DataInfo;
import com.example.yangliang.okhttputils.constant.Url;
import com.google.gson.Gson;
import com.liang.OkHttpLibrary.IOKCallBack;
import com.liang.OkHttpLibrary.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求框架OKHttp复习
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //数据源
    private List<DataInfo.ListBean> dataList = new ArrayList<>();
    private boolean netConnect;
    private ProgressDialog progressDialog;
    private ListView mListView;
    private HomeLvAdapter adapter;
    private TextView mEmptyContent;
    private ImageView mRefreshBtn;
    private DataInfo dataInfo;
    private ImageView mSearchBtn;
    private TextView mHomeHallBtn;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断当前网络状态
        netConnect = OkHttpUtils.isNetworkConnected(this);
        if (!netConnect) {
            Toast.makeText(this, "当前网络未连接,请查看网络状态", Toast.LENGTH_SHORT).show();
        }

        //初始化dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.def_head);
        builder.setTitle("提示");
        builder.setMessage("您关注的主播正在直播！");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(intent);

            }
        });
        builder.create().show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载中...");

        //初始化视图
        mTvTitle = (TextView) findViewById(R.id.run_content_tv_name);
        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyContent = (TextView) findViewById(R.id.empty_tv);
        mRefreshBtn = (ImageView) findViewById(R.id.refresh_btn);
//        mSearchBtn = (ImageView) findViewById(R.id.btn_search);
        mHomeHallBtn = (TextView) findViewById(R.id.tv_home_hall);
        //初始化数据源
        initData();
        //初始化适配器
        adapter = new HomeLvAdapter(this, dataList);
        //绑定适配器
        mListView.setAdapter(adapter);
        //初始化监听事件
        mRefreshBtn.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
//        mSearchBtn.setOnClickListener(this);
        mHomeHallBtn.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);


    }


    /**
     * 初始化数据源
     */
    private void initData() {

        progressDialog.show();

        downloadData();


    }

    /**
     * 下载数据
     */
    private void downloadData() {
        OkHttpUtils.newInstance()
                .start(Url.BASE_URL)
                .callback(new IOKCallBack() {
                    @Override
                    public void success(String result) {

                        progressDialog.dismiss();

                        //解析JSON数据
                        Gson gson = new Gson();
                        DataInfo dataInfo = gson.fromJson(result, DataInfo.class);
                        if (netConnect && result != null) {
                            dataList.addAll(dataInfo.getList());
                            //刷新适配器
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.refresh_btn:
                downloadData();
                break;

//            case R.id.btn_search:
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent);
//                break;

            case R.id.tv_home_hall:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.run_content_tv_name:
                Intent webIntent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(webIntent);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
        intent.putExtra("name", dataList.get(position).getName());
        intent.putExtra("description", dataList.get(position).getDescs());
        intent.putExtra("icon", dataList.get(position).getIconurl());
        startActivity(intent);
    }


    private long waitTime = 2000;
    private long touchTime = 0;

    /**
     * 连按两次退出应用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {

            long currentTime = System.currentTimeMillis();

            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (KeyEvent.KEYCODE_HOME == keyCode) {
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
