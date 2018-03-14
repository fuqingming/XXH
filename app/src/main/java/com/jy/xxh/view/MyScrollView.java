package com.jy.xxh.view;

/**
 * Created by asus on 2018/2/8.
 */

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**

 * Created by baiyuliang on 2016-5-5.

 */

public class MyScrollView extends NestedScrollView {

    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context) {

        super(context);

    }

    public MyScrollView(Context context, AttributeSet attrs,

                        int defStyle) {

        super(context, attrs, defStyle);

    }

    public MyScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {

        this.scrollViewListener = scrollViewListener;

    }

    @Override

    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        super.onScrollChanged(x, y, oldx, oldy);

        if (scrollViewListener != null) {

            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);

        }

    }

    public interface ScrollViewListener {

        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

    }

}
