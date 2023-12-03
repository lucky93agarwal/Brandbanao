package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.adapter.BackgroundAdapter;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListener;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.listener.RecyclerViewLoadMoreScroll;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.utils.GridSpacingItemDecoration;
import com.astarconcepts.brandbanao.utils.StorageUtils;
import com.astarconcepts.brandbanao.utils.YourDataProvider;
import com.stepstone.apprating.CKt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BackgroundFragment2 extends Fragment {

    public RecyclerViewLoadMoreScroll scrollListener;
    BackgroundAdapter backgroundAdapter;
    ArrayList<BackgroundImage> category_list;
    GetSnapListener onGetSnap;
    RecyclerView recyclerView;
    float screenHeight;
    float screenWidth;
    YourDataProvider yourDataProvider;

    public static BackgroundFragment2 newInstance(ArrayList<BackgroundImage> arrayList) {
        BackgroundFragment2 backgroundFragment2 = new BackgroundFragment2();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CKt.DIALOG_DATA, arrayList);
        backgroundFragment2.setArguments(bundle);
        return backgroundFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @NonNull Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.main_fragment, viewGroup, false);
        this.recyclerView = inflate.findViewById(R.id.overlay_artwork);

        this.onGetSnap = (GetSnapListener) getActivity();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) displayMetrics.heightPixels;
        this.category_list = getArguments().getParcelableArrayList(CKt.DIALOG_DATA);

        setCategory();

        return inflate;
    }

    public File getCacheFolder(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {
            file = new StorageUtils(context).getPackageStorageDir("cachefolder");
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        } else {
            file = null;
        }
        return !file.isDirectory() ? context.getCacheDir() : file;
    }

    private void setCategory() {
        this.yourDataProvider = new YourDataProvider();
        this.yourDataProvider.setBackgroundList(this.category_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 40, true));
        this.backgroundAdapter = new BackgroundAdapter(getActivity(), this.yourDataProvider.getLoadMoreItems());
        this.recyclerView.setAdapter(this.backgroundAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                int itemViewType = BackgroundFragment2.this.backgroundAdapter.getItemViewType(i);
                if (itemViewType != 0) {
                    return itemViewType != 1 ? -1 : 3;
                }
                return 1;
            }
        });

        this.scrollListener = new RecyclerViewLoadMoreScroll(gridLayoutManager);
        this.scrollListener.setOnLoadMoreListener(() -> BackgroundFragment2.this.LoadMoreData());
        this.recyclerView.addOnScrollListener(this.scrollListener);

        this.backgroundAdapter.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> {

            final ProgressDialog progressDialog = new ProgressDialog(BackgroundFragment2.this.getContext());
            progressDialog.setMessage(BackgroundFragment2.this.getResources().getString(R.string.plzwait));
            progressDialog.setCancelable(false);
            progressDialog.show();
            BackgroundFragment2 backgroundFragment2 = BackgroundFragment2.this;
            final File cacheFolder = backgroundFragment2.getCacheFolder(backgroundFragment2.getContext());
            MyApplication.getInstance().addToRequestQueue(new ImageRequest(str, bitmap -> {
                try {
                    progressDialog.dismiss();

                    // Save Background Image - Working

                    try {
                        File file = new File(cacheFolder, "localFileName.png");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        try {
                            BackgroundFragment2.this.onGetSnap.onSnapFilter(num.intValue(), 104, file.getAbsolutePath());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }, 0, 0, null, volleyError -> progressDialog.dismiss()));
        });
    }


    public void LoadMoreData() {
        this.backgroundAdapter.addLoadingView();
        new Handler().postDelayed(() -> {
            BackgroundFragment2.this.backgroundAdapter.removeLoadingView();
            BackgroundFragment2.this.backgroundAdapter.addData();
            BackgroundFragment2.this.backgroundAdapter.notifyDataSetChanged();
            BackgroundFragment2.this.scrollListener.setLoaded();
        }, 3000);
    }
}
