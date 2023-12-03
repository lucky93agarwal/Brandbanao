package com.astarconcepts.brandbanao.custom.animated_video.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.fragment.MusicFragment;
import com.google.android.material.tabs.TabLayout;

public class SelectMusicActivity extends AppCompatActivity {

    Activity context;
    private TabLayout tabs;
    private ViewPager viewpager;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_select);
        initView();
        context = this;
        initViews();

        back.setOnClickListener(view -> onBackPressed());

        new AdsUtils(context).showBannerAds(context);

    }

    private void initViews() {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);

    }

    private void setupViewPager(ViewPager viewPager) {
        FavouriteActivity.ViewPagerAdapter adapter = new FavouriteActivity.ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(MusicFragment.newInstance(true), "For You");
        adapter.addFragment(MusicFragment.newInstance(false), "Device");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

    }

    private void initView() {
        tabs = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.viewpager);
        back = findViewById(R.id.back);
    }
}
