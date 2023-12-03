package com.astarconcepts.brandbanao.respository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.api.ApiService;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailInfo;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailThumbFull;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailWithList;
import com.astarconcepts.brandbanao.model.video.DashboardTemplateResponseMain;
import com.astarconcepts.brandbanao.model.video.TemplateResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplateBasedRepository {

    ApiService apiService;


    public TemplateBasedRepository() {

        apiService = ApiClient.getApiDataService();
    }

    public LiveData<DashboardTemplateResponseMain> getDashboardTemplate(Integer page) {

        MutableLiveData<DashboardTemplateResponseMain> data = new MutableLiveData<>();
        apiService.getDashboardTemplate(page).enqueue(new Callback<DashboardTemplateResponseMain>() {
            @Override
            public void onResponse(Call<DashboardTemplateResponseMain> call, Response<DashboardTemplateResponseMain> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<DashboardTemplateResponseMain> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<TemplateResponse> getSearchedTemplates(Integer page, String cat, Integer limit) {

        MutableLiveData<TemplateResponse> data = new MutableLiveData<>();


        apiService.getSearchedTemplates(page, cat, limit).enqueue(new Callback<TemplateResponse>() {
            @Override
            public void onResponse(Call<TemplateResponse> call, Response<TemplateResponse> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<TemplateResponse> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }


    public LiveData<ThumbnailWithList> getPosterHome(Integer page) {

        MutableLiveData<ThumbnailWithList> data = new MutableLiveData<>();


        apiService.getPosterHome(page).enqueue(new Callback<ThumbnailWithList>() {
            @Override
            public void onResponse(Call<ThumbnailWithList> call, Response<ThumbnailWithList> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<ThumbnailWithList> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }


    public LiveData<ThumbnailInfo> getPosterData(Integer page) {

        MutableLiveData<ThumbnailInfo> data = new MutableLiveData<>();

        apiService.getPosterData(page).enqueue(new Callback<ThumbnailInfo>() {
            @Override
            public void onResponse(Call<ThumbnailInfo> call, Response<ThumbnailInfo> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<ThumbnailInfo> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<ThumbBG> getPosterStickers() {
        MutableLiveData<ThumbBG> data = new MutableLiveData<>();
        apiService.getPosterStickers().enqueue(new Callback<ThumbBG>() {
            @Override
            public void onResponse(Call<ThumbBG> call, Response<ThumbBG> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ThumbBG> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ThumbBG> getPosterBackground() {
        MutableLiveData<ThumbBG> data = new MutableLiveData<>();
        apiService.getPosterBackground().enqueue(new Callback<ThumbBG>() {
            @Override
            public void onResponse(Call<ThumbBG> call, Response<ThumbBG> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ThumbBG> call, Throwable t) {
                t.printStackTrace();
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<ThumbnailThumbFull> getPosterSearch(Integer page, String cat) {
        MutableLiveData<ThumbnailThumbFull> data = new MutableLiveData<>();
        apiService.getPosterSearch(page, cat).enqueue(new Callback<ThumbnailThumbFull>() {
            @Override
            public void onResponse(Call<ThumbnailThumbFull> call, Response<ThumbnailThumbFull> response) {
                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<ThumbnailThumbFull> call, Throwable t) {
                t.printStackTrace();
                data.setValue(null);
            }
        });
        return data;
    }

}
