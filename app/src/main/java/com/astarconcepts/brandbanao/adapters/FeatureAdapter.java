package com.astarconcepts.brandbanao.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.databinding.ItemFeatureBinding;
import com.astarconcepts.brandbanao.model.FeatureItem;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.ui.activities.PreviewActivity;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.MyViewHolder> {

    public List<FeatureItem> featureItemList;
    Activity context;
    PreferenceManager preferenceManager;
    private String From;

    private InterstitialsAdsManager interstitialsAdsManager;

    public FeatureAdapter(Activity context, String From) {
        this.context = context;
        this.From = From;
        preferenceManager = new PreferenceManager(context);

        interstitialsAdsManager = new InterstitialsAdsManager(context);

    }


    public void setFeatureItemList(List<FeatureItem> featureItemList) {
        this.featureItemList = featureItemList;
        notifyDataSetChanged();


    }  public void addFeatureItems(List<FeatureItem> featureItemList) {
        this.featureItemList.addAll(featureItemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeatureBinding binding = ItemFeatureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position % 2 == 0) {
            holder.binding.mainConstraint.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.binding.txtViewTrending.setOnClickListener(v -> interstitialsAdsManager.showInterstitialAd(() -> goToPreviewActivityViewMore(position)));

        holder.adapters = new TrendingAdapter(context, data -> interstitialsAdsManager.showInterstitialAd(() -> gotoPreviewActivity(holder, data, position)));


        holder.binding.rvFeature.setAdapter(holder.adapters);
        holder.binding.tvFeature.setText(featureItemList.get(position).title);


        holder.adapters.setTrending(featureItemList.get(position).postItemList);


    }

    private void gotoPreviewActivity(MyViewHolder holder, PostItem data, int position) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, featureItemList.get(position).type);
        intent.putExtra(Constant.INTENT_FEST_ID, data.fest_id);
        intent.putExtra(Constant.INTENT_FEST_NAME, featureItemList.get(position).title);
        intent.putExtra(Constant.INTENT_POST_IMAGE, data.image_url);
        intent.putExtra(Constant.INTENT_POS, holder.adapters.getItemPosition());
        intent.putExtra(Constant.INTENT_VIDEO, featureItemList.get(position).video);
        intent.putExtra("From", From);
        context.startActivity(intent);


    }

    private void goToPreviewActivityViewMore(int position) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, featureItemList.get(position).type);
        intent.putExtra(Constant.INTENT_FEST_ID, featureItemList.get(position).festId);
        intent.putExtra(Constant.INTENT_FEST_NAME, featureItemList.get(position).title);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, featureItemList.get(position).video);
        intent.putExtra("From", From);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (featureItemList != null && featureItemList.size() > 0) {
            return featureItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFeatureBinding binding;
        TrendingAdapter adapters;

        public MyViewHolder(@NonNull ItemFeatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
