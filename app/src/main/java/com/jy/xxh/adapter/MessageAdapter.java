package com.jy.xxh.adapter;


import android.content.Context;
import android.widget.TextView;

import com.jy.xxh.R;
import com.jy.xxh.bean.base.MessageBean;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewAdapter;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class MessageAdapter extends BaseRecyclerViewAdapter<MessageBean> {

    @BindView(R.id.tv_title)
    TextView m_ivTitle;
    @BindView(R.id.tv_time)
    TextView m_ivTime;
    @BindView(R.id.tv_text)
    TextView m_ivText;

    public MessageAdapter(Context context, List<MessageBean> datas) {
        super(context, datas);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_message_info;
    }

    @Override
    protected void covert(BaseRecyclerViewHolder holder, MessageBean data, int position) {
        ButterKnife.bind(this, holder.getView());
        m_ivTitle.setText(data.getN_title());
        m_ivTime.setText(data.getN_time());
        m_ivText.setText(data.getN_content());
    }

}
