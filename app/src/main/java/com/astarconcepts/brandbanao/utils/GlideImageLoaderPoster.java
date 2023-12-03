package com.astarconcepts.brandbanao.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.astarconcepts.brandbanao.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideImageLoaderPoster {
    public ProgressBar mProgressBar;
    private ImageView mImageView;

    public GlideImageLoaderPoster(ImageView imageView, ProgressBar progressBar) {
        this.mImageView = imageView;
        this.mProgressBar = progressBar;
    }

    public void load(final String str, RequestOptions requestOptions) {
        if (str != null && requestOptions != null) {
            onConnecting();
            ProgressAppGlideModule.expect(str, new ProgressAppGlideModule.UIonProgressListener() {
                public float getGranualityPercentage() {
                    return 1.0f;
                }

                public void onProgress(long j, long j2) {
                    if (GlideImageLoaderPoster.this.mProgressBar != null) {
                        GlideImageLoaderPoster.this.mProgressBar.setProgress((int) ((j * 100) / j2));
                    }
                }
            });

            Glide.with(MyApplication.getAppContext()).load(str).transition((TransitionOptions) DrawableTransitionOptions.withCrossFade()).thumbnail(0.01f).apply(requestOptions.skipMemoryCache(true)).listener((RequestListener) new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                    ProgressAppGlideModule.forget(str);
                    GlideImageLoaderPoster.this.onFinished();
                    return false;
                }

                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                    ProgressAppGlideModule.forget(str);
                    GlideImageLoaderPoster.this.onFinished();
                    return false;
                }
            }).into(this.mImageView);

        }
    }

    private void onConnecting() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    public void onFinished() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null && this.mImageView != null) {
            progressBar.setVisibility(View.GONE);
            this.mImageView.setVisibility(View.VISIBLE);
        }
    }
}
