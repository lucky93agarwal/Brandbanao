package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.FilterItem;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.FilterType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class RecyclerVideoFilterAdapter extends RecyclerView.Adapter<RecyclerVideoFilterAdapter.ViewHolder> {

    public interface FilterCallback {
        void onFilterSelect(FilterItem item);
    }

    Context context;
    int selectedPosition = 500;
    FilterCallback onOverlaySelected;
    private final List<FilterItem> mItemList;

    public RecyclerVideoFilterAdapter(Context context2, FilterCallback onOverlaySelected) {
        this.context = context2;
        this.onOverlaySelected = onOverlaySelected;
        mItemList = initFilters();
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
            RecyclerVideoFilterAdapter recyclerOverLayAdapter = RecyclerVideoFilterAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
            RecyclerVideoFilterAdapter recyclerOverLayAdapter2 = RecyclerVideoFilterAdapter.this;
            recyclerOverLayAdapter2.selectedPosition = i;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            onOverlaySelected.onFilterSelect(mItemList.get(i));
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

    private List<FilterItem> initFilters() {
        List<FilterItem> items = new LinkedList<FilterItem>();
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_one, "None", FilterType.NONE));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_two, "BlackWhite", FilterType.GRAY));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_three, "Watercolour", FilterType.KUWAHARA));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_four, "Snow", FilterType.SNOW));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_five, "Lut_1", FilterType.LUT1));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_six, "Cameo", FilterType.CAMEO));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_seven, "Lut_2", FilterType.LUT2));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_eight, "Lut_3", FilterType.LUT3));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_nine, "Lut_4", FilterType.LUT4));
        items.add(new FilterItem(com.hw.photomovie.R.drawable.video_filter_ten, "Lut_5", FilterType.LUT5));
        return items;
    }
}
