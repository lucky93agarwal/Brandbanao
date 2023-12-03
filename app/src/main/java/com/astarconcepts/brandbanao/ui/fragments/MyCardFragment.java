package com.astarconcepts.brandbanao.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.BusinessCardAdapter;
import com.astarconcepts.brandbanao.databinding.FragmentDigitalBinding;
import com.astarconcepts.brandbanao.ui.activities.PreviewDigitalCardActivity;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;

public class MyCardFragment extends Fragment {
    FragmentDigitalBinding binding;

    Activity context;
    StaggeredGridLayoutManager mLinearLayoutManager;
    BusinessCardAdapter businessCardAdapter;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDigitalBinding.inflate(getLayoutInflater());

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        shimmerLayoutManage(View.VISIBLE);

        preferenceManager = new PreferenceManager(context);

        binding.llAddCard.setOnClickListener(v -> {

            ViewPager viewPager = context.findViewById(R.id.viewpager);
            viewPager.setCurrentItem(1);

        });


        mLinearLayoutManager = new StaggeredGridLayoutManager(2, 1);


        binding.rvCard.setLayoutManager(this.mLinearLayoutManager);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            businessCardAdapter.digitalCardModels.clear();
            businessCardAdapter.notifyDataSetChanged();
            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 100);
            shimmerLayoutManage(View.VISIBLE);
        });

        getData();

    }

    private void getData() {

        Constant.getHomeViewModel(this).getMybusinesslist(preferenceManager.getString(Constant.USER_ID)).observe(getViewLifecycleOwner(), digitalCardModels -> {
            if (digitalCardModels != null) {

                binding.swipeRefresh.setRefreshing(false);


                if (!digitalCardModels.isEmpty()) {

                    binding.llAddCard.setVisibility(View.GONE);

                    businessCardAdapter = new BusinessCardAdapter(context, data -> startActivity(new Intent(context, PreviewDigitalCardActivity.class)
                            .putExtra("url", data.getCard())));

                    businessCardAdapter.setBusinessCard(digitalCardModels);
                    binding.rvCard.setAdapter(businessCardAdapter);

                }else {

                    binding.llAddCard.setVisibility(View.VISIBLE);

                }

                binding.llNotFound.setVisibility(View.GONE);
                shimmerLayoutManage(View.GONE);
                binding.shimmerViewContainer.stopShimmer();

            }
        });


    }

    private void shimmerLayoutManage(int visible) {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(visible);
    }

}