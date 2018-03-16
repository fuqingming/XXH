package com.jy.xxh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.joker.pager.listener.OnItemClickListener;
import com.jy.xxh.adapter.FragmentHallAdapter;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;
import com.jy.xxh.base.BaseFragment;
import com.jy.xxh.bean.base.RoomBean;
import com.jy.xxh.bean.response.ResponseHallBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.recyclerview.RecycleViewDivider;
import com.joker.pager.BannerBean;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolder;
import com.joker.pager.holder.ViewHolderCreator;
import com.powyin.scroll.widget.ISwipe;
import com.powyin.scroll.widget.SwipeRefresh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 *
 * */
public class FragmentHall extends BaseFragment {

	private RecyclerView m_recyclerView;
	private BannerPager m_bpBanner;
	SwipeRefresh swipeRefresh;

	private FragmentHallAdapter m_adapterFragmentHall;

	protected String toChatUsername;
	private String m_strTeacherPhoto;
	private String m_strTeacherName;
	private String m_strTeacherBreif;
	private String m_strTeacherId;

	private List<BannerBean> m_bannerBean;
	private List<RoomBean> m_roomBean;
	private ArrayList<String> m_arrBanner;

	@Override
	protected int setLayoutResourceID() {
		return R.layout.fragment_hall;
	}

	@Override
	protected void init() {
		super.init();
		swipeRefresh = getContentView().findViewById(R.id.swipe_refresh);
		m_recyclerView = getContentView().findViewById(R.id.recycler_view);
		m_bpBanner = getContentView().findViewById(R.id.banner_pager);

		m_bannerBean = new ArrayList<>();
		m_roomBean = new ArrayList<>();
		m_arrBanner = new ArrayList<>();
	}

	@Override
	protected void setUpView() {
		super.setUpView();
		initListView();
	}

