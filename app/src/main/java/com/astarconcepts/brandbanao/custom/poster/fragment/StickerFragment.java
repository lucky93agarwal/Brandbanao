package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.adapter.VerticalStickerAdapter;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListenerData;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.MainBG;
import com.astarconcepts.brandbanao.custom.poster.model.Snap2;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
import com.astarconcepts.brandbanao.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StickerFragment extends Fragment {

    public LottieAnimationView loading_view;
    public VerticalStickerAdapter snapAdapter;
    public ArrayList<MainBG> thumbnailBg = new ArrayList<>();
    public int[] viewTypes;
    GetSnapListenerData onGetSnap;
    RecyclerView recyclerView;
    ArrayList<Object> snapData = new ArrayList<>();

    public static StickerFragment newInstance() {
        return new StickerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.mainart_fragment, viewGroup, false);
        this.recyclerView = inflate.findViewById(R.id.overlay_artwork);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setHasFixedSize(true);
        this.onGetSnap = (GetSnapListenerData) getActivity();

        this.loading_view = inflate.findViewById(R.id.loading_view);


        Constant.getTemplateBasedViewModel(this).getPosterStickers().observe(getViewLifecycleOwner(), thumbBG -> {
            if (thumbBG != null) {


                loadData(thumbBG);

            }

        });


        return inflate;
    }

    private void loadData(ThumbBG thumbBG) {


        try {
            thumbnailBg = thumbBG.getThumbnail_bg();

            for (int i = 0; i < thumbnailBg.size(); i++) {
                if ((thumbnailBg.get(i)).getCategory_list().size() != 0) {
                    snapData.add(new Snap2(1, thumbnailBg.get(i).getCategory_name(), thumbnailBg.get(i).getCategory_list(), ((MainBG) thumbnailBg.get(i)).getCategory_id(), ""));
                }
            }
            loading_view.setVisibility(View.GONE);
            viewTypes = new int[snapData.size()];
            for (int i2 = 0; i2 < snapData.size(); i2++) {
                viewTypes[i2] = 0;
                if (snapData != null) {
                    snapAdapter = new VerticalStickerAdapter(getActivity(), snapData, viewTypes, 0);
                    recyclerView.setAdapter(snapAdapter);
                }
            }
            snapAdapter.setItemClickCallback((OnClickCallback<ArrayList<String>, ArrayList<BackgroundImage>, String, Activity>) (arrayList, arrayList2, str1, activity) -> {
                if (str1.equals("")) {
                    onGetSnap.onSnapFilter(arrayList2, 0);
                } else {
                    onGetSnap.onSnapFilter(0, 34, str1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
