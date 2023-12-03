package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.utils.Constant.BUSINESS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.CategoryAdapter;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.CategorySectionAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.VideoCategoryAdapter;
import com.astarconcepts.brandbanao.databinding.ActivityBusinessCategoryBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.video.DashboardResponseModel;
import com.astarconcepts.brandbanao.model.video.DashboardTemplateResponseMain;
import com.astarconcepts.brandbanao.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCategoryActivity extends AppCompatActivity implements ClickListener<CategoryItem> {

    ActivityBusinessCategoryBinding binding;
    CategoryAdapter adapter;
    private VideoCategoryAdapter videoCategoryAdapter;

    Activity activity;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;


        setUpUi();

        new AdsUtils(this).showBannerAds(activity);

    }

    private void setUpUi() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.business_category));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        adapter = new CategoryAdapter(this, this, false);
        binding.rvCustomCategory.setAdapter(adapter);
        loadData();
    }

    private void loadData() {

        Constant.getTemplateBasedViewModel(this).getDashboardTemplate(page).observe(this, dashboardTemplateResponseMain -> {

            if (dashboardTemplateResponseMain != null){

                videoCategoryAdapter = new VideoCategoryAdapter(activity, dashboardTemplateResponseMain.getData());

                binding.rvCustomCategory.setAdapter(videoCategoryAdapter);

            }

        });

    }

    @Override
    public void onClick(CategoryItem data) {
        Intent intent = new Intent(this, BusinessSubCatActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, BUSINESS);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, data.video);
        startActivity(intent);
    }

}