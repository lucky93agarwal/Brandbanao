package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.adapter.StickerAdapter;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListener;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.listener.RecyclerViewLoadMoreScroll;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.utils.GridSpacingItemDecoration;
import com.astarconcepts.brandbanao.utils.YourDataProvider;
import com.stepstone.apprating.CKt;

import java.util.ArrayList;


public class StickerFragmentMore extends Fragment {

    ArrayList<BackgroundImage> categoryList;
    GetSnapListener onGetSnap;
    RecyclerView recyclerView;
    float screenHeight;
    float screenWidth;

    RecyclerViewLoadMoreScroll scrollListener;
    StickerAdapter stickerAdapter;
    YourDataProvider yourDataProvider;

    public static StickerFragmentMore newInstance(ArrayList<BackgroundImage> arrayList) {
        StickerFragmentMore stickerFragmentMore = new StickerFragmentMore();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CKt.DIALOG_DATA, arrayList);
        stickerFragmentMore.setArguments(bundle);
        return stickerFragmentMore;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.main_fragment, viewGroup, false);
        this.recyclerView = inflate.findViewById(R.id.overlay_artwork);
        this.onGetSnap = (GetSnapListener) getActivity();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) displayMetrics.heightPixels;

        this.categoryList = getArguments().getParcelableArrayList(CKt.DIALOG_DATA);
        setCategory();
        return inflate;
    }

    private void setCategory() {
        this.yourDataProvider = new YourDataProvider();
        this.yourDataProvider.setStickerList(this.categoryList);
        this.stickerAdapter = new StickerAdapter(getActivity(), this.yourDataProvider.getLoadMoreStickerItems(), getResources().getDimensionPixelSize(R.dimen.logo_image_size), getResources().getDimensionPixelSize(R.dimen.image_padding));

        Log.i("DATA", "DATA-->" + this.yourDataProvider.getLoadMoreStickerItems().size());

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 40, true));
        this.recyclerView.setAdapter(this.stickerAdapter);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                int itemViewType = StickerFragmentMore.this.stickerAdapter.getItemViewType(i);
                if (itemViewType != 0) {
                    return itemViewType != 1 ? -1 : 3;
                }
                return 1;
            }
        });
        this.scrollListener = new RecyclerViewLoadMoreScroll(mLayoutManager);
        this.scrollListener.setOnLoadMoreListener(() -> StickerFragmentMore.this.LoadMoreData());
        this.recyclerView.addOnScrollListener(this.scrollListener);
        this.stickerAdapter.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> StickerFragmentMore.this.onGetSnap.onSnapFilter(num.intValue(), 34, str));
    }


    public void LoadMoreData() {
        this.stickerAdapter.addLoadingView();
        new Handler().postDelayed(() -> {
            StickerFragmentMore.this.stickerAdapter.removeLoadingView();
            StickerFragmentMore.this.stickerAdapter.addData(StickerFragmentMore.this.yourDataProvider.getLoadMoreStickerItemsS());
            StickerFragmentMore.this.stickerAdapter.notifyDataSetChanged();
            StickerFragmentMore.this.scrollListener.setLoaded();
        }, 3000);
    }


}
