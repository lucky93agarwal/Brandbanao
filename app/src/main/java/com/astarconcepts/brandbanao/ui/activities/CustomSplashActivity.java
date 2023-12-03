package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.utils.Constant.DARK_MODE_ON;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.FrameUtils;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.NetworkConnectivity;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;
import com.astarconcepts.brandbanao.viewmodel.HomeViewModel;
import com.astarconcepts.brandbanao.viewmodel.UserViewModel;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

@SuppressLint("CustomSplashScreen")
public class CustomSplashActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    UniversalDialog universalDialog;
    String status = "";
    NetworkConnectivity networkConnectivity;

    UserViewModel userViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.hideNavigation(this, false);
        setContentView(R.layout.activity_splashy);

        preferenceManager = new PreferenceManager(this);
        universalDialog = new UniversalDialog(this, false);
        status = preferenceManager.getString(Constant.STATUS);

        networkConnectivity = new NetworkConnectivity(this);

        if (preferenceManager.getString(DARK_MODE_ON).equals("yes")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        loadData();
    }

    private void getUserDetails() {

        if (!preferenceManager.getBoolean(Constant.IS_LOGIN)) {

            gotoMainActivity();

            return;
        }


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserById(preferenceManager.getString(Constant.USER_ID)).observe(this, userItem -> {

            if (userItem != null) {

                MyUtils.showResponse(userItem);

                status = userItem.status;
                preferenceManager.setInt(Constant.USER_BUSINESS_LIMIT, userItem.businessLimit);
                preferenceManager.setString(Constant.PLAN_NAME, userItem.planName);
                preferenceManager.setString(Constant.PLAN_ID, userItem.planId);
                preferenceManager.setString(Constant.PLAN_DURATION, userItem.planDuration);
                preferenceManager.setString(Constant.PLAN_START_DATE, userItem.planStartDate);
                preferenceManager.setString(Constant.PLAN_END_DATE, userItem.planEndDate);
                preferenceManager.setString(Constant.USER_EMAIL, userItem.email);
                preferenceManager.setString(Constant.USER_NAME, userItem.userName);
                preferenceManager.setString(Constant.USER_IMAGE, userItem.userImage);
                preferenceManager.setString(Constant.USER_PHONE, userItem.phone);
                preferenceManager.setString(Constant.USER_ID, userItem.userId);
                preferenceManager.setString(Constant.STATUS, userItem.status);
                preferenceManager.setString(Constant.USER_PHONE, userItem.phone);
                preferenceManager.setString(Constant.USER_DESIGNATION, userItem.designation);
                preferenceManager.setString(Constant.LOGIN_TYPE, userItem.login_type);
                preferenceManager.setBoolean(Constant.IS_SUBSCRIBE, userItem.isSubscribed);


                MyUtils.saveLogoPath(this, userItem.getUserImage(), new FrameUtils.OnLogoDownloadListener() {
                    @Override
                    public void onLogoDownloaded(String logoPath) {

                        preferenceManager.setString(Constant.USER_IMAGE_PATH, logoPath);

                        Log.d("logssso", "logo profile : " + logoPath);

                    }

                    @Override
                    public void onLogoDownloadError() {

                    }
                });

                if (status != null) {
                    if (status.equalsIgnoreCase("0")) {
                        showDialog(CustomSplashActivity.this, "Disable Account",
                                "Your Account is disable");
                    } else {
                        gotoMainActivity();
                    }
                } else {
                    gotoMainActivity();
                }
            } else {

                universalDialog.showErrorDialog(getString(R.string.user_error), getString(R.string.try_again));
                universalDialog.show();

                universalDialog.okBtn.setOnClickListener(v -> {
                    universalDialog.cancel();
                    loadData();
                });


            }

        });

    }

    public void loadData() {

        if (networkConnectivity.isConnected()) {
            FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build();
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            firebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(task -> {

                        Util.showLog("API_KEY: " + firebaseRemoteConfig.getString("apiKey"));
                        preferenceManager.setString(Constant.api_key, firebaseRemoteConfig.getString("apiKey"));

                        AppConfig.API_KEY = preferenceManager.getString(Constant.api_key);
                        loadAppData();

                    })
                    .addOnFailureListener(e -> {
                        Util.showErrorLog("Firebase", e);
                        gotoMainActivity();
                    });

        } else {
            Util.showLog("Internet is not connected");
            gotoMainActivity();
        }
    }

    public void loadAppData() {


        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getAppInfo().observe(this, appInfos -> {


            if (appInfos != null) {

                MyUtils.showResponse(appInfos);


                if (appInfos.getAppInfo() != null) {


                    preferenceManager.setString(Constant.PRIVACY_POLICY, appInfos.getAppInfo().getPrivacyPolicy());

                    preferenceManager.setString(Constant.TERM_CONDITION, appInfos.getAppInfo().getTermsCondition());

                    preferenceManager.setString(Constant.REFUND_POLICY, appInfos.getAppInfo().getRefundPolicy());

                    preferenceManager.setString(Constant.PRIVACY_POLICY_LINK, appInfos.getAppInfo().getPrivacyPolicy());

                    preferenceManager.setString(Constant.ONESIGNAL_APP_ID, appInfos.getAppInfo().getOnesignalAppId());


                    preferenceManager.setString(Constant.RAZORPAY_KEY_ID, appInfos.getAppInfo().getRazorpayKeyId());

                    preferenceManager.setString(Constant.PAYTM_ID, appInfos.getAppInfo().paytmMerchantId);
                    preferenceManager.setString(Constant.PAYTM_KEY, appInfos.getAppInfo().paytmMerchantKey);

                    preferenceManager.setString(Constant.STRIPE_KEY, appInfos.getAppInfo().stripePublishableKey);
                    preferenceManager.setString(Constant.STRIPE_SECRET_KEY, appInfos.getAppInfo().stripeSecretKey);

                    preferenceManager.setString(Constant.RazorPay, appInfos.getAppInfo().razorpayEnable);
                    preferenceManager.setString(Constant.Paytm, appInfos.getAppInfo().paytm_enable);
                    preferenceManager.setString(Constant.Stripe, appInfos.getAppInfo().stripeEnable);

                    preferenceManager.setString(Constant.CURRENCY, appInfos.getAppInfo().currency);

                }

                if (appInfos.getAdsModel() != null) {
                    preferenceManager.setString(Constant.PUBLISHER_ID, appInfos.getAdsModel().getAdmobAppId());


                    preferenceManager.setString(Constant.BANNER_AD_ID, appInfos.getAdsModel().getAdmobBanner());
                    preferenceManager.setString(Constant.INTERSTITIAL_AD_ID, appInfos.getAdsModel().getAdmobInter());
                    preferenceManager.setBoolean(Constant.ADS_ENABLE, appInfos.getAdsModel().getAdEnabled() == 1);
                    preferenceManager.setBoolean(Constant.INTERSTITIAL_AD_ENABLED, appInfos.getAdsModel().getInterstitialAdsEnable() == 1);
                    preferenceManager.setBoolean(Constant.BANNER_AD_ENABLED, appInfos.getAdsModel().getBannerAdsEnable() == 1);
                    preferenceManager.setBoolean(Constant.NATIVE_AD_ENABLED, appInfos.getAdsModel().getNativeAdsEnable() == 1);
                    preferenceManager.setBoolean(Constant.OPEN_APP_AD_ENABLED, appInfos.getAdsModel().getAppOpensAdsEnable() == 1);
                    preferenceManager.setBoolean(Constant.REWARD_AD_ENABLED, appInfos.getAdsModel().getRewardedAdsEnable() == 1);

                    preferenceManager.setInt(Constant.INTERSTITIAL_AD_CLICK, appInfos.getAdsModel().getAdmobInterCount());
                    preferenceManager.setString(Constant.NATIVE_AD_ID, appInfos.getAdsModel().getAdmobNative());
                    preferenceManager.setInt(Constant.NATIVE_AD_COUNT, appInfos.getAdsModel().getAdmobNativeCount());
                    preferenceManager.setString(Constant.REWARD_AD, appInfos.getAdsModel().getAdmobReward());
                    preferenceManager.setString(Constant.OPEN_AD_ID, appInfos.getAdsModel().getAdmobAppOpen());


                }


                if (appInfos.getOfferItem() != null) {

                    preferenceManager.setString(Constant.OFFER_IMAGE, appInfos.getOfferItem().image);

                } else {

                    preferenceManager.remove(Constant.OFFER_IMAGE);

                }


            }

            getUserDetails();


        });


    }


    private void gotoMainActivity() {
        Intent intent;

        if (getIntent().getBooleanExtra(Constant.INTENT_IS_FROM_NOTIFICATION, false)) {

            String type = preferenceManager.getString(Constant.PRF_TYPE);

            if (type.equals(Constant.FESTIVAL) || type.equals(Constant.CATEGORY) || type.equals(Constant.BUSINESS_SUB)) {
                intent = new Intent(CustomSplashActivity.this, PreviewActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, preferenceManager.getString(Constant.PRF_TYPE));
                intent.putExtra(Constant.INTENT_FEST_ID, preferenceManager.getString(Constant.PRF_ID));
                intent.putExtra(Constant.INTENT_FEST_NAME, preferenceManager.getString(Constant.PRF_NAME));
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                intent.putExtra(Constant.INTENT_VIDEO, false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (type.equals(Constant.EXTERNAL)) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(preferenceManager.getString(Constant.PRF_LINK)));
            } else {
                intent = new Intent(CustomSplashActivity.this, SubsPlanActivity.class);
            }

            startActivity(intent);
            finish();

        } else {
            if (preferenceManager.getBoolean(Constant.IS_LOGIN)) {
                intent = new Intent(CustomSplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(CustomSplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }

    public void showDialog(Activity activity, String msg, String msgn) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_message);

        TextView dialogTitleTextView = dialog.findViewById(R.id.dialogTitleTextView);
        dialogTitleTextView.setText(msg);

        TextView dialogMessageTextView = dialog.findViewById(R.id.dialogMessageTextView);
        dialogMessageTextView.setText(msgn);

        LinearLayout dialogOkButton = dialog.findViewById(R.id.dialogOkButton);
        LinearLayout dialogCancelButton = dialog.findViewById(R.id.dialogCancelButton);
        dialogOkButton.setOnClickListener(v -> {

            dialog.dismiss();
            Intent intent = new Intent(CustomSplashActivity.this, LoginActivity.class);
            intent.putExtra(Constant.LOGIN_TYPE, Constant.GOOGLE);
            startActivity(intent);

        });


        dialogCancelButton.setOnClickListener(v -> finish());

        dialog.show();

    }
}