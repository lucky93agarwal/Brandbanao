package com.astarconcepts.brandbanao.adapters;

import static com.astarconcepts.brandbanao.api.ApiClient.App_URl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.databinding.ItemPersonalLayoutBinding;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.SnapHelperOneByOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyPostAdapter extends RecyclerView.Adapter<DailyPostAdapter.ViewHolder> {

    Context context;
    List<PostItem> list;
    private PreferenceManager preferenceManager;
    private String userName;
    private String userImage;
    private String userNumber;
    private String userDesignation;

    public void setData(List<PostItem> daily_post) {


        int size = list.size();

        this.list = daily_post;


        notifyItemRangeInserted(size,daily_post.size());

    }

    public interface OnClickEvent {
        void onClick(View view, View posterview, PostItem postItem);
    }

    OnClickEvent onClickEvent;

    public DailyPostAdapter(Context context, List<PostItem> list, OnClickEvent onClickEvent) {
        this.context = context;
        this.list = list;
        this.onClickEvent = onClickEvent;
        this.preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonalLayoutBinding binding = ItemPersonalLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);


        try {
            int pos = position;
            holder.binding.setPosts(list.get(pos));

            holder.binding.recyclerview.setAdapter(new CustomPagerAdapter());

            final int random = new Random().nextInt((3 - 1) + 1) + 1;
            holder.binding.recyclerview.scrollToPosition(random);

            SnapHelperOneByOne snapHelperOneByOne = new SnapHelperOneByOne();
            snapHelperOneByOne.attachToRecyclerView(holder.binding.recyclerview);
            if (preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)) {
                holder.binding.watermark.setVisibility(View.GONE);
            }

            holder.binding.watermark.setOnClickListener(view -> onClickEvent.onClick(view, holder.binding.mainLayOut, list.get(position)));
            holder.binding.downloadBtn.setOnClickListener(view -> onClickEvent.onClick(view, holder.binding.mainLayOut, list.get(position)));
            holder.binding.shareBtn.setOnClickListener(view -> onClickEvent.onClick(view, holder.binding.mainLayOut, list.get(position)));
            holder.binding.editBtn.setOnClickListener(view -> onClickEvent.onClick(view, holder.binding.mainLayOut, list.get(position)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPersonalLayoutBinding binding;

        public ViewHolder(@NonNull ItemPersonalLayoutBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;


        }
    }

    public class CustomPagerAdapter extends RecyclerView.Adapter<CustomPagerAdapter.ViewHolder> {

        List<Integer> list = new ArrayList<>();

        public CustomPagerAdapter() {
            list.add(R.layout.personal_frame_one);
            list.add(R.layout.personal_frame_three);
            list.add(R.layout.personal_frame_two);

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(list.get(viewType), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setIsRecyclable(false);

            userName = preferenceManager.getString(Constant.USER_NAME);
            userImage = preferenceManager.getString(Constant.USER_IMAGE);
            userNumber = preferenceManager.getString(Constant.USER_PHONE);
            userDesignation = preferenceManager.getString(Constant.USER_DESIGNATION);

            if (userImage != null && userImage.contains("http")) {

                GlideDataBinding.bindImage(holder.imageView, userImage);

            } else {

                GlideDataBinding.bindImage(holder.imageView, App_URl + userImage);
            }

            holder.nameTv.setText(userName);
            holder.number_tv.setText(userNumber);
            holder.ivDesignation.setText(userDesignation);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv, number_tv, ivDesignation;
            ImageView imageView;
            LinearLayout frameLay;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                frameLay = itemView.findViewById(R.id.frameLay);
                imageView = itemView.findViewById(R.id.profile_pic);
                nameTv = itemView.findViewById(R.id.user_name);
                number_tv = itemView.findViewById(R.id.number_email);
                ivDesignation = itemView.findViewById(R.id.ivDesignation);

            }
        }
    }
}
