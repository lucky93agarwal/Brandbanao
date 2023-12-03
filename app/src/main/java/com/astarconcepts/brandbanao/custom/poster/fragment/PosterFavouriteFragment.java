package com.astarconcepts.brandbanao.custom.poster.fragment;

import static com.astarconcepts.brandbanao.utils.Constant.NATIVE_AD_COUNT;
import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_POSTER;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.adapter.FavouriteAdapter;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class PosterFavouriteFragment extends Fragment {

    Activity context;
    List<FavoriteList> favoriteList = new ArrayList<>();
    PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private LinearLayout noData;
    private AppDatabase appDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poster_favourite, container, false);

        initView(view);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        preferenceManager = new PreferenceManager(context);

        initView(view);

        appDatabase = AppDatabase.getDatabase(getContext());

        List<FavoriteList> favoriteListtemp = appDatabase.myDao().getFavoriteData(TEMPLATE_TYPE_POSTER);

        for (int i = 0; i < favoriteListtemp.size(); i++) {


            if (i % preferenceManager.getInt(NATIVE_AD_COUNT) == 0 && i != 0) {

                favoriteList.add(null);

            }
            favoriteList.add(favoriteListtemp.get(i));

        }

        if (favoriteList.size() > 0) {

            noData.setVisibility(View.GONE);

        } else {
            noData.setVisibility(View.VISIBLE);
        }

        setRecyclerData();

    }

    private void setRecyclerData() {

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new FavouriteAdapter(context, favoriteList, TEMPLATE_TYPE_POSTER));

    }

    private void initView(View view) {

        recyclerView = view.findViewById(R.id.favorite_recycler);
        noData = view.findViewById(R.id.no_data);
        LottieAnimationView lottiAnimationNodata = view.findViewById(R.id.lottiAnimationNodata);
        lottiAnimationNodata.playAnimation();
    }
}