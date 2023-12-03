package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelImages;
import com.astarconcepts.brandbanao.custom.animated_video.model.SelectImageModel;
import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.MyViewHolder> {
    Activity context;
    ArrayList<ModelImages> alImages;
    ArrayList<SelectImageModel> selectImageModels;
    int width_Ratio = 0;
    int height_Ratio = 0;

    public GalleryImageAdapter(Activity context, ArrayList<ModelImages> alImages, ArrayList<SelectImageModel> selectImageModels) {
        this.context = context;
        this.alImages = alImages;
        this.selectImageModels = selectImageModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new MyViewHolder(view);
    }

    int gcd(int p, int q) {
        if (q == 0) {
            return p;
        } else {
            return gcd(q, p % q);
        }
    }

    void ratio(int a, int b) {
        final int gcd = gcd(a, b);
        showAnswer(a / gcd, b / gcd);
    }

    void showAnswer(int a, int b) {
        width_Ratio = a;
        height_Ratio = b;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load("file://" + alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().get(position))
                .into(holder.galleryItemIv);

        holder.itemView.setOnClickListener(v -> {

            ratio(selectImageModels.get(SelectImageActivity.selectedReplcePos).getWidth(), selectImageModels.get(SelectImageActivity.selectedReplcePos).getHeight());

            beginCrop(Uri.parse("file://" + alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().get(position)),
                    width_Ratio,
                    height_Ratio);

        });

    }

    @Override
    public int getItemCount() {
        return alImages.get(SelectImageActivity.selectedAlbumPosition).getAlImagepath().size();
    }

    private void beginCrop(Uri uri, int WIDTH_RATIO, int HEIGHT_RATIO) {
        if (uri != null) {

            Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), new File(uri.getPath()).getName()));

            UCrop.Options options = new UCrop.Options();

            if (MyUtils.getFileExtension(uri.getPath()).equalsIgnoreCase("png")) {

                options.setCompressionFormat(Bitmap.CompressFormat.PNG);

            } else {
                options.setCompressionQuality(90);
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            }

            UCrop.of(uri, destinationUri)
                    .withOptions(options)
                    .withAspectRatio(WIDTH_RATIO, HEIGHT_RATIO)
                    .start(context);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryItemIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryItemIv = itemView.findViewById(R.id.gallery_item_iv);
        }
    }

}
