package com.astarconcepts.brandbanao.custom.poster.adapter;


import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_VIDEO;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.PreviewVideoTemplate;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.TemplateListAdapter;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.GlideImageLoader;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.TemplateUtils;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {


    static PreferenceManager preferenceManager;
    Activity context;
    List<FavoriteList> favoriteList;
    AppDatabase favoriteDatabase;
    String templateTypePoster;
    String ratio;
    private InterstitialsAdsManager interstitialsAdsManager;

    FavoriteList model;

    public FavouriteAdapter(Activity context, List<FavoriteList> favoriteList, String templateTypePoster) {

        this.context = context;
        this.favoriteList = favoriteList;
        favoriteDatabase = AppDatabase.getDatabase(context);
        preferenceManager = new PreferenceManager(context);
        this.templateTypePoster = templateTypePoster;

        interstitialsAdsManager = new InterstitialsAdsManager(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View inflate;
        if (viewType == 0) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_view, viewGroup, false);
        } else {


            if (templateTypePoster.contains(Constant.TEMPLATE_TYPE_POSTER)) {


                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poster_adapter_list, viewGroup, false);

            } else {

                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_template_adapter_list, viewGroup, false);

            }

        }
        return new MyViewHolder(inflate);

    }

    public class MyTouchListenerCustom implements View.OnTouchListener {

        MyViewHolder holder;

        public MyTouchListenerCustom(MyViewHolder holder) {
            this.holder = holder;
        }

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


                interstitialsAdsManager.showInterstitialAd(() -> goToNextActivity(model));

                return true;
            }
        });

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);


            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_test);
            animation.setDuration(300);
            holder.itemView.startAnimation(animation);

            return true;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        int itemViewType = holder.getItemViewType();


        if (itemViewType == 0) {

            new AdsUtils(context).loadNativeAd(context, holder.fbPlaceHolder);


        } else if (itemViewType == 1) {


            model = favoriteList.get(position);

            if (model.isPremium()) {
                holder.ivPremiumLottie.setVisibility(View.VISIBLE);
            } else {
                holder.ivPremiumLottie.setVisibility(View.GONE);
            }

            if (templateTypePoster.contains(TEMPLATE_TYPE_VIDEO)) {

                TemplateModel templateModel1 = new TemplateModel();

                templateModel1.setCode(model.getCode());
                templateModel1.setPremium(model.isPremium());
                templateModel1.setTemplate_json(model.getTemplate_json());
                templateModel1.setZip_link(model.getZip_link());
                templateModel1.setZip_link_preview(model.getZip_link_preview());
                templateModel1.setCreated(model.getCreated());
                templateModel1.setViews(model.getViews());
                templateModel1.setResImageNum(model.getResImageNum());
                templateModel1.setCategory(model.getCategory());

                TemplateListAdapter.showLottieAnimation(context, holder.lottieAnimationView, templateModel1, holder.loadingAnim);

                holder.lottieAnimationView.setOnTouchListener(new MyTouchListenerCustom(holder));


            } else {

                ratio = model.getTemplate_w_h_ratio();

                if (this.ratio != null) {
                    if (this.ratio.equalsIgnoreCase("720:1280")) {
                        holder.ivImage1.setVisibility(View.VISIBLE);
                        holder.videoThumb.setVisibility(View.GONE);
                        new GlideImageLoader(holder.ivImage1, holder.progressBar1)
                                .load(model.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
                    } else {
                        holder.ivImage1.setVisibility(View.GONE);
                        holder.videoThumb.setVisibility(View.VISIBLE);
                        new GlideImageLoader(holder.videoThumb, holder.progressBar1)
                                .load(model.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
                    }
                } else {
                    holder.ivImage1.setVisibility(View.GONE);
                    holder.videoThumb.setVisibility(View.VISIBLE);
                    new GlideImageLoader(holder.videoThumb, holder.progressBar1)
                            .load(model.getPost_thumb(), new RequestOptions().priority(Priority.HIGH));
                }

                Picasso.get().load(model.getPost_thumb()).placeholder(R.drawable.bg_card).into(holder.videoThumb);

                holder.videoThumb.setOnTouchListener(new View.OnTouchListener() {
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

                            interstitialsAdsManager.showInterstitialAd(() -> goToNextActivity(model));

                            return true;
                        }


                    });


                    @Override
                    public boolean onTouch(View v, MotionEvent event) {


                        gestureDetector.onTouchEvent(event);
                        return true;
                    }

                });
            }

            holder.downloadTextCount.setText("" + MyUtils.format(model.getCreated()));

            holder.favourite.setImageResource(R.drawable.ic_delete);

            holder.favourite.setOnClickListener(view -> {

                FavoriteList favoriteList1 = favoriteList.get(position);

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                animation.setDuration(300);

                holder.itemView.startAnimation(animation);

                favoriteList.remove(position);

                favoriteDatabase.myDao().delete(favoriteList1);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                }, animation.getDuration());


            });

        }
    }

    private void goToNextActivity(FavoriteList list) {

        if (list.getTemplate_type().contains(Constant.TEMPLATE_TYPE_POSTER)) {

            new TemplateUtils(context).openPosterActivity(list.getPoster_id());

        }
        if (list.getTemplate_type().contains(TEMPLATE_TYPE_VIDEO)) {

            // video
            TemplateModel templateModel = new TemplateModel();

            templateModel.setCode(list.getCode());
            templateModel.setPremium(list.isPremium());
            templateModel.setTemplate_json(list.getTemplate_json());
            templateModel.setZip_link(list.getZip_link());
            templateModel.setZip_link_preview(list.getZip_link_preview());
            templateModel.setCreated(list.getCreated());
            templateModel.setViews(list.getViews());
            templateModel.setResImageNum(list.getResImageNum());
            templateModel.setCategory(list.getCategory());

            MyUtils.templateModel = templateModel;

            Intent intent = new Intent(context, PreviewVideoTemplate.class);

            context.startActivity(intent);

        }


    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    @Override
    public int getItemViewType(int i) {
        return this.favoriteList.get(i) != null ? 1 : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView videoThumb, ivImage1;
        private final FrameLayout fbPlaceHolder;
        ImageView favourite;
        LottieAnimationView loadingAnim;
        LottieAnimationView lottieAnimationView;
        private TextView downloadTextCount;
        private ProgressBar progressBar1;
        private ImageView ivPremiumLottie;


        public MyViewHolder(View view) {
            super(view);
            videoThumb = view.findViewById(R.id.iv_image);
            favourite = view.findViewById(R.id.favourite);
            downloadTextCount = view.findViewById(R.id.downloadTextCount);
            fbPlaceHolder = itemView.findViewById(R.id.fl_adplaceholder);
            ivImage1 = view.findViewById(R.id.iv_image1);
            progressBar1 = view.findViewById(R.id.progressBar1);
            ivPremiumLottie = view.findViewById(R.id.ivPremium);

            if (templateTypePoster.contains(TEMPLATE_TYPE_VIDEO)) {

                loadingAnim = itemView.findViewById(R.id.loading_anim);
                lottieAnimationView = itemView.findViewById(R.id.lottie);

            }


        }
    }
}
