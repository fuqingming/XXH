package com.jy.xxh;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.jy.xxh.base.BaseFragment;
import com.jy.xxh.cache.AsyncImageLoader;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.switchbutton.FSwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

import static com.jy.xxh.base.BaseApplication.applicationContext;

/**
 *
 *
 * */
public class FragmentMine extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.sb_switch)
	FSwitchButton m_sbSwitch;
	@BindView(R.id.iv_icon)
	ImageView m_ivIcon;
	@BindView(R.id.tv_name)
	TextView m_tvName;
	@BindView(R.id.ll_pop)
	LinearLayout m_llPop;

	private View m_pwMenuView;

	@Override
	protected int setLayoutResourceID() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void init() {
		super.init();
		unbinder = ButterKnife.bind(this, getContentView());
	}

	@Override
	protected void setUpView() {
		super.setUpView();

		EventBus.getDefault().register(this);

		AsyncImageLoader.getInstace(getMContext()).loadBitmap(m_ivIcon,  SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon), R.mipmap.head_s);

		if(SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverIsReceiveMessage)){
			m_sbSwitch.setChecked(true,false,true);
		}else{
			m_sbSwitch.setChecked(false,false,false);
		}

		m_sbSwitch.setOnCheckedChangedCallback(new FSwitchButton.OnCheckedChangedCallback()
		{
			@Override
			public void onCheckedChanged(boolean checked, FSwitchButton view)
			{
				SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsReceiveMessage,checked);
				if(checked){
					JPushInterface.resumePush(applicationContext);
				}else{
					JPushInterface.stopPush(applicationContext);
				}
			}
		});
	}

	@OnClick({R.id.rl_login,R.id.ll_message,R.id.ll_attention,R.id.ll_opinion,R.id.ll_phone})
	public void onViewClicked(View view){
		Intent it;
		switch (view.getId()) {
			case R.id.rl_login:
				if (DemoHelper.getInstance().isLoggedIn()) {
					it = new Intent(getMContext(),MineActivity.class);
					startActivity(it);
					return;
				}
				it = new Intent(getMContext(),LoginActivity.class);
				getMContext().startActivity(it);
				break;
			case R.id.ll_message:
				if (!DemoHelper.getInstance().isLoggedIn()) {
					it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				it = new Intent(getMContext(),MessageActivity.class);
				getMContext().startActivity(it);
				break;
			case R.id.ll_attention:
				if (!DemoHelper.getInstance().isLoggedIn()) {
					it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				it = new Intent(getMContext(),AttentionActivity.class);
				getMContext().startActivity(it);
				break;
			case R.id.ll_opinion:
				if (!DemoHelper.getInstance().isLoggedIn()) {
					it = new Intent(getMContext(),LoginActivity.class);
					startActivity(it);
					return;
				}
				it = new Intent(getMContext(),OpinionActivity.class);
				getMContext().startActivity(it);
				break;
			case R.id.ll_phone:
				TelephonePopupWindow m_pwMenu = new TelephonePopupWindow(getActivity());
				//显示窗口
				m_pwMenu.showAtLocation(m_llPop, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DemoHelper.getInstance().isLoggedIn()) {
			m_tvName.setText(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
		}else{
			m_tvName.setText("登陆/注册");
			m_ivIcon.setImageResource(R.mipmap.head_s);
		}

	}

	public class TelephonePopupWindow extends PopupWindow
	{
		public TelephonePopupWindow(Activity context)
		{
			super(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_pwMenuView = inflater.inflate(R.layout.ppw_customer_service, null);
			TextView m_tvSales = (TextView) m_pwMenuView.findViewById(R.id.tv_sales);
			TextView m_tvCancel = (TextView) m_pwMenuView.findViewById(R.id.tv_cancel);
			this.setContentView(m_pwMenuView);						//设置SelectPicPopupWindow的View
			this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);	//设置SelectPicPopupWindow弹出窗体的宽
			this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);	//设置SelectPicPopupWindow弹出窗体的高
			this.setFocusable(true);								//设置SelectPicPopupWindow弹出窗体可点击
			this.setAnimationStyle(R.style.AnimBottom);				//设置SelectPicPopupWindow弹出窗体动画效果
			ColorDrawable dw = new ColorDrawable(0xb0000000);		//实例化一个ColorDrawable颜色为半透明
			this.setBackgroundDrawable(dw);							//设置SelectPicPopupWindow弹出窗体的背景

			m_pwMenuView.setOnTouchListener(new View.OnTouchListener() //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			{

				@SuppressLint("ClickableViewAccessibility")
				public boolean onTouch(View v, MotionEvent event)
				{

					int height = m_pwMenuView.findViewById(R.id.popu_layout).getTop();
					int y = (int) event.getY();
					if(event.getAction() == MotionEvent.ACTION_UP)
					{
						if(y < height)
						{
							dismiss();
						}
					}
					return true;
				}
			});

			m_tvCancel.setOnClickListener(new View.OnClickListener()//取消
			{

				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			});
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventBus(Bitmap bitmap){
		m_ivIcon.setImageBitmap(bitmap);
	}
}