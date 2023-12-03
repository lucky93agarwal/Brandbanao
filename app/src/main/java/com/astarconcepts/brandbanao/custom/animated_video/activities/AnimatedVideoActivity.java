package com.astarconcepts.brandbanao.custom.animated_video.activities;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.CategorySectionAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.VideoCategoryAdapter;
import com.astarconcepts.brandbanao.databinding.ActivityAnimatedVideoBinding;
import com.astarconcepts.brandbanao.model.video.DashboardResponseModel;
import com.astarconcepts.brandbanao.model.video.DashboardTemplateResponseMain;
import com.astarconcepts.brandbanao.utils.PaginationListener;
import com.astarconcepts.brandbanao.viewmodel.TemplateBasedViewModel;

import java.util.ArrayList;


public class AnimatedVideoActivity extends AppCompatActivity {

    ActivityAnimatedVideoBinding binding;
    Activity context;
    private CategorySectionAdapter categorySectionAdapter;
    private VideoCategoryAdapter videoCategoryAdapter;
    private int page = 1;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private ArrayList<DashboardResponseModel> dashboardResponseModels;
    TemplateBasedViewModel templateBasedViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimatedVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.mainContainer.setVisibility(View.GONE);
        dashboardResponseModels = new ArrayList<>();

        layoutManager = new LinearLayoutManager(context);

        templateBasedViewModel = new ViewModelProvider(this).get(TemplateBasedViewModel.class);

        binding.recyclerview.setLayoutManager(layoutManager);

        binding.toolbar.toolName.setText(getString(R.string.animated_videos));

        binding.toolbar.back.setOnClickListener(v -> AnimatedVideoActivity.this.onBackPressed());

        binding.recyclerview.addOnScrollListener(new PaginationListener(layoutManager) {
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

                isLoading = true;

                page = page + 1;

                new Handler(getMainLooper()).postDelayed(AnimatedVideoActivity.this::loadDataMore, 100);

            }
        });

        loadData();

        binding.refreshLayout.setOnRefreshListener(() -> {

            page = 1;

            isLoading = false;

            if (categorySectionAdapter != null) categorySectionAdapter.clearData();

            new Handler(AnimatedVideoActivity.this.getMainLooper()).postDelayed(AnimatedVideoActivity.this::loadData, 100);

        });

    }

    private void loadData() {


        templateBasedViewModel.getDashboardTemplate(page).observe(this, dashboardTemplateResponseMain -> {

            if (dashboardTemplateResponseMain != null) {

                DashboardTemplateResponseMain templateResponse = dashboardTemplateResponseMain;

                dashboardResponseModels = templateResponse.getData();

                videoCategoryAdapter = new VideoCategoryAdapter(context, dashboardTemplateResponseMain.getData());
                binding.rvVideoCat.setAdapter(videoCategoryAdapter);


                categorySectionAdapter = new CategorySectionAdapter(context, dashboardResponseModels);

                binding.recyclerview.setAdapter(categorySectionAdapter);


            }
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.mainContainer.setVisibility(View.VISIBLE);
            binding.refreshLayout.setRefreshing(false);

        });


    }

    private void loadDataMore() {

        templateBasedViewModel.getDashboardTemplate(page).observe(this, dashboardTemplateResponseMain -> {

            if (dashboardTemplateResponseMain != null) {

                dashboardResponseModels = dashboardTemplateResponseMain.data;

                if (dashboardResponseModels != null && !dashboardResponseModels.isEmpty()) {

                    categorySectionAdapter.setData(dashboardResponseModels);

                }


            }

            isLoading = false;

            binding.refreshLayout.setRefreshing(false);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.mainContainer.setVisibility(View.VISIBLE);
        });

    }

}