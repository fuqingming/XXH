package com.jy.xxh;


import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.jy.xxh.base.FragmentActivityBase;
import com.xiao.nicevideoplayer.constants.GlobalVariables;

public class MainActivity extends FragmentActivityBase {

    // Tab文字及图标
    private int m_iconArray[] = {R.drawable.main_tab_hall_selector,
                                 R.drawable.main_tab_mine_selector};

    // 各tab页对应的fragment
    private Class<?> m_fragmentArray[] = { FragmentHall.class,
                                           FragmentMine.class,};

    private String m_textArray[] = null;

    private FragmentTabHost m_tabHost = null;
    private LayoutInflater m_layoutInflater = null;

    private RelativeLayout m_rlStartUp;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();

        m_textArray = getResources().getStringArray(R.array.lottery_main_tab_text);

        m_layoutInflater = LayoutInflater.from(this);

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
//        int nSpecialAction = intent.getIntExtra("SpecialAction", -1);
//
//        switch(nSpecialAction)
//        {
//            case SPECIAL_ACTION_EXIT:					// 退出应用
//            {
//                exitApp();
//                return;
//            }
//        }

        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void setUpView() {
        m_rlStartUp = findViewById(R.id.rl_start_up);
        if(!SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverIsFirstUse)){
            SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsFirstUse,true);
            m_rlStartUp.setVisibility(View.GONE);
        }else{
            m_rlStartUp.postDelayed(new Runnable() {
                @Override
                public void run() {
                    m_rlStartUp.setVisibility(View.GONE);
                }
            },3000);
        }
        m_tabHost = findViewById(android.R.id.tabhost);
        m_tabHost.setup(this, getSupportFragmentManager(), R.id.fl_real_tabcontent);
        m_tabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        for (int i = 0; i < m_fragmentArray.length; i++)
        {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = m_tabHost.newTabSpec(m_fragmentArray[i].getName()).setIndicator(getTabItemView(i));

            // 将Tab按钮添加进Tab选项卡中
            m_tabHost.addTab(tabSpec, m_fragmentArray[i], null);

            // 设置Tab按钮的背景
            //m_tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_item_bg_selector);
        }

    }

    /**
     *
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index)
    {
        View vTab = m_layoutInflater.inflate(R.layout.main_tab_item_view, null);

        ImageView ivIcon = vTab.findViewById(R.id.iv_icon);
        ivIcon.setImageResource(m_iconArray[index]);

        TextView tvText = vTab.findViewById(R.id.tv_text);
        tvText.setText(m_textArray[index]);

        return vTab;
    }

    public void setCurrentTab(Class<?> fragmentClass)
    {
        m_tabHost.setCurrentTabByTag(fragmentClass.getName());
    }

    private long lastBackPress;
    @Override
    public void onBackPressed()//手机自带返回键所用
    {
        if (m_tabHost.getCurrentTab() != 0)
        {
            setCurrentTab(FragmentHall.class);
            return;
        }

        if (System.currentTimeMillis() - lastBackPress < 1000) {
            super.onBackPressed();
        } else {
            lastBackPress = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "再按一次注销", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
