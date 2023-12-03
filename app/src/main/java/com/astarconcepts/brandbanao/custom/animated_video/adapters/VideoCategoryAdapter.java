package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SearchActivity;
import com.astarconcepts.brandbanao.model.video.DashboardResponseModel;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;

import java.util.ArrayList;
import java.util.List;


public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.MyViewHolder> {

    Activity context;
    List<DashboardResponseModel> catModels;
    List<TemplateModel> mdata;
    private TextView itemTitle;
    private LinearLayout cat_card;

    public VideoCategoryAdapter(Activity context, List<DashboardResponseModel> catModels) {
        this.context = context;
        this.catModels = catModels;
        this.mdata = new ArrayList<>();


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sub_category, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);


        DashboardResponseModel item = catModels.get(position);

        itemTitle.setText("" + item.getCategory());

        cat_card.setOnClickListener(view -> {

            Intent intent = new Intent(context, SearchActivity.class)
                    .putExtra(Constant.TAG_SEARCH_TERM, item.getCategory());
            context.startActivity(intent);


        });


    }

    @Override
    public int getItemCount() {
        return catModels.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    private void initView(View view) {
        itemTitle = view.findViewById(R.id.txt_category);
        cat_card = view.findViewById(R.id.cat_card);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);


        }
    }
}
