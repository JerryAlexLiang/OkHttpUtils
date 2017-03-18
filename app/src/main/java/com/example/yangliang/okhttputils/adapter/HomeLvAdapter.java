package com.example.yangliang.okhttputils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.bean.DataInfo;
import com.example.yangliang.okhttputils.constant.Url;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * 创建日期：2017/3/15 on 上午11:37
 * 描述:主页面ListView适配器
 * 作者:yangliang
 */
public class HomeLvAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataInfo.ListBean> dataList;
    private LayoutInflater mInflater;

    public HomeLvAdapter(Context mContext, List<DataInfo.ListBean> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.home_lv_adapter_item, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //数据映射
        DataInfo.ListBean listBean = dataList.get(position);
        viewHolder.nameTextView.setText(listBean.getName());
        viewHolder.authorTextView.setText(listBean.getAuthor());
        viewHolder.timeTextView.setText(listBean.getAddtime());

        String icon_url = Url.URL_IMAGE + listBean.getIconurl();
        /*
        下载图片,使用Picasso类库加载网络图片，然后用Transformation方法转化,
        这是一款支持圆角，椭圆，圆形的RoundedImageView类库，可以生成ImageView和Drawable
         */
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadius(15)
                .oval(false)
                .build();
        Picasso.with(mContext).load(icon_url).fit()
                .transform(transformation).into(viewHolder.iconImageView);

        return view;
    }

    class ViewHolder {

        TextView nameTextView;
        TextView authorTextView;
        TextView timeTextView;
        ImageView iconImageView;

        ViewHolder(View view) {
            view.setTag(this);
            nameTextView = (TextView) view.findViewById(R.id.tv_name);
            authorTextView = (TextView) view.findViewById(R.id.tv_author);
            timeTextView = (TextView) view.findViewById(R.id.tv_time);
            iconImageView = (ImageView) view.findViewById(R.id.img_icon);
        }

    }
}
