package com.example.yangliang.okhttputils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.adapter.SearchLvAdapter;
import com.example.yangliang.okhttputils.bean.SearchInfo;
import com.example.yangliang.okhttputils.constant.Url;
import com.google.gson.Gson;
import com.liang.OkHttpLibrary.IOKCallBack;
import com.liang.OkHttpLibrary.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //初始化数据源
    private List<SearchInfo.ListBean> dataList = new ArrayList<>();

    private boolean netConnect;
    private ListView mSearchListView;
    private String key = "";
    private ImageView mSearchTo;
    private ImageView mGoBackBtn;
    private EditText mSearchKey;
    private SearchLvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //判断当前网络状态
        netConnect = OkHttpUtils.isNetworkConnected(this);
        if (!netConnect) {
            Toast.makeText(this, "当前网络未连接,请查看网络状态", Toast.LENGTH_SHORT).show();
        }

        //初始化数据源
        mSearchListView = (ListView) findViewById(R.id.list_view_search);
        mSearchKey = (EditText) findViewById(R.id.et_search_key);
        mGoBackBtn = (ImageView) findViewById(R.id.action_back);
        mSearchTo = (ImageView) findViewById(R.id.btn_search);

        //初始化数据源
        initData();
        //初始化适配器
        adapter = new SearchLvAdapter(SearchActivity.this,dataList);
        //绑定适配器
        mSearchListView.setAdapter(adapter);
        //初始化监听事件
        mGoBackBtn.setOnClickListener(this);
        mSearchTo.setOnClickListener(this);
        mSearchListView.setOnItemClickListener(this);

    }

    /**
     * 初始化数据源
     */
    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        OkHttpUtils.newInstance()
                .start(Url.URL_SEARCH)
                .post(map)
                .callback(new IOKCallBack() {
                    @Override
                    public void success(String result) {

                        if (result.equals("{\"list\":}")) {
                            Toast.makeText(SearchActivity.this, "暂无相关礼包", Toast.LENGTH_SHORT).show();
                        } else {
                            //解析JSON数据
                            Gson gson = new Gson();
                            SearchInfo searchInfo = gson.fromJson(result, SearchInfo.class);

                            if (netConnect) {
                                dataList.addAll(searchInfo.getList());
                                //刷新适配器
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }
                });

    }

    /**
     * 点击加监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.action_back:
                //返回上一层
                onBackPressed();
                break;

            case R.id.btn_search:

                key = mSearchKey.getText().toString();

                if (!TextUtils.isEmpty(key)) {

                    //清空当前显示的ListView
                    dataList.clear();
                    //重新加载数据
                    initData();

                } else {
                    Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    /**
     * listView的点击监听事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(SearchActivity.this, ContentActivity.class);
        intent.putExtra("name", dataList.get(position).getGname());
        intent.putExtra("description", dataList.get(position).getGiftname());
        intent.putExtra("icon", dataList.get(position).getIconurl());
        startActivity(intent);

    }
}
