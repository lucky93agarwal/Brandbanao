package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.activity.BackgrounImageActivity;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.Snap2;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class VeticalViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public OnClickCallback<ArrayList<String>, ArrayList<BackgroundImage>, String, Activity> mSingleCallback;
    Activity context;
    int flagForActivity;
    ArrayList<Object> mSnaps;

    public VeticalViewAdapter(Activity activity, ArrayList<Object> arrayList, int i) {
        this.mSnaps = arrayList;
        this.context = activity;
        this.flagForActivity = i;
    }

    public void onSnap(int i) {
    }

    @Override
    public int getItemViewType(int i) {
        if (this.mSnaps.get(i) == null) {
            return 0;
        }
        return this.mSnaps.get(i).equals("Ads") ? 2 : 1;
    }

    @NotNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_group, viewGroup, false));
        }
        if (i != 2) {
            return new LoadingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_view, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_group, viewGroup, false));

    }

    public void addData() {
        notifyDataSetChanged();
    }

    public void addLoadingView() {
        new Handler().post(() -> {
            VeticalViewAdapter.this.mSnaps.add(null);
            VeticalViewAdapter veticalViewAdapter = VeticalViewAdapter.this;
            veticalViewAdapter.notifyItemInserted(veticalViewAdapter.mSnaps.size() - 1);
        });
    }

    public void removeLoadingView() {
        ArrayList<Object> arrayList = this.mSnaps;
        arrayList.remove(arrayList.size() - 1);
        notifyItemRemoved(this.mSnaps.size());
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 1) {

            ViewHolder viewHolder2 = (ViewHolder) viewHolder;

            final Snap2 snap2 = (Snap2) this.mSnaps.get(i);

            viewHolder2.snapTextView.setText(snap2.getText().toUpperCase());

            viewHolder2.recyclerView.setOnFlingListener(null);

            if (snap2.getGravity() == 8388611 || snap2.getGravity() == 8388613) {
                viewHolder2.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder2.recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
                new GravitySnapHelper(snap2.getGravity(), false, this).attachToRecyclerView(viewHolder2.recyclerView);

            } else if (snap2.getGravity() == 1 || snap2.getGravity() == 16) {
                viewHolder2.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder2.recyclerView.getContext(), snap2.getGravity() == 1 ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL, false));
                new LinearSnapHelper().attachToRecyclerView(viewHolder2.recyclerView);

            } else if (snap2.getGravity() == 17) {
                viewHolder2.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder2.recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
                new GravityPagerSnapHelper(GravityCompat.START).attachToRecyclerView(viewHolder2.recyclerView);

            } else {
                viewHolder2.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder2.recyclerView.getContext()));
                new GravitySnapHelper(snap2.getGravity()).attachToRecyclerView(viewHolder2.recyclerView);
            }

            if (((Snap2) this.mSnaps.get(i)).getPosterThumbFulls().size() > 3) {

                viewHolder2.seeMoreTextView.setVisibility(View.VISIBLE);

            } else {

                viewHolder2.seeMoreTextView.setVisibility(View.GONE);

            }

            ArrayList<BackgroundImage> arrayList = new ArrayList<>();
            if (snap2.getPosterThumbFulls().size() >= 5) {
                for (int i2 = 0; i2 < 5; i2++) {
                    arrayList.add(snap2.getPosterThumbFulls().get(i2));
                }
            } else {
                arrayList = snap2.getPosterThumbFulls();
            }

            ArrayList<BackgroundImage> arrayList2 = arrayList;

            if (this.flagForActivity == 1) {

                viewHolder2.recyclerView.setAdapter(new AdaptersBackground(this.context, snap2.getGravity() == 8388611 || snap2.getGravity() == 8388613 || snap2.getGravity() == 1, snap2.getGravity() == 17, arrayList2, this.flagForActivity));

            } else {

                AdaptersBackground adaptersBackground = new AdaptersBackground(this.context, snap2.getGravity() == 8388611 || snap2.getGravity() == 8388613 || snap2.getGravity() == 1, snap2.getGravity() == 17, arrayList2, this.flagForActivity);
                viewHolder2.recyclerView.setAdapter(adaptersBackground);
                adaptersBackground.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList1, num, str, activity) -> VeticalViewAdapter.this.mSingleCallback.onClickCallBack(null, snap2.getPosterThumbFulls(), str, VeticalViewAdapter.this.context));

            }

            viewHolder2.seeMoreTextView.setOnClickListener(view -> {

                if (VeticalViewAdapter.this.flagForActivity == 1) {

                    ((BackgrounImageActivity) VeticalViewAdapter.this.context).itemClickSeeMoreAdapter(snap2.getPosterThumbFulls());

                } else {

                    VeticalViewAdapter.this.mSingleCallback.onClickCallBack(null, snap2.getPosterThumbFulls(), "", VeticalViewAdapter.this.context);

                }
            });

        }
    }

    public int getItemCount() {
        return this.mSnaps.size();
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

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View view) {
            super(view);
        }
    }


}
