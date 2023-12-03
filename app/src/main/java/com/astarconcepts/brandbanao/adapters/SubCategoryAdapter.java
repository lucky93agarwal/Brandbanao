package com.astarconcepts.brandbanao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ItemBusinessSubCategoryBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.ui.activities.DailyPostActivity;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<CategoryItem> listener;
    public List<CategoryItem> categoryItemList;
    boolean isHome;

    int selectedPosision = 0;

    public SubCategoryAdapter(Context context, ClickListener<CategoryItem> listener, boolean isHome) {
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
        ItemBusinessSubCategoryBinding binding = ItemBusinessSubCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (categoryItemList != null) {

            holder.binding.setCategoryData(categoryItemList.get(position));

            if (selectedPosision == position) {

                if (context.getClass() == DailyPostActivity.class){

                    holder.binding.catCard.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_border));

                }else{

                    holder.binding.catCard.setBackground(ContextCompat.getDrawable(context, R.drawable.cat_bg));
                }



            } else {
                holder.binding.catCard.setBackground(ContextCompat.getDrawable(context, R.drawable.cat_bg));
            }


        }

        holder.itemView.setOnClickListener(v -> {

            listener.onClick(categoryItemList.get(position));

            selectedPosision = position;
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        if (categoryItemList != null && categoryItemList.size() > 0) {
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
        ItemBusinessSubCategoryBinding binding;

        public MyViewHolder(@NonNull ItemBusinessSubCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
