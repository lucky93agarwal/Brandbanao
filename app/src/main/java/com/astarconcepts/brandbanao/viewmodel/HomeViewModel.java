package com.astarconcepts.brandbanao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.astarconcepts.brandbanao.api.ApiStatus;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelAudio;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
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
import com.astarconcepts.brandbanao.model.video.DashboardResponseModel;
import com.astarconcepts.brandbanao.model.video.DashboardTemplateResponseMain;
import com.astarconcepts.brandbanao.model.video.TemplateResponse;
import com.astarconcepts.brandbanao.respository.HomeRespository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private HomeRespository respository;

    public HomeViewModel() {
        respository = new HomeRespository();
    }

    public LiveData<HomeItem> getHomeData() {
        return respository.getHomeData();
    }

    public LiveData<CouponItem> validateCoupon(String userId, String promo) {
        return respository.validateCoupon(userId, promo);
    }

    public LiveData<List<FeatureItem>> getGreetingData(String categoryid, int pagecount) {
        return respository.getGreetingData(categoryid, pagecount);
    }


    public LiveData<List<SubsPlanItem>> getSubscriptionsPlanList() {
        return respository.getSubscriptionsPlanList();
    }
    public LiveData<List<ServiceItem>> getAllService() {
        return respository.getAllService();
    }

    public LiveData<List<PostItem>> getAllPostsByCategory(int page, String type,String postType, String categoryid, String language, boolean isVideo) {
        return respository.getAllPostsByCategory(page, type, postType, categoryid, language, isVideo);
    }

    public LiveData<List<CategoryItem>> getBusinessSubCategory(String type, String subCatid) {
        return respository.getBusinessSubCategory(type, subCatid);
    }


    public LiveData<List<PostItem>> getDailyPosts(int page, String catid, String language) {
        return respository.getDailyPosts(page, catid, language);
    }

    public LiveData<List<CategoryItem>> getCategories(String type) {

        return respository.getCategories(type);
    }

    public LiveData<FrameResponse> getFrames(String ratio, String type, String userId) {

        return respository.getCustomFrame(userId,type, ratio);
    }


    public LiveData<List<ModelAudio>> getAllMusic(int page, Integer catId, String langId) {

        return respository.getAllMusic(page, catId, langId);
    }


    public LiveData<List<LanguageItem>> getLanguagess() {

        return respository.getLanguagess();
    }

    public LiveData<AppInfos> getAppInfo() {

        return respository.getAppInfo();
    }


    public LiveData<List<SliderItem>> getHomeBanners() {

        return respository.getHomeBanners();
    }


   public LiveData<List<FestivalItem>> getFestivals(String type, boolean video) {

        return respository.getFestivals(type,video);
    }

    public LiveData<HomeItem> getSubBusinessCatHome(String subCategoryId) {

        return respository.getSubBusinessCatHome(subCategoryId);
    }

    public LiveData<List<DigitalCardModel>> getBusinessCards() {
        return respository.getBusinessCards();

    }

    public LiveData<List<DigitalCardModel>> getMybusinesslist(String userId) {
        return respository.getMybusinesslist(userId);

    }

    public LiveData<ApiStatus> addBusinessCard(String userId, Integer cardId) {
        return respository.addBusinessCard(userId, cardId);

    }


}
