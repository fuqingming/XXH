package com.jy.xxh.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jy.xxh.R;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class FragmentHallAdapter extends BaseRecyclerViewAdaptera<RoomBean> {

    @BindView(R.id.iv_pic)
    ImageView m_ivPic;
    @BindView(R.id.tv_title)
    TextView m_ivTitle;
    @BindView(R.id.tv_name)
    TextView m_ivName;
    @BindView(R.id.tv_count)
    TextView m_ivPersionCount;
    @BindView(R.id.iv_lock)
    ImageView m_ivLock;
    @BindView(R.id.ll_item_click)
    LinearLayout m_llItemClick;
    @BindView(R.id.tv_online)
    TextView m_tvOnline;

    public FragmentHallAdapter() {
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_live_info;
    }

    @Override
    protected void covert(BaseRecyclerViewHolder holder, final RoomBean data, final int position) {
        ButterKnife.bind(this, holder.getView());
        Glide.with(mContext).load(data.getR_icon()).placeholder(R.mipmap.station_pic).into(m_ivPic);
        m_ivTitle.setText(data.getR_room_breif());
        m_ivName.setText(data.getT_nic_name());
        m_ivPersionCount.setText(data.getR_t_online()+"人参与");
        if(data.getR_is_screte() == RoomBean.isLocked){
            m_ivLock.setVisibility(View.VISIBLE);
        }else{
            m_ivLock.setVisibility(View.GONE);
        }

        m_llItemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ListenerSelectFragment.OnSelectClick(position);
            }
        });

        if(data.getR_t_online() == RoomBean.isOnline){
            m_tvOnline.setText("在线");
        }else{
            m_tvOnline.setText("离线");
        }
    }



}
