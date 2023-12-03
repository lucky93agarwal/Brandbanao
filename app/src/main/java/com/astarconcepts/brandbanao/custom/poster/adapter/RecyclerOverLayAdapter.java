package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;


public class RecyclerOverLayAdapter extends RecyclerView.Adapter<RecyclerOverLayAdapter.ViewHolder> {
    Context context;
    int[] makeUpEditImage;
    int selected_position = 500;
    OnOverlaySelected onOverlaySelected;

    public RecyclerOverLayAdapter(Context context2, int[] iArr, OnOverlaySelected onOverlaySelected) {
        this.context = context2;
        this.makeUpEditImage = iArr;
        this.onOverlaySelected = onOverlaySelected;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    public int getItemCount() {
        return this.makeUpEditImage.length;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Glide.with(this.context).load(this.makeUpEditImage[i]).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).centerCrop()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(viewHolder.imageView);
        if (this.selected_position == i) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {

            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout.setOnClickListener(view -> {
            RecyclerOverLayAdapter recyclerOverLayAdapter = RecyclerOverLayAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selected_position);
            RecyclerOverLayAdapter recyclerOverLayAdapter2 = RecyclerOverLayAdapter.this;
            recyclerOverLayAdapter2.selected_position = i;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selected_position);
            onOverlaySelected.selectedImage(this.makeUpEditImage[i]);
        });
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_adapter, viewGroup, false));
        viewGroup.setId(i);
        viewGroup.setFocusable(false);
        viewGroup.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public interface OnOverlaySelected {
        void selectedImage(int drawable);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;
        ImageView viewImage;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.item_image);
            this.viewImage = view.findViewById(R.id.view_image);
            this.layout = view.findViewById(R.id.lay);
        }
    }
}
