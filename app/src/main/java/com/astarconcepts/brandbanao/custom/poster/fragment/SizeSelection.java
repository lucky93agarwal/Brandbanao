package com.astarconcepts.brandbanao.custom.poster.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSelectSize;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SizeSelection extends BottomSheetDialogFragment implements View.OnClickListener {
    GetSelectSize getSizeOptions;
    View view;

    public SizeSelection(GetSelectSize getSizeOptions) {
        this.getSizeOptions = getSizeOptions;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.size_options_grid, viewGroup, false);
        this.view.findViewById(R.id.size_btn01).setOnClickListener(this);
        this.view.findViewById(R.id.size_btn02).setOnClickListener(this);
        this.view.findViewById(R.id.size_btn03).setOnClickListener(this);
        this.view.findViewById(R.id.size_btn04).setOnClickListener(this);
        this.view.findViewById(R.id.size_btn05).setOnClickListener(this);

        this.view.findViewById(R.id.size_btn06).setOnClickListener(this);

        this.view.findViewById(R.id.size_btnwattpad).setOnClickListener(this);
        return this.view;
    }

    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.size_btn01:
            case R.id.size_btn02:
            case R.id.size_btn03:
            case R.id.size_btn04:
            case R.id.size_btn05:
            case R.id.size_btn06:
                this.getSizeOptions.ratioOptions(view2.getTag().toString());
                return;
            case R.id.size_btnwattpad:
                this.getSizeOptions.sizeOptions(view2.getTag().toString());
                return;
            default:
                return;
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeMemory();
    }

    public void freeMemory() {
        try {
            new Thread(() -> {
                try {
                    Glide.get(SizeSelection.this.getActivity()).clearDiskCache();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            Glide.get(getActivity()).clearMemory();
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
