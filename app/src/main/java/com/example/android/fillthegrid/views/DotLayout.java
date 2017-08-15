package com.example.android.fillthegrid.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import org.malcdevelop.cyclicview.CyclicView;

/**
 * Created by Reggie on 12/08/2017.
 * Custom {@link TabLayout} class which will allow for use with {@link org.malcdevelop.cyclicview.CyclicView}
 */

public class DotLayout extends TabLayout {

    CyclicView cyclicView;

    public DotLayout(Context context) {
        super(context);
    }

    public void setupWithCyclicViewPager(@Nullable CyclicView cyclicView) {
        setupWithCyclicViewPager(cyclicView, true);
    }

    public void setupWithCyclicViewPager(@Nullable CyclicView cyclicView, boolean autoRefresh) {
        setupWithCyclicViewPager(cyclicView, autoRefresh, false);
    }

    private void setupWithCyclicViewPager(@Nullable final CyclicView cyclicView, boolean autoRefresh,
                                          boolean implicitSetup) {
        /*if(cyclicView != null){
            // If we've already been setup with a CyclicView, remove us from it
            if (super.mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
            if (mAdapterChangeListener != null) {
                mViewPager.removeOnAdapterChangeListener(mAdapterChangeListener);
            }
        }
        }*/
    }
}
