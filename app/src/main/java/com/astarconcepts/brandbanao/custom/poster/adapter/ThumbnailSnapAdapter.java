package com.astarconcepts.brandbanao.custom.poster.adapter;

import static com.astarconcepts.brandbanao.utils.Constant.TAG_SEARCH_TERM;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.activity.PosterSeeMoreActivity;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailDataList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ThumbnailSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    private ArrayList<ThumbnailDataList> thumbnailDataLists;

    public ThumbnailSnapAdapter(Activity activity, ArrayList<ThumbnailDataList> posterDatas) {

        this.context = activity;
        this.thumbnailDataLists = posterDatas;
    }

    public void clearData()
    {
        thumbnailDataLists.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }


    @NotNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_group, viewGroup, false));

    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int i) {


        ViewHolder viewHolder2 = (ViewHolder) viewHolder;


        ThumbnailDataList thumbnailThumbFull = thumbnailDataLists.get(i);

        viewHolder2.snapTextView.setText(thumbnailThumbFull.getCat_name());


        viewHolder2.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        viewHolder2.recyclerView.setNestedScrollingEnabled(false);
        viewHolder2.recyclerView.setAdapter(new ThumbnailCategoryWithListAdapter(this.context, thumbnailDataLists.get(i).getPoster_list(), thumbnailDataLists.get(i).getTemplate_w_h_ratio()));

        viewHolder2.seeMoreTextView.setOnClickListener(view -> {

            Intent intent = new Intent(context, PosterSeeMoreActivity.class);
            intent.putExtra(TAG_SEARCH_TERM, thumbnailThumbFull.getCat_name());
            context.startActivity(intent);

        });


    }


    public void addData(ArrayList<ThumbnailDataList> list) {

        int size = thumbnailDataLists.size();

        this.thumbnailDataLists.addAll(list);

        notifyItemRangeChanged(size, list.size());
    }


    public int getItemCount() {

        return thumbnailDataLists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private LinearLayout seeMoreTextView;
        private TextView snapTextView;

        ViewHolder(View view) {
            super(view);
            this.snapTextView = view.findViewById(R.id.itemTitle);
            this.seeMoreTextView = view.findViewById(R.id.moreClick);
            this.recyclerView = view.findViewById(R.id.recycler_view_list);
        }
    }

}
