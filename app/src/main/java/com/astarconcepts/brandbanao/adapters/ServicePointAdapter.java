package com.astarconcepts.brandbanao.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.databinding.ItemServicePointBinding;
import com.astarconcepts.brandbanao.databinding.ItemSubPointBinding;

import java.util.List;

public class ServicePointAdapter extends RecyclerView.Adapter<ServicePointAdapter.MyViewHolder> {

    List<String> subsPointItemList;

    public ServicePointAdapter(List<String> subsPointItemList) {
        this.subsPointItemList = subsPointItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicePointBinding binding = ItemServicePointBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setPointdata(subsPointItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return subsPointItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemServicePointBinding binding;

        public MyViewHolder(ItemServicePointBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
