package com.jy.xxh;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.jy.xxh.adapter.AttentionAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.base.AttentionBean;
import com.jy.xxh.bean.response.ResponseAttentionBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.recyclerview.RecycleViewDivider;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttentionActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "AttentionActivity";
    public static final int IS_ACTIVITY_CHANGE = 1;

    KProgressHUD kProgressHUD;

    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView m_recyclerView;

    private AttentionAdapter m_adapterAttention;
    private List<AttentionBean> m_arrAttentionBean;
    private int page;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_attention;
    }

    protected void init(){
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
        m_arrAttentionBean = new ArrayList<>();
        page = 0;
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"我的关注",true);

        m_adapterAttention = new AttentionAdapter(this, m_arrAttentionBean);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.setSwipeItemClickListener(swipeItemClickListener);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.useDefaultLoadMore();
        m_recyclerView.setLoadMoreListener(loadMoreListener);
        m_recyclerView.setAdapter(m_adapterAttention);
        m_recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.app_backgrount_color)));
        callHttpForAttention();
    }

    private SwipeItemClickListener swipeItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Intent it = new Intent(AttentionActivity.this,TeacherDetailsActivity.class);
            it.putExtra("strTeacherId",m_arrAttentionBean.get(position).getT_id());
            startActivityForResult(it,IS_ACTIVITY_CHANGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IS_ACTIVITY_CHANGE && resultCode == RESULT_OK)
        {
            m_arrAttentionBean.clear();
            m_adapterAttention.notifyDataSetChanged();
        }
    }

    private SwipeMenuRecyclerView.LoadMoreListener loadMoreListener = new SwipeMenuRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            callHttpForAttention();
        }
    };

    private void callHttpForAttention(){
        String urlDataString = "?u_id="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
        HttpClient.get(ApiStores.my_Attension + urlDataString, new HttpCallback<ResponseAttentionBean>() {
            @Override
            public void OnSuccess(ResponseAttentionBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    page ++;
                    m_arrAttentionBean.addAll(response.getContent().getInfo());
                    m_adapterAttention.notifyDataSetChanged();
                    m_recyclerView.loadMoreFinish(response.getContent().getInfo().isEmpty(), !response.getContent().getInfo().isEmpty());
                }else{
                    m_recyclerView.loadMoreError(0, response.getMessage());
                    messageCenter("提示",response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                m_recyclerView.loadMoreError(0, message);
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

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }
}
