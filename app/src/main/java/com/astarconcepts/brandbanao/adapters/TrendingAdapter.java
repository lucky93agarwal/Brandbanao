package com.astarconcepts.brandbanao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ItemPostBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.utils.MyUtils;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {

    public Context context;
    public List<PostItem> postItemList;
    ClickListener<PostItem> listener;
    int newPosition = 0;
    private int itemWidth;

    public TrendingAdapter(Context context, ClickListener<PostItem> listener) {
        this.context = context;
        this.listener = listener;
        itemWidth = MyApplication.getColumnWidth(2, context.getResources().getDimension(R.dimen._15sdp));
    }

    public void setTrending(List<PostItem> postItemList) {
        this.postItemList = postItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setPostdata(postItemList.get(position));


        MyUtils.showResponse(postItemList.get(position));


      //  ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
     //   params.width = itemWidth;
      //  params.height = itemWidth;

     ///   holder.binding.cvBase.setLayoutParams(params);

        holder.itemView.setOnClickListener(v -> {

            newPosition = position;
            listener.onClick(postItemList.get(position));

        });
    }


    public int getItemPosition() {

        return newPosition;

    }

    @Override
    public int getItemCount() {
        if (postItemList != null && !postItemList.isEmpty()) {
            return postItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        public MyViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
