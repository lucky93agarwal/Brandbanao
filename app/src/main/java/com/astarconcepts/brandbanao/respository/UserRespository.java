package com.astarconcepts.brandbanao.respository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.api.ApiService;
import com.astarconcepts.brandbanao.api.ApiStatus;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.model.WhatsappOtpResponse;
import com.astarconcepts.brandbanao.utils.Constant;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRespository {


    private ApiService apiService;

    public UserRespository() {
        apiService = ApiClient.getApiDataService();
    }

    public LiveData<UserItem> getUserById(String userId) {
        MutableLiveData<UserItem> data = new MutableLiveData<>();
        apiService.getUserById(userId).enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<UserItem> userLoginGoogle(String email, String displayName, String photoUrl, String mobile, String loginType) {
        MutableLiveData<UserItem> data = new MutableLiveData<>();
        apiService.userLoginGoogle(displayName, email, photoUrl, mobile, loginType, "").enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                t.printStackTrace();
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<UserItem> profileUpdate(String userId, String profileImagePath,  String userName, String userEmail, String phoneNo, String userDesignation) {

        MutableLiveData<UserItem> data = new MutableLiveData<>();

        RequestBody requestFile = null;
        RequestBody imageuri = null;
        MultipartBody.Part body = null;


        if (profileImagePath != null) {

            File file = new File(profileImagePath);
            requestFile = RequestBody.create(MediaType.parse(Constant.multipart), file);
            imageuri = RequestBody.create(MediaType.parse(Constant.multipart), profileImagePath);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        }

        RequestBody userid = RequestBody.create(MediaType.parse(Constant.multipart), userId);
        RequestBody name = RequestBody.create(MediaType.parse(Constant.multipart), userName);
        RequestBody email = RequestBody.create(MediaType.parse(Constant.multipart), userEmail);
        RequestBody phone = RequestBody.create(MediaType.parse(Constant.multipart), phoneNo);
        RequestBody designation = RequestBody.create(MediaType.parse(Constant.multipart), userDesignation);


        ApiClient.getApiDataService().profileUpdate(userid, imageuri, body, name, email, phone, designation).
                enqueue(new Callback<UserItem>() {
                    @Override
                    public void onResponse(Call<UserItem> call, Response<UserItem> response) {

                        Log.i("RESPONSE", "RESPONSE-->" + new Gson().toJson(response.body()));


                        data.setValue(response.body());


                    }

                    @Override
                    public void onFailure(Call<UserItem> call, Throwable t) {
                        t.printStackTrace();

                        data.setValue(null);

                    }
                });


        return data;
    }


    public LiveData<ApiStatus> contactUsMessage(String uid, String name, String email, String number, String message) {
        MutableLiveData<ApiStatus> data = new MutableLiveData<>();
        apiService.contactUsMessage(uid, name, email, number, message).enqueue(new Callback<ApiStatus>() {
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


    public LiveData<ApiStatus> storeTransaction(String userId, String planId, String paymentId, String planPrice, String couponCode, String type) {
        MutableLiveData<ApiStatus> data = new MutableLiveData<>();

        apiService.storeTransaction(userId, planId, paymentId, planPrice, couponCode,type).enqueue(new Callback<ApiStatus>() {
            @Override
            public void onResponse(Call<ApiStatus> call, Response<ApiStatus> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ApiStatus> call, Throwable t) {
                t.printStackTrace();
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<BusinessItem> getDefaultBusiness(String userId, String type) {
        MutableLiveData<BusinessItem> data = new MutableLiveData<>();
        apiService.getBusinessDefaultList(userId, type).enqueue(new Callback<BusinessItem>() {
            @Override
            public void onResponse(Call<BusinessItem> call, Response<BusinessItem> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BusinessItem> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<BusinessItem> setDefaultBusiness(String type, String userId, String businessId) {
        MutableLiveData<BusinessItem> data = new MutableLiveData<>();
        apiService.setDefaultBusiness(type, userId, businessId).enqueue(new Callback<BusinessItem>() {
            @Override
            public void onResponse(Call<BusinessItem> call, Response<BusinessItem> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BusinessItem> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<BusinessItem> submitBusiness(String userId, String businessId, String businessImage, String name, String email, String phone, String website, String address,  String insta, String youtube, String facebook, String twitter, String tagline, String type, String businesscategories1) {

        MutableLiveData<BusinessItem> data = new MutableLiveData<>();


        MultipartBody.Part body = null;
        RequestBody businessid = null;
        RequestBody imageuri =null;

        if (businessImage != null) {

            File file = new File(businessImage);
            RequestBody requestFile = RequestBody.create(MediaType.parse(Constant.multipart), file);
            body = MultipartBody.Part.createFormData(Constant.BUSSINESS_IMAGE, file.getName(), requestFile);
            imageuri = RequestBody.create(MediaType.parse(Constant.multipart), businessImage);

        }

        if (businessId !=null && !businessId.isEmpty()){

            businessid = RequestBody.create(MediaType.parse(Constant.multipart), businessId);

        }


        Log.d("usersss", "id: "+userId+
                "name"+name+
                "email:"+email+
                "website:"+website+
                "address:"+address+
                "type:"+type+
                "bus id:"+businesscategories1);


        RequestBody userid = RequestBody.create(MediaType.parse(Constant.multipart), userId);
        RequestBody businessname = RequestBody.create(MediaType.parse(Constant.multipart), name);
        RequestBody businessemail = RequestBody.create(MediaType.parse(Constant.multipart), email);
        RequestBody businessphone = RequestBody.create(MediaType.parse(Constant.multipart), phone);
        RequestBody businesswebsite = RequestBody.create(MediaType.parse(Constant.multipart), website);
        RequestBody businessaddress = RequestBody.create(MediaType.parse(Constant.multipart), address);
        RequestBody businessinsta = RequestBody.create(MediaType.parse(Constant.multipart), insta);
        RequestBody businessyoutube = RequestBody.create(MediaType.parse(Constant.multipart), youtube);
        RequestBody businessfacebook = RequestBody.create(MediaType.parse(Constant.multipart), facebook);
        RequestBody businesstwitter = RequestBody.create(MediaType.parse(Constant.multipart), twitter);
        RequestBody businesstype = RequestBody.create(MediaType.parse(Constant.multipart), type);
        RequestBody businesscategories = RequestBody.create(MediaType.parse(Constant.multipart), businesscategories1);

        RequestBody businessTagliness = RequestBody.create(MediaType.parse(Constant.multipart), tagline);


        ApiClient.getApiDataService().submitBusiness(userid, businessid, imageuri, body, businessname, businessemail, businessphone, businesswebsite, businessaddress, businessinsta, businessyoutube, businessfacebook, businesstwitter,
                        businessTagliness, businesstype, businesscategories).
                enqueue(new Callback<BusinessItem>() {
                    @Override
                    public void onResponse(Call<BusinessItem> call, Response<BusinessItem> response) {

                        Log.i("RESPONSE", "RESPONSE-->" + new Gson().toJson(response.body()));

                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<BusinessItem> call, Throwable t) {

                        t.printStackTrace();

                        data.setValue(null);

                    }
                });

        return data;
    }


    public LiveData<WhatsappOtpResponse> createWhatsappOtp(String number) {


        MutableLiveData<WhatsappOtpResponse> data = new MutableLiveData<>();

        apiService.createWhatsappOtp(number).enqueue(new Callback<WhatsappOtpResponse>() {
            @Override
            public void onResponse(Call<WhatsappOtpResponse> call, Response<WhatsappOtpResponse> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<WhatsappOtpResponse> call, Throwable t) {

                data.setValue(null);
            }
        });

        return data;
    }

}
