package com.astarconcepts.brandbanao.ui.activities;

import static android.view.View.GONE;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.ServicesAdapter;
import com.astarconcepts.brandbanao.adapters.SubsPlanAdapter;
import com.astarconcepts.brandbanao.databinding.ActivityServicesBinding;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.Util;


public class ServicesActivity extends AppCompatActivity {

    ActivityServicesBinding binding;
    ServicesAdapter servicesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();

        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.toolName.setText(getString(R.string.services));


        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);



        new AdsUtils(this).showBannerAds(this);

        binding.refreshLayout.setOnRefreshListener(() -> {

            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 100);

            binding.refreshLayout.setRefreshing(false);

           // binding.progreee.setVisibility(View.VISIBLE);

            binding.shimmerViewContainer.startShimmer();
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);

        });
    }


    public void getData() {

        Constant.getHomeViewModel(this).getAllService().observe(this, serviceItems -> {


            if (serviceItems != null) {

                servicesAdapter = new ServicesAdapter(this, item -> {

                });

                servicesAdapter.setServiceItemList(serviceItems);
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(GONE);

                binding.allList.setAdapter(servicesAdapter);

                Toast.makeText(this, "data "+serviceItems.size(), Toast.LENGTH_SHORT).show();
                binding.allList.setVisibility(View.VISIBLE);
                binding.noData.setVisibility(GONE);
                binding.progreee.setVisibility(GONE);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(GONE);

            }

        });

    }


}