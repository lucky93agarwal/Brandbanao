package com.astarconcepts.brandbanao.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.BusinessListAdapter;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.databinding.FragmentMyBussinessBottomSheetBinding;
import com.astarconcepts.brandbanao.listener.DismisListner;
import com.astarconcepts.brandbanao.model.BusinessItem;

import com.astarconcepts.brandbanao.ui.activities.AddBusinessActivity;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBusinessFragmentBottomSheet extends BottomSheetDialogFragment {

    PreferenceManager preferenceManager;
    UniversalDialog universalDialog;
    private BusinessListAdapter getBusinessAdapter;
    private ArrayList<BusinessItem> businessItemArrayList;
    private StaggeredGridLayoutManager recyclerViewLayoutManager;

    FragmentMyBussinessBottomSheetBinding binding;
    Activity context;
    DismisListner dismisListner;

    public MyBusinessFragmentBottomSheet() {
    }

    public MyBusinessFragmentBottomSheet(DismisListner dismisListner) {
        this.dismisListner = dismisListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyBussinessBottomSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();


        preferenceManager = new PreferenceManager(context);

        universalDialog = new UniversalDialog(context, false);

        businessItemArrayList = new ArrayList<>();

        int businessLimit = preferenceManager.getInt(Constant.USER_BUSINESS_LIMIT);

        loadBusinessData();

        binding.titleTv.setText("Businesses");

        binding.addBusinessProfile.setOnClickListener(v -> {

            if (businessLimit > businessItemArrayList.size()) {
                Intent intent = new Intent(context, AddBusinessActivity.class);
                intent.putExtra("Action", "Insert");

                dismiss();

                startActivity(intent);
            } else {
                universalDialog.showWarningDialog(getString(R.string.upgrade), getString(R.string.your_business_limit),
                        getString(R.string.upgrade), true);

                universalDialog.setCustomAnimationResource("premium_anim.json");
                universalDialog.show();
                universalDialog.okBtn.setOnClickListener(view1-> {
                    universalDialog.cancel();
                    startActivity(new Intent(context, SubsPlanActivity.class));
                });

                universalDialog.cancelBtn.setOnClickListener(view1 -> universalDialog.cancel());
            }
        });


    }


    private void loadBusinessData() {

        binding.shimmerLay.setVisibility(View.VISIBLE);
       binding.addBusinessProfile.setVisibility(View.GONE);
        MyUtils.showResponse("user", preferenceManager.getString(Constant.USER_ID));

        ApiClient.getApiDataService().getBusinessList(preferenceManager.getString(Constant.USER_ID), "business_list").enqueue
                (new Callback<List<BusinessItem>>() {
                    @Override
                    public void onResponse(Call<List<BusinessItem>> call, Response<List<BusinessItem>> response) {

                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {

                                businessItemArrayList.addAll(response.body());
                                recyclerViewLayoutManager = new StaggeredGridLayoutManager(1, 1);
                                binding.recyclerView.setLayoutManager(recyclerViewLayoutManager);


                                getBusinessAdapter = new BusinessListAdapter(context, businessItemArrayList, (view, i) -> {

                                    if (dismisListner != null){
                                        dismisListner.onDismis(response.body().get(i));
                                    }

                                });


                                binding.recyclerView.setAdapter(getBusinessAdapter);
                                getBusinessAdapter.notifyDataSetChanged();
                                binding.shimmerLay.setVisibility(View.GONE);
                                binding.llNotFound.setVisibility(View.GONE);
                            } else {


                            binding.llNotFound.setVisibility(View.VISIBLE);
                            binding.shimmerLay.setVisibility(View.GONE);

                        }

                        binding.addBusinessProfile.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<List<BusinessItem>> call, Throwable t) {
                        universalDialog.dissmissLoadingDialog();

                        binding.llNotFound.setVisibility(View.VISIBLE);
                        binding.shimmerLay.setVisibility(View.GONE);

                        binding.addBusinessProfile.setVisibility(View.VISIBLE);

                        Toast.makeText(context, "Failed"+ t, Toast.LENGTH_SHORT).show();


                    }
                });
    }
}