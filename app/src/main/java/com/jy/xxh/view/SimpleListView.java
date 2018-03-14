package com.jy.xxh.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.xxh.R;

/**
 * 项目名称：Huakui
 * 创建人：付三
 * 创建时间：2016/6/3 13:58
 *
 * @version V1.0
 */
public class SimpleListView extends SwipeRefreshLayout {
    private ListView mListView;
    private LoadMoreStatus mLoadMoreStatus = LoadMoreStatus.CLICK_TO_LOAD;
    private OnLoadListener mOnLoadListener;
    private TextView mLoadMoreView;
    private AbsListView.OnScrollListener mOnScrollListener;
    private View mEmptyView;
    private ListAdapter mAdapter;

    /**
     * 加载更多状态
     */
    public static enum LoadMoreStatus {
        /**
         * 点击加载更多
         */
        CLICK_TO_LOAD,
        /**
         * 正在加载
         */
        LOADING,
        /**
         * 没有更多内容了
         */
        LOADED_ALL
    }

    /**
     * 加载监听器
     */
    public static interface OnLoadListener {
        /**
         * 下来刷新或者加载更多时触发该回调
         *
         * @param isRefresh true为下拉刷新 false为加载更多
         */
        public void onLoad(boolean isRefresh);
    }

    public SimpleListView(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mListView = new ListView(context, attrs);
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean mIsEnd = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
                if (scrollState == SCROLL_STATE_IDLE) {
                    //1:到达底部 2：底部当前可以加载更多 3：顶部不在刷新中状态
                    if (mIsEnd && mLoadMoreStatus == LoadMoreStatus.CLICK_TO_LOAD && !isRefreshing()) {
                        setLoadMoreStatus(LoadMoreStatus.LOADING);
                        if (mLoadMoreStatus != null) {
                            mOnLoadListener.onLoad(false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                    mIsEnd = true;
                } else {
                    mIsEnd = false;
                }
            }
        });

        super.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLoadMoreStatus != LoadMoreStatus.LOADING) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoad(true);
                    }
                } else {
                    SimpleListView.super.setRefreshing(false);
                }
            }
        });

    }

    public void addHeaderView(View view) {
        mListView.addHeaderView(view);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        mListView.addHeaderView(v, data, isSelectable);
    }

    public void addFooterView(View view) {
        mListView.addFooterView(view);
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        mListView.addFooterView(v, data, isSelectable);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mListView.setOnItemClickListener(listener);
    }

    public void setEmptyView(View emptyView) {
        if (emptyView != null) {
            mEmptyView = emptyView;
            if (mAdapter != null && mAdapter.getCount() > 0) {
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }
//            mListView.setEmptyView(emptyView);
        }
    }

    @Override
    @Deprecated
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
    }

    @Override
    @Deprecated
    public void setRefreshing(boolean refreshing) {
    }

    public void setAdapter(final ListAdapter adapter) {
        if (adapter == null) {
            return;
        }
        mAdapter = adapter;
        if (mLoadMoreView == null) {
            mLoadMoreView = new TextView(getContext());
            mLoadMoreView.setTextColor(0xff333333);
            mLoadMoreView.setTextSize(14);
            mLoadMoreView.setGravity(Gravity.CENTER);
//            int count = adapter.getCount();
//            mLoadMoreView.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
            mLoadMoreView.setVisibility(View.GONE);
//            if (mEmptyView != null) {
//                mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
//            }
//            mLoadMoreView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mLoadMoreStatus == LoadMoreStatus.CLICK_TO_LOAD && !isRefreshing()) {
//                        setLoadMoreStatus(LoadMoreStatus.LOADING);
//                        if (mLoadMoreStatus != null) {
//                            mOnLoadListener.onLoad(false);
//                        }
//                    }
//                }
//            });
            mLoadMoreView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.ten) *4 ));
            mListView.addFooterView(mLoadMoreView);
        }
        mListView.setAdapter(adapter);
//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                int count = adapter.getCount();
//                mLoadMoreView.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
//                if (mEmptyView != null) {
//                    mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
//                }
//            }
//        });
    }

    private void setLoadMoreStatus(LoadMoreStatus status) {
        mLoadMoreStatus = status;
        if (mLoadMoreView != null) {
            if (mLoadMoreStatus == LoadMoreStatus.LOADED_ALL) {
                mLoadMoreView.setText("");
            } else if (mLoadMoreStatus == LoadMoreStatus.LOADING) {
                mLoadMoreView.setText("");
            } else {
                mLoadMoreView.setText("");
            }
        }
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mOnLoadListener = listener;
    }

    public void finishLoad(boolean loadAll) {
        super.setRefreshing(false);
        setLoadMoreStatus(loadAll ? LoadMoreStatus.LOADED_ALL : LoadMoreStatus.CLICK_TO_LOAD);
    }

}
