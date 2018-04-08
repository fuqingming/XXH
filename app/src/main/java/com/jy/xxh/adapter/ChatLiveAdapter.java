/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jy.xxh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.ChatLiveActivity;
import com.jy.xxh.R;
import com.jy.xxh.bean.base.ChatMessageBean;
import com.jy.xxh.util.TimeUtils;
import com.jy.xxh.view.commonRecyclerAdapter.CommonRecyclerAdapter;
import com.jy.xxh.view.commonRecyclerAdapter.ViewHolder;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;
import com.xiao.nicevideoplayer.constants.GlobalVariables;

import org.kymjs.kjframe.KJBitmap;

public class ChatLiveAdapter extends CommonRecyclerAdapter<ChatMessageBean> {
    private ChatLiveActivity.OnChatItemClickListener listener;
    private Context mContext;

    public ChatLiveAdapter(@NonNull Context context, ChatLiveActivity.OnChatItemClickListener listener) {
        super(context, 0);
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public int getItemLayoutResId(ChatMessageBean data, int position) {
        int layoutResId = -1;
        if(data.getNic_name().equals(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame))){
            layoutResId = R.layout.chat_item_list_right;
        }else{
            layoutResId = R.layout.chat_item_list_left;
        }

        return layoutResId;
    }

    @Override
    public void bindData(ViewHolder holder, final ChatMessageBean data, final int position) {
        RelativeLayout rlContent = holder.viewBinder().getView().findViewById(R.id.chat_item_layout_content);
        ImageView ivIcon = holder.viewBinder().getView().findViewById(R.id.chat_item_avatar);
        final ImageView ivChatimage = holder.viewBinder().getView().findViewById(R.id.chat_item_content_image);
        ImageView ivSendfail = holder.viewBinder().getView().findViewById(R.id.chat_item_fail);
        ProgressBar mProgress = holder.viewBinder().getView().findViewById(R.id.chat_item_progress);
        TextView tvChatcontent = holder.viewBinder().getView().findViewById(R.id.chat_item_content_text);
        TextView tvDate = holder.viewBinder().getView().findViewById(R.id.chat_item_date);
        ImageView ivTeacherType = holder.viewBinder().getView().findViewById(R.id.iv_teacher_type);
        TextView tvName = holder.viewBinder().getView().findViewById(R.id.tv_name);
        TextView tvChatText = holder.viewBinder().getView().findViewById(R.id.chat_text);

        ivTeacherType.setVisibility(View.GONE);
        if(!data.getNic_name().equals(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame))){
            rlContent.setBackgroundResource(R.drawable.chat_left_bg_selector);
            tvChatText.setVisibility(View.GONE);
        }else{
            rlContent.setBackgroundResource(R.drawable.chat_to_bg_selector);
        }

        ivChatimage.setVisibility(View.GONE);
        tvChatcontent.setVisibility(View.VISIBLE);
        tvChatcontent.setText(data.getC_st_content());

        if (listener != null) {
            tvChatcontent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTextClick(position);
                }
            });
            ivChatimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.getC_messageType().equals(ChatMessageBean.user_pic) || data.getC_messageType().equals(ChatMessageBean.teacher_pic)){
                        String path = "";
                        if(data.getC_messageType().equals(ChatMessageBean.user_pic)){
                            path = data.getC_st_content();
                        }else if(data.getC_messageType().equals(ChatMessageBean.teacher_pic)){
                            path = data.getC_t_content();
                        }
                        listener.onPhotoClick(position,path,ivChatimage);
                    }
                }
            });
        }

        tvDate.setText(TimeUtils.time2String(data.getC_time_linux()*1000, "MM-dd HH:mm"));
        Glide.with(mContext).load(data.getUser_photo()).placeholder(R.drawable.default_head).into(ivIcon);
        tvName.setText(data.getNic_name());

        mProgress.setVisibility(View.GONE);
        ivSendfail.setVisibility(View.GONE);
    }
}
