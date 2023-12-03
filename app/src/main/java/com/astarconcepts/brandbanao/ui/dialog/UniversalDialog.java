package com.astarconcepts.brandbanao.ui.dialog;


import static com.astarconcepts.brandbanao.utils.Constant.DARK_MODE_ON;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

public class UniversalDialog {

    public TextView msgTextView;
    public TextView titleTextView;
    public TextView descriptionTextView;
    public AppCompatButton oKButton;
    public AppCompatButton canceLButton;
    public AppCompatButton halfButton;
    public RatingBar ratingBar;
    public FrameLayout flNative;
    public float newRating;
    public Activity activity;
    public LottieAnimationView successAnim;
    public LottieAnimationView errorAnim;
    public LottieAnimationView warningAnim;
    public LottieAnimationView confirmAnim;
    public Button okBtn;
    public Button cancelBtn;
    public LottieAnimationView deleteAnim;
    public CardView cbRazorPay;
    public ProgressBar pbPayment;
    public ImageView ivPayCancel;
    public EditText etCode;
    public TextView textView;
    public TextView tvError;
    public TextView tvCode;
    public TextView tvCodeDec;
    public TextView tvPrice;
    public Button btnApply;
    public RelativeLayout rlOpen;
    public ConstraintLayout csApplied;
    public int finalPrice;
    public ImageView ivCancel;
    public RoundedImageView ivOffer;
    public CardView cvOffer;
    PreferenceManager preferenceManager;
    private ImageView imageView;
    private Dialog dialog;
    private View view;
    private boolean cancelable;
    private boolean attached = false;
    private ImageView cancel;

    ProgressDialog progressDialog;

    public UniversalDialog(Activity activity, Boolean cancelable) {
        this.activity = activity;
        this.dialog = new Dialog(activity);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.cancelable = cancelable;

        preferenceManager = new PreferenceManager(activity);
        if (preferenceManager.getString(DARK_MODE_ON).equals("yes")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    public  void showLoadingDialog(Activity activity, String message) {


        if (progressDialog == null) {

            progressDialog = new ProgressDialog(activity);

            progressDialog.setMessage(message);

            progressDialog.show();

            return;
        }

        progressDialog.show();

    }

    public  void dissmissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }


    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void showAppInfoDialog(String updateText, String cancelText, String title, String description) {
        this.dialog.setContentView(R.layout.dialog_app_info);
        descriptionTextView = dialog.findViewById(R.id.descriptionTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);

        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        msgTextView = dialog.findViewById(R.id.titleTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        msgTextView.setText(title);
        descriptionTextView.setText(Html.fromHtml(description,Html.FROM_HTML_MODE_COMPACT));
        okBtn.setText(updateText);
        cancelBtn.setText(cancelText);

        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(descriptionTextView, "font_body_xs_size");
        GlideDataBinding.setTextSize(cancelBtn, "button_text_12");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");

        GlideDataBinding.setFont(descriptionTextView, "bold");
        GlideDataBinding.setFont(msgTextView, "extra_bold");
        GlideDataBinding.setFont(cancelBtn, "medium");
        GlideDataBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));
            dialog.setCancelable(cancelable);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            okBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }
    }

    public void showSuccessDialog(String message, String okTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        successAnim = dialog.findViewById(R.id.success_animation);
        successAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

      //  okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.green_A700));

        titleTextView.setText(dialog.getContext().getString(R.string.success));
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideDataBinding.setTextSize(titleTextView, "font_body_size");
        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");

        GlideDataBinding.setFont(titleTextView, "extra_bold");
        GlideDataBinding.setFont(msgTextView, "bold");
        GlideDataBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }
    }

    public void showErrorDialog(String message, String okTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        errorAnim = dialog.findViewById(R.id.error_animation);
        errorAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);


        titleTextView.setText(dialog.getContext().getString(R.string.error));
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideDataBinding.setTextSize(titleTextView, "font_body_size");
        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");

        GlideDataBinding.setFont(titleTextView, "extra_bold");
        GlideDataBinding.setFont(msgTextView, "bold");
        GlideDataBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            this.dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
            cancelBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }
    }

    public void showWarningDialog(String title, String message, String okTitle, boolean cancelable) {
        this.dialog.setContentView(R.layout.dialog_message);
        warningAnim = dialog.findViewById(R.id.warn_animation);
        warningAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);

        if (cancelable) {
            cancelBtn.setVisibility(View.VISIBLE);
        }

        titleTextView.setText(title);
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideDataBinding.setTextSize(titleTextView, "font_body_size");
        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");
        GlideDataBinding.setTextSize(cancelBtn, "button_text_12");

        GlideDataBinding.setFont(titleTextView, "extra_bold");
        GlideDataBinding.setFont(msgTextView, "bold");
        GlideDataBinding.setFont(okBtn, "medium");
        GlideDataBinding.setFont(cancelBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
            cancelBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }


    }


    public void setCustomAnimationResource(String lottieName) {
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.warn_animation);
        lottieAnimationView.setAnimation(lottieName);

    }


    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void showConfirmDialog(String title, String message, String okTitle, String cancelTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        confirmAnim = dialog.findViewById(R.id.confirm_animation);
        confirmAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);
        cancelBtn.setVisibility(View.VISIBLE);
        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        titleTextView.setText(title);
        titleTextView.setAllCaps(true);
        msgTextView.setText(message);
        okBtn.setText(okTitle);
        cancelBtn.setText(cancelTitle);

        GlideDataBinding.setTextSize(titleTextView, "font_body_size");
        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");
        GlideDataBinding.setTextSize(cancelBtn, "button_text_12");

        GlideDataBinding.setFont(titleTextView, "extra_bold");
        GlideDataBinding.setFont(msgTextView, "bold");
        GlideDataBinding.setFont(okBtn, "medium");
        GlideDataBinding.setFont(cancelBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
            cancelBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }
    }

    public void showOfferDialog(String imageUrl) {
        this.dialog.setContentView(R.layout.offer_dialog);

        ivOffer = dialog.findViewById(R.id.iv_offer);
        ivCancel = dialog.findViewById(R.id.iv_cancel);
        cvOffer = dialog.findViewById(R.id.cardView7);

        GlideDataBinding.bindImage(ivOffer, imageUrl);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
        }
    }

    public void showDeleteDialog(String title, String message, String okTitle, String cancelTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        deleteAnim = dialog.findViewById(R.id.delete_animation);
        deleteAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);
        cancelBtn.setVisibility(View.VISIBLE);
        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        titleTextView.setText(title);
        titleTextView.setAllCaps(true);
        msgTextView.setText(message);
        okBtn.setText(okTitle);
        cancelBtn.setText(cancelTitle);

        GlideDataBinding.setTextSize(titleTextView, "font_body_size");
        GlideDataBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideDataBinding.setTextSize(okBtn, "button_text_12");
        GlideDataBinding.setTextSize(cancelBtn, "button_text_12");

        GlideDataBinding.setFont(titleTextView, "extra_bold");
        GlideDataBinding.setFont(msgTextView, "bold");
        GlideDataBinding.setFont(okBtn, "medium");
        GlideDataBinding.setFont(cancelBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> {


                UniversalDialog.this.cancel();
            });
            cancelBtn.setOnClickListener(view -> UniversalDialog.this.cancel());
        }
    }


}
