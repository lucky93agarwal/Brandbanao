package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.adapter.BackgroundAdapter;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListener;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.listener.RecyclerViewLoadMoreScroll;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.utils.GridSpacingItemDecoration;
import com.astarconcepts.brandbanao.utils.YourDataProvider;
import com.stepstone.apprating.CKt;

import java.util.ArrayList;


public class BackgroundFragment1 extends Fragment {

    public RecyclerViewLoadMoreScroll scrollListener;
    BackgroundAdapter backgroundAdapter;
    ArrayList<BackgroundImage> category_list;
    GetSnapListener onGetSnap;
    RecyclerView recyclerView;
    float screenHeight;
    float screenWidth;
    YourDataProvider yourDataProvider;

    public static BackgroundFragment1 newInstance(ArrayList<BackgroundImage> arrayList) {
        BackgroundFragment1 backgroundFragment1 = new BackgroundFragment1();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CKt.DIALOG_DATA, arrayList);
        backgroundFragment1.setArguments(bundle);
        return backgroundFragment1;
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

        this.category_list = getArguments().getParcelableArrayList(CKt.DIALOG_DATA);

        setCategory();

        this.backgroundAdapter.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> BackgroundFragment1.this.onGetSnap.onSnapFilter(num.intValue(), 1001, str));

        return inflate;
    }

    private void setCategory() {
        this.yourDataProvider = new YourDataProvider();
        this.yourDataProvider.setBackgroundList(this.category_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 40, true));
        this.backgroundAdapter = new BackgroundAdapter(getActivity(), this.yourDataProvider.getLoadMoreItems());
        this.recyclerView.setAdapter(this.backgroundAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                int itemViewType = BackgroundFragment1.this.backgroundAdapter.getItemViewType(i);
                if (itemViewType != 0) {
                    return itemViewType != 1 ? -1 : 3;
                }
                return 1;
            }
        });
        this.scrollListener = new RecyclerViewLoadMoreScroll(gridLayoutManager);
        this.scrollListener.setOnLoadMoreListener(() -> BackgroundFragment1.this.LoadMoreData());
        this.recyclerView.addOnScrollListener(this.scrollListener);
    }


    public void LoadMoreData() {
        this.backgroundAdapter.addLoadingView();
        new Handler().postDelayed(() -> {
            BackgroundFragment1.this.backgroundAdapter.removeLoadingView();
            BackgroundFragment1.this.backgroundAdapter.addData();
            BackgroundFragment1.this.backgroundAdapter.notifyDataSetChanged();
            BackgroundFragment1.this.scrollListener.setLoaded();
        }, 3000);
    }
}
