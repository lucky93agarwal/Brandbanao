package com.astarconcepts.brandbanao.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Configure {
    public static File GetFileDir(Context context) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return context.getExternalFilesDir((String) null);
        }
        return context.getFilesDir();
    }

}
