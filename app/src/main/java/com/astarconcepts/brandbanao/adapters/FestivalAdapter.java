package com.astarconcepts.brandbanao.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ItemFestivalBinding;
import com.astarconcepts.brandbanao.databinding.ItemHomeFestivalBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.FestivalItem;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.List;

public class FestivalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Activity context;
    public List<FestivalItem> festivalItemList;
    public ClickListener<FestivalItem> listener;
    public boolean isHome;
    PreferenceManager preferenceManager;
    private int itemWidth = 0;
    private InterstitialsAdsManager interstitialsAdsManager;

    public FestivalAdapter(Activity context, ClickListener<FestivalItem> listener, boolean isHome) {
        this.context = context;
        this.listener = listener;
        this.isHome = isHome;
        itemWidth = MyApplication.getColumnWidth(2, context.getResources().getDimension(R.dimen._4sdp));
        preferenceManager = new PreferenceManager(context);

        interstitialsAdsManager = new InterstitialsAdsManager(context);

    }


    public void setFestData(List<FestivalItem> festivalItems) {
        this.festivalItemList = festivalItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHome) {
            ItemHomeFestivalBinding binding = ItemHomeFestivalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        } else {
            ItemFestivalBinding binding1 = ItemFestivalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder2(binding1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).binding.setFestivalData(festivalItemList.get(position));
            holder.itemView.setOnClickListener(v -> listener.onClick(festivalItemList.get(position)));
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ((MyViewHolder2) holder).binding.cardView.getLayoutParams();
            params.width = itemWidth;
            params.height = (int) (itemWidth * 0.7f);
            ((MyViewHolder2) holder).binding.cardView.setLayoutParams(params);
            ((MyViewHolder2) holder).binding.setFestivalData(festivalItemList.get(position));

            holder.itemView.setOnClickListener(v -> interstitialsAdsManager.showInterstitialAd(() -> listener.onClick(festivalItemList.get(position))));

        }
    }

    @Override
    public int getItemCount() {
        if (festivalItemList != null && festivalItemList.size() > 0) {
            return festivalItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemHomeFestivalBinding binding;

        public MyViewHolder(@NonNull ItemHomeFestivalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        ItemFestivalBinding binding;

        public MyViewHolder2(@NonNull ItemFestivalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
