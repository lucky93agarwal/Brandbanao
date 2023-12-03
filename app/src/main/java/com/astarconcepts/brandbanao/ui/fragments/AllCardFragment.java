package com.astarconcepts.brandbanao.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.BusinessCardAdapter;
import com.astarconcepts.brandbanao.databinding.FragmentDigitalBinding;
import com.astarconcepts.brandbanao.ui.activities.DigitalCardActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

public class AllCardFragment extends Fragment {

    FragmentDigitalBinding binding;

    Activity context;
    StaggeredGridLayoutManager mLinearLayoutManager;
    BusinessCardAdapter businessCardtemAdapter;
    private PreferenceManager preferenceManager;

    private UniversalDialog dialog;
    private int cardId;

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

        preferenceManager = new PreferenceManager(context);

        mLinearLayoutManager = new StaggeredGridLayoutManager(2, 1);

        binding.rvCard.setLayoutManager(this.mLinearLayoutManager);

        shimmerLayoutManage(View.VISIBLE);
        addCardDialog();

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(true);
            businessCardtemAdapter.digitalCardModels.clear();
            businessCardtemAdapter.notifyDataSetChanged();
            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 100);
        });

        getData();

    }

    private void getData() {

        Constant.getHomeViewModel(this).getBusinessCards().observe(getViewLifecycleOwner(), digitalCardModels -> {

            if (digitalCardModels != null) {
                if (!digitalCardModels.isEmpty()) {

                    businessCardtemAdapter = new BusinessCardAdapter(context, data -> {


                        if (data.isPremium() && !new PreferenceManager(context).getBoolean(Constant.IS_SUBSCRIBE)) {

                            MyUtils.openPlanActivity(context);

                        } else {
                            dialog.show();
                        }

                        cardId = data.get_id();

                    });

                    businessCardtemAdapter.setBusinessCard(digitalCardModels);
                    binding.rvCard.setAdapter(businessCardtemAdapter);
                }

            }

            binding.swipeRefresh.setRefreshing(false);
            shimmerLayoutManage(View.GONE);
        });


    }


    private void shimmerLayoutManage(int visible) {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(visible);
    }

    public void addCardDialog() {


        dialog = new UniversalDialog(context, false);

        dialog.showConfirmDialog(getString(R.string.add_card), getString(R.string.confirm_to_add_card), getString(R.string.continue_ok), getString(R.string.cancel));

        dialog.cancelBtn.setOnClickListener(v -> dialog.cancel());

        dialog.okBtn.setOnClickListener(v -> addCard());

    }

    private void addCard() {

        Constant.getHomeViewModel(this).addBusinessCard(preferenceManager.getString(Constant.USER_ID), cardId).observe(getViewLifecycleOwner(), apiStatus -> {
            if (apiStatus != null) {


                DigitalCardActivity cardActivity = (DigitalCardActivity) context;
                Toast.makeText(context, "" + apiStatus.message, Toast.LENGTH_SHORT).show();

                dialog.cancel();
                ViewPager viewPager = context.findViewById(R.id.viewpager);
                viewPager.setCurrentItem(0);
                new Handler(Looper.getMainLooper()).post(cardActivity::businessAdded);

            }
        });

    }

}