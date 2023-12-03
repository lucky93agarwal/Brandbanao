package com.astarconcepts.brandbanao.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.databinding.ItemAlbumBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.DigitalCardModel;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.List;

public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<DigitalCardModel> listener;
    public List<DigitalCardModel> digitalCardModels;

    public BusinessCardAdapter(Context context, ClickListener<DigitalCardModel> listener) {
        this.context = context;
        this.listener = listener;

    }

    public void setBusinessCard(List<DigitalCardModel> categories) {
        this.digitalCardModels = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumBinding binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setDigitalCard(digitalCardModels.get(position));
        holder.itemView.setOnClickListener(v -> listener.onClick(digitalCardModels.get(position)));

    }


    @Override
    public int getItemCount() {

        return digitalCardModels.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAlbumBinding binding;

        public MyViewHolder(@NonNull ItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
