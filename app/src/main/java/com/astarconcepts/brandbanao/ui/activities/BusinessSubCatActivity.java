package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.utils.Constant.BUSINESS_SUB;
import static com.astarconcepts.brandbanao.utils.Constant.INTENT_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.adapters.FeatureAdapter;
import com.astarconcepts.brandbanao.adapters.SubCategoryAdapter;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.databinding.ActivityBusinessSubCatBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.HomeItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessSubCatActivity extends AppCompatActivity implements ClickListener<CategoryItem> {
    ActivityBusinessSubCatBinding binding;
    SubCategoryAdapter subCategoryAdapter;
    String catName;
    String catID;
    PreferenceManager preferenceManager;
    String festtype;
    private HomeItem homeItems;
    private FeatureAdapter featureAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessSubCatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        preferenceManager = new PreferenceManager(this);
        binding.loadingView.setVisibility(View.VISIBLE);

        new AdsUtils(this).showBannerAds(this);

        Intent intent = getIntent();
        catName = intent.getStringExtra(Constant.INTENT_FEST_NAME);
        catID = intent.getStringExtra(Constant.INTENT_FEST_ID);
        festtype = intent.getStringExtra(INTENT_TYPE);

        getData();
        setUiViews();

    }

    private void setUiViews() {

        binding.toolbar.toolName.setText(catName);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        subCategoryAdapter = new SubCategoryAdapter(this, this, false);
        binding.rvSubCat.setAdapter(subCategoryAdapter);

        featureAdapter = new FeatureAdapter(this, "Custom");
        binding.rvPost.setAdapter(featureAdapter);
        binding.rvPost.setNestedScrollingEnabled(false);
        binding.rvPost.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                // no need to code
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                // no need to code
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });


    }

    private void getData() {

        Constant.getHomeViewModel(this).getBusinessSubCategory("business_sub_category", catID).observe(this, categoryItems -> {
            if (categoryItems != null) {
                binding.swipeRefresh.setRefreshing(false);
                subCategoryAdapter.setCategories(categoryItems);
                binding.llNotFound.setVisibility(View.GONE);
                binding.loadingView.setVisibility(View.GONE);

            } else {
                binding.llNotFound.setVisibility(View.VISIBLE);
                binding.loadingView.setVisibility(View.GONE);
            }
        });

        Constant.getHomeViewModel(this).getSubBusinessCatHome(catID).observe(this, homeItem -> {
            if (homeItem != null) {
                binding.swipeRefresh.setRefreshing(false);
                featureAdapter.setFeatureItemList(homeItems.featureItemList);
            }
        });

    }

    @Override
    public void onClick(CategoryItem data) {

        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, BUSINESS_SUB);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, data.video);
        startActivity(intent);
    }
}