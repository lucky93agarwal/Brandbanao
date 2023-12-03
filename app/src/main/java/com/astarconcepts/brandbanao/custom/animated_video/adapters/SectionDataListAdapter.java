package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import static com.astarconcepts.brandbanao.custom.animated_video.adapters.TemplateListAdapter.showLottieAnimation;
import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_VIDEO;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.PreviewVideoTemplate;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;

import java.util.List;


public class SectionDataListAdapter extends RecyclerView.Adapter<SectionDataListAdapter.MyViewHolder> {

    private final AppDatabase favoriteDatabase;
    Activity context;
    List<TemplateModel> templateList;
    PreferenceManager preferenceManager;
    private InterstitialsAdsManager interstitialsAdsManager;

    public SectionDataListAdapter(Activity context, List<TemplateModel> mdata) {
        this.context = context;
        this.templateList = mdata;
        favoriteDatabase = AppDatabase.getDatabase(context);
        preferenceManager = new PreferenceManager(context);

        interstitialsAdsManager = new InterstitialsAdsManager(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_template_adapter_home, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        TemplateModel templateModel = templateList.get(position);
        templateModel.setTemplate_type("templates");
         

        try {

            showLottieAnimation(context, holder.lottieAnimationView, templateModel, holder.loadingAnim);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.downloadCount.setText("" + MyUtils.format(templateModel.getCreated()));

        if (favoriteDatabase.myDao().isFavoriteVideo(Integer.parseInt(templateModel.getId())) == 1) {

            holder.favourite.setImageResource(R.drawable.ic_like_fill);

        } else {

            holder.favourite.setImageResource(R.drawable.ic_favorite);

        }

        holder.ivPremium.setVisibility(templateModel.isPremium()?View.VISIBLE:View.GONE);


        holder.lottieAnimationView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


                @Override

                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);


                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_test);
                    animation.setDuration(300);
                    holder.itemView.startAnimation(animation);

                    return true;
                }


                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    interstitialsAdsManager.showInterstitialAd(() -> openNextActivity(position, holder));


                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_test);
                animation.setDuration(300);
                holder.itemView.startAnimation(animation);

                gestureDetector.onTouchEvent(event);
                return true;
            }

        });

        holder.favourite.setOnClickListener(view -> {

            FavoriteList favorite = new FavoriteList();
            TemplateModel model = templateList.get(position);
            favorite.setTemplate_type(TEMPLATE_TYPE_VIDEO);
            favorite.setData_html(model.getData_html());
            favorite.setZip_link(model.getZip_link());
            favorite.setZip_link_preview(model.getZip_link_preview());
            favorite.setId(Integer.parseInt(model.getId()));
            favorite.setCategory(model.getCategory());
            favorite.setPremium(model.isPremium());
            favorite.setTitle(model.getTitle());
            favorite.setViews(model.getViews());
            favorite.setCreated(model.getCreated());
            favorite.setCode(model.getCode());
            favorite.setResImageNum(model.getResImageNum());
            favorite.setTemplate_json(model.getTemplate_json());

            holder.favourite.setImageResource(R.drawable.ic_like_fill);

            if (favoriteDatabase.myDao().isFavoriteVideo(Integer.parseInt(model.getId())) == 1) {

                favoriteDatabase.myDao().deleteVideo(Integer.parseInt(model.getId()));
                holder.favourite.setImageResource(R.drawable.ic_favorite);

            } else {
                holder.favourite.setImageResource(R.drawable.ic_like_fill);
                favoriteDatabase.myDao().addData(favorite);
            }

        });


    }

    private void openNextActivity(int position, @NonNull MyViewHolder holder) {


        MyUtils.templateModel = templateList.get(position);

        Intent intent = new Intent(context, PreviewVideoTemplate.class);

        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {

        if (templateList.size() >= 5) {
            return 5;
        } else {
            return templateList.size();
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView downloadCount;
        ImageView favourite;
        CardView flMain;
        LottieAnimationView lottieAnimationView;
        LottieAnimationView loadingAnim;
        ImageView ivPremium;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadCount = itemView.findViewById(R.id.downloadTextCount);

            favourite = itemView.findViewById(R.id.favourite);
            flMain = itemView.findViewById(R.id.cardTemplate);
            ivPremium = itemView.findViewById(R.id.ivPremium);
            lottieAnimationView = itemView.findViewById(R.id.lottie);
            loadingAnim = itemView.findViewById(R.id.loading_anim);


        }
    }
}
