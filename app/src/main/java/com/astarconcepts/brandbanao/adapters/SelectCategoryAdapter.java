package com.astarconcepts.brandbanao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.databinding.ItemSelectCategoryBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;

import java.util.List;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<CategoryItem> listener;
    public List<CategoryItem> categoryItemList;
    boolean isHome;

    public SelectCategoryAdapter(Context context, ClickListener<CategoryItem> listener, boolean isHome) {
        this.context = context;
        this.listener = listener;
        this.isHome = isHome;
    }

    public void setCategories(List<CategoryItem> categories) {
        this.categoryItemList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectCategoryBinding binding = ItemSelectCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setCategoryData(categoryItemList.get(position));
        int index = 0;
        for (int i = 0; i < position; i++) {
            index++;
            if (index == 10) {
                index = 0;
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(categoryItemList.get(position)));
    }

    @Override
    public int getItemCount() {
        if (categoryItemList != null && !categoryItemList.isEmpty()) {
            if (categoryItemList.size() > AppConfig.HOME_CATEGORY_SHOW && isHome) {
                return AppConfig.HOME_CATEGORY_SHOW;
            } else {
                return categoryItemList.size();
            }
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSelectCategoryBinding binding;

        public MyViewHolder(@NonNull ItemSelectCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
