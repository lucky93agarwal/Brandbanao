package com.astarconcepts.brandbanao.custom.poster.adapter;

import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_POSTER;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailThumbFull;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.GlideImageLoader;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.TemplateUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreThumbnailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Object> posterDatas;
    public Activity context;
    String ratio;
    private boolean isLoaderVisible = false;

    private ImageView ivImage, ivImage1;
    private ProgressBar progressBar1;

    private TextView downloadTextCount;
    private PreferenceManager prefManager;
    private AppDatabase favoriteDatabase;
    private ImageView ivPremiumLottie;

    public SeeMoreThumbnailListAdapter(ArrayList<Object> arrayList, Activity activity) {
        this.posterDatas = arrayList;
        this.context = activity;
        favoriteDatabase = AppDatabase.getDatabase(context);

        prefManager = new PreferenceManager(activity);

    }


    public  void clearData(){

        posterDatas.clear();
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (i == 1) {

            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poster_adapter_list, viewGroup, false));

        }
        if (i != 2) {
            return new LoadingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_view, viewGroup, false));
        }
        return new AdHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_view, viewGroup, false));
    }

    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);

        if (itemViewType == 1) {

            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

            myViewHolder.setIsRecyclable(false);

            final ThumbnailThumbFull thumbnailThumbFull = (ThumbnailThumbFull) this.posterDatas.get(i);
            ratio = thumbnailThumbFull.getTemplate_w_h_ratio();

            if (thumbnailThumbFull.isPremium()) {
                ivPremiumLottie.setVisibility(View.VISIBLE);
            } else {
                ivPremiumLottie.setVisibility(View.GONE);
            }
            prefManager.setBoolean(Constant.IS_PREMIUM, thumbnailThumbFull.isPremium());

            Glide.with(context.getApplicationContext()).load(thumbnailThumbFull.getPost_thumb()).thumbnail(Glide.with(context.getApplicationContext()).load(thumbnailThumbFull.getPost_thumb()))
                    .placeholder(R.drawable.spaceholder).into(ivImage);


  /*          if (this.ratio != null) {
                if (this.ratio.equalsIgnoreCase("720:1280")) {
                    ivImage1.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.GONE);
                    new GlideImageLoader(ivImage1, progressBar1)
                            .load(thumbnailThumbFull.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
                } else {
                    ivImage1.setVisibility(View.GONE);
                    ivImage.setVisibility(View.VISIBLE);
                    new GlideImageLoader(ivImage, progressBar1)
                            .load(thumbnailThumbFull.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
                }
            } else {
                ivImage1.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
                new GlideImageLoader(ivImage, progressBar1)
                        .load(thumbnailThumbFull.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
            }


          new GlideImageLoader(ivImage, progressBar1)
                    .load(thumbnailThumbFull.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
*/

            downloadTextCount.setText("" + MyUtils.format(thumbnailThumbFull.getCreated()));


            if (favoriteDatabase.myDao().isFavorite(thumbnailThumbFull.getPost_id()) == 1) {

                myViewHolder.favourite.setImageResource(R.drawable.ic_like_fill);

            } else {
                myViewHolder.favourite.setImageResource(R.drawable.ic_favorite);

            }


            viewHolder.itemView.setOnClickListener(v -> new TemplateUtils(context).openPosterActivity(thumbnailThumbFull.getPost_id()));


            myViewHolder.favourite.setOnClickListener(view -> {

                FavoriteList favorite = new FavoriteList();

                ThumbnailThumbFull model = thumbnailThumbFull;

                favorite.setTemplate_type(TEMPLATE_TYPE_POSTER);

                favorite.setPost_thumb(model.getPost_thumb());
                favorite.setId(model.getPost_id());
                favorite.setTemplate_w_h_ratio(model.getTemplate_w_h_ratio());


                myViewHolder.favourite.setImageResource(R.drawable.ic_like_fill);

                if (favoriteDatabase.myDao().isFavorite(model.getPost_id()) == 1) {
                    favoriteDatabase.myDao().deletePoster(model.getPost_id());
                    myViewHolder.favourite.setImageResource(R.drawable.ic_favorite);
                } else {
                    myViewHolder.favourite.setImageResource(R.drawable.ic_like_fill);
                    favoriteDatabase.myDao().addData(favorite);
                }


            });


        } else if (viewHolder instanceof AdHolder) {

            new AdsUtils(context).loadNativeAd(context, ((AdHolder) viewHolder).fbPlaceHolder);

        }
    }


    public void addData(List<Object> list) {

        int prezie = posterDatas.size();

        this.posterDatas.addAll(list);

        notifyItemRangeChanged(prezie, list.size());


    }


    public int getItemCount() {
        ArrayList<Object> arrayList = this.posterDatas;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (this.isLoaderVisible) {
            if (i == this.posterDatas.size() - 1) {
                return 0;
            }
            if (this.posterDatas.get(i) instanceof String) {
                return 2;
            }
            return 1;
        } else if (this.posterDatas.get(i) instanceof String) {
            return 2;
        } else {
            return 1;
        }
    }

    private void initView(View view) {
        ivImage = view.findViewById(R.id.iv_image);
        downloadTextCount = view.findViewById(R.id.downloadTextCount);
        ivPremiumLottie = view.findViewById(R.id.ivPremium);

    }

    private static class AdHolder extends RecyclerView.ViewHolder {
        FrameLayout fbPlaceHolder;

        AdHolder(View view) {
            super(view);
            fbPlaceHolder = view.findViewById(R.id.fl_adplaceholder);
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View view) {
            super(view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView favourite;

        public MyViewHolder(View view) {
            super(view);
            favourite = view.findViewById(R.id.favourite);
            initView(view);
        }
    }
}
