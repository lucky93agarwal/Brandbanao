package com.astarconcepts.brandbanao.custom.poster.listener;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewLoadMoreScroll extends RecyclerView.OnScrollListener {
    private final RecyclerView.LayoutManager mLayoutManager;
    private boolean isLoading;
    private int lastVisibleItem;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 5;

    public RecyclerViewLoadMoreScroll(LinearLayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }

    public RecyclerViewLoadMoreScroll(GridLayoutManager gridLayoutManager) {
        this.mLayoutManager = gridLayoutManager;
        this.visibleThreshold *= gridLayoutManager.getSpanCount();
    }

    public void setLoaded() {
        this.isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int i, int i2) {
        super.onScrolled(recyclerView, i, i2);
        if (i2 > 0) {
            int totalItemCount = this.mLayoutManager.getItemCount();
            RecyclerView.LayoutManager layoutManager = this.mLayoutManager;
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                this.lastVisibleItem = getLastVisibleItem(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null));
            } else if (layoutManager instanceof GridLayoutManager) {
                this.lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                this.lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (!this.isLoading && totalItemCount <= this.lastVisibleItem + this.visibleThreshold) {
                OnLoadMoreListener onLoadMoreListener = this.mOnLoadMoreListener;
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
                this.isLoading = true;
            }
        }
    }

    public int getLastVisibleItem(int[] iArr) {
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (i2 == 0) {
                i = iArr[i2];
            } else if (iArr[i2] > i) {
                i = iArr[i2];
            }
        }
        return i;
    }
}
