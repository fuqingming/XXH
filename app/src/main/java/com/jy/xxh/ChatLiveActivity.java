package com.jy.xxh;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mobstat.StatService;
import com.blankj.utilcode.util.SPUtils;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.hyphenate.util.EMLog;
import com.jy.xxh.adapter.ChatLiveAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.VideoPlayerBaseActivity;
import com.jy.xxh.bean.base.ChatLiveMessageBean;
import com.jy.xxh.bean.base.ChatMessageBean;
import com.jy.xxh.bean.base.Messages;
import com.jy.xxh.bean.response.ResponseBaseBean;
import com.jy.xxh.bean.response.ResponseChatLiveMessageBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.Constant;
import com.jy.xxh.util.ChatRoomListener;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.NiceUtil;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.BigImage.FengNiaoImageSource;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.kymjs.kjframe.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import static com.hyphenate.util.EasyUtils.TAG;

public class ChatLiveActivity extends VideoPlayerBaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    /** 文字消息 **/
    public static final int MESSAGE_TYPE_TEXT = 1;
    /** 图片消息 **/
    public static final int MESSAGE_TYPE_PIC = 2;

    /** 普通模式 **/
    public static final int MODE_NORMAL = 10;
    /** 全屏模式  **/
    public static final int MODE_FULL_SCREEN = 11;

    private int mCurrentMode;

    KProgressHUD kProgressHUD;
    @BindView(R.id.ll_play)
    RelativeLayout m_llPlay;

    private MsgReceiver msgReceiver;

    protected EaseChatPrimaryMenuBase.EaseChatPrimaryMenuListener listener;
    protected ChatRoomListener chatRoomListener;

    @BindView(R.id.pullLoadMoreRecyclerViewAll)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerViewAll;
    private RecyclerView mRecyclerViewAll;

    @BindView(R.id.et_edit)
    EditText m_etEdit;
    @BindView(R.id.ll_pic)
    LinearLayout m_llPic;
    @BindView(R.id.iv_pic_send)
    ImageView m_ivPicSend;
    @BindView(R.id.tv_send)
    TextView m_tvSend;
    @BindView(R.id.iv_enlarge)
    ImageView m_ivEnlarge;
    @BindView(R.id.rl_live)
    RelativeLayout m_rlLive;

    private List<ChatLiveMessageBean> m_msgDataAll = new ArrayList<>();
    private ChatLiveAdapter adapterAll;

    private String m_strRoomeId;
    private String m_strTeacherId;
    private boolean m_bOnlySee = false;         //false:查看全部    true:查看老师

    private int page;
    private int teacherPage;

    private View mLoadingView;
    @BindView(R.id.SurfaceView)
    SurfaceView mSurfaceView;
    private PLMediaPlayer mMediaPlayer;
    private AVOptions mAVOptions;
    private long mLastUpdateStatTime = 0;

    @Override
    public void initWidget() {
        ButterKnife.bind(this);

        page = 0;
        teacherPage = 0;
        m_strTeacherId = getIntent().getStringExtra("strTeacherId");
        m_strRoomeId = getIntent().getStringExtra("strRoomId");

        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
        m_rlLive.setVisibility(View.VISIBLE);

        chatRoomListener = new ChatRoomListener() {
            @Override
            protected String setRoomID() {
                return m_strRoomeId;
            }

            @Override
            protected Context setContext() {
                return ChatLiveActivity.this;
            }
        };

        m_etEdit.setMaxHeight(Utils.dp2px(this,100));

        m_llPic.setVisibility(View.GONE);
        m_ivPicSend.setVisibility(View.GONE);
        m_tvSend.setVisibility(View.VISIBLE);

        initPlayer();

        initListView();

        onClickView();

        registerReciever();

        callHttpFor();
    }

    private void initPlayer(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLoadingView = findViewById(R.id.LoadingView);
        mSurfaceView.getHolder().addCallback(mCallback);
        mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        mAVOptions.setInteger(AVOptions.KEY_LOG_LEVEL, 0);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop();
        }
    };

    public void releaseWithoutStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
        }
    }

    private void prepare() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            return;
        }

        try {
            mMediaPlayer = new PLMediaPlayer(this, mAVOptions);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setDataSource(getIntent().getStringExtra("strLiveUrl"));
//            mMediaPlayer.setDataSource("rtmp://live.hkstv.hk.lxdns.com/live/hks");
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            if(!NiceUtil.isWiFiActive(ChatLiveActivity.this)){
                m_llPlay.setVisibility(View.VISIBLE);
            }else{
                m_llPlay.setVisibility(View.GONE);
                m_ivEnlarge.setVisibility(View.VISIBLE);
                mMediaPlayer.prepareAsync();
            }
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int percent) {
            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
            long current =  System.currentTimeMillis();
            if (current - mLastUpdateStatTime > 3000) {
                mLastUpdateStatTime = current;
            }
        }
    };

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mMediaPlayer.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:

                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    break;
                default:
                    break;
            }
            EventBus.getDefault().post("");
            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.d(TAG, "Play Completed !");
            finish();
        }
    };

    private PLOnPreparedListener mOnPreparedListener = new PLOnPreparedListener() {
        @Override
        public void onPrepared(int preparedTime) {
            Log.i(TAG, "On Prepared ! prepared time = " + preparedTime + " ms");
            mMediaPlayer.start();
        }
    };

    @OnClick({R.id.tv_play,R.id.iv_back_live,R.id.iv_enlarge})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_play:
                if(!NiceUtil.isNetworkAvalible(ChatLiveActivity.this)){
                    Utils.showDialogWifi(ChatLiveActivity.this);
                    return;
                }
                m_llPlay.setVisibility(View.GONE);
                m_ivEnlarge.setVisibility(View.VISIBLE);
                mMediaPlayer.prepareAsync();
                break;
            case R.id.iv_back_live:
                if(!exitFullScreen()){
                    finish();
                }
                break;
            case R.id.iv_enlarge:
                if(!exitFullScreen()){
                    enterFullScreen();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        Map<String, Object> map = new HashMap<>();
        map.put("room_id",m_strRoomeId);
        map.put("t_type",m_bOnlySee ? 1 : 0);
        map.put("page", m_bOnlySee ? teacherPage:page);
        callHttpForSearchChatMsgAll(map);
    }

    @Override
    public void onLoadMore() {

    }

    private void onClickView(){

        m_tvSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                String strMessage = m_etEdit.getText().toString().trim();
                if(strMessage.isEmpty()){
                    return;
                }

                ChatLiveMessageBean chatMessageBean = new ChatLiveMessageBean(ChatLiveMessageBean.user_char,strMessage,System.currentTimeMillis()/1000,"",
                        SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame),
                        SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),"",
                        SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId));
                m_msgDataAll.add(chatMessageBean);
                adapterAll.addLast(chatMessageBean);
                m_etEdit.setText("");

//                EMMessage message = EMMessage.createTxtSendMessage(strMessage,m_strRoomeId);
//                message.setAttribute("nickname", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
//                message.setAttribute("headerImageUrl", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon));
//                message.setAttribute("memberType", Messages.MSG_OTHER_MEMBER);
//                message.setAttribute("messageType", ChatMessageBean.user_char);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                EMClient.getInstance().chatManager().sendMessage(message);

                callHttpForSendMsg(MESSAGE_TYPE_TEXT,strMessage,"");
                mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
            }
        });

        m_ivPicSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRadio();
            }
        });
    }

    /**
     * 自定义单选
     */
    private void openRadio() {
        RxGalleryFinal
                .with(ChatLiveActivity.this)
                .image()
                .radio()
//                .cropAspectRatioOptions(0, new AspectRatio("3:3", 30, 10))
//                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Uri dataUri = Utils.getMediaUriFromPath(ChatLiveActivity.this,imageRadioResultEvent.getResult().getOriginalPath());
                        if (dataUri != null) {
                            File file = FileUtils.uri2File(ChatLiveActivity.this, dataUri);

                            ChatLiveMessageBean chatMessageBean = new ChatLiveMessageBean(ChatMessageBean.user_pic, file.getAbsolutePath(),System.currentTimeMillis(),"",
                                    SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame),
                                    SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),"",
                                    SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId));
                            adapterAll.addLast(chatMessageBean);
                            mRecyclerViewAll.scrollToPosition(adapterAll.getItemCount()-1);
                            EMMessage messageEMM = EMMessage.createImageSendMessage(file.getAbsolutePath(), false, m_strRoomeId);
                            messageEMM.setAttribute("nickname", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
                            messageEMM.setAttribute("headerImageUrl", SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon));
                            messageEMM.setAttribute("memberType", Messages.MSG_OTHER_MEMBER);
                            messageEMM.setAttribute("messageType", ChatLiveMessageBean.user_pic);

                            messageEMM.setChatType(EMMessage.ChatType.ChatRoom);
                            EMClient.getInstance().chatManager().sendMessage(messageEMM);
                        }
                    }
                })
                .openGallery();
    }

    private void initListView() {
        mRecyclerViewAll = mPullLoadMoreRecyclerViewAll.getRecyclerView();
        mRecyclerViewAll.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerViewAll.setPushRefreshEnable(false);
        mPullLoadMoreRecyclerViewAll.setRefreshing(true);
        mPullLoadMoreRecyclerViewAll.setLinearLayout();
        mPullLoadMoreRecyclerViewAll.setOnPullLoadMoreListener(this);
        adapterAll = new ChatLiveAdapter(this,  getOnChatItemClickListener());
        mPullLoadMoreRecyclerViewAll.setAdapter(adapterAll);
        adapterAll.addAll(m_msgDataAll);
        Utils.setOnTouchEditTextOutSideHideIM(ChatLiveActivity.this,mRecyclerViewAll);
    }

    private void enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) return;
        NiceUtil.scanForActivity(ChatLiveActivity.this) .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mPullLoadMoreRecyclerViewAll.setVisibility(View.GONE);
        findViewById(R.id.ll_edit).setVisibility(View.GONE);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) m_rlLive.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = ActionBar.LayoutParams.MATCH_PARENT;
        mCurrentMode = MODE_FULL_SCREEN;
    }

    private boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            NiceUtil.scanForActivity(ChatLiveActivity.this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mPullLoadMoreRecyclerViewAll.setVisibility(View.VISIBLE);
            findViewById(R.id.ll_edit).setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) m_rlLive.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.height = Utils.dp2px(ChatLiveActivity.this,230);
            mCurrentMode = MODE_NORMAL;
            return true;
        }
        return false;
    }

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private ChatLiveActivity.OnChatItemClickListener getOnChatItemClickListener() {
        return new ChatLiveActivity.OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position,String imgPath,ImageView thumb) {
                ImageViewAware thumbAware = new ImageViewAware(thumb);
                String url = new FengNiaoImageSource(imgPath, 3840, 5760).getThumb(100, 100).url;
                Intent intent = new Intent(ChatLiveActivity.this, PicViewActivity.class);
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
        void onPhotoClick(int position, String imgPath, ImageView imageView);

        void onTextClick(int position);

        void onFaceClick(int position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(m_strRoomeId);
        unregisterReceiver(msgReceiver);
        release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
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
        msgReceiver = new MsgReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.BROADCAST_RECEIVEMESSAGE_CHAT);
        registerReceiver(msgReceiver, filter1);
    }

    /** 收到消息广播接收者 刷新界面 **/
    class MsgReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int iMsgType = intent.getIntExtra("iMsgType",3);
            String strMemberType = intent.getStringExtra("memberType");
            String strContext = "";
            if(iMsgType == Messages.MSG_TYPE_TEXT){
                strContext = intent.getStringExtra("strText");
            }else if(iMsgType == Messages.MSG_TYPE_PHOTO){
                strContext = intent.getStringExtra("strFilePath");
            }

            String strStuName = intent.getStringExtra("strStuName");
            String strStuQuestion = intent.getStringExtra("strStuQuestion");

            String strNickName = intent.getStringExtra("nickname");
            String strHeaderImageUrl = intent.getStringExtra("headerImageUrl");
            String strID = intent.getStringExtra("strID");
            long strTime = intent.getLongExtra("strTime",0);

            ChatLiveMessageBean chatMessageBean = null;

            if(strMemberType.equals(ChatLiveMessageBean.teacher_char) || strMemberType.equals(ChatLiveMessageBean.teacher_rep)){
                strContext = Utils.delHTMLTag(strContext);
            }

            if(strMemberType.equals(ChatLiveMessageBean.user_char) || strMemberType.equals(ChatLiveMessageBean.user_pic)){
                chatMessageBean = new ChatLiveMessageBean(strMemberType,strContext,strTime,"",strNickName, strHeaderImageUrl,"",strID);
            }else if(strMemberType.equals(ChatLiveMessageBean.teacher_char) || strMemberType.equals(ChatLiveMessageBean.teacher_pic)){
                chatMessageBean = new ChatLiveMessageBean(strMemberType, "",strTime,strContext,strNickName,strHeaderImageUrl,"",strID);
            }else if(strMemberType.equals(ChatLiveMessageBean.teacher_rep)){
                chatMessageBean = new ChatLiveMessageBean(strMemberType, strStuQuestion,strTime,strContext,strNickName, strHeaderImageUrl,strStuName,strID);
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
        }
    }

    private void callHttpFor(){
        joinRoom();
        Map<String, Object> map = new HashMap<>();
        map.put("room_id",m_strRoomeId);
        map.put("t_type",m_bOnlySee ? 1 : 0);
        map.put("page", page);
        callHttpForSearchChatMsgAll(map);
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
                kProgressHUD.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ChatLiveActivity.this.isFinishing() || !m_strRoomeId.equals(value.getId()))
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
        HttpClient.post(ApiStores.seacher_record ,map, new HttpCallback<ResponseChatLiveMessageBean>() {
            @Override
            public void OnSuccess(final ResponseChatLiveMessageBean response) {
                Log.d("",response.toString());
                if(response.getResult()){

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
                mPullLoadMoreRecyclerViewAll.setPullLoadMoreCompleted();
            }
        });
    }

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }

    @Override
    public void onBackPressed() {
        if (exitFullScreen()) {
            return;
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
