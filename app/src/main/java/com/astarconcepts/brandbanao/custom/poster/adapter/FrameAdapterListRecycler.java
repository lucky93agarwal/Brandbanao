package com.astarconcepts.brandbanao.custom.poster.adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.TemplateListAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.astarconcepts.brandbanao.databinding.ItemFrameBinding;
import com.astarconcepts.brandbanao.model.FrameModel;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapterListRecycler extends RecyclerView.Adapter<FrameAdapterListRecycler.MyViewHolder> {

    Activity context;
    List<FrameModel> list;
    List<TemplateModel> templateModels = new ArrayList<>();
    int selectedPosition = 0;
    FramesAdapter.OnFrameSelect onOverlaySelected;

    public FrameAdapterListRecycler(Activity context2, List<FrameModel> iArr, FramesAdapter.OnFrameSelect onOverlaySelected) {
        this.context = context2;
        this.list = iArr;
        this.onOverlaySelected = onOverlaySelected;

        for (int position = 0; position < iArr.size(); position++) {

            TemplateModel templateModel = new TemplateModel();
            templateModel.setId(list.get(position).getId());
            templateModel.setTemplate_type("frame");
            templateModel.setCategory(list.get(position).getFrame_category());
            templateModel.setZip_link_preview(list.get(position).getFrame_zip());
            templateModel.setCode(list.get(position).getTitle());

            templateModels.add(templateModel);
        }

    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.binding.setFrameData(list.get(position));

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.binding.cvBase.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.active_color)));
        } else {
            holder.binding.cvBase.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent_color)));
        }

        if (list.get(position).getType().contains("animated")) {

           TemplateListAdapter.showLottieAnimation(context, holder.binding.lottieAnimation, templateModels.get(position), holder.binding.loader);
            Glide.with(context).load(R.drawable.spaceholder).into(holder.binding.ivPost);

        }else{

            Glide.with(context).load(list.get(position).getThumbnail()).into(holder.binding.ivPost);

        }


        holder.itemView.setOnClickListener(view -> {

            PreferenceManager preferenceManager = new PreferenceManager(context);
            preferenceManager.setInt(Constant.SELECTED_FRAME_POSITION, position);
            notifyItemChanged(selectedPosition);
            selectedPosition = position;
            notifyItemChanged(selectedPosition);
            onOverlaySelected.onSelect(selectedPosition, list.get(position));
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFrameBinding binding = ItemFrameBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFrameBinding binding;

        public MyViewHolder(ItemFrameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
