package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageOneByOne;
import com.astarconcepts.brandbanao.custom.animated_video.activities.VideoEditorActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    Activity context;
    ArrayList<String> imageList;
    ArrayList<String> imageWidths;
    ArrayList<String> imageHeight;
    LottieAnimationView lottieAnimationView;
    int PICK_IMAGE = 1005;


    public ImageAdapter(Activity context, ArrayList<String> imageList, ArrayList<String> image_widths, ArrayList<String> imageHeight, LottieAnimationView lottieAnimationView) {
        this.context = context;
        this.imageList = imageList;
        this.imageWidths = image_widths;
        this.imageHeight = imageHeight;
        this.lottieAnimationView = lottieAnimationView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.img_list_adpter, parent, false);
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
        VideoEditorActivity.WIDTH_RATIO = a;
        VideoEditorActivity.HEIGHT_RATIO = b;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position)).into(holder.images);


        holder.itemView.setOnClickListener(v -> {

            ratio(Integer.parseInt(imageWidths.get(position)), Integer.parseInt(imageHeight.get(position)));

            VideoEditorActivity.position = position;

            Intent intent = new Intent(context, SelectImageOneByOne.class);
            context.startActivityForResult(intent, PICK_IMAGE);
        });


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.gallery);
        }
    }
}
