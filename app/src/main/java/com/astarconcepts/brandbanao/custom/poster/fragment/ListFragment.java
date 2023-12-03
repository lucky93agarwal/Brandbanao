package com.astarconcepts.brandbanao.custom.poster.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.astarconcepts.brandbanao.custom.poster.adapter.LockLayerItemAdapter;
import com.astarconcepts.brandbanao.utils.Constant;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    public static View HintView;
    public static RelativeLayout lay_Notext;
    public ArrayList<Pair<Long, View>> mItemArray = new ArrayList<>();
    private ArrayList<View> arrView = new ArrayList<>();
    private DragListView mDragListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.list_layout, viewGroup, false);

        this.mDragListView = inflate.findViewById(R.id.drag_list_view);
        this.mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        this.mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int i) {
            }

            @Override
            public void onItemDragEnded(int i, int i2) {
                if (i != i2) {
                    for (int size = ListFragment.this.mItemArray.size() - 1; size >= 0; size--) {
                        ((ListFragment.this.mItemArray.get(size)).second).bringToFront();
                    }
                    ThumbnailActivity.txtStkrRel.requestLayout();
                    ThumbnailActivity.txtStkrRel.postInvalidate();
                }
            }
        });
        ((TextView) inflate.findViewById(R.id.txt_Nolayers)).setTypeface(Constant.getTextTypeface(getActivity()));
        lay_Notext = inflate.findViewById(R.id.lay_text);
        HintView = inflate.findViewById(R.id.HintView);
        (inflate.findViewById(R.id.lay_frame)).setOnClickListener(view -> {

            if (ThumbnailActivity.lay_container.getVisibility() == View.VISIBLE) {
                ThumbnailActivity.lay_container.animate().translationX((float) (-ThumbnailActivity.lay_container.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
                new Handler().postDelayed(() -> {
                    ThumbnailActivity.lay_container.setVisibility(View.GONE);
                    ThumbnailActivity.btn_layControls.setVisibility(View.VISIBLE);
                }, 200);
            }
        });
        return inflate;
    }

    public void getLayoutChild() {
        this.arrView.clear();
        this.mItemArray.clear();
        if (ThumbnailActivity.txtStkrRel.getChildCount() != 0) {
            lay_Notext.setVisibility(View.GONE);
            for (int childCount = ThumbnailActivity.txtStkrRel.getChildCount() - 1; childCount >= 0; childCount--) {
                this.mItemArray.add(new Pair((long) childCount, ThumbnailActivity.txtStkrRel.getChildAt(childCount)));
                this.arrView.add(ThumbnailActivity.txtStkrRel.getChildAt(childCount));
            }
        } else {
            lay_Notext.setVisibility(View.VISIBLE);
        }
        setupListRecyclerView();
    }

    private void setupListRecyclerView() {
        this.mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mDragListView.setAdapter(new LockLayerItemAdapter(getActivity(), this.mItemArray, R.layout.list_item, R.id.drag_img, false), true);
        this.mDragListView.setCanDragHorizontally(false);
    }


}
