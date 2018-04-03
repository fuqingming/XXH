package com.jy.xxh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.hyphenate.util.EMLog;
import com.jy.xxh.adapter.ChatAllAdapter;
import com.jy.xxh.adapter.ChatTeacherAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.bean.base.ChatMessageBean;
import com.jy.xxh.bean.response.ResponseBaseBean;
import com.jy.xxh.bean.response.ResponseChatBean;
import com.jy.xxh.bean.response.ResponseChatMessageBean;
import com.jy.xxh.bean.response.ResponseFollowBean;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.xiao.nicevideoplayer.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.Constant;
import com.jy.xxh.util.ChatRoomListener;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.view.BigImage.FengNiaoImageSource;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.kymjs.chat.bean.Message;
import org.kymjs.chat.utils.Utils;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

import static com.hyphenate.util.EasyUtils.TAG;

public class ChatActivity extends KJActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    public static final int UNCONCERNED = 0;                //未关注
    public static final int ALREADY_PAID_ATTENTION_TO = 1;  //已关注

    public static final int MESSAGE_TYPE_TEXT = 1;          //文字消息
    public static final int MESSAGE_TYPE_PIC = 2;           //图片消息

    private NiceVideoPlayer mNiceVideoPlayer;

    KProgressHUD kProgressHUD;
    private KJBitmap kjb;

    protected EaseChatPrimaryMenuBase.EaseChatPrimaryMenuListener listener;
    protected ChatRoomListener chatRoomListener;

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerViewAll;
    private RecyclerView mRecyclerViewAll;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerViewTeacher;
    private RecyclerView mRecyclerViewTeacher;

    private EditText m_etEdit;
    private LinearLayout m_llPic;
    private ImageView m_ivPicSend;
    private TextView m_tvSend;
    private ImageView m_ivOnlySee;
    private ImageView m_ivPicChat;
    private RelativeLayout m_rlPicChat;

    private boolean m_isFollow;

    private ImageView m_ivAdd;
    private TextView m_tvFollow;
    private RelativeLayout m_rlBtnFollow;

    private List<ChatMessageBean> m_msgTeacherDatas = new ArrayList<>();
    private List<ChatMessageBean> m_msgDataAll = new ArrayList<>();
    private ChatAllAdapter adapterAll;
    private ChatTeacherAdapter adapterTeacher;

    private String m_strRoomeId;
    private String m_strTeacherId;
    private boolean m_bOnlySee = false;         //false:查看全部    true:查看老师

    private int page;
    private int teacherPage;

    @Override
    public void setRootView() {
        setContentView(org.kymjs.chat.R.layout.activity_chat);
    }

    @Override
    public void initWidget() {
        super.initWidget();

        page = 0;
        teacherPage = 0;
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
        String m_strTeacherPhoto = getIntent().getStringExtra("strTeacherPhoto");
        String m_strTeacherName = getIntent().getStringExtra("strTeacherName");
        String m_strTeacherBreif = getIntent().getStringExtra("strTeacherBreif");
        m_strTeacherId = getIntent().getStringExtra("strTeacherId");
        m_strRoomeId = getIntent().getStringExtra("strRoomId");

        chatRoomListener = new ChatRoomListener() {
            @Override
            protected String setRoomID() {
                return m_strRoomeId;
            }

            @Override
            protected Context setContext() {
                return ChatActivity.this;
            }
        };
        kjb = new KJBitmap();

        m_etEdit = findViewById(org.kymjs.chat.R.id.et_edit);
        m_llPic = findViewById(org.kymjs.chat.R.id.ll_pic);
        m_ivPicSend = findViewById(org.kymjs.chat.R.id.iv_pic_send);
        m_ivPicChat = findViewById(org.kymjs.chat.R.id.iv_pic_chat);
        m_rlPicChat = findViewById(R.id.rl_pic_chat);
        m_tvSend = findViewById(org.kymjs.chat.R.id.tv_send);
        m_ivOnlySee = findViewById(org.kymjs.chat.R.id.iv_only_see);
        m_etEdit.setMaxHeight(Utils.dp2px(this,100));

        m_llPic.setVisibility(View.GONE);
        m_ivPicSend.setVisibility(View.GONE);
        m_tvSend.setVisibility(View.VISIBLE);

        SimpleDraweeView m_ivTeacherPhoto = findViewById(R.id.iv_icon);
        TextView m_tvName = findViewById(R.id.tv_name);
        TextView m_tvTitle = findViewById(R.id.tv_title);
        LoaderManager.getLoader().loadNet(m_ivTeacherPhoto, m_strTeacherPhoto,new ILoader.Options(R.mipmap.head_s,R.mipmap.head_s));
        m_tvName.setText(m_strTeacherName);
        m_tvTitle.setText(m_strTeacherBreif);

        m_ivAdd = findViewById(R.id.iv_add);
        m_tvFollow = findViewById(R.id.tv_follow);
        m_rlBtnFollow = findViewById(R.id.rl_btn_follow);

        initListView();

        onClickView();

        registerReciever();

        callHttpFor();

        initVideoPlayer();
    }

    private void initVideoPlayer() {
        mNiceVideoPlayer = findViewById(R.id.nice_video_player);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
        String videoUrl = "http://gxtvod.58hengku.com/gxt/record/2018-03-14/gxt/beiyong1/2018-03-14-14:02:27_2018-03-14-15:03:29.m3u8";
//        String videoUrl = getIntent().getStringExtra("strPlayUrl");
//        videoUrl = Environment.getExternalStorageDirectory().getPath().concat("/办公室小野.mp4");
        mNiceVideoPlayer.setUp(videoUrl, null);
        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("");
        controller.setLenght(98000);
        Glide.with(this)
                .load("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg")
                .placeholder(R.mipmap.station_pic)
                .crossFade()
                .into(controller.imageView());
        mNiceVideoPlayer.setController(controller);//isFullScreen
    }

    @Override
    public void onRefresh() {
        Map<String, Object> map = new HashMap<>();
        map.put("room_id",m_strRoomeId);
        map.put("t_type",m_bOnlySee ? 1 : 0);
        map.put("page", m_bOnlySee ? teacherPage:page);
        if(m_bOnlySee){
            callHttpForSearchChatMsgTeacher(map);
        }else{
            callHttpForSearchChatMsgAll(map);
        }
    }

    @Override
    public void onLoadMore() {

    }

    private void onClickView(){

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        m_ivOnlySee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!m_bOnlySee){
                    m_ivOnlySee.setImageResource(R.drawable.see_all);
                    m_bOnlySee = true;
                    mPullLoadMoreRecyclerViewTeacher.setVisibility(View.VISIBLE);
                    mPullLoadMoreRecyclerViewAll.setVisibility(View.GONE);
                    if(m_msgTeacherDatas.size() == 0){
                        Map<String, Object> map = new HashMap<>();
                        map.put("room_id",m_strRoomeId);
                        map.put("t_type",m_bOnlySee ? 1 : 0);
                        map.put("page", m_bOnlySee ? teacherPage:page);
                        callHttpForSearchChatMsgTeacher(map);
                    }
                }else{
                    m_ivOnlySee.setImageResource(R.drawable.teacher_look);
                    m_bOnlySee = false;
                    mPullLoadMoreRecyclerViewTeacher.setVisibility(View.GONE);
                    mPullLoadMoreRecyclerViewAll.setVisibility(View.VISIBLE);
                }
            }
        });

        m_tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strMessage = m_etEdit.getText().toString().trim();
                if(strMessage.isEmpty()){
                    return;
                }

                ChatMessageBean chatMessageBean = new ChatMessageBean(ChatMessageBean.user_char,strMessage,System.currentTimeMillis()/1000,"",
                        SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame),
                        SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),"");
                m_msgDataAll.add(chatMessageBean);
                adapterAll.addLast(chatMessageBean);
                m_etEdit.setText("");

