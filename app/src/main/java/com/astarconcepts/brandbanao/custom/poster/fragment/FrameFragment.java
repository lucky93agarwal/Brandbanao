package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.adapter.FramesAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.FrameAdapterListRecycler;
import com.astarconcepts.brandbanao.model.FrameModel;

import java.util.List;


public class FrameFragment extends Fragment {

    FramesAdapter.OnFrameSelect listner;
    List<FrameModel> frameModelslist;
    RecyclerView recyclerView;
    FrameAdapterListRecycler adapter;


    public FrameFragment(List<FrameModel> frameModelslist, FramesAdapter.OnFrameSelect onMusicSelect) {

        this.frameModelslist = frameModelslist;
        this.listner = onMusicSelect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.recyclerView = view.findViewById(R.id.overlay_artwork);

         adapter =new FrameAdapterListRecycler(getActivity(), frameModelslist, (position, frameModel) -> listner.onSelect(position, frameModel));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       if (frameModelslist !=null && frameModelslist.isEmpty()){
            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
        }
    }


}