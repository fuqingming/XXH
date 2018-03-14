package com.jy.xxh.adapter;


import android.content.Context;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.R;
import com.jy.xxh.bean.base.AttentionBean;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewAdapter;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewHolder;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class AttentionAdapter extends BaseRecyclerViewAdapter<AttentionBean> {

    @BindView(R.id.iv_icon)
    SimpleDraweeView m_ivIcon;
    @BindView(R.id.tv_name)
    TextView m_ivName;
    @BindView(R.id.tv_text)
    TextView m_ivText;

    public AttentionAdapter(Context context, List<AttentionBean> datas) {
        super(context, datas);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_attention_info;
    }

    @Override
    protected void covert(BaseRecyclerViewHolder holder, AttentionBean data, int position) {
        ButterKnife.bind(this, holder.getView());
        LoaderManager.getLoader().loadNet(m_ivIcon,data.getT_photo(),new ILoader.Options(R.mipmap.head_s,R.mipmap.head_s));
        m_ivName.setText(data.getT_name()+"导师");
        m_ivText.setText(data.getT_brief());
    }

}
