package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.activity.BackgrounImageActivity;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.utils.GlideImageLoader;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpStatus;


public class AdaptersBackground extends RecyclerView.Adapter<AdaptersBackground.ViewHolder> {

    private final boolean mHorizontal;
    private final boolean mPager;
    Context context;
    int flagForActivity;
    SharedPreferences preferences;
    private ArrayList<BackgroundImage> backgroundImages;
    private OnClickCallback<ArrayList<String>, Integer, String, Activity> mSingleCallback;

    public AdaptersBackground(Context context2, boolean z, boolean z2, ArrayList<BackgroundImage> arrayList, int i) {
        this.mHorizontal = z;
        this.backgroundImages = arrayList;
        this.mPager = z2;
        this.context = context2;
        this.flagForActivity = i;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context2);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (this.mPager) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_pager, viewGroup, false));
        }
        if (this.mHorizontal) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_vertical, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        final String str = this.backgroundImages.get(i).getImage_url();

        new GlideImageLoader(viewHolder.imageView, viewHolder.mProgressBar).load(str, new RequestOptions().centerCrop().override(HttpStatus.SC_MULTIPLE_CHOICES, HttpStatus.SC_MULTIPLE_CHOICES).placeholder(R.drawable.spaceholder).error(R.drawable.spaceholder).priority(Priority.HIGH));

        viewHolder.ivLock.setVisibility(View.GONE);

        viewHolder.imageView.setOnClickListener(view -> {
            if (AdaptersBackground.this.flagForActivity == 1) {

                ((BackgrounImageActivity) AdaptersBackground.this.context).ongetPositions(str);

            } else {

                AdaptersBackground.this.mSingleCallback.onClickCallBack(null, i, str, (FragmentActivity) AdaptersBackground.this.context);

            }
        });
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;


    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public int getItemCount() {
        return this.backgroundImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private ImageView ivLock;
        private ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.imageView = view.findViewById(R.id.imageView);
            this.ivLock = view.findViewById(R.id.iv_lock);
            this.mProgressBar = view.findViewById(R.id.progressBar1);
        }

        public void onClick(View view) {
            Log.d("backgroundImages", AdaptersBackground.this.backgroundImages.get(getAdapterPosition()).getImage_url());
        }
    }
}
