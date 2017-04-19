package com.example.yangliang.okhttputils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.constant.Url;
import com.squareup.picasso.Picasso;

/**
 * 详情页面
 */
public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    private String name;
    private String icon_url;
    private TextView mContentName;
    private TextView mDescription;
    private ImageView mContentIconUrl;
    private String description;
    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //获取传值
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        icon_url = intent.getStringExtra("icon");

        //初始化视图
        mContentName = (TextView) findViewById(R.id.tv_content_name);
        mDescription = (TextView) findViewById(R.id.tv_description);
        mContentIconUrl = (ImageView) findViewById(R.id.img_content_icon);
        mBackBtn = (ImageView) findViewById(R.id.action_back);

        //初始化监听事件
        mBackBtn.setOnClickListener(this);

        //加载值
        mContentName.setText(name);
        mDescription.setText(description);

        //使用Picasso下载图片
        Picasso.with(ContentActivity.this).load(Url.URL_IMAGE + icon_url).into(mContentIconUrl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_back:
                //返回上一层
                finish();
                break;

        }
    }
}
