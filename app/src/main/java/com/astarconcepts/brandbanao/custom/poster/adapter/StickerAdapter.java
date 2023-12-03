package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import java.util.List;


public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "StickerAdapter";
    private final int cellLimit;
    public PreferenceManager PreferenceManager;
    public boolean isDownloadProgress = true;
    public OnClickCallback<ArrayList<String>, Integer, String, Activity> mSingleCallback;
    ArrayList<BackgroundImage> category_list;
    Activity context;

    public StickerAdapter(Activity activity, ArrayList<BackgroundImage> arrayList, int i, int i2) {
        this.context = activity;
        this.PreferenceManager = new PreferenceManager(this.context);
        this.category_list = arrayList;

        this.cellLimit = 0;
    }

    public static String getFileNameFromUrl(String str) {
        return str.substring(str.lastIndexOf(47) + 1).split("\\?")[0].split("#")[0];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemCount() {
        int size2 = this.category_list.size();
        int i = this.cellLimit;
        return i > 0 ? Math.min(size2, i) : size2;
    }

    @Override
    public int getItemViewType(int i) {
        return this.category_list.get(i) == null ? 1 : 0;
    }

    public void addData(List<BackgroundImage> list) {
        notifyDataSetChanged();
    }

    public void addLoadingView() {
        new Handler().post(() -> {
            StickerAdapter.this.category_list.add(null);
            StickerAdapter stickerAdapter = StickerAdapter.this;
            stickerAdapter.notifyItemInserted(stickerAdapter.category_list.size() - 1);
        });
    }

    public void removeLoadingView() {
        ArrayList<BackgroundImage> arrayList = this.category_list;
        arrayList.remove(arrayList.size() - 1);
        notifyItemRemoved(this.category_list.size());
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 0) {
            final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final BackgroundImage backgroundImage = this.category_list.get(i);


            String str = backgroundImage.getImage_url();


            String[] split = Uri.parse(str).getPath().split("/");
            final String str2 = split[split.length - 2];
            Log.e("url", "==" + str2);
            File file = new File(this.PreferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/" + getFileNameFromUrl(str));
            if (file.exists()) {
                viewHolder2.downloadProgress.setVisibility(View.GONE);
                viewHolder2.imgDownload.setVisibility(View.GONE);
                Glide.with(this.context).load(file.getPath()).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).fitCenter()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(viewHolder2.imageView);
            } else {
                viewHolder2.downloadProgress.setVisibility(View.GONE);
                viewHolder2.imgDownload.setVisibility(View.VISIBLE);
                Glide.with(this.context).load(str).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).fitCenter()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(viewHolder2.imageView);
            }
            viewHolder2.imgDownload.setOnClickListener(view -> {
                if (!ConnectivityReceiver.isConnected()) {
                    Toast.makeText(StickerAdapter.this.context, "No Internet Connection!!!", Toast.LENGTH_SHORT).show();
                } else if (StickerAdapter.this.isDownloadProgress) {
                    StickerAdapter.this.isDownloadProgress = false;
                    viewHolder2.downloadProgress.setVisibility(View.VISIBLE);
                    String str1 = backgroundImage.getImage_url();
                    StickerAdapter.this.DownloadSticker(str1, new File(StickerAdapter.this.PreferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/").getPath(), StickerAdapter.getFileNameFromUrl(str1));
                } else {
                    Toast.makeText(StickerAdapter.this.context, "Please wait..", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder2.layout.setOnClickListener(view -> {
                String str12 = backgroundImage.getImage_url();
                File file1 = new File(StickerAdapter.this.PreferenceManager.getString(Constant.sdcardPath) + "/cat/" + str2 + "/" + StickerAdapter.getFileNameFromUrl(str12));
                if (file1.exists()) {
                    StickerAdapter.this.mSingleCallback.onClickCallBack(null, i, file1.getPath(), StickerAdapter.this.context);
                }
            });
        }
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_list_row, viewGroup, false));
        }
        return new LoadingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_view, viewGroup, false));
    }

    public void DownloadSticker(String str, String str2, String str3) {
        AndroidNetworking.download(str, str2, str3).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                StickerAdapter.this.isDownloadProgress = true;
                StickerAdapter.this.notifyDataSetChanged();
                Log.e(StickerAdapter.TAG, "onDownloadComplete: ");
            }

            public void onError(ANError aNError) {
                StickerAdapter.this.isDownloadProgress = true;
                Log.e(StickerAdapter.TAG, "onError: ");
                StickerAdapter.this.notifyDataSetChanged();
                Toast.makeText(StickerAdapter.this.context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View view) {
            super(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar downloadProgress;
        ImageView imageView;
        RelativeLayout imgDownload;
        RelativeLayout layout;


        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.item_image);
            this.imgDownload = view.findViewById(R.id.imgDownload);

            this.layout = view.findViewById(R.id.lay);
            this.downloadProgress = view.findViewById(R.id.downloadProgress);
        }
    }
}
