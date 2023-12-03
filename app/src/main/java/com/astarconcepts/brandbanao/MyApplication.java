package com.astarconcepts.brandbanao;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.astarconcepts.brandbanao.AdsUtils.AppOpenManager;
import com.astarconcepts.brandbanao.custom.poster.fragment.PosterFragment;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.NetworkConnectivity;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    public static Context context;
    public AppOpenManager appOpenAdManager;
    public static MyApplication myApplication;
    private static MyApplication mInstance;
    PreferenceManager preferenceManager;
    NetworkConnectivity networkConnectivity;
    private RequestQueue mRequestQueue;

    public static int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public static int getColumnWidth(int column, float grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    public void showAppOpenAds() {
        try {
            if (preferenceManager.getBoolean(Constant.OPEN_APP_AD_ENABLED) && preferenceManager.getBoolean(Constant.ADS_ENABLE) && !preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)) {
                appOpenAdManager = new AppOpenManager(myApplication, preferenceManager.getString(Constant.OPEN_AD_ID));
            }
        } catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
        }
    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            synchronized (MyApplication.class) {
                myApplication = mInstance;
            }
        }
        return myApplication;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            VolleyLog.DEBUG = true;
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(PosterFragment.TAG);
        Log.d("URLCheck", "" + request.getUrl());


        request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(request);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = myApplication = this;
        mInstance = MyApplication.this;
        MyApplication.context = getApplicationContext();

        networkConnectivity = new NetworkConnectivity(this);
        preferenceManager = new PreferenceManager(this);

        FirebaseApp.initializeApp(this);

        context = this;
        if (networkConnectivity.isConnected()) {
            AppConfig.IS_CONNECTED = true;
        } else {
            AppConfig.IS_CONNECTED = false;
        }

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(preferenceManager.getString(Constant.ONESIGNAL_APP_ID));
        // Show App open Ads

        showAppOpenAds();

        // Admob Ads Initialization
        MobileAds.initialize(this, initializationStatus -> {
        });


    }
}
