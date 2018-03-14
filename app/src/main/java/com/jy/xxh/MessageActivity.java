package com.jy.xxh;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jy.xxh.adapter.MessageAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.base.MessageBean;
import com.jy.xxh.bean.response.ResponseMessageBean;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.Utils;
import com.jy.xxh.view.recyclerview.RecycleViewDivider;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "MessageActivity";

    KProgressHUD kProgressHUD;

    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView m_recyclerView;

    private MessageAdapter m_adapterMessage;
    private List<MessageBean> m_arrMessageBean;
    private int page;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_message;
    }

    protected void init(){
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
        m_arrMessageBean = new ArrayList<>();
        page = 0;
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"我的消息",true);

        m_adapterMessage = new MessageAdapter(this, m_arrMessageBean);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.useDefaultLoadMore();
        m_recyclerView.setLoadMoreListener(loadMoreListener);
        m_recyclerView.setAdapter(m_adapterMessage);
        m_recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.app_backgrount_color)));
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        callHttpForNotice();
    }

    private SwipeMenuRecyclerView.LoadMoreListener loadMoreListener = new SwipeMenuRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            callHttpForNotice();
        }
    };

    private void callHttpForNotice(){
        String urlDataString = "?begin="+page;
        HttpClient.get(ApiStores.my_Notice + urlDataString, new HttpCallback<ResponseMessageBean>() {
            @Override
            public void OnSuccess(ResponseMessageBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    page ++;
                    m_arrMessageBean.addAll(response.getContent());
                    m_adapterMessage.notifyDataSetChanged();
                    m_recyclerView.loadMoreFinish(response.getContent().isEmpty(), !response.getContent().isEmpty());
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
