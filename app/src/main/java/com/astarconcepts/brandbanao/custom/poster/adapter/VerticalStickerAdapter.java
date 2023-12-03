package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.activity.BackgrounImageActivity;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.Snap2;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class VerticalStickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    private final ArrayList<Object> mSnaps;
    private final int[] viewTypes;
    public OnClickCallback<ArrayList<String>, ArrayList<BackgroundImage>, String, Activity> mSingleCallback;
    Activity context;
    int flagForActivity;

    public VerticalStickerAdapter(Activity activity, ArrayList<Object> arrayList, int[] iArr, int i) {
        this.mSnaps = arrayList;
        this.context = activity;
        this.viewTypes = iArr;
        this.flagForActivity = i;
    }

    @Override
    public int getItemViewType(int i) {
        return this.viewTypes[i];
    }

    @NotNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_group, viewGroup, false));
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) != 3) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;

            final Snap2 snap2 = (Snap2) this.mSnaps.get(i);

            viewHolder2.snapTextView.setText(snap2.getText().toUpperCase());
            viewHolder2.recyclerView.setOnFlingListener(null);


            if (((Snap2) this.mSnaps.get(i)).getPosterThumbFulls().size() > 3) {
                viewHolder2.seeMoreTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder2.seeMoreTextView.setVisibility(View.GONE);
            }


            ArrayList<BackgroundImage> arrayList2 = new ArrayList<>();
            if (snap2.getPosterThumbFulls().size() >= 5) {
                for (int i4 = 0; i4 < 5; i4++) {
                    arrayList2.add(snap2.getPosterThumbFulls().get(i4));
                }
            } else {
                arrayList2 = snap2.getPosterThumbFulls();
            }
            AdapterStickers adapterStickers = new AdapterStickers(this.context, snap2.getGravity() == 8388611 || snap2.getGravity() == 8388613 || snap2.getGravity() == 1, snap2.getGravity() == 17, arrayList2, this.flagForActivity);
            viewHolder2.recyclerView.setAdapter(adapterStickers);
            adapterStickers.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> mSingleCallback.onClickCallBack(null, snap2.getPosterThumbFulls(), str, context));


            viewHolder2.seeMoreTextView.setOnClickListener(view -> {
                if (flagForActivity == 1) {
                    ((BackgrounImageActivity) context).itemClickSeeMoreAdapter(snap2.getPosterThumbFulls());
                } else {
                    mSingleCallback.onClickCallBack(null, snap2.getPosterThumbFulls(), "", context);
                }
            });
        } else if (this.mSnaps.get(i) == null) {
        }
    }

    public int getItemCount() {
        return this.mSnaps.size();
    }

    public void onSnap(int i) {
        Log.d("Snapped: ", i + "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        LinearLayout seeMoreTextView;
        TextView snapTextView;

        public ViewHolder(View view) {
            super(view);
            this.snapTextView = view.findViewById(R.id.itemTitle);
            this.seeMoreTextView = view.findViewById(R.id.moreClick);
            this.recyclerView = view.findViewById(R.id.recycler_view_list);
        }
    }


}
