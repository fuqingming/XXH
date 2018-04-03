package com.jy.xxh;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.joker.pager.BannerBean;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolder;
import com.joker.pager.holder.ViewHolderCreator;
import com.jy.xxh.adapter.BaseRecyclerViewAdaptera;
import com.jy.xxh.adapter.FragmentHallAdapter;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;
import com.jy.xxh.base.BaseListFragment;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.bean.response.ResponseHallBean;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.recyclerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * */
public class FragmentHall extends BaseListFragment<RoomBean> {

	private FragmentHallAdapter m_fragmentTrainAdapter= new FragmentHallAdapter();

	protected String toChatUsername;
	private String m_strTeacherPhoto;
	private String m_strTeacherName;
	private String m_strTeacherBreif;
	private String m_strTeacherId;

	private LinearLayout m_llLive;
	private View m_vSpliter;
	private LinearLayout m_llLiveText;
	private ImageView m_ivLivePic;
	private ImageView m_ivPlay;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_hall;
	}

	@Override
	public void initView() {
		super.initView();
		Utils.initCommonTitle(getContentView(),"直播");
	}

	@Override
	protected BaseRecyclerViewAdaptera<RoomBean> getListAdapter() {
		return m_fragmentTrainAdapter;
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void initLayoutManager() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addItemDecoration(new RecycleViewDivider(getMContext(), LinearLayoutManager.VERTICAL, 9, getResources().getColor(R.color.app_backgrount_color)));
		mRecyclerView.setLoadMoreEnabled(false);
		View header = LayoutInflater.from(getMContext()).inflate(R.layout.common_fragment_hall_live,mRecyclerView, false);
		m_llLive = header.findViewById(R.id.ll_live);
		m_vSpliter = header.findViewById(R.id.v_spliter);
		m_llLiveText = header.findViewById(R.id.ll_live_text);
		m_ivLivePic = header.findViewById(R.id.iv_live_pic);
		m_ivPlay = header.findViewById(R.id.iv_play);

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

	protected void requestData(){
		HttpClient.get(ApiStores.banner, new HttpCallback<ResponseHallBean>() {
			@Override
			public void OnSuccess(ResponseHallBean response) {
				if(response.getResult()){
//					Glide.with(getMContext()).load(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon)).placeholder(R.mipmap.head_s).into(m_ivIcon);
//					if(m_bannerBean.size() > 0 ){
//						m_bannerBean.clear();
//						m_arrBanner.clear();
//					}
//					m_bannerBean.addAll(response.getContent().getBanner());
//					for(int i = 0 ; i < m_bannerBean.size() ; i ++){
//						m_arrBanner.add(m_bannerBean.get(i).getB_link());
//					}
//					initBanner();
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
			}
		});
	}
}