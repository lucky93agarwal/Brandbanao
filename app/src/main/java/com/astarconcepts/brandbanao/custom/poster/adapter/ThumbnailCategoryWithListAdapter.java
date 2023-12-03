package com.astarconcepts.brandbanao.custom.poster.adapter;

import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_POSTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailThumbFull;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.GlideImageLoaderPoster;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.TemplateUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ThumbnailCategoryWithListAdapter extends RecyclerView.Adapter<ThumbnailCategoryWithListAdapter.ViewHolder> {

    private final AppDatabase favoriteDatabase;
    public ArrayList<ThumbnailThumbFull> thumbnailThumbFulls;
    Activity context;
    String ratio;
    PreferenceManager preferenceManager;
    private ImageView ivImage;
    private ImageView ivImage1;
    private ProgressBar progressBar1;
    private TextView downloadTextCount;
    private ImageView ivPremiumLottie;

    public ThumbnailCategoryWithListAdapter(Activity context2, ArrayList<ThumbnailThumbFull> arrayList, String str) {

        this.thumbnailThumbFulls = arrayList;
        this.context = context2;
        this.ratio = str;
        favoriteDatabase = AppDatabase.getDatabase(context);
        preferenceManager = new PreferenceManager(context);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poster_adapter_home, viewGroup, false));
    }

    public void onBindViewHolder(@NotNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {

        if (this.ratio != null) {

                    }
        ivImage.setVisibility(View.VISIBLE);
      /*  new GlideImageLoaderPoster(ivImage, progressBar1).load(this.thumbnailThumbFulls.get(i).getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
*/

        Glide.with(context.getApplicationContext()).load(this.thumbnailThumbFulls.get(i).getPost_thumb()).thumbnail(Glide.with(context.getApplicationContext()).load(this.thumbnailThumbFulls.get(i).getPost_thumb()))
                .placeholder(R.drawable.spaceholder).into(ivImage);

        ThumbnailThumbFull templateModel = thumbnailThumbFulls.get(i);

        downloadTextCount.setText("" + MyUtils.format(templateModel.getCreated()));

        preferenceManager.setBoolean(Constant.IS_PREMIUM, templateModel.isPremium());

        if (favoriteDatabase.myDao().isFavorite(templateModel.getPost_id()) == 1) {

            viewHolder.favourite.setImageResource(R.drawable.ic_like_fill);


        } else {

            viewHolder.favourite.setImageResource(R.drawable.ic_favorite);

        }

        if (templateModel.isPremium()) {
            ivPremiumLottie.setVisibility(View.VISIBLE);
        } else {
            ivPremiumLottie.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(v -> new TemplateUtils(context).openPosterActivity(thumbnailThumbFulls.get(i).getPost_id()));

        viewHolder.favourite.setOnClickListener(view -> {

            FavoriteList favorite = new FavoriteList();

            ThumbnailThumbFull templateModel1 = thumbnailThumbFulls.get(i);

            favorite.setTemplate_type(TEMPLATE_TYPE_POSTER);
            favorite.setPost_thumb(templateModel1.getPost_thumb());
            favorite.setPremium(templateModel1.isPremium());
            favorite.setPoster_id(templateModel1.getPost_id());
            favorite.setCreated(templateModel1.getCreated());
            favorite.setTemplate_w_h_ratio(templateModel1.getTemplate_w_h_ratio());
            if (favoriteDatabase.myDao().isFavorite(thumbnailThumbFulls.get(i).getPost_id()) == 1) {

                favoriteDatabase.myDao().deletePoster(thumbnailThumbFulls.get(i).getPost_id());
                viewHolder.favourite.setImageResource(R.drawable.ic_favorite);

            } else {

                viewHolder.favourite.setImageResource(R.drawable.ic_like_fill);
                favoriteDatabase.myDao().addData(favorite);

            }


        });


    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public int getItemCount() {
        return this.thumbnailThumbFulls.size();
    }

    private void initView(View view) {
        ivImage = view.findViewById(R.id.iv_image);
        progressBar1 = view.findViewById(R.id.progressBar1);
        downloadTextCount = view.findViewById(R.id.downloadTextCount);
        ivPremiumLottie = view.findViewById(R.id.ivPremium);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView favourite;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            initView(view);
            favourite = view.findViewById(R.id.favourite);
        }

        public void onClick(View view) {
            Log.d("backgroundImages", "==" + ThumbnailCategoryWithListAdapter.this.thumbnailThumbFulls.get(getAdapterPosition()).getPost_id());
        }
    }
}
