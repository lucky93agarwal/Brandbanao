package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.TransferItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hw.photomovie.PhotoMovieFactory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class RecyclerVideoAnimationAdapter extends RecyclerView.Adapter<RecyclerVideoAnimationAdapter.ViewHolder> {

    public interface AnimatinCallback {
        void onAnimationSelect(TransferItem item);
    }

    Context context;
    int selectedPosition = 500;
    AnimatinCallback onOverlaySelected;
    private final List<TransferItem> mItemList;

    public RecyclerVideoAnimationAdapter(Context context2, AnimatinCallback onOverlaySelected) {
        this.context = context2;
        this.onOverlaySelected = onOverlaySelected;
        mItemList = getTransfers();
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
        return this.mItemList.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int i = position;
        Glide.with(this.context).load(this.mItemList.get(i).imgRes).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).centerCrop()).placeholder(R.drawable.logo)).error(R.drawable.logo)).into(viewHolder.imageView);
        if (this.selectedPosition == i) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout.setOnClickListener(view -> {
            RecyclerVideoAnimationAdapter recyclerOverLayAdapter = RecyclerVideoAnimationAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
            RecyclerVideoAnimationAdapter recyclerOverLayAdapter2 = RecyclerVideoAnimationAdapter.this;
            recyclerOverLayAdapter2.selectedPosition = i;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            onOverlaySelected.onAnimationSelect(mItemList.get(i));
        });
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_border_adapter, viewGroup, false));
        viewGroup.setId(i);
        viewGroup.setFocusable(false);
        viewGroup.setFocusableInTouchMode(false);
        return viewHolder;
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

    public List<TransferItem> getTransfers() {
        List<TransferItem> items = new LinkedList<TransferItem>();
        items.add(new TransferItem(R.drawable.movie_left_right, "LeftRight", PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS));
        items.add(new TransferItem(R.drawable.movie_updown, "UpDown", PhotoMovieFactory.PhotoMovieType.VERTICAL_TRANS));
        items.add(new TransferItem(R.drawable.movie_window, "Window", PhotoMovieFactory.PhotoMovieType.WINDOW));
        items.add(new TransferItem(R.drawable.movie_zoom, "Zoom", PhotoMovieFactory.PhotoMovieType.GRADIENT));
        items.add(new TransferItem(R.drawable.movie_translation, "Tranlation", PhotoMovieFactory.PhotoMovieType.SCALE_TRANS));
        items.add(new TransferItem(R.drawable.movie_thaw, "Thaw", PhotoMovieFactory.PhotoMovieType.THAW));
        items.add(new TransferItem(R.drawable.movie_scale, "Scale", PhotoMovieFactory.PhotoMovieType.SCALE));
        return items;
    }
}