//                EMMessage message = EMMessage.createTxtSendMessage(strMessage,m_strRoomeId);
//                message.setAttribute("nickname", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
//                message.setAttribute("headerImageUrl", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon));
//                message.setAttribute("memberType", Message.MSG_OTHER_MEMBER);
//                message.setAttribute("messageType", ChatMessageBean.user_char);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                EMClient.getInstance().chatManager().sendMessage(message);

                callHttpForSendMsg(MESSAGE_TYPE_TEXT,strMessage,"");
                mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
            }
        });

//        RxTextView.textChanges(m_etEdit)
//                .debounce(100, TimeUnit.MILLISECONDS)
//                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(CharSequence charSequence) throws Exception {
//                        return Observable.just(charSequence.toString());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new io.reactivex.functions.Consumer<String>() {
//                    @Override
//                    public void accept(String queryData) throws Exception {
//                        if(m_etEdit.getText().length() > 0){
//                            m_llPic.setVisibility(View.GONE);
//                            m_ivPicSend.setVisibility(View.GONE);
//                            m_tvSend.setVisibility(View.VISIBLE);
//                        }else{
//                            m_llPic.setVisibility(View.VISIBLE);
//                            m_ivPicSend.setVisibility(View.VISIBLE);
//                            m_tvSend.setVisibility(View.GONE);
//                        }
//                    }
//                });

        m_ivPicSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRadio();
            }
        });

        m_rlBtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callHttpFollow();
            }
        });

        m_rlPicChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_rlPicChat.setVisibility(View.GONE);
            }
        });
        m_ivPicChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_rlPicChat.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 自定义单选
     */
    private void openRadio() {
        RxGalleryFinal
                .with(ChatActivity.this)
                .image()
                .radio()
//                .cropAspectRatioOptions(0, new AspectRatio("3:3", 30, 10))
//                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Uri dataUri = Utils.getMediaUriFromPath(ChatActivity.this,imageRadioResultEvent.getResult().getOriginalPath());
                        if (dataUri != null) {
                            File file = FileUtils.uri2File(aty, dataUri);

                            ChatMessageBean chatMessageBean = new ChatMessageBean(ChatMessageBean.user_pic, file.getAbsolutePath(),System.currentTimeMillis(),"",
                                    SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame),
                                    SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),"");
                            adapterAll.addLast(chatMessageBean);
                            mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
                            EMMessage messageEMM = EMMessage.createImageSendMessage(file.getAbsolutePath(), false, m_strRoomeId);
                            messageEMM.setAttribute("nickname", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
                            messageEMM.setAttribute("headerImageUrl", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon));
                            messageEMM.setAttribute("memberType", Message.MSG_OTHER_MEMBER);
                            messageEMM.setAttribute("messageType", ChatMessageBean.user_pic);

                            messageEMM.setChatType(EMMessage.ChatType.ChatRoom);
                            EMClient.getInstance().chatManager().sendMessage(messageEMM);
                        }
                    }
                })
                .openGallery();
    }

    private void initListView() {
        mPullLoadMoreRecyclerViewAll = findViewById(org.kymjs.chat.R.id.pullLoadMoreRecyclerViewAll);
        mRecyclerViewAll = mPullLoadMoreRecyclerViewAll.getRecyclerView();
        mRecyclerViewAll.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerViewAll.setPushRefreshEnable(false);
        mPullLoadMoreRecyclerViewAll.setRefreshing(true);
        mPullLoadMoreRecyclerViewAll.setLinearLayout();
        mPullLoadMoreRecyclerViewAll.setOnPullLoadMoreListener(this);
        adapterAll = new ChatAllAdapter(this,  getOnChatItemClickListener());
        mPullLoadMoreRecyclerViewAll.setAdapter(adapterAll);
        adapterAll.addAll(m_msgDataAll);

        mPullLoadMoreRecyclerViewTeacher = findViewById(org.kymjs.chat.R.id.pullLoadMoreRecyclerViewTeacher);
        mRecyclerViewTeacher = mPullLoadMoreRecyclerViewTeacher.getRecyclerView();
        mRecyclerViewTeacher.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerViewTeacher.setPushRefreshEnable(false);
        mPullLoadMoreRecyclerViewTeacher.setRefreshing(true);
        mPullLoadMoreRecyclerViewTeacher.setLinearLayout();
        mPullLoadMoreRecyclerViewTeacher.setOnPullLoadMoreListener(this);
        adapterTeacher = new ChatTeacherAdapter(this,  getOnChatItemClickListener());
        mPullLoadMoreRecyclerViewTeacher.setAdapter(adapterTeacher);
        adapterTeacher.addAll(m_msgTeacherDatas);

        Utils.setOnTouchEditTextOutSideHideIM(this,mRecyclerViewAll);
        Utils.setOnTouchEditTextOutSideHideIM(this,mRecyclerViewTeacher);

    }

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private ChatActivity.OnChatItemClickListener getOnChatItemClickListener() {
        return new ChatActivity.OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position,String imgPath,ImageView thumb) {
                ImageViewAware thumbAware = new ImageViewAware(thumb);
                String url = new FengNiaoImageSource(imgPath, 3840, 5760).getThumb(100, 100).url;
                Intent intent = new Intent(ChatActivity.this, PicViewActivity.class);
                intent.putExtra("image", new FengNiaoImageSource(imgPath, 3840, 5760));
                ImageSize targetSize = new ImageSize(thumbAware.getWidth(), thumbAware.getHeight());
                String memoryCacheKey = MemoryCacheUtils.generateKey(url, targetSize);
                intent.putExtra("cache_key", memoryCacheKey);
                Rect rect = new Rect();
                thumb.getGlobalVisibleRect(rect);
                intent.putExtra("rect", rect);
                intent.putExtra("scaleType", thumb.getScaleType());
                startActivity(intent);
            }

            @Override
            public void onTextClick(int position) {
            }

            @Override
            public void onFaceClick(int position) {
            }
        };
    }

    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position, String imgPath,ImageView imageView);

        void onTextClick(int position);

        void onFaceClick(int position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(m_strRoomeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();

        //获取当前屏幕内容的高度
//        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                //获取View可见区域的bottom
//                Rect rect = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                if(bottom!=0 && oldBottom!=0 && bottom - rect.bottom <= 0){
//
//                }else {
//                    mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
//                    mRecyclerViewTeacher.scrollToPosition(adapterTeacher.getItemCount()-1);
//                }
//            }
//        });

        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        StatService.onPause(this);
        super.onPause();
    }

    private void registerReciever() {
        MsgReceiver msgReceiver = new MsgReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.BROADCAST_RECEIVEMESSAGE_CHAT);
        registerReceiver(msgReceiver, filter1);
    }

    /** 收到消息广播接收者 刷新界面 **/
    class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int iMsgType = intent.getIntExtra("iMsgType",3);
            String strMemberType = intent.getStringExtra("memberType");
            String strContext = "";
            if(iMsgType == Message.MSG_TYPE_TEXT){
                strContext = intent.getStringExtra("strText");
            }else if(iMsgType == Message.MSG_TYPE_PHOTO){
                strContext = intent.getStringExtra("strFilePath");
            }

            String strStuName = intent.getStringExtra("strStuName");
            String strStuQuestion = intent.getStringExtra("strStuQuestion");

            String strNickName = intent.getStringExtra("nickname");
            String strHeaderImageUrl = intent.getStringExtra("headerImageUrl");
            long strTime = intent.getLongExtra("strTime",0);

            ChatMessageBean chatMessageBean = null;

            if(strMemberType.equals(ChatMessageBean.teacher_char) || strMemberType.equals(ChatMessageBean.teacher_rep)){
                strContext = Utils.delHTMLTag(strContext);
            }

            if(strMemberType.equals(ChatMessageBean.user_char) || strMemberType.equals(ChatMessageBean.user_pic)){
                chatMessageBean = new ChatMessageBean(strMemberType,strContext,strTime,"",strNickName, strHeaderImageUrl,"");
            }else if(strMemberType.equals(ChatMessageBean.teacher_char) || strMemberType.equals(ChatMessageBean.teacher_pic)){
                chatMessageBean = new ChatMessageBean(strMemberType, "",strTime,strContext,strNickName,strHeaderImageUrl,"");
            }else if(strMemberType.equals(ChatMessageBean.teacher_rep)){
                chatMessageBean = new ChatMessageBean(strMemberType, strStuQuestion,strTime,strContext,strNickName, strHeaderImageUrl,strStuName);
            }

            boolean isBottomAll;
            if(!mRecyclerViewAll.canScrollVertically(1)) {
                isBottomAll = true;
            }else {
                isBottomAll = false;
            }

            adapterAll.addLast(chatMessageBean);

            if(isBottomAll){
                mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
            }

            if(strMemberType.equals(ChatMessageBean.teacher_char) || strMemberType.equals(ChatMessageBean.teacher_pic) || strMemberType.equals(ChatMessageBean.teacher_rep)&& m_bOnlySee){
                boolean isBottomTeacher;
                if(!mRecyclerViewTeacher.canScrollVertically(1)) {
                    isBottomTeacher = true;
                }else {
                    isBottomTeacher = false;
                }
                adapterTeacher.addLast(chatMessageBean);
                if(isBottomTeacher){
                    mRecyclerViewTeacher.scrollToPosition(adapterTeacher.getItemCount()-1);
                }
            }
        }
    }

    private void callHttpFor(){
        String urlDataString = "?t_id="+m_strTeacherId+"&u_id="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
        HttpClient.get(ApiStores.Search_attension + urlDataString, new HttpCallback<ResponseChatBean>() {
            @Override
            public void OnSuccess(ResponseChatBean response) {
                if(response.getResult()){
                    if(response.getContent().getInfo().getA_id_attention() == ALREADY_PAID_ATTENTION_TO){
                        m_isFollow = true;
                    }else if(response.getContent().getInfo().getA_id_attention() == UNCONCERNED){
                        m_isFollow = false;
                    }
                    isFollow();
                    joinRoom();
                    Map<String, Object> map = new HashMap<>();
                    map.put("room_id",m_strRoomeId);
                    map.put("t_type",m_bOnlySee ? 1 : 0);
                    map.put("page", page);
                    callHttpForSearchChatMsgAll(map);
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
                kProgressHUD.show();
            }

            @Override
            public void OnRequestFinish() {
                kProgressHUD.dismiss();
            }
        });
    }

    private void isFollow(){
        if(m_isFollow){
            m_ivAdd.setVisibility(View.GONE);
            m_tvFollow.setText("已关注");
        }else{
            m_ivAdd.setVisibility(View.VISIBLE);
            m_tvFollow.setText("关注");
        }
    }

    private void joinRoom(){
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomListener);
        onChatRoomViewCreation();
    }

    protected void onChatRoomViewCreation() {
        kProgressHUD.show();
        EMClient.getInstance().chatroomManager().joinChatRoom(m_strRoomeId, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ChatActivity.this.isFinishing() || !m_strRoomeId.equals(value.getId()))
                            return;
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(m_strRoomeId);
                        if (room != null) {

                        } else {
                        }
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                EMLog.d(TAG, "join room failure : " + error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kProgressHUD.dismiss();
                    }
                });
                finish();
            }
        });
    }

    private void callHttpForSendMsg(int msgType,String msgText,String msgPic){
        String urlDataString = "";
        if(msgType == MESSAGE_TYPE_TEXT){
            urlDataString = "?c_st_id="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId)+"&t_type="+ msgType+"&c_st_content=" + msgText +"&c_room_id="+m_strRoomeId;
        }else{
            urlDataString = "?c_st_id="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId)+"&t_type="+ msgType+"&chat_image="+msgPic +"&c_room_id="+m_strRoomeId;
        }
        HttpClient.get(ApiStores.student_say + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                Log.d("",response.toString());
                if(response.getResult()){

                }else{
                    messageCenter("提示",response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
            }

            @Override
            public void OnRequestFinish() {
            }
        });
    }

    private void callHttpForSearchChatMsgAll(Map<String, Object> map){
        HttpClient.post(ApiStores.seacher_record ,map, new HttpCallback<ResponseChatMessageBean>() {
            @Override
            public void OnSuccess(final ResponseChatMessageBean response) {
                Log.d("",response.toString());
                if(response.getResult()){

                    mPullLoadMoreRecyclerViewAll.setPullLoadMoreCompleted();
                    if(response.getContent().size() == 0 ){
                        mPullLoadMoreRecyclerViewAll.setPullRefreshEnable(false);
                        return;
                    }
                    page ++;
                    adapterAll.addAllTop(response.getContent());

                    if(page == 1){
                        mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
                    }
                }else{
                    messageCenter("提示",response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
            }

            @Override
            public void OnRequestFinish() {
            }
        });
    }
    private void callHttpForSearchChatMsgTeacher(Map<String, Object> map){
//
        HttpClient.post(ApiStores.seacher_record, map, new HttpCallback<ResponseChatMessageBean>() {
            @Override
            public void OnSuccess(final ResponseChatMessageBean response) {
                if (response.getResult()){
                    mPullLoadMoreRecyclerViewTeacher.setPullLoadMoreCompleted();
                    if(response.getContent().size() == 0 ){
                        mPullLoadMoreRecyclerViewTeacher.setPullRefreshEnable(false);
                        return;
                    }
                    teacherPage ++;
                    adapterTeacher.addAllTop(response.getContent());

                    if(teacherPage == 1){
                        mRecyclerViewTeacher.scrollToPosition(adapterTeacher.getItemCount()-1);
                    }
                }else {
                    messageCenter("提示",response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("提示",message);
            }

            @Override
            public void OnRequestStart() {
            }

            @Override
            public void OnRequestFinish() {
            }
        });
    }

    private void callHttpFollow(){
        String urlDataString = "?a_tid="+m_strTeacherId+"&a_uid="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
        HttpClient.get(ApiStores.add_attension + urlDataString, new HttpCallback<ResponseFollowBean>() {
            @Override
            public void OnSuccess(ResponseFollowBean response) {
                if(response.getResult()){
                    if(response.getContent().getInfo() == ALREADY_PAID_ATTENTION_TO){
                        m_isFollow = true;
                    }else if(response.getContent().getInfo() == UNCONCERNED){
                        m_isFollow = false;
                    }
                    isFollow();
                }
            }

            @Override
            public void OnFailure(String message) {
                kProgressHUD.dismiss();
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
            }

            @Override
            public void OnRequestFinish() {
            }
        });
    }

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        if(m_rlPicChat.getVisibility() == View.VISIBLE){
            m_rlPicChat.setVisibility(View.GONE);
            return;
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        joinRoom();
    }
}
