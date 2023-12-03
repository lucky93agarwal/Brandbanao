package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.utils.Constant.CATEGORY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.CategoryAdapter;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.databinding.ActivityCategoryBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements ClickListener<CategoryItem> {

    ActivityCategoryBinding binding;
    CategoryAdapter categoryAdapter;
    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        new AdsUtils(this).showBannerAds(this);
        setUiViews();
        getData();
    }

    private void setUiViews() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_category));

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        categoryAdapter = new CategoryAdapter(this, this, false);
        binding.rvCategory.setAdapter(categoryAdapter);

    }

    private void getData() {

        Constant.getHomeViewModel(this).getCategories("categories").observe(this, categoryItems -> {
            binding.swipeRefresh.setRefreshing(false);
            if (categoryItems != null) {
                categoryAdapter.setCategories(categoryItems);
                binding.animationView.setVisibility(View.GONE);
            } else {
                binding.animationView.setVisibility(View.VISIBLE);
            }

        });


    }

    @Override
    public void onClick(CategoryItem data) {

        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, CATEGORY);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, data.video);
        startActivity(intent);

    }

}