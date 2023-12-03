package com.astarconcepts.brandbanao.custom.poster.fragment;

import static android.os.Looper.getMainLooper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.adapter.ThumbnailSnapAdapter;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailWithList;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PaginationListener;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PosterFragment extends Fragment {
    public static final String TAG = "PosterFragment";
    public PreferenceManager preferenceManager;
    Activity context;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView recyclerView;
    ThumbnailSnapAdapter snapAdapter;
    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;
    private NestedScrollView mainContainer;

    private int page = 1;
    private boolean isLoading = false;

    public PosterFragment() {

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        context = getActivity();
        return inflater.inflate(R.layout.activity_animated_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);

        this.preferenceManager = new PreferenceManager(context);

        this.mLinearLayoutManager = new LinearLayoutManager(context);

        this.recyclerView.setLayoutManager(this.mLinearLayoutManager);


        getPosterList();

        refreshLayout.setOnRefreshListener(() -> {

            if (snapAdapter != null) {
                snapAdapter.clearData();

            }


            new Handler(getMainLooper()).postDelayed(this::getPosterList, 100);

            snapAdapter.notifyDataSetChanged();

        });

        recyclerView.addOnScrollListener(new PaginationListener(mLinearLayoutManager) {
            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void loadMoreItems() {

                page = page + 1;

                isLoading = true;

                new Handler(getMainLooper()).postDelayed(PosterFragment.this::getPosterListMore, 100);


            }
        });


    }

    private void initView(View view) {
        this.recyclerView = view.findViewById(R.id.recyclerview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mainContainer = view.findViewById(R.id.main_container);
    }


    public void getPosterList() {

        Constant.getTemplateBasedViewModel(this).getPosterHome(page).observe(getViewLifecycleOwner(), thumbnailWithList -> {

            if (thumbnailWithList!= null) {

                snapAdapter = new ThumbnailSnapAdapter(context,thumbnailWithList.getData());
                recyclerView.setAdapter(snapAdapter);

                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);

            }

            refreshLayout.setRefreshing(false);
        });


    }

    public void getPosterListMore() {

        Constant.getTemplateBasedViewModel(this).getPosterHome(page).observe(getViewLifecycleOwner(), thumbnailWithList -> {

            if (thumbnailWithList != null) {


                snapAdapter.addData(thumbnailWithList.getData());

            }
            isLoading = false;
        });



    }


}
