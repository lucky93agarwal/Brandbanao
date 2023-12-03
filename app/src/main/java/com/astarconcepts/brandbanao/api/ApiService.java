package com.astarconcepts.brandbanao.api;

import com.astarconcepts.brandbanao.custom.animated_video.model.ModelAudio;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailInfo;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailThumbFull;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailWithList;
import com.astarconcepts.brandbanao.model.AppInfos;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.CouponItem;
import com.astarconcepts.brandbanao.model.DigitalCardModel;
import com.astarconcepts.brandbanao.model.FeatureItem;
import com.astarconcepts.brandbanao.model.FestivalItem;
import com.astarconcepts.brandbanao.model.FrameResponse;
import com.astarconcepts.brandbanao.model.HomeItem;
import com.astarconcepts.brandbanao.model.LanguageItem;
import com.astarconcepts.brandbanao.model.ModelCreated;
import com.astarconcepts.brandbanao.model.PaytmResponse;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.model.ServiceItem;
import com.astarconcepts.brandbanao.model.SliderItem;
import com.astarconcepts.brandbanao.model.StripeResponse;
import com.astarconcepts.brandbanao.model.SubsPlanItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.model.WhatsappOtpResponse;
import com.astarconcepts.brandbanao.model.video.DashboardTemplateResponseMain;
import com.astarconcepts.brandbanao.model.video.TemplateResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    //*** get user details****

    @GET("userDetail")
    Call<UserItem> getUserById(@Query("id") String userId);

    //*** login with google****

    @GET("userLoginGoogle")
    Call<UserItem> userLoginGoogle(
            @Query("username") String userName,
            @Query("email") String userEmail,
            @Query("image") String profilePhotoUrl,
            @Query("mobile_no") String userMobile,
            @Query("type") String type,
            @Query("user_id") String userId);

    //*** update profile****
    @Multipart
    @POST("profileUpdate")
    Call<UserItem> profileUpdate(
            @Part("user_id") RequestBody userId,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody userName,
            @Part("email") RequestBody userEmail,
            @Part("mobile_no") RequestBody phone,
            @Part("designation") RequestBody designation);


    //*** get all Business list****
    @FormUrlEncoded
    @POST("manageBusiness")
    Call<List<BusinessItem>> getBusinessList(@Field("userId") String userId, @Field("type") String type);

    //*** get default Business****
    @FormUrlEncoded
    @POST("manageBusiness")
    Call<BusinessItem> getBusinessDefaultList(@Field("userId") String userId, @Field("type") String type);

    @FormUrlEncoded
    @POST("paytm-payment")
    Call<PaytmResponse> createPaytmPayment(@Field("order_amount") String order_amount,
                                           @Field("order_id") String order_id,
                                           @Field("user_id") String user_id);

    //*** Add Business****
    @Multipart
    @POST("manageBusiness")
    Call<BusinessItem> submitBusiness(
            @Part("userId") RequestBody userId,
            @Part("business_id") RequestBody business_id,
            @Part("bussinessImage") RequestBody logo,
            @Part MultipartBody.Part file,
            @Part("business_name") RequestBody name,
            @Part("business_email") RequestBody email,
            @Part("business_number") RequestBody phone,
            @Part("business_website") RequestBody website,
            @Part("business_address") RequestBody address,
            @Part("business_instagram") RequestBody instagram,
            @Part("business_youtube") RequestBody youtube,
            @Part("business_facebook") RequestBody facebook,
            @Part("business_twitter") RequestBody twitter,
            @Part("business_tagline") RequestBody tagline,
            @Part("type") RequestBody type,
            @Part("business_category") RequestBody businesscategories);


    //*** UPdate Business****
    @Multipart
    @POST("manageBusiness")
    Call<BusinessItem> updateBusiness(
            @Part("userId") RequestBody userId,
            @Part("bussinessImage") RequestBody logo,
            @Part MultipartBody.Part file,
            @Part("business_id") RequestBody bussiness_id,
            @Part("bussiness_name") RequestBody name,
            @Part("bussiness_email") RequestBody email,
            @Part("bussiness_number") RequestBody phone,
            @Part("bussiness_website") RequestBody website,
            @Part("bussiness_address") RequestBody address,
            @Part("type") RequestBody type,
            @Part("bussiness_category") RequestBody businesvscategories);


    //*** Delete Business****

    //*** Set Default Business****
    @FormUrlEncoded
    @POST("manageBusiness")
    Call<BusinessItem> setDefaultBusiness(@Field("type") String type, @Field("userId") String userId,
                                       @Field("business_id") String bussinessId);


    //******** Send Contact *********

    @GET("ContactUs")
    Call<ApiStatus> contactUsMessage(@Query("user_id") String userid,
                                     @Query("name") String name,
                                     @Query("email") String email,
                                     @Query("mobile_no") String number,
                                     @Query("message") String massage);

    //*** Get All Home Data****
    @GET("getHomeData")
    Call<HomeItem> getHomeDatas();

    //*** My business Fragment Data load****

    @GET("getSubBusinessCatHome")
    Call<HomeItem> getSubBusinessCatHome(@Query("cat_id") String SubCategoryId);

    //*** Get Categories****

    @GET("getCategory")
    Call<List<CategoryItem>> getCategories(@Query("type") String type);

    //***Get Sub Category****

    @GET("getCategory")
    Call<List<CategoryItem>> getBusinessSubCategory(@Query("type") String type, @Query("sub_id") String SubCategoryId);

    //***Get Festival Category****

    @GET("getCategory")
    Call<List<FestivalItem>> getFestivals(@Query("type") String type, @Query("video") boolean video);

    //*** get All Language****
    @GET("getAllLanguages")
    Call<List<LanguageItem>> getLanguagess();


    //*** get All Language****
    @GET("getCustomFrame")
    Call<FrameResponse> getCustomFrames(@Query("user_id") String userId,@Query("frame_type") String type, @Query("ratio") String ratio);


    //******* Get All Types data in PreviewActivity *********

    @GET("getAllPost")
    Call<List<PostItem>> getAllPostsByCategory(@Query("page") Integer page, @Query("type") String type, @Query("dimension") String postType, @Query("catId") String categoryID, @Query("language") String language, @Query("video") Boolean video);


    //*** Get App Info****
    @GET("dailyPost")
    Call<List<PostItem>> getDailyPostData(@Query("page") Integer page, @Query("catId") String categoryID, @Query("language") String language);

    @GET("greetingData")
    Call<List<FeatureItem>> getGreetingData(@Query("catId") String categoryID,@Query("page")Integer page);


    //*** Create Transaction after succesfull purchase****

    @POST("createTransactions")
    Call<ApiStatus> storeTransaction(@Query("user_id") String userId,
                                     @Query("plan_id") String planId,
                                     @Query("payment_id") String paymentId,
                                     @Query("payment_amount") String planPrice,
                                     @Query("coupon_code") String couponCode,
                                     @Query("payment_type") String type);

    //******** Send Payment *********



    //********  Stripe Payment *********


    @FormUrlEncoded
    @POST("stripe-payment")
    Call<StripeResponse> createStripePayment(@Field("order_amount") String order_amount);


    //*** Get Home Banners****
    @GET("getAllBanners")
    Call<List<SliderItem>> getAllBannerss();

    //*** Get Subscriptions Plans****
    @GET("getSubscriptionPlanList")
    Call<List<SubsPlanItem>> getSubscriptionsPlanList();

    //*** validate Coupons****

    @GET("couponValidation")
    Call<CouponItem> validateCoupon(@Query("user_id") String userId, @Query("coupon_code") String couponCode);

    //*** Get App Info****
    @GET("getAppInfo")
    Call<AppInfos> getAppInfo();


    //*** Custom Template Based Features****


    //*** Animated Video Templates****


    //*** Get Custom- Search Animated Video ****


    @GET("getAnimatedSearchVideo")
    Call<TemplateResponse> getSearchedTemplates(@Query("page") Integer page, @Query("cat") String cat,@Query("limit") Integer limit);


