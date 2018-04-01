package com.jy.xxh;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.jy.xxh.adapter.BaseRecyclerViewAdaptera;
import com.jy.xxh.adapter.MessageAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseListActivity;
import com.jy.xxh.bean.base.MessageBean;
import com.jy.xxh.bean.response.ResponseMessageBean;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.Utils;

public class MessageActivity extends BaseListActivity<MessageBean> {

    private MessageAdapter m_adapterMessage = new MessageAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        super.initView();
        Utils.initCommonTitle(this,"我的消息");
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected BaseRecyclerViewAdaptera<MessageBean> getListAdapter() {
        return m_adapterMessage;
    }

    @Override
    protected void initLayoutManager() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

            }

        });
    }

    protected void requestData(){
        String urlDataString = "?begin="+mCurrentPage;
        HttpClient.get(ApiStores.my_Notice + urlDataString, new HttpCallback<ResponseMessageBean>() {
            @Override
            public void OnSuccess(ResponseMessageBean response) {
                executeOnLoadDataSuccess(response.getContent());
                totalPage = response.getContent().size();
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

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }
}
