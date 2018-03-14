package com.jy.xxh.adapter;


import android.content.Context;
import android.widget.TextView;

import com.jy.xxh.R;
import com.jy.xxh.bean.base.TeacherDetailsBean;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewAdapter;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class TeacherDetailsAdapter extends BaseRecyclerViewAdapter<TeacherDetailsBean> {

    @BindView(R.id.tv_title)
    TextView m_tvTitle;
    @BindView(R.id.tv_text)
    TextView m_ivText;

    public TeacherDetailsAdapter(Context context, List<TeacherDetailsBean> datas) {
        super(context, datas);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_teacher_details_info;
    }

    @Override
    protected void covert(BaseRecyclerViewHolder holder, TeacherDetailsBean data, int position) {
        ButterKnife.bind(this, holder.getView());

        m_tvTitle.setText(data.getTitle());
        m_ivText.setText(data.getText());
    }

}
