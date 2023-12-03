package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class BackgroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BackgroundImage> categoryList;
    Activity context;
    int size = 0;
    private OnClickCallback<ArrayList<String>, Integer, String, Activity> mSingleCallback;

    public BackgroundAdapter(Activity activity, ArrayList<BackgroundImage> arrayList) {
        this.context = activity;

        this.categoryList = arrayList;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setLayoutParams(int i) {
        this.size = i;
    }

    public int getItemCount() {
        ArrayList<BackgroundImage> arrayList = this.categoryList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 0) {

            ViewHolder viewHolder2 = (ViewHolder) viewHolder;

            viewHolder2.downloadProgress.setVisibility(View.GONE);

            final String str = this.categoryList.get(i).getImage_url();

            viewHolder2.imgDownload.setVisibility(View.GONE);

            Glide.with(this.context).load(str).thumbnail(0.1f).apply(((((new RequestOptions().dontAnimate()).override(200, 200)).fitCenter()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(viewHolder2.imageView);

            viewHolder2.layout.setOnClickListener(view -> BackgroundAdapter.this.mSingleCallback.onClickCallBack(null, i, str, context));

        }
    }

    public void addData() {
        notifyDataSetChanged();
    }

    public void addLoadingView() {
        new Handler().post(() -> {
            categoryList.add(null);
            notifyItemInserted(categoryList.size() - 1);
        });
    }

    public void removeLoadingView() {
        ArrayList<BackgroundImage> arrayList = this.categoryList;
        arrayList.remove(arrayList.size() - 1);
        notifyItemRemoved(this.categoryList.size());
    }


    @NotNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.background_image_listrow, viewGroup, false));
        }
        return new LoadingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_view, viewGroup, false));
    }

    @Override
    public int getItemViewType(int i) {
        return this.categoryList.get(i) == null ? 1 : 0;
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar downloadProgress;
        ImageView imageView;
        RelativeLayout imgDownload;
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            this.imgDownload = view.findViewById(R.id.imgDownload);
            this.imageView = view.findViewById(R.id.thumbnail_image);
            this.layout = view.findViewById(R.id.main);
            this.downloadProgress = view.findViewById(R.id.downloadProgress);
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View view) {
            super(view);
        }
    }
}
