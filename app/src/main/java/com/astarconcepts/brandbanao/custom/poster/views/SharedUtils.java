package com.astarconcepts.brandbanao.custom.poster.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtils {
    public static final String SHARED_NAME = "shared_file_new";
    public static final String SHARED_EARNMONEY_DEMO = "shared_earn_money_demo";
    public static final String ADMOB_ADS_MODEL = "ADMOB_ADS_MODEL";
    public static final String SETTING_MODEL = "setting_model";


    public static SharedPreferences instance;

    public static void init(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SHARED_NAME, 0);
        }

    }

    public static String getString(String sharedString) {
        return instance.getString(sharedString, "");
    }

    public static Integer getInteger(String sharedString) {
        return instance.getInt(sharedString, 0);
    }

    public static Boolean getBoolean(String tag) {
        return instance.getBoolean(tag, false);
    }

    public static void setInteger(String sharedString, int str) {
        Editor edit = instance.edit();
        edit.putInt(sharedString, str);
        edit.apply();
    }

    public static void setString(String sharedString, String str) {
        Editor edit = instance.edit();
        edit.putString(sharedString, str);
        edit.apply();
    }

    public static void set(String tag, Boolean isTapEnable) {
        Editor edit1 = instance.edit();
        edit1.putBoolean(tag, isTapEnable);
        edit1.apply();
    }
}