	private void initListView(){
		m_adapterFragmentHall = new FragmentHallAdapter(getMContext(), m_roomBean);
		m_recyclerView.setLayoutManager(new LinearLayoutManager(getMContext()));
		m_recyclerView.setNestedScrollingEnabled(false);
		m_recyclerView.addItemDecoration(new RecycleViewDivider(getMContext(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.app_backgrount_color)));
		m_recyclerView.setFocusable(false);
		m_recyclerView.setAdapter(m_adapterFragmentHall);
		m_adapterFragmentHall.onSelectFragmentClickListener(new FragmentHallAdapter.OnSelectClickListener()
		{

			@Override
			public void OnSelectClick(int position)
			{
				if (!DemoHelper.getInstance().isLoggedIn()) {
					Intent it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				toChatUsername = m_roomBean.get(position).getR_room_id();
				m_strTeacherPhoto = m_roomBean.get(position).getT_photo();
				m_strTeacherName = m_roomBean.get(position).getT_nic_name();
				m_strTeacherBreif = m_roomBean.get(position).getR_room_breif();
				m_strTeacherId = m_roomBean.get(position).getT_id();
				if(m_roomBean.get(position).getR_is_screte() == RoomBean.isLocked){
					Utils.showCommonDialogEdit(getMContext(),m_roomBean.get(position).getR_pwd(),new OnTaskSuccessComplete()
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
		swipeRefresh.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
			@Override
			public void onRefresh() {
				callHttpForHall();
			}

			@Override
			public void onLoading() {}
		});
	}

	@Override
	protected void setUpData() {
		if(!SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverIsUserOpenMain)){
			callHttpForHoll();
		}
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

	private void initBanner() {
		Utils.initCommonTitle(getContentView(),"直播");

		final PagerOptions pagerOptions = new PagerOptions.Builder(getMContext())
				.setTurnDuration(2000)
				.setIndicatorSize(Utils.dp2px(getMContext(),6))
				.setIndicatorColor(getMContext().getResources().getColor(R.color.dark),getMContext().getResources().getColor(R.color.red) )
				.setIndicatorAlign(RelativeLayout.CENTER_IN_PARENT)
				.setIndicatorMarginBottom(0)
				.build();

		m_bpBanner.setPagerOptions(pagerOptions).setPages(m_arrBanner, new ViewHolderCreator<BannerPagerHolder>() {
			@Override
			public FragmentHall.BannerPagerHolder createViewHolder() {
				final View view = LayoutInflater.from(getMContext()).inflate(R.layout.item_image_banner_analysis, null);
				return new FragmentHall.BannerPagerHolder(view);
			}
		});
		m_bpBanner.startTurning();
		m_bpBanner.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(int location, int position) {
				if(m_bannerBean.get(position).getB_turn_link() != null && !"".equals(m_bannerBean.get(position).getB_turn_link())){
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(m_bannerBean.get(position).getB_turn_link());
					intent.setData(content_url);
					startActivity(intent);
				}
			}
		});
	}

	private class BannerPagerHolder extends ViewHolder<String> {

		private ImageView mImage;

		private BannerPagerHolder(View itemView) {
			super(itemView);
			mImage = itemView.findViewById(R.id.image);
		}

		@Override
		public void onBindView(View view, String data, int position) {
			Glide.with(mImage.getContext())
					.load(data)
					.into(mImage);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(m_bpBanner!=null){
			m_bpBanner.startTurning();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if(m_bpBanner!=null){
			m_bpBanner.stopTurning();
		}
	}

	private void callHttpForHoll(){

		HttpClient.get(ApiStores.banner, new HttpCallback<ResponseHallBean>() {
			@Override
			public void OnSuccess(ResponseHallBean response) {
				if(response.getResult()){
					if(m_bannerBean.size() > 0 ){
						return;
					}
					m_bannerBean.addAll(response.getContent().getBanner());
					for(int i = 0 ; i < m_bannerBean.size() ; i ++){
						m_arrBanner.add(m_bannerBean.get(i).getB_link());
					}
					initBanner();
					SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsUserOpenMain,true);
					m_roomBean.addAll(response.getContent().getRoom());
					m_adapterFragmentHall.notifyDataSetChanged();
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
	private void callHttpForHall(){

		HttpClient.get(ApiStores.banner, new HttpCallback<ResponseHallBean>() {
			@Override
			public void OnSuccess(ResponseHallBean response) {
				if(response.getResult()){
					if(m_bannerBean.size() > 0 ){
						m_bannerBean.clear();
						m_arrBanner.clear();
					}
					if(m_roomBean.size() > 0 ){
						m_roomBean.clear();
					}

					m_bannerBean.addAll(response.getContent().getBanner());
					for(int i = 0 ; i < m_bannerBean.size() ; i ++){
						m_arrBanner.add(m_bannerBean.get(i).getB_link());
					}
					initBanner();
					SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsUserOpenMain,true);
					m_roomBean.addAll(response.getContent().getRoom());
					m_adapterFragmentHall.notifyDataSetChanged();
				}
			}

			@Override
			public void OnFailure(String message) {
				messageCenter("错误",message);
				swipeRefresh.setFreshResult(ISwipe.FreshStatus.SUCCESS);
			}

			@Override
			public void OnRequestStart() {
				kProgressHUD.show();
			}

			@Override
			public void OnRequestFinish() {
				kProgressHUD.dismiss();
				swipeRefresh.setFreshResult(ISwipe.FreshStatus.SUCCESS);
			}
		});
	}
	private void messageCenter(String title,String message){
		m_adapterFragmentHall.notifyDataSetChanged();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null){
			m_bannerBean = (List<BannerBean>) savedInstanceState.getSerializable("LIST_DATA_BANNER");
			m_roomBean = (List<RoomBean>) savedInstanceState.getSerializable("LIST_DATA_ROOM");
			m_arrBanner = savedInstanceState.getStringArrayList("LIST_DATA_ARR_BANNER");
			initListView();
			initBanner();
		}
	}
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable("LIST_DATA_BANNER", (Serializable) m_bannerBean);
		outState.putSerializable("LIST_DATA_ROOM", (Serializable) m_roomBean);
		outState.putStringArrayList("LIST_DATA_ARR_BANNER",  m_arrBanner);
	}
}