package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.utils.ConnectivityReceiver;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class AdapterStickers extends RecyclerView.Adapter<AdapterStickers.ViewHolder> {

    private final boolean mHorizontal;
    private final boolean mPager;
    private final ArrayList<BackgroundImage> backgroundImages;
    Context context;
    int flagForActivity;
    PreferenceManager preferenceManager;
    private boolean isDownloadProgress = true;
    private OnClickCallback<ArrayList<String>, Integer, String, Activity> mSingleCallback;

    public AdapterStickers(Context context2, boolean z, boolean z2, ArrayList<BackgroundImage> arrayList, int i) {
        this.mHorizontal = z;
        this.backgroundImages = arrayList;
        this.mPager = z2;
        this.context = context2;
        this.flagForActivity = i;
        preferenceManager = new PreferenceManager(context);

    }

    public static String getFileNameFromUrl(String str) {
        return str.substring(str.lastIndexOf(47) + 1).split("\\?")[0].split("#")[0];
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (this.mPager) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_pager, viewGroup, false));
        }
        if (this.mHorizontal) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker_adapters, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_vertical, viewGroup, false));
    }

    public void onBindViewHolder(@NotNull final ViewHolder viewHolder, final int i) {


        final BackgroundImage backgroundImage = this.backgroundImages.get(i);

        String str = backgroundImage.getImage_url();

        String[] split = Uri.parse(str).getPath().split("/");

        final String str2 = split[split.length - 2];

        File file = new File(preferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/" + getFileNameFromUrl(str));

        if (file.exists()) {

            viewHolder.imgDownload.setVisibility(View.GONE);

            viewHolder.mProgressBar.setVisibility(View.GONE);

            Glide.with(this.context).load(file.getPath()).thumbnail(0.1f).apply(new RequestOptions().dontAnimate().fitCenter().placeholder(R.drawable.spaceholder).error(R.drawable.spaceholder)).into(viewHolder.imageView);

        } else {

            viewHolder.imgDownload.setVisibility(View.VISIBLE);

            Glide.with(this.context).load(str).thumbnail(0.1f).apply(new RequestOptions().dontAnimate().fitCenter().placeholder(R.drawable.spaceholder).error(R.drawable.spaceholder)).into(viewHolder.imageView);

        }


        viewHolder.imgDownload.setOnClickListener(view -> {
            if (!ConnectivityReceiver.isConnected()) {

                Toast.makeText(context, "No Internet Connection!!!", Toast.LENGTH_SHORT).show();

            } else if (isDownloadProgress) {

                isDownloadProgress = false;

                viewHolder.mProgressBar.setVisibility(View.VISIBLE);


                File file1 = new File(preferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/");

                String fileNameFromUrl = AdapterStickers.getFileNameFromUrl(str);

                viewHolder.imgDownload.setVisibility(View.GONE);

                downloadSticker(str, file1.getPath(), fileNameFromUrl);

            } else {
                Toast.makeText(context, "Please wait..", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imageView.setOnClickListener(view -> {


            File file12 = new File(preferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/" + AdapterStickers.getFileNameFromUrl(str));

            if (file12.exists()) {
                mSingleCallback.onClickCallBack(null, i, file12.getPath(), (FragmentActivity) context);
            }
        });
    }

    public void downloadSticker(String str, String str2, String str3) {
        AndroidNetworking.download(str, str2, str3).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                isDownloadProgress = true;
                notifyDataSetChanged();
            }

            public void onError(ANError aNError) {
                isDownloadProgress = true;
                notifyDataSetChanged();
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
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
        private RelativeLayout imgDownload;
        private ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.imgDownload = view.findViewById(R.id.imgDownload);
            this.imageView = view.findViewById(R.id.imageView);
            this.mProgressBar = view.findViewById(R.id.progressBar1);
        }

        public void onClick(View view) {
            Log.d("backgroundImages", backgroundImages.get(getAdapterPosition()).getImage_url());
        }
    }
}
