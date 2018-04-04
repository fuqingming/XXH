package com.jy.xxh;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.jy.xxh.adapter.AttentionAdapter;
import com.jy.xxh.adapter.BaseRecyclerAdapter;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseListActivity;
import com.jy.xxh.bean.base.AttentionBean;
import com.jy.xxh.bean.response.ResponseAttentionBean;
import com.xiao.nicevideoplayer.constants.GlobalVariables;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.Utils;

public class AttentionActivity extends BaseListActivity<AttentionBean> {
    public static final int IS_ACTIVITY_CHANGE = 1;
    private AttentionAdapter m_adapterAttention = new AttentionAdapter();

    @Override
    public void initView() {
        super.initView();
        Utils.initCommonTitle(this,"我的关注");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention;
    }

    @Override
    protected BaseRecyclerAdapter<AttentionBean> getListAdapter() {
        return m_adapterAttention;
    }

    @Override
    protected void initLayoutManager() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLoadMoreEnabled(false);
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
                Intent it = new Intent(AttentionActivity.this,TeacherDetailsActivity.class);
                it.putExtra("strTeacherId",m_adapterAttention.getListData().get(position).getT_id());
                startActivityForResult(it,IS_ACTIVITY_CHANGE);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IS_ACTIVITY_CHANGE && resultCode == RESULT_OK)
        {
            onRefreshView();
        }
    }

    protected void requestData(){
        String urlDataString = "?u_id="+ SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
        HttpClient.get(ApiStores.my_Attension + urlDataString, new HttpCallback<ResponseAttentionBean>() {
            @Override
            public void OnSuccess(ResponseAttentionBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    executeOnLoadDataSuccess(response.getContent().getInfo());
                    totalPage = response.getContent().getInfo().size();
                }else{

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

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }
}
