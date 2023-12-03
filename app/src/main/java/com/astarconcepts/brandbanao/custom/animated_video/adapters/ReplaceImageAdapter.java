package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity;
import com.astarconcepts.brandbanao.custom.animated_video.model.SelectImageModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReplaceImageAdapter extends RecyclerView.Adapter<ReplaceImageAdapter.MyViewHolder> {

    Context context;
    ArrayList<SelectImageModel> replaceImageList;
    int selected_Replce_pos;

    public ReplaceImageAdapter(Context context, ArrayList<SelectImageModel> replaceImageList, int selected_Replce_pos) {
        this.context = context;
        this.replaceImageList = replaceImageList;
        this.selected_Replce_pos = selected_Replce_pos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.replace_image_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SelectImageModel selectImageModel = replaceImageList.get(position);

        if (selectImageModel.isUpdated()) {

            holder.selected_images_iv.setAlpha(1.0f);
            holder.plus.setVisibility(View.GONE);

        } else {
            holder.selected_images_iv.setAlpha(0.5f);
            holder.plus.setVisibility(View.VISIBLE);
        }


        if (SelectImageActivity.selectedReplcePos == position) {

            holder.selected_iv_corner.setVisibility(View.VISIBLE);

        } else {
            holder.selected_iv_corner.setVisibility(View.GONE);
        }


        Glide.with(context).load(replaceImageList.get(position).getPath()).into(holder.selected_images_iv);

        holder.itemView.setOnClickListener(view -> {
            SelectImageActivity.selectedReplcePos = position;
            notifyDataSetChanged();
        });

    }

    public void updateImage(int position,String path) {

        replaceImageList.get(position).setUpdated(true);
        replaceImageList.get(position).setPath(path);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return replaceImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView selected_iv_corner, selected_images_iv, plus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            selected_images_iv = itemView.findViewById(R.id.selected_images_iv);
            selected_iv_corner = itemView.findViewById(R.id.selected_iv_corner);
            plus = itemView.findViewById(R.id.plus);
        }
    }


}
