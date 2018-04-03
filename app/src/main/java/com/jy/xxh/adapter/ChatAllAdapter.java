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
import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.ChatActivity;
import com.jy.xxh.R;
import com.jy.xxh.bean.base.ChatMessageBean;
import com.xiao.nicevideoplayer.constants.GlobalVariables;
import com.jy.xxh.util.TimeUtils;
import com.jy.xxh.view.commonRecyclerAdapter.CommonRecyclerAdapter;
import com.jy.xxh.view.commonRecyclerAdapter.ViewHolder;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import org.kymjs.kjframe.KJBitmap;

/**
 * Created by asus on 2018/2/7.
 */

public class ChatAllAdapter extends CommonRecyclerAdapter<ChatMessageBean> {
    private KJBitmap kjb;
    private Context mContext;
    private ChatActivity.OnChatItemClickListener listener;

    public ChatAllAdapter(@NonNull Context context, ChatActivity.OnChatItemClickListener listener) {
        super(context, 0);
        this.mContext = context;
        kjb = new KJBitmap();
        this.listener = listener;
    }

    @Override
    public int getItemLayoutResId(ChatMessageBean data, int position) {
        int layoutResId = -1;
        if(data.getC_messageType().equals(ChatMessageBean.teacher_rep) || data.getC_messageType().equals(ChatMessageBean.teacher_char) || data.getC_messageType().equals(ChatMessageBean.teacher_pic)){
            layoutResId = R.layout.chat_item_list_left;
        }else if(data.getC_messageType().equals(ChatMessageBean.user_char) || data.getC_messageType().equals(ChatMessageBean.user_pic)){
            layoutResId = R.layout.chat_item_list_right;
        }

        return layoutResId;
    }

    @Override
    public void bindData(ViewHolder holder, final ChatMessageBean data, final int position) {
        RelativeLayout rlContent = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_layout_content);
        SimpleDraweeView ivIcon = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_avatar);
        final ImageView ivChatimage = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_content_image);
        ImageView ivSendfail = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_fail);
        ProgressBar mProgress = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_progress);
        TextView tvChatcontent = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_content_text);
        TextView tvDate = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_item_date);
        ImageView ivTeacherType = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.iv_teacher_type);
        TextView tvName = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.tv_name);
        TextView tvChatText = holder.viewBinder().getView().findViewById(org.kymjs.chat.R.id.chat_text);

        //如果是老师发言显示老师图片
        if(data.getC_messageType().equals(ChatMessageBean.teacher_rep) || data.getC_messageType().equals(ChatMessageBean.teacher_char) || data.getC_messageType().equals(ChatMessageBean.teacher_pic)){
            ivTeacherType.setVisibility(View.VISIBLE);
            rlContent.setBackgroundResource(org.kymjs.chat.R.drawable.chat_from_bg_selector);
            if(data.getC_messageType().equals(ChatMessageBean.teacher_rep)){
                tvChatText.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                sb.append(data.getC_replay_name());
                sb.append(":");
                sb.append(data.getC_st_content());
                tvChatText.setText(sb.toString());
            }else{
                tvChatText.setVisibility(View.GONE);
            }
        }else{
            if(!data.getNic_name().equals(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame))){
                ivTeacherType.setVisibility(View.GONE);
                rlContent.setBackgroundResource(org.kymjs.chat.R.drawable.chat_from_other_selector);
            }else{
                rlContent.setBackgroundResource(org.kymjs.chat.R.drawable.chat_to_bg_selector);
            }
        }

        //如果是文本类型，则隐藏图片，如果是图片则隐藏文本
        if (data.getC_messageType().equals(ChatMessageBean.user_char) || data.getC_messageType().equals(ChatMessageBean.teacher_char) || data.getC_messageType().equals(ChatMessageBean.teacher_rep)) {
            ivChatimage.setVisibility(View.GONE);
            tvChatcontent.setVisibility(View.VISIBLE);
//            if(data.getC_messageType().equals(user_char) || data.getC_messageType().equals(teacher_char) || data.getC_messageType().equals(teacher_rep))
//            if (data.getC_st_content().contains("href")) {
//                tvChatcontent = UrlUtils.handleHtmlText(tvChatcontent, data.getC_st_content());
//            } else {
//                tvChatcontent = UrlUtils.handleText(tvChatcontent, data.getC_st_content());
//            }
            String charText = "";
            if(data.getC_messageType().equals(ChatMessageBean.teacher_rep)){
                charText = data.getC_t_content();
            }else if(data.getC_messageType().equals(ChatMessageBean.teacher_char)){
                charText = data.getC_t_content();
            }else{
                charText = data.getC_st_content();
            }
//            tvChatcontent.setText(Utils.delHTMLTag(charText));
            tvChatcontent.setText(charText);
        } else {
            tvChatcontent.setVisibility(View.GONE);
            ivChatimage.setVisibility(View.VISIBLE);

            String picPath = "";
            if(data.getC_messageType().equals(ChatMessageBean.user_pic)){
                picPath = data.getC_st_content();
            }else{
                picPath = data.getC_t_content();
            }

            //如果内存缓存中有要显示的图片，且要显示的图片不是holder复用的图片，则什么也不做，否则显示一张加载中的图片
            if (kjb.getMemoryCache(picPath) != null && picPath != null &&  picPath.equals(ivChatimage.getTag())) {
            } else {
                ivChatimage.setImageResource(org.kymjs.chat.R.drawable.loading_image);
            }

            kjb.display(ivChatimage, picPath, 300, 300);
        }

        //如果是表情或图片，则不显示气泡，如果是图片则显示气泡
//        if (data.getType() != Message.MSG_TYPE_TEXT) {
//            holder.rlContent.setBackgroundResource(android.R.color.transparent);
//        } else {
//            if (Message.MSG_MYSELF.equals(data.getMumberType())) {
//                holder.rlContent.setBackgroundResource(R.drawable.chat_to_bg_selector);
//            } else if(Message.MSG_TEACHER.equals(data.getMumberType())){
//                holder.rlContent.setBackgroundResource(R.drawable.chat_from_bg_selector);
//            }else if(Message.MSG_OTHER_MEMBER.equals(data.getMumberType())){
//                holder.rlContent.setBackgroundResource(R.drawable.chat_from_other_selector);
//            }
//        }

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
//                    switch (data.getType()) {
//                        case Message.MSG_TYPE_PHOTO:
//                            listener.onPhotoClick(position);
//                            break;
//                        case Message.MSG_TYPE_FACE:
//                            listener.onFaceClick(position);
//                            break;
//                    }
                }
            });
        }

        tvDate.setText(TimeUtils.time2String(data.getC_time_linux()*1000, "MM-dd HH:mm"));
        LoaderManager.getLoader().loadNet(ivIcon, data.getUser_photo(), new ILoader.Options(org.kymjs.chat.R.drawable.default_head, org.kymjs.chat.R.drawable.default_head));

        tvName.setText(data.getNic_name());

        mProgress.setVisibility(View.GONE);
        ivSendfail.setVisibility(View.GONE);
    }

    private OnSelectFragmentClickListener m_ListenerSelectFragment = null;

    public interface OnSelectFragmentClickListener
    {
        void OnSelectFragmentClick();
    }

    public void onSelectFragmentClickListener(OnSelectFragmentClickListener listener)
    {
        m_ListenerSelectFragment = listener;
    }
}
