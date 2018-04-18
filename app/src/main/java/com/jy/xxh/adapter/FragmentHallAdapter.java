package com.jy.xxh.adapter;


import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.R;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.cache.AsyncImageLoader;
import com.jy.xxh.cache.ImageCacheManager;
import com.jy.xxh.util.ImageLoader;
import com.jy.xxh.view.recyclerview.BaseRecyclerViewHolder;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class FragmentHallAdapter extends BaseRecyclerAdapter<RoomBean> {

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
    @BindView(R.id.ll_width)
    LinearLayout llWith;

    private AsyncImageLoader imageLoader;

    public FragmentHallAdapter() {
        ImageCacheManager cacheMgr = new ImageCacheManager(mContext);
        imageLoader = new AsyncImageLoader(mContext, cacheMgr.getMemoryCache(), cacheMgr.getPlacardFileCache());
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_live_info;
    }

    @Override
    protected void covert(BaseRecyclerViewHolder holder, final RoomBean data, final int position) {
        ButterKnife.bind(this, holder.getView());
//        Glide.with(mContext).load(data.getR_icon()).into(m_ivPic);
//        Glide.with(mContext).load(data.getR_icon()).placeholder(R.mipmap.station_pic).into(m_ivPic);
        m_ivPic.setTag(data.getR_icon());
        AsyncImageLoader.getInstace(mContext).loadBitmap(m_ivPic, data.getR_icon(), R.mipmap.station_pic);
//        ImageLoader.getInstace().loadRoundedCornersImg(getMContext(), m_ivIcon, url,15,getResources().getDrawable(R.drawable.head_s));
//        Bitmap bmp = imageLoader.loadBitmap(m_ivPic, data.getR_icon(), true);
//        if(bmp == null) {
//            m_ivPic.setImageResource(R.mipmap.station_pic);
//        } else {
//            m_ivPic.setImageBitmap(bmp);
//        }
//        ImageLoader.getInstace().loadImg(mContext, m_ivPic, data.getR_icon());
//        m_ivPic.post(new Runnable() {
//            @Override
//            public void run() {
//                int height = llWith.getMeasuredWidth();
//
//                ViewGroup.LayoutParams tvShowAllPara = m_ivPic.getLayoutParams();
//                tvShowAllPara.width = height;
//                m_ivPic.setLayoutParams(tvShowAllPara);
//            }
//        });
//        Glide.with(mContext).load(data.getR_icon()).into(m_ivPic);
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