/*
    @GET("getAnimatedSearchVideo")
    Call<TemplateResponse> getAnimatedCustomTemplates(@Query("page") Integer page, @Query("cat") String cat, @Query("limit") Integer limit);

*/

    //*** Get Custom- Home Animated Video ****

    @GET("getAnimatedVideoHome")
    Call<DashboardTemplateResponseMain> getDashboardTemplate(@Query("page") Integer page);



   @GET("getAnimatedVideoHome")
    Call<DashboardTemplateResponseMain>  getAnimatedCategory(@Query("page") Integer page);

    @GET("musicList")
    Call<List<ModelAudio>> getAllMusic(@Query("page") Integer page, @Query("catId") Integer catId, @Query("langId") String  langId);

    //***  Poster Templates ****


    //***  get Home Posters data****

    @GET("getPosterHome")
    Call<ThumbnailWithList> getPosterHome(@Query("page") Integer page);


    //***  get Digital card data****
    @GET("digitalBusinessCardList")
    Call<List<DigitalCardModel>> getDigitalBusinessCardList();

    @GET("myBusinessCardList")
    Call<List<DigitalCardModel>> getMybusinesslist(@Query("user_id") String userId);


    @GET("addBusinessCard")
    Call<ApiStatus> addBusinessCard(@Query("user_id") String userId, @Query("businesscard_id") Integer cardId);


    //***  get Single Poster Data****
    @GET("getPosterData")
    Call<ThumbnailInfo> getPosterData(@Query("post_id") Integer postId);


    @GET("addCreated")
    Call<ModelCreated> addCreated(@Query("postId") Integer postId, @Query("type") String type);

    //***  get Posters backgrounds****
    @GET("getPosterBG")
    Call<ThumbBG> getPosterBackground();

    //***  get Posters Stickers****
    @GET("getPosterSticker")
    Call<ThumbBG> getPosterStickers();


    //***  Search Posters ****

    @GET("getPosterSearch")
    Call<ThumbnailThumbFull> getPosterSearch(@Query("page") Integer page, @Query("cat") String cat);


    @GET("whatsapp-otp")
    Call<WhatsappOtpResponse> createWhatsappOtp(@Query("number") String number);

    @GET("getDesiredCustomFrame")
    Call<ApiStatus> getDesiredCustomFrame(@Query("user_id") String userId, @Query("frame_type") String type);
    @GET("getAllService")
    Call<List<ServiceItem>> getAllService();

}
