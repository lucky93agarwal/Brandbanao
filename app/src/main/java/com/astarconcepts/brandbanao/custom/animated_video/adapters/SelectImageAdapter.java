package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModelImages> allMenu;
    GalleryImageAdapter galleryImageAdapter;
    OneByOneGalleryImageAdapter oneByOneGalleryImageAdapter;
    DrawerLayout drawerLayout;
    TextView selectedFolderName;

    public SelectImageAdapter(Context context, ArrayList<ModelImages> alMenu, GalleryImageAdapter galleryImageAdapter, DrawerLayout drawerLayout, TextView selectedFoldername) {
        this.context = context;
        this.allMenu = alMenu;
        this.galleryImageAdapter = galleryImageAdapter;
        this.drawerLayout = drawerLayout;
        this.selectedFolderName = selectedFoldername;

    }

    public SelectImageAdapter(Context context, ArrayList<ModelImages> alMenu, OneByOneGalleryImageAdapter oneByOneGalleryImageAdapter, DrawerLayout drawerLayout, TextView selectedFoldername) {
        this.context = context;
        this.allMenu = alMenu;
        this.oneByOneGalleryImageAdapter = oneByOneGalleryImageAdapter;
        this.drawerLayout = drawerLayout;
        this.selectedFolderName = selectedFoldername;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {

            selectedFolderName.setText(allMenu.get(SelectImageActivity.selectedAlbumPosition).getStr_folder() + " (" + allMenu.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().size() + ")");
            holder.albumName.setText(allMenu.get(position).getStr_folder());
            holder.albumCount.setText("(" + allMenu.get(position).getAlImagepath().size() + ")");
            Glide.with(context).load("file://" + allMenu.get(position).getAlImagepath().get(0)).into(holder.albumIv);
            holder.itemView.setOnClickListener(view -> {
                SelectImageActivity.selectedAlbumPosition = position;
                selectedFolderName.setText(allMenu.get(SelectImageActivity.selectedAlbumPosition).getStr_folder() + " (" + allMenu.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().size() + ")");

                if (galleryImageAdapter != null) {
                    galleryImageAdapter.notifyDataSetChanged();
                }

                if (oneByOneGalleryImageAdapter != null) {
                    oneByOneGalleryImageAdapter.notifyDataSetChanged();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return allMenu.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumIv;
        TextView albumCount;
        TextView albumName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name);
            albumCount = itemView.findViewById(R.id.album_count);
            albumIv = itemView.findViewById(R.id.album_iv);
        }
    }
}
