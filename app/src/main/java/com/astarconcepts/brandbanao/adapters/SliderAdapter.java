package com.astarconcepts.brandbanao.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.model.SliderItem;
import com.astarconcepts.brandbanao.ui.activities.PreviewActivity;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private Activity context;
    private List<SliderItem> mSliderItems;

    public SliderAdapter(Activity context, List<SliderItem> mSliderItemsData) {
        this.context = context;
        this.mSliderItems = mSliderItemsData;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);



        Glide.with(viewHolder.itemView)
                .load(sliderItem.getSliderImage())
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(v -> {

            if (sliderItem.getSliderType() != null) {
                if (sliderItem.getSliderType().equals(Constant.EXTERNAL)) {

                    context.startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(sliderItem.getSliderUrl()), "text/plain").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                } else if (sliderItem.getSliderType().equals(context.getString(R.string.subscription))) {
                    MyUtils.openPlanActivity(context);
                } else if (sliderItem.getSliderType().equals(Constant.CATEGORY)) {

                    Intent intent = new Intent(context, PreviewActivity.class);
                    intent.putExtra(Constant.INTENT_TYPE, Constant.CATEGORY);
                    intent.putExtra(Constant.INTENT_FEST_ID, sliderItem.getBannerValue());
                    intent.putExtra(Constant.INTENT_FEST_NAME, sliderItem.getCategory_name());
                    intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                    intent.putExtra(Constant.INTENT_VIDEO,false);
                    intent.putExtra("From", Constant.CATEGORY);
                    context.startActivity(intent);
                }
            } else {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterViewHolder extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}