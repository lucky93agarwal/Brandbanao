package com.astarconcepts.brandbanao.api;

import android.util.Log;

import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static {
        System.loadLibrary("brandboost_library");
    }

    public static native String baseUrlFromJNI();

    public static String App_URl = baseUrlFromJNI();

    public static native List<String> getCommands2(String tempVideoPath, String watermarkPath, boolean isWatermarkEnabled, String musicPath, boolean isMute, String outputPath);

    public static native String getFilterComplex();

    public static ApiService getApiDataService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", new PreferenceManager(MyApplication.getAppContext()).getString(Constant.api_key))
                            .header("Content-Type", "text/plain")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);


                    int tryCount = 0;
                    while (!response.isSuccessful() && tryCount < 3) {

                        Log.d("intercept", "Request is not successful - " + tryCount);

                        tryCount++;

                        // retry the request
                        response.close();
                        response = chain.proceed(original);
                    }


                    return response;
                })
                .build();



        return new Retrofit.Builder()
                .baseUrl(baseUrlFromJNI())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService.class);
    }

}
