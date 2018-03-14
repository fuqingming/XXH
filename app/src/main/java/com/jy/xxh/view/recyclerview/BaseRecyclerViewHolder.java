package com.jy.xxh.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HH
 * Date: 2017/11/13
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public View getView() {
        return mView;
    }
}
