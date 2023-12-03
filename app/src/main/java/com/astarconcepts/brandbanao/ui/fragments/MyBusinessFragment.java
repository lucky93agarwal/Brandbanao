package com.astarconcepts.brandbanao.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.adapters.FeatureAdapter;
import com.astarconcepts.brandbanao.adapters.SubCategoryAdapter;
import com.astarconcepts.brandbanao.databinding.FragmentMyBusinessBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.FeatureItem;
import com.astarconcepts.brandbanao.model.HomeItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.ui.activities.MyBusinessCategoryActivity;
import com.astarconcepts.brandbanao.ui.activities.PreviewActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyBusinessFragment extends Fragment implements ClickListener<CategoryItem> {

    public static final int REQUEST_CODE = 012;
    FragmentMyBusinessBinding binding;
    SubCategoryAdapter subCategoryAdapter;
    FeatureAdapter featureAdapter;
    List<CategoryItem> categoryItems;
    HomeItem homeItems;
    PreferenceManager preferenceManager;
    List<FeatureItem> featureItems;
    private MyBusinessFragmentBottomSheet bussinessBottomSheetBinding;

    public MyBusinessFragment() {
        // Required empty public constructor
    }

    UserItem userItem;
    BusinessItem businessItem;
    Activity context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyBusinessBinding.inflate(getLayoutInflater());


        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeItems = new HomeItem();
        categoryItems = new ArrayList<>();
        featureItems = new ArrayList<>();
        preferenceManager = new PreferenceManager(getContext());

        setUi();

        context = getActivity();

        binding.txtchange.setOnClickListener(view1 -> startActivityForResult(new Intent(getContext(), MyBusinessCategoryActivity.class), REQUEST_CODE));

        businessItem = Constant.getBusinessItem(context);
        userItem = Constant.getUserItem(context);

        if (preferenceManager.getBoolean(Constant.BUSINESS_IS_DEFAULT)) {

            getAllData(preferenceManager.getString(Constant.BUSINESS_CATEGORY_ID));
            binding.txtcustomname.setText(preferenceManager.getString(Constant.BUSINESS_CATEGORY_NAME));

        }

        if (businessItem == null) {

            bussinessBottomSheetBinding = new MyBusinessFragmentBottomSheet(businessItem1 -> {

                if (businessItem1 != null) {

                    bussinessBottomSheetBinding.dismiss();

                    Constant.setDefaultBusiness(context, businessItem1);

                    businessItem = Constant.getBusinessItem(context);

                    if (featureItems != null) {

                        featureItems.clear();
                        featureAdapter.notifyDataSetChanged();
                    }

                    if (categoryItems != null) {
                        categoryItems.clear();
                        subCategoryAdapter.notifyDataSetChanged();
                    }

                    getAllData(preferenceManager.getString(Constant.BUSINESS_CATEGORY_ID));
                    binding.txtcustomname.setText(preferenceManager.getString(Constant.BUSINESS_CATEGORY_NAME));

                }


            });

            bussinessBottomSheetBinding.show(getChildFragmentManager(), "");


        }


        editTextSearchBar();
    }

    private void editTextSearchBar() {
        binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {

            if (i == EditorInfo.IME_ACTION_SEARCH) {

                Toast.makeText(context, "category " + featureAdapter, Toast.LENGTH_SHORT).show();
                filter(binding.etSearch.getText().toString());
                Toast.makeText(context, "categoryssss " + featureAdapter, Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString() != null) {
                    filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //code
            }
        });
    }

    void filter(String text) {

        ArrayList<FeatureItem> temp = new ArrayList();

        for (FeatureItem d : featureItems) {

            if (d.title.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }

        featureAdapter.setFeatureItemList(temp);
    }

    private void setUi() {

        shimmerLayoutManage();

        subCategoryAdapter = new SubCategoryAdapter(getActivity(), this, false);
        binding.rvSubCat.setAdapter(subCategoryAdapter);

        featureAdapter = new FeatureAdapter(getActivity(), "Custom");
        binding.rvCustom.setAdapter(featureAdapter);
        binding.rvCustom.setNestedScrollingEnabled(false);
        binding.rvCustom.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {

            if (featureAdapter != null) {

                featureItems.clear();
                featureAdapter.notifyDataSetChanged();
            }

            if (subCategoryAdapter != null) {
                categoryItems.clear();
                subCategoryAdapter.notifyDataSetChanged();
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> getAllData(preferenceManager.getString(Constant.BUSINESS_CATEGORY_ID)), 2000);


        });

    }

    private void shimmerLayoutManage() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.mainContainer.setVisibility(View.GONE);
        binding.appBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == -1) {

            shimmerLayoutManage();
            changeBusinessCategory();



        }


    }

    private void changeBusinessCategory() {

        if (featureItems != null) {

        featureItems.clear();
        featureAdapter.notifyDataSetChanged();
    }

        if (categoryItems != null) {
            categoryItems.clear();
            subCategoryAdapter.notifyDataSetChanged();
        }

        String businessCategoryId = preferenceManager.getString(Constant.BUSINESS_CATEGORY_ID);

        UniversalDialog dialog = new UniversalDialog(context, false);

        dialog.showLoadingDialog(context, "Changing Business");

        Constant.getUserViewModel(this).submitBusiness(userItem.getUserId(), businessItem.getBusinessid(), null,
                businessItem.name,
                businessItem.email, businessItem.phone,
                businessItem.website, businessItem.address, businessItem.getSocial_instagram(), businessItem.social_youtube, businessItem.getSocial_facebook(), businessItem.getSocial_twitter(), businessItem.getTagline(), "update", businessCategoryId).observe(this, businessItem -> {

            if (businessItem != null) {

                Constant.setDefaultBusiness(context, businessItem);
                // Update Business category here
                binding.txtcustomname.setText(businessItem.getBusinessCategory());
                getAllData(businessItem.getCategoryId());
                dialog.dissmissLoadingDialog();

            }

        });
    }

    @Override
    public void onClick(CategoryItem data) {

        Intent intent = new Intent(getActivity(), PreviewActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, Constant.BUSINESS_SUB);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, data.video);
        startActivity(intent);

    }


    private void getAllData(String id) {

        Constant.getHomeViewModel(this).getBusinessSubCategory("business_sub_category", id).observe(getViewLifecycleOwner(), categoryItems -> {

            if (categoryItems != null) {

                Log.i("RESPONSE", "RESPONSE-->" + new Gson().toJson(categoryItems));

                subCategoryAdapter.setCategories(categoryItems);


            }

        });

        // get All Sub Categories + their Data(Posts)

        Constant.getHomeViewModel(this).getSubBusinessCatHome(id).observe(getViewLifecycleOwner(), homeItem -> {

            Log.i("RESPONSE BUSINESS", "RESPONSE BUSINESS SUB-->" + new Gson().toJson(homeItem));

            if (homeItem != null) {
                binding.swipeRefresh.setRefreshing(false);
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.mainContainer.setVisibility(View.VISIBLE);
                binding.appBar.setVisibility(View.VISIBLE);

                homeItems = homeItem;

                featureItems.addAll(homeItems.featureItemList);
                featureAdapter.setFeatureItemList(featureItems);

            }


        });

    }


}