package com.jy.xxh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.jy.xxh.adapter.BaseRecyclerAdapter;
import com.jy.xxh.adapter.FragmentHallAdapter;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;
import com.jy.xxh.base.BaseListFragment;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.bean.base.VideoBean;
import com.jy.xxh.bean.response.ResponseHallBean;
import com.jy.xxh.bean.response.ResponseHallLiveTypeBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.NiceUtil;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.error.ErrorLayout;
import com.jy.xxh.view.recyclerview.RecycleViewDivider;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * */
public class FragmentHall extends BaseListFragment<RoomBean> {

	public static final String LIVE_IS_PLAY = "1";		//直播中
	public static final String LIVE_IS_UNPLAY = "2";	//未直播

	KProgressHUD kProgressHUD;

	private FragmentHallAdapter m_fragmentTrainAdapter= new FragmentHallAdapter();

	private VideoBean m_videoBean;
	protected String toChatUsername;
	private String m_strTeacherPhoto;
	private String m_strTeacherName;
	private String m_strTeacherBreif;
	private String m_strTeacherId;

	private View m_vSpliter;
	private LinearLayout m_llLiveText;
	private ImageView m_ivLivePic;
	private ImageView m_ivPlay;
	private TextView m_tvLiveType;
	private TextView m_tvText;
	private TextView m_tvLiveTime;
	private LinearLayout m_llTeacherDetails;
	private LinearLayout m_llLiveTime;
	private ImageView m_ivIcon;

	private boolean m_isFirstOpen = true;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_hall;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void initView() {
		super.initView();
		Utils.initCommonTitle(getContentView(),"直播");
		kProgressHUD = new HUDProgressUtils().showLoadingImage(getMContext());
	}

	@Override
	protected BaseRecyclerAdapter<RoomBean> getListAdapter() {
		return m_fragmentTrainAdapter;
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void initLayoutManager() {
		EventBus.getDefault().register(this);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addItemDecoration(new RecycleViewDivider(getMContext(), LinearLayoutManager.VERTICAL, 9, getResources().getColor(R.color.app_backgrount_color)));
		mRecyclerView.setLoadMoreEnabled(false);
		View header = LayoutInflater.from(getMContext()).inflate(R.layout.common_fragment_hall_live,mRecyclerView, false);
		TextView tvTitleLive = header.findViewById(R.id.tv_title_live);
		TextPaint tpTitleLive = tvTitleLive .getPaint();
		tpTitleLive.setFakeBoldText(true);
		TextView tvTextLive = header.findViewById(R.id.tv_text_live);
		TextPaint tpTextLive = tvTextLive .getPaint();
		tpTextLive.setFakeBoldText(true);
		m_vSpliter = header.findViewById(R.id.v_spliter);
		m_tvLiveType = header.findViewById(R.id.tv_live_type);
		m_tvText = header.findViewById(R.id.tv_text);
		m_tvLiveTime = header.findViewById(R.id.tv_live_time);
		m_llTeacherDetails = header.findViewById(R.id.ll_teacher_details);
		m_llLiveTime = header.findViewById(R.id.ll_live_time);
		m_llLiveText = header.findViewById(R.id.ll_live_text);
		m_ivLivePic = header.findViewById(R.id.iv_live_pic);
		m_ivPlay = header.findViewById(R.id.iv_play);
		m_ivIcon = header.findViewById(R.id.iv_icon);
		m_ivPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!DemoHelper.getInstance().isLoggedIn()) {
					Intent it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				if(!NiceUtil.isNetworkAvalible(getContext())){
					Utils.showDialogWifi(getActivity());
					return;
				}
				requestDataLiveType();
			}
		});

		mRecyclerViewAdapter.addHeaderView(header);
		mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				onRefreshView();
			}
		});

		mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

				if ( REQUEST_COUNT <= totalPage) {
					mCurrentPage++;
					requestData();
					isRequestInProcess = true;
				} else {
					mRecyclerView.setNoMore(true);
				}
			}
		});

		mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (!DemoHelper.getInstance().isLoggedIn()) {
					Intent it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				if(!NiceUtil.isNetworkAvalible(getContext())){
					Utils.showDialogWifi(getActivity());
					return;
				}
				toChatUsername = m_fragmentTrainAdapter.getListData().get(position).getR_room_id();
				m_strTeacherPhoto = m_fragmentTrainAdapter.getListData().get(position).getT_photo();
				m_strTeacherName = m_fragmentTrainAdapter.getListData().get(position).getT_nic_name();
				m_strTeacherBreif = m_fragmentTrainAdapter.getListData().get(position).getR_room_breif();
				m_strTeacherId = m_fragmentTrainAdapter.getListData().get(position).getT_id();
				if(m_fragmentTrainAdapter.getListData().get(position).getR_is_screte() == RoomBean.isLocked){
					Utils.showCommonDialogEdit(getMContext(),m_fragmentTrainAdapter.getListData().get(position).getR_pwd(),new OnTaskSuccessComplete()
					{
						@Override
						public void onSuccess(Object obj)
						{
							intentChatActivity();
						}
					});
				}else{
					intentChatActivity();
				}
			}

		});
	}

	private void intentChatActivity(){
		Intent it = new Intent(getMContext(),ChatActivity.class);
		it.putExtra("strRoomId",toChatUsername);
		it.putExtra("strTeacherPhoto",m_strTeacherPhoto);
		it.putExtra("strTeacherName",m_strTeacherName);
		it.putExtra("strTeacherBreif",m_strTeacherBreif);
		it.putExtra("strTeacherId",m_strTeacherId);
		startActivity(it);
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getMContext(), "主界面");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPageEnd(getMContext(), "主界面");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	protected void requestData(){
		String strUrl;
		if(m_isFirstOpen && DemoHelper.getInstance().isLoggedIn()){
			strUrl = ApiStores.banner+"?uid="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
		}else{
			strUrl = ApiStores.banner;
		}

		HttpClient.get(strUrl, new HttpCallback<ResponseHallBean>() {
			@Override
			public void OnSuccess(ResponseHallBean response) {
				if(response.getResult()){
					m_videoBean = response.getContent().getVideo().get(0);
					if(LIVE_IS_UNPLAY.equals(m_videoBean.getV_type())){
						m_tvLiveType.setText("未直播");
						m_llTeacherDetails.setVisibility(View.GONE);
						m_llLiveTime.setVisibility(View.VISIBLE);
						m_tvLiveTime.setText(m_videoBean.getTimeD());
					}else if(LIVE_IS_PLAY.equals(m_videoBean.getV_type())){
						m_tvLiveType.setText("直播中");
						m_llTeacherDetails.setVisibility(View.VISIBLE);
						m_llLiveTime.setVisibility(View.GONE);
						Glide.with(getMContext()).load(m_videoBean.getT_photo()).placeholder(R.mipmap.head_s).into(m_ivIcon);
						m_tvText.setText(m_videoBean.getT_nic_name());
					}
					Glide.with(getMContext()).load(m_videoBean.getImg_url()).placeholder(R.mipmap.station_pic).into(m_ivLivePic);

					executeOnLoadDataSuccess(response.getContent().getRoom());
					totalPage = response.getContent().getRoom().size();
				}
			}

			@Override
			public void OnFailure(String message) {
				executeOnLoadDataError(null);
			}

			@Override
			public void OnRequestStart() {
			}

			@Override
			public void OnRequestFinish() {
				executeOnLoadFinish();
				m_isFirstOpen = false;
			}
		});
	}

	private void requestDataLiveType(){
		HttpClient.get(ApiStores.banners, new HttpCallback<ResponseHallLiveTypeBean>() {
			@Override
			public void OnSuccess(ResponseHallLiveTypeBean response) {
				if(response.getResult()){
					if(LIVE_IS_UNPLAY.equals(response.getContent().get(0).getV_type())){
						m_tvLiveType.setText("未直播");
						m_llTeacherDetails.setVisibility(View.GONE);
						m_llLiveTime.setVisibility(View.VISIBLE);
						m_tvLiveTime.setText(m_videoBean.getTimeD());
					}else if(LIVE_IS_PLAY.equals(response.getContent().get(0).getV_type())){
						m_tvLiveType.setText("直播中");
						m_llTeacherDetails.setVisibility(View.VISIBLE);
						m_llLiveTime.setVisibility(View.GONE);
						Glide.with(getMContext()).load(m_videoBean.getT_photo()).placeholder(R.mipmap.head_s).into(m_ivIcon);
						m_tvText.setText(m_videoBean.getT_nic_name());
					}

					if(LIVE_IS_UNPLAY.equals(m_videoBean.getV_type())){
						Utils.showToast(getContext(),"直播还未开始！");
						return;
					}

					toChatUsername = m_videoBean.getRoom_id();

					Intent it = new Intent(getMContext(),ChatLiveActivity.class);
					it.putExtra("strRoomId",toChatUsername);
//				it.putExtra("strLiveUrl","rtmp://gxtplay.58hengku.com/gxt/yxfxh1");
					it.putExtra("strLiveUrl",m_videoBean.getVideo_url());
//					it.putExtra("strLiveUrl","rtmp://gxtplay.58hengku.com/gxt/linshiyong ");
//				it.putExtra("strLiveUrl","rtmp://live.hkstv.hk.lxdns.com/live/hks");

					startActivity(it);
				}
			}

			@Override
			public void OnFailure(String message) {
				executeOnLoadDataError(null);
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
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventBus(String strLiveIsFalse){
		Utils.showToast(getMContext(),"直播已经停止");
//		onRefreshView();
//		m_videoBean.setV_type(LIVE_IS_UNPLAY);
	}
}