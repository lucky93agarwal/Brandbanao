package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OneByOneGalleryImageAdapter extends RecyclerView.Adapter<OneByOneGalleryImageAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModelImages> alImages;

    public OneByOneGalleryImageAdapter(Context context, ArrayList<ModelImages> alImages) {
        this.context = context;
        this.alImages = alImages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load("file://" + alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().get(position))
                .into(holder.galleryItemIv);
        holder.itemView.setOnClickListener(v -> {

            ((Activity) context).setResult(Activity.RESULT_OK, new Intent().setData(Uri.parse("file://" + alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().get(position))));
            ((Activity) context).finish();
        });

    }

    @Override
    public int getItemCount() {
        return alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryItemIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryItemIv = itemView.findViewById(R.id.gallery_item_iv);
        }
    }

}
