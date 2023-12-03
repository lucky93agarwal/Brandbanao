package com.astarconcepts.brandbanao.respository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.api.ApiService;
import com.astarconcepts.brandbanao.api.ApiStatus;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelAudio;
import com.astarconcepts.brandbanao.model.AppInfos;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.CouponItem;
import com.astarconcepts.brandbanao.model.DigitalCardModel;
import com.astarconcepts.brandbanao.model.FeatureItem;
import com.astarconcepts.brandbanao.model.FestivalItem;
import com.astarconcepts.brandbanao.model.FrameResponse;
import com.astarconcepts.brandbanao.model.HomeItem;
import com.astarconcepts.brandbanao.model.LanguageItem;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.model.ServiceItem;
import com.astarconcepts.brandbanao.model.SliderItem;
import com.astarconcepts.brandbanao.model.SubsPlanItem;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRespository {


    private ApiService apiService;

    public HomeRespository() {
        apiService = ApiClient.getApiDataService();
    }

    public LiveData<HomeItem> getHomeData() {
        MutableLiveData<HomeItem> data = new MutableLiveData<>();
        apiService.getHomeDatas().enqueue(new Callback<HomeItem>() {
            @Override
            public void onResponse(Call<HomeItem> call, Response<HomeItem> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeItem> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<CouponItem> validateCoupon(String userId, String couponCode) {
        MutableLiveData<CouponItem> data = new MutableLiveData<>();
        apiService.validateCoupon(userId, couponCode).enqueue(new Callback<CouponItem>() {
            @Override
            public void onResponse(Call<CouponItem> call, Response<CouponItem> response) {
                Log.d("response___t", "" + response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CouponItem> call, Throwable t) {
                data.setValue(null);
                Log.d("response___t", "E-> " + t.getMessage());
            }
        });
        return data;
    }

    public LiveData<List<FeatureItem>> getGreetingData(String categoryid, int pagecount) {
        MutableLiveData<List<FeatureItem>> data = new MutableLiveData<>();
        apiService.getGreetingData(categoryid, pagecount).enqueue(new Callback<List<FeatureItem>>() {
            @Override
            public void onResponse(Call<List<FeatureItem>> call, Response<List<FeatureItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<FeatureItem>> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<DigitalCardModel>> getBusinessCards() {
        MutableLiveData<List<DigitalCardModel>> data = new MutableLiveData<>();
        apiService.getDigitalBusinessCardList().enqueue(new Callback<List<DigitalCardModel>>() {
            @Override
            public void onResponse(Call<List<DigitalCardModel>> call, Response<List<DigitalCardModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DigitalCardModel>> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<DigitalCardModel>> getMybusinesslist(String userId) {
        MutableLiveData<List<DigitalCardModel>> data = new MutableLiveData<>();
        apiService.getMybusinesslist(userId).enqueue(new Callback<List<DigitalCardModel>>() {
            @Override
            public void onResponse(Call<List<DigitalCardModel>> call, Response<List<DigitalCardModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DigitalCardModel>> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<ApiStatus> addBusinessCard(String userId, Integer cardId) {
        MutableLiveData<ApiStatus> data = new MutableLiveData<>();
        apiService.addBusinessCard(userId, cardId).enqueue(new Callback<ApiStatus>() {
            @Override
            public void onResponse(Call<ApiStatus> call, Response<ApiStatus> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ApiStatus> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<SubsPlanItem>> getSubscriptionsPlanList() {
        MutableLiveData<List<SubsPlanItem>> data = new MutableLiveData<>();
        apiService.getSubscriptionsPlanList().enqueue(new Callback<List<SubsPlanItem>>() {
            @Override
            public void onResponse(Call<List<SubsPlanItem>> call, Response<List<SubsPlanItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<SubsPlanItem>> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<List<ServiceItem>> getAllService() {
        MutableLiveData<List<ServiceItem>> data = new MutableLiveData<>();
        apiService.getAllService().enqueue(new Callback<List<ServiceItem>>() {
            @Override
            public void onResponse(Call<List<ServiceItem>> call, Response<List<ServiceItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ServiceItem>> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<CategoryItem>> getCategories(String type) {
        MutableLiveData<List<CategoryItem>> data = new MutableLiveData<>();
        apiService.getCategories(type).enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<List<PostItem>> getAllPostsByCategory(int page, String type,String postType, String categoryid, String language, boolean isVideo) {
        MutableLiveData<List<PostItem>> data = new MutableLiveData<>();
        apiService.getAllPostsByCategory(page, type,postType, categoryid, language, isVideo).enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                t.printStackTrace();
                Log.d("errors","rrrpr"+t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<CategoryItem>> getBusinessSubCategory(String type, String subCatid) {
        MutableLiveData<List<CategoryItem>> data = new MutableLiveData<>();
        apiService.getBusinessSubCategory(type, subCatid).enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<HomeItem> getSubBusinessCatHome(String subCategoryId) {

        MutableLiveData<HomeItem> data = new MutableLiveData<>();

        apiService.getSubBusinessCatHome(subCategoryId).enqueue(new Callback<HomeItem>() {
            @Override
            public void onResponse(Call<HomeItem> call, Response<HomeItem> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeItem> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;

    }


    public LiveData<List<SliderItem>> getHomeBanners() {

        MutableLiveData<List<SliderItem>> data = new MutableLiveData<>();

        apiService.getAllBannerss().enqueue(new Callback<List<SliderItem>>() {
            @Override
            public void onResponse(Call<List<SliderItem>> call, Response<List<SliderItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<SliderItem>> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;

    }


    public LiveData<List<FestivalItem>> getFestivals(String type, boolean video) {

        MutableLiveData<List<FestivalItem>> data = new MutableLiveData<>();

        apiService.getFestivals(type,video).enqueue(new Callback<List<FestivalItem>>() {
            @Override
            public void onResponse(Call<List<FestivalItem>> call, Response<List<FestivalItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<FestivalItem>> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;

    }

    public LiveData<List<PostItem>> getDailyPosts(int page, String catid, String language) {
        MutableLiveData<List<PostItem>> data = new MutableLiveData<>();
        apiService.getDailyPostData(page, catid, language).enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getCustomFrame(String ratio, String type, String user_id) {
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getCustomFrames(user_id,type, ratio).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<List<ModelAudio>> getAllMusic(int page, int catId,String langId) {
        MutableLiveData<List<ModelAudio>> data = new MutableLiveData<>();
        apiService.getAllMusic(page, catId, langId).enqueue(new Callback<List<ModelAudio>>() {
            @Override
            public void onResponse(Call<List<ModelAudio>> call, Response<List<ModelAudio>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ModelAudio>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<List<LanguageItem>> getLanguagess() {


        MutableLiveData<List<LanguageItem>> data = new MutableLiveData<>();

        apiService.getLanguagess().enqueue(new Callback<List<LanguageItem>>() {
            @Override
            public void onResponse(Call<List<LanguageItem>> call, Response<List<LanguageItem>> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<LanguageItem>> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<AppInfos> getAppInfo() {

        MutableLiveData<AppInfos> data = new MutableLiveData<>();

        apiService.getAppInfo().enqueue(new Callback<AppInfos>() {
            @Override
            public void onResponse(Call<AppInfos> call, Response<AppInfos> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<AppInfos> call, Throwable t) {

                Log.d("errrorrr",""+t.getMessage());

                t.printStackTrace();
                data.setValue(null);

            }
        });
        return data;
    }


}
