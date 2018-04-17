package com.jy.xxh;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.util.Utils;

import com.jy.xxh.huanxin.DemoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

import static com.jy.xxh.base.BaseApplication.applicationContext;

public class GuideActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "GuideActivity";
//    private DemoModel settingsModel;
    @BindView(R.id.guide_pages)
    ViewPager viewPager;
    @BindView(R.id.iv_indicator1)
    ImageView ivIndicator1;
    @BindView(R.id.iv_indicator2)
    ImageView ivIndicator2;
    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        super.init();

        if(SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverIsFirstUse)){
            if(!SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverIsReceiveMessage)){
                JPushInterface.stopPush(applicationContext);
            }
            intentMainActivity();
            return;
        }
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);

        DemoHelper.getInstance().initHandler(this.getMainLooper());

        LayoutInflater inflater = getLayoutInflater();
        View vPage1 = inflater.inflate(R.layout.activity_guide_page_1, null);
        View vPage2 = inflater.inflate(R.layout.activity_guide_page_2, null);

        ArrayList<View> pageViews = new ArrayList<>();
        pageViews.add(vPage1);
        pageViews.add(vPage2);

        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverWifiPlay,true);
        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsReceiveMessage,true);

        viewPager.setAdapter(new GuidePagerAdapter(GuideActivity.this, pageViews));

        // 立即体验
        Button btnGoto = vPage2.findViewById(R.id.btn_goto);
        btnGoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                intentMainActivity();
            }
        });

        // 页面切换
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                Log.d(LOG_TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                Log.d(LOG_TAG, "-------scrolled arg0:" + arg0);
                Log.d(LOG_TAG, "-------scrolled arg1:" + arg1);
                Log.d(LOG_TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int arg0)
            {
                Log.d(LOG_TAG, "------selected:" + arg0);
                setCurSelectedIndicator(arg0);
            }

            private void setCurSelectedIndicator(int nIndex)
            {
                ivIndicator1.setImageResource(R.drawable.shape_circle_gray);
                ivIndicator2.setImageResource(R.drawable.shape_circle_gray);
                switch (nIndex+1)
                {
                    case 1:
                        ivIndicator1.setImageResource(R.drawable.shape_circle_red);
                        break;


                    case 2:
                        ivIndicator2.setImageResource(R.drawable.shape_circle_red);
                        break;

                    default:
                        break;
                }
            }
        });
//        settingsModel = DemoHelper.getInstance().getModel();
//        settingsModel.setSettingMsgNotification(true);
//        PreferenceManager.getInstance().setCustomAppkey("1119180227099238#xxh-demo");
//        settingsModel.enableCustomAppkey(true);
    }

    private void intentMainActivity(){
        finish();

        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        GuideActivity.this.startActivity(intent);
    }

    private class GuidePagerAdapter extends PagerAdapter
    {
        private List<View> m_listView;

        public GuidePagerAdapter(Context context, List<View> listView)
        {
            this.m_listView = listView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object object)
        {
            (collection).removeView(m_listView.get(position));
        }

        @Override
        public int getCount()
        {
            return m_listView.size();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position)
        {
            ( collection).addView(m_listView.get(position));
            return m_listView.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }

    // 按两次返回键退出程序
    private static final int WAIT_NEXT_KEY_BACK_DURATION = 1000 * 2;
    private Boolean m_bFistKeyBackPressed = false;
    private Boolean m_bIsWaitingNextKeyBack = false;
    private Timer m_timerWaitingNextKeyBack = new Timer();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (!m_bFistKeyBackPressed)
            {
                m_bFistKeyBackPressed = true;

                Utils.showToast(this, "再按一次退出程序");

                if (!m_bIsWaitingNextKeyBack)
                {
                    m_bIsWaitingNextKeyBack = true;

                    m_timerWaitingNextKeyBack.schedule(new TimerTask()
                    {
                        public void run()
                        {
                            m_bFistKeyBackPressed = false;
                            m_bIsWaitingNextKeyBack = false;
                        }
                    }, WAIT_NEXT_KEY_BACK_DURATION);
                }
                return true;
            }
            else
            {
                finish();
            }
        }
        return false;
    }
}
