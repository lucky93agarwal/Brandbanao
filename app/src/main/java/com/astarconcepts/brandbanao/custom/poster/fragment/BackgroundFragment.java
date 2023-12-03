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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.adapter.VeticalViewAdapter;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListenerData;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.listener.RecyclerViewLoadMoreScroll;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.MainBG;
import com.astarconcepts.brandbanao.custom.poster.model.Snap2;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.StorageUtils;
import com.astarconcepts.brandbanao.utils.YourDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class BackgroundFragment extends Fragment {

    PreferenceManager preferenceManager;
    LinearLayoutManager mLinearLayoutManager;
    GetSnapListenerData onGetSnap;
    RecyclerView recyclerView;
    float screenHeight;
    float screenWidth;
    RecyclerViewLoadMoreScroll scrollListener;
    ArrayList<Object> snapData;
    ArrayList<MainBG> thumbnail_bg;
    VeticalViewAdapter veticalViewAdapter;
    YourDataProvider yourDataProvider;
    private LottieAnimationView loading_view;

    public static BackgroundFragment newInstance() {
        return new BackgroundFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.main_fragment, viewGroup, false);


        this.onGetSnap = (GetSnapListenerData) getActivity();

        this.recyclerView = inflate.findViewById(R.id.overlay_artwork);
        this.mLinearLayoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.mLinearLayoutManager);


        this.recyclerView.setHasFixedSize(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) displayMetrics.heightPixels;
        this.preferenceManager = new PreferenceManager(getActivity());
        this.loading_view = inflate.findViewById(R.id.loading_view);

        snapData = new ArrayList<>();
        thumbnail_bg = new ArrayList<>();

        Constant.getTemplateBasedViewModel(this).getPosterBackground().observe(getViewLifecycleOwner(), thumbBG -> {

            if (thumbBG != null) {

                MyUtils.showResponse(thumbBG);

                loadData(thumbBG);

            }
        });


        return inflate;
    }

    private void loadData(ThumbBG thumbBG) {
        try {


            BackgroundFragment.this.thumbnail_bg = thumbBG.getThumbnail_bg();


            if (thumbnail_bg != null) {


                for (int i = 0; i < thumbnail_bg.size(); i++) {
                    if ((thumbnail_bg.get(i)).getCategory_list().size() != 0) {
                        snapData.add(new Snap2(1, (thumbnail_bg.get(i)).getCategory_name(), (thumbnail_bg.get(i)).getCategory_list(), (thumbnail_bg.get(i)).getCategory_id(), ""));
                    }
                }
            }


            if (!preferenceManager.getBoolean("isAdsDisabled", false)) {

                loadNativeAds();


            } else if (snapData != null) {
                veticalViewAdapter = new VeticalViewAdapter(getActivity(), snapData, 0);
                recyclerView.setAdapter(veticalViewAdapter);
            }
            yourDataProvider = new YourDataProvider();
            yourDataProvider.setPosterList(snapData);
            if (snapData != null) {
                veticalViewAdapter = new VeticalViewAdapter(getActivity(), yourDataProvider.getLoadMorePosterItems(), 0);
                recyclerView.setAdapter(veticalViewAdapter);
                scrollListener = new RecyclerViewLoadMoreScroll(mLinearLayoutManager);
                scrollListener.setOnLoadMoreListener(() -> LoadMoreData());
                recyclerView.addOnScrollListener(scrollListener);
            }
            recyclerView.setAdapter(veticalViewAdapter);
            veticalViewAdapter.setItemClickCallback((OnClickCallback<ArrayList<String>, ArrayList<BackgroundImage>, String, Activity>) (arrayList, arrayList2, str1, activity) -> {
                if (str1.equals("")) {
                    onGetSnap.onSnapFilter(arrayList2, 1);
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getResources().getString(R.string.plzwait));
                progressDialog.setCancelable(false);
                progressDialog.show();
                final File cacheFolder = getCacheFolder(getContext());
                MyApplication.getInstance().addToRequestQueue(new ImageRequest(str1, (Response.Listener<Bitmap>) bitmap -> {
                    try {
                        progressDialog.dismiss();
                        try {
                            File file = new File(cacheFolder, "localFileName.png");
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            try {
                                onGetSnap.onSnapFilter(0, 104, file.getAbsolutePath());
                            } catch (Exception e) {
                                try {
                                    e.printStackTrace();
                                } catch (NullPointerException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }, 0, 0, (Bitmap.Config) null, volleyError -> progressDialog.dismiss()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void LoadMoreData() {
        this.veticalViewAdapter.addLoadingView();
        new Handler().postDelayed(() -> {
            veticalViewAdapter.removeLoadingView();
            veticalViewAdapter.addData();
            veticalViewAdapter.notifyDataSetChanged();
            scrollListener.setLoaded();
        }, 3000);
    }


    public void loadNativeAds() {
        insertAdsInMenuItems();
    }

    private void insertAdsInMenuItems() {
        this.loading_view.setVisibility(View.GONE);
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
}
