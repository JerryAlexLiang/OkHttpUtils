package com.example.yangliang.okhttputils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangliang.okhttputils.R;
import com.example.yangliang.okhttputils.bean.SearchInfo;
import com.example.yangliang.okhttputils.constant.Url;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * 创建日期：2017/3/16 on 下午5:38
 * 描述:礼包(搜索)页面listView的适配器
 * 作者:yangliang
 */
public class SearchLvAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchInfo.ListBean> dataList;
    private LayoutInflater mLayoutInflater;

    public SearchLvAdapter(Context mContext, List<SearchInfo.ListBean> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
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
        if (view==null){
            view = mLayoutInflater.inflate(R.layout.search_lv_adapter_item,parent, false);
            viewHolder = new ViewHolder(view);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //数据映射
        SearchInfo.ListBean listBean = dataList.get(position);
        viewHolder.nameTextView.setText(listBean.getGname());
        viewHolder.giftnameTextView.setText(listBean.getGiftname());
        viewHolder.numberTextView.setText(String.valueOf(listBean.getNumber()));
        viewHolder.timeTextView.setText(listBean.getAddtime());

        /*
        下载图片,使用Picasso类库加载网络图片，然后用Transformation方法转化,
        这是一款支持圆角，椭圆，圆形的RoundedImageView类库，可以生成ImageView和Drawable
         */

        String icon_url = Url.URL_IMAGE + listBean.getIconurl();

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadius(15)
                .oval(false)
                .build();

        Picasso.with(mContext).load(icon_url).fit()
                .transform(transformation).into(viewHolder.iconImageView);

        return view;
    }

    class ViewHolder{

        TextView nameTextView;
        TextView giftnameTextView;
        TextView numberTextView;
        TextView timeTextView;
        ImageView iconImageView;

        ViewHolder(View view) {
            view.setTag(this);
            nameTextView = (TextView) view.findViewById(R.id.tv_gname);
            giftnameTextView = (TextView) view.findViewById(R.id.tv_giftname);
            numberTextView = (TextView) view.findViewById(R.id.tv_number);
            timeTextView = (TextView) view.findViewById(R.id.tv_time);
            iconImageView = (ImageView) view.findViewById(R.id.img_icon);
        }
    }
}
