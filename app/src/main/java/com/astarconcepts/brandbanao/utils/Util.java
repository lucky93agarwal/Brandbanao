package com.astarconcepts.brandbanao.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.BuildConfig;
import com.astarconcepts.brandbanao.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONException;

public class Util {

    private static Typeface fromAsset;
    private static Fonts currentTypeface;
    private static Dialog dialog;

    public static void showLog(String message) {

        if (BuildConfig.DEBUG) {
            Log.d("BrandBanao", message);
        }
    }

    public static void showErrorLog(String s, JSONException e) {
        if (BuildConfig.DEBUG) {
            try {
                StackTraceElement l = e.getStackTrace()[0];
                Log.d("BrandBanao", s);
                Log.d("BrandBanao", "Line : " + l.getLineNumber());
                Log.d("BrandBanao", "Method : " + l.getMethodName());
                Log.d("BrandBanao", "Class : " + l.getClassName());
            } catch (Exception ee) {
                Log.d("BrandBanao", "Error in psErrorLogE");
            }
        }
    }

    public static void showErrorLog(String log, Object obj) {
        if (BuildConfig.DEBUG) {
            try {
                Log.d("BrandBanao", log);
                Log.d("BrandBanao", "Line : " + getLineNumber());
                Log.d("BrandBanao", "Class : " + getClassName(obj));
            } catch (Exception ee) {
                Log.d("BrandBanao", "Error in psErrorLog");
            }
        }
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static String getClassName(Object obj) {
        return "" + (obj).getClass();
    }

    public static void fadeIn(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
                } else if (fonts == Fonts.ROBOTO_BOLD) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
                }
            }
        } else {
            if (fonts == Fonts.NOTO_SANS) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
            } else if (fonts == Fonts.ROBOTO_BOLD) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            }

            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public static void loadFirebase(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Util.showLog("Config params updated: " + updated);

                    } else {

                    }
                    preferenceManager.setString(Constant.api_key, mFirebaseRemoteConfig.getString("apiKey"));

                    AppConfig.API_KEY = preferenceManager.getString(Constant.api_key);
                })
                .addOnFailureListener(e -> Util.showErrorLog("Firebase", e));
    }

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showErrorLog(e.getMessage(), e);
        }
    }

    public enum Fonts {
        ROBOTO,
        NOTO_SANS,
        ROBOTO_LIGHT,
        ROBOTO_MEDIUM,
        ROBOTO_BOLD
    }

}
