package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SearchActivity;
import com.astarconcepts.brandbanao.model.video.DashboardResponseModel;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;

import java.util.ArrayList;
import java.util.List;


public class CategorySectionAdapter extends RecyclerView.Adapter<CategorySectionAdapter.MyViewHolder> {

    Activity context;
    List<DashboardResponseModel> catModels = new ArrayList<>();
    SectionDataListAdapter adapter;
    List<TemplateModel> mdata;
    private TextView itemTitle;
    private LinearLayout btnMore;
    private RecyclerView recyclerViewList;

    public CategorySectionAdapter(Activity context, List<DashboardResponseModel> catModels) {

        this.context = context;
        this.catModels = catModels;
        this.mdata = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story_group, parent, false));


    }

    public void clearData() {
        catModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);


        DashboardResponseModel item = catModels.get(position);

        itemTitle.setText("" + item.getCategory());

        btnMore.setOnClickListener(view -> {

            Intent intent = new Intent(context, SearchActivity.class)
                    .putExtra(Constant.TAG_SEARCH_TERM, item.getCategory());
            context.startActivity(intent);


        });


        if (item.getTemplateModels() != null && !item.getTemplateModels().isEmpty()) {

            ArrayList<TemplateModel> mdata = item.getTemplateModels();
            adapter = new SectionDataListAdapter(context, mdata);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            recyclerViewList.setAdapter(adapter);
            recyclerViewList.setNestedScrollingEnabled(false);

        }


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
        itemTitle = view.findViewById(R.id.itemTitle);
        btnMore = view.findViewById(R.id.moreClick);
        recyclerViewList = view.findViewById(R.id.recycler_view_list);
    }

    public void setData(ArrayList<DashboardResponseModel> msg) {

        int totalSize = catModels.size();

        int newSize = msg.size();

        catModels.addAll(msg);

        notifyItemRangeChanged(totalSize, newSize);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);


        }
    }
}
