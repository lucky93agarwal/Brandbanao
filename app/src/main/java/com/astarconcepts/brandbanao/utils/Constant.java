package com.astarconcepts.brandbanao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.ModelCreated;
import com.astarconcepts.brandbanao.model.SubsPlanItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.viewmodel.HomeViewModel;
import com.astarconcepts.brandbanao.viewmodel.TemplateBasedViewModel;
import com.astarconcepts.brandbanao.viewmodel.UserViewModel;
import com.michael.easydialog.EasyDialog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Constant {

    public static final String TEMPLATE_TYPE_POSTER = "poster";
    public static final String TEMPLATE_TYPE_VIDEO = "video";
    public static final String IS_PREMIUM = "premium";
    public static final String NATIVE_AD_COUNT = "native_ad_click";
    public static final String REWARD_AD = "rewardAd";
    public static final String BUSINESS_SUB = "business_sub";
    public static final String PLAN_EXPIRED = "plan_expired";
    public static final String TODAY_DATE_PATTERN = "yyyy-MM-dd";
    public static final String ONESIGNAL_APP_ID = "ONESIGNAL_APP_ID";
    public static final String TAG_SEARCH_TERM = "tag_search";
    public static final String BUSSINESS_IMAGE = "bussinessImage";
    public static final String multipart = "multipart/form-data";
    public static final String DARK_MODE_ON = "CheckedItem";
    public static final String NOTIFICATION_ENABLED = "NotificationEnabled";
    public static final String NOTIFICATION_FIRST = "NOTIFICATION_FIRST";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String api_key = "demoKey";
    public static final String GOOGLE = "google";
    public static final String FESTIVAL = "festival";
    public static final String CATEGORY = "category";
    public static final String CUSTOM = "custom";
    public static final String BUSINESS = "business";
    public static final String SUBS_PLAN = "subscriptionPlan";
    public static final String EXTERNAL = "externalLink";
    public static final String EMAILAUTH = "password"; // don't change
    public static final String GOOGLEAUTH = "google.com"; // don't change
    public static final String PRIVACY_POLICY = "PRIVACY_POLICY";
    public static final String TERM_CONDITION = "TERM_CONDITION";
    public static final String REFUND_POLICY = "REFUND_POLICY";
    public static final String RAZORPAY_KEY_ID = "RAZORPAY_KEY_ID";

    public static final String STRIPE_KEY = "STRIPE_KEY";
    public static final String STRIPE_SECRET_KEY = "STRIPE_SECRET_KEY";

    public static final String PAYTM_ID = "PAYTM_ID";
    public static final String PAYTM_KEY = "PAYTM_KEY";

    public static final String CURRENCY = "currency";
    public static final String RazorPay = "razorpay"; // don't change'
    public static final String Paytm = "paytm"; // don't change'
    public static final String Stripe = "stripe"; // don't change'

    /**
     * Ads Data
     */
    public static final String PRIVACY_POLICY_LINK = "PRIVACY_POLICY_LINK";
    public static final String ADS_ENABLE = "ADS_ENABLE";
    public static final String PUBLISHER_ID = "PUBLISHER_ID";
    public static final String BANNER_AD_ID = "BANNER_AD_ID";
    public static final String INTERSTITIAL_AD_ID = "INTERSTITIAL_AD_ID";
    public static final String INTERSTITIAL_AD_CLICK = "INTERSTITIAL_AD_CLICK";
    public static final String NATIVE_AD_ID = "NATIVE_AD_ID";
    public static final String OPEN_AD_ID = "OPEN_AD_ID";
    public static final String INTERSTITIAL_AD_ENABLED = "INTERSTITIAL_AD_ENABLED";
    public static final String BANNER_AD_ENABLED ="BANNER_AD_ENABLED" ;
    public static final String NATIVE_AD_ENABLED = "NATIVE_AD_ENABLED";
    public static final String OPEN_APP_AD_ENABLED =  "OPEN_APP_AD_ENABLED";
    public static final String REWARD_AD_ENABLED = "REWARD_AD_ENABLED";

    public static final String OFFER_IMAGE = "OFFERIMAGE";

    public static final int RESULT_OK = -1;

    /**
     * User Data
     */
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_DESIGNATION = "USER_DESIGNATION";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_LANGUAGE = "USER_LANGUAGE";
    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String USER_BUSINESS_LIMIT = "USER_BUSINESS_LIMIT";
    public static final String STATUS = "STATUS";
    public static final String DEVICE = "android";
    /**
     * Active Plan Data
     */
    public static final String PLAN_NAME = "PLAN_NAME";
    public static final String PLAN_DURATION = "PLAN_DURATION";
    public static final String PLAN_START_DATE = "PLAN_START_DATE";
    public static final String PLAN_END_DATE = "PLAN_END_DATE";
    public static final String PLAN_ID = "PLAN_ID";
    public static final String IS_SUBSCRIBE = "IS_SUBSCRIBE";
    /**
     * Intent Data
     */
    public static final String INTENT_TYPE = "INTENT_TYPE";
    public static final String INTENT_FEST_ID = "INTENT_FEST_ID";
    public static final String INTENT_FEST_NAME = "INTENT_FEST_NAME";
    public static final String INTENT_POST_IMAGE = "INTENT_POST_IMAGE";
    public static final String INTENT_POST_LIST = "INTENT_POST_LIST";
    public static final String INTENT_VIDEO = "INTENT_VIDEO";
    public static final String INTENT_POS = "INTENT_POS";
    public static final String INTENT_IS_FROM_NOTIFICATION = "INTENT_IS_FROM_NOTIFICATION";
    public static final String IS_NOT = "IS_NOT";
    public static final String PRF_ID = "PRF_ID";
    public static final String PRF_NAME = "PRF_NAME";
    public static final String PRF_TYPE = "PRF_TYPE";
    public static final String PRF_LINK = "PRF_LINK";
    public static final String APP_HIDED_FOLDER = "Postermaker/";
    public static final String PHONE = "phone";
    public static final String GREETING = "greeting";
    public static final Integer DATA_LIMIT = 10;
    public static final String BUSINESS_ID = "business_id";

    public static final String BUSINESS_ITEM = "Business";
    public static final String BUSINESS_NAME = "Business_name";
    public static final String BUSINESS_LOGO = "Business_logo";
    public static final String BUSINESS_EMAIL = "business_email";
    public static final String BUSINESS_ADDRESS = "business_addrress";
    public static final String BUSINESS_MOBILE = "mobile_no";
    public static final String BUSINESS_WEBSITE = "website";
    public static final String BUSINESS_CATEGORY_NAME = "category_name";
    public static final String BUSINESS_CATEGORY_ID = "category_id";
    public static final String BUSINESS_IS_DEFAULT= "is_default";
    public static final String BUSSINESS_FACEBOOK = "business_fb";
    public static final String BUSSINESS_TWITTER = "business_twitter";
    public static final String BUSSINESS_INSTAGRAM = "BUSSINESS_INSTAGRAM";
    public static final String BUSSINESS_YOUTUBE = "BUSSINESS_YOUTUBE";
    public static final String BUSINESS_TAGLINE = "BUSINESS_TAGLINE";
    public static final int BUSINESS_LOGO_WIDTH = 150;
    public static final int BUSINESS_LOGO_HEIGHT = 150;
    public static final String BUSINESS_UPDATED = "BUSINESS_UPDATED";
    public static final String FRAME_TYPE_IMAGE = "image";
    public static final String FRAME_TYPE_ANIMATED= "animated";
    public static final String SELECTED_FRAME_POSITION = "SELECTED_FRAME_POSITION";
    public static final String BUSINESS_LOGO_PATH = "BUSINESS_LOGO_PATH";
    public static final String USER_IMAGE_PATH = "USER_IMAGE_PATH";
    public static final Integer ANIMATED_CUSTOM_SHOW_LIMIT = 6;
    public static final String ENABLE = "ENABLE";
    public static final String WHATSAPP_NUMBER = "WHATSAPP_NUMBER";
    public static String sdcardPath = null;
    public static BusinessItem businessItem;
    public static Bitmap bitmap;
    public static Bitmap bitmaps = null;
    public static String selectedRatio = "1:1";
    public static int[] imageId = {R.drawable.btxt0, R.drawable.btxt1, R.drawable.btxt2, R.drawable.btxt3, R.drawable.btxt4, R.drawable.btxt5, R.drawable.btxt6, R.drawable.btxt7, R.drawable.btxt8, R.drawable.btxt9, R.drawable.btxt10, R.drawable.btxt11, R.drawable.btxt12, R.drawable.btxt13, R.drawable.btxt14, R.drawable.btxt15, R.drawable.btxt16, R.drawable.btxt17, R.drawable.btxt18, R.drawable.btxt19, R.drawable.btxt20, R.drawable.btxt21, R.drawable.btxt22, R.drawable.btxt23, R.drawable.btxt24, R.drawable.btxt25, R.drawable.btxt26, R.drawable.btxt27, R.drawable.btxt28, R.drawable.btxt29, R.drawable.btxt30, R.drawable.btxt31, R.drawable.btxt32, R.drawable.btxt33, R.drawable.btxt34, R.drawable.btxt35, R.drawable.btxt36, R.drawable.btxt37, R.drawable.btxt38, R.drawable.btxt39};
    public static Bitmap bitmapSticker = null;
    public static String isRated = "isRated";
    public static String onTimeLayerScroll = "onTimeLayerScroll";
    public static String onTimeRecentHint = "onTimeRecentHint";
    public static int[] overlayArr = {R.drawable.os1, R.drawable.os2, R.drawable.os3, R.drawable.os4, R.drawable.os5, R.drawable.os6, R.drawable.os7, R.drawable.os8, R.drawable.os9, R.drawable.os10, R.drawable.os11, R.drawable.os12, R.drawable.os13, R.drawable.os14, R.drawable.os15, R.drawable.os16, R.drawable.os17, R.drawable.os18, R.drawable.os19, R.drawable.os20, R.drawable.os21, R.drawable.os22, R.drawable.os23, R.drawable.os24, R.drawable.os25, R.drawable.os26, R.drawable.os27, R.drawable.os28, R.drawable.os29, R.drawable.os30, R.drawable.os31, R.drawable.os32, R.drawable.os33, R.drawable.os34, R.drawable.os35, R.drawable.os36, R.drawable.os37, R.drawable.os38, R.drawable.os39, R.drawable.os40, R.drawable.os41, R.drawable.os42, R.drawable.os43, R.drawable.os44, R.drawable.os45};
    public static String rewid = "";
    public static String uri = "";
    public static SubsPlanItem subsPlanItem;

    public static ArrayList<String> movieImageList = new ArrayList<>();

    public static BusinessItem getBusinessItem(Activity context) {
        // Retrieve User from Shared
        PreferenceManager prefManager = new PreferenceManager(context);

        String name = prefManager.getString(BUSINESS_NAME);
        String businessId = prefManager.getString(BUSINESS_ID);

        if (name == null || name.isEmpty() || businessId == null || businessId.isEmpty() || !prefManager.getBoolean(BUSINESS_IS_DEFAULT) ) {
            return null; // Return null if the businessItem is empty or not set
        }

        BusinessItem businessItem = new BusinessItem();
        businessItem.setName(name);
        businessItem.setBusinessid(businessId);
        businessItem.setEmail(prefManager.getString(BUSINESS_EMAIL));
        businessItem.setWebsite(prefManager.getString(BUSINESS_WEBSITE));
        businessItem.setCategoryId(prefManager.getString(BUSINESS_CATEGORY_ID));
        businessItem.setBusinesscategory(prefManager.getString(BUSINESS_CATEGORY_NAME));
        businessItem.setAddress(prefManager.getString(BUSINESS_ADDRESS));
        businessItem.setDefault(prefManager.getBoolean(BUSINESS_IS_DEFAULT));
        businessItem.setLogo(prefManager.getString(BUSINESS_LOGO));
        businessItem.setPhone(prefManager.getString(BUSINESS_MOBILE));
        businessItem.setSocial_facebook(prefManager.getString(BUSSINESS_FACEBOOK));
        businessItem.setSocial_instagram(prefManager.getString(BUSSINESS_INSTAGRAM));
        businessItem.setSocial_twitter(prefManager.getString(BUSSINESS_TWITTER));
        businessItem.setSocial_youtube(prefManager.getString(BUSSINESS_YOUTUBE));
        businessItem.setTagline(prefManager.getString(BUSINESS_TAGLINE));

        return businessItem;
    }


    public static void setDefaultBusiness(Activity context, BusinessItem businessItem) {
        //Save User to Shared
        PreferenceManager prefManager = new PreferenceManager(context);

        if (businessItem != null) {
            prefManager.setString(BUSINESS_NAME, businessItem.getName());
            prefManager.setString(BUSINESS_ID, businessItem.getBusinessid());
            prefManager.setString(BUSINESS_EMAIL, businessItem.getEmail());
            prefManager.setString(BUSINESS_WEBSITE, businessItem.getWebsite());
            prefManager.setString(BUSINESS_CATEGORY_ID, businessItem.getCategoryId());
            prefManager.setString(BUSINESS_CATEGORY_NAME, businessItem.getBusinessCategory());
            prefManager.setString(BUSINESS_ADDRESS, businessItem.getAddress());

            if (businessItem.isDefault !=null){

                prefManager.setBoolean(BUSINESS_IS_DEFAULT, businessItem.getDefault());
            }
            prefManager.setString(BUSINESS_LOGO, businessItem.getLogo());
            prefManager.setString(BUSINESS_MOBILE, businessItem.getPhone());
            prefManager.setString(BUSSINESS_FACEBOOK, businessItem.getSocial_facebook());
            prefManager.setString(BUSSINESS_INSTAGRAM, businessItem.getSocial_instagram());
            prefManager.setString(BUSSINESS_TWITTER, businessItem.getSocial_twitter());
            prefManager.setString(BUSSINESS_YOUTUBE, businessItem.getSocial_youtube());
            prefManager.setString(BUSINESS_TAGLINE, businessItem.getTagline());
            prefManager.setBoolean(BUSINESS_UPDATED, true);
        } else {
            // Clear the values if businessItem is null
            prefManager.remove(BUSINESS_NAME);
            prefManager.remove(BUSINESS_ID);
            prefManager.remove(BUSINESS_EMAIL);
            prefManager.remove(BUSINESS_WEBSITE);
            prefManager.remove(BUSINESS_CATEGORY_ID);
            prefManager.remove(BUSINESS_CATEGORY_NAME);
            prefManager.remove(BUSINESS_ADDRESS);
            prefManager.remove(BUSINESS_IS_DEFAULT);
            prefManager.remove(BUSINESS_LOGO);
            prefManager.remove(BUSINESS_MOBILE);
            prefManager.remove(BUSSINESS_FACEBOOK);
            prefManager.remove(BUSSINESS_INSTAGRAM);
            prefManager.remove(BUSSINESS_TWITTER);
            prefManager.remove(BUSSINESS_YOUTUBE);
            prefManager.remove(BUSINESS_TAGLINE);
            prefManager.remove(BUSINESS_UPDATED);
        }

    }


    public static HomeViewModel getHomeViewModel(ViewModelStoreOwner viewModelStoreOwner){

        return new ViewModelProvider(viewModelStoreOwner).get(HomeViewModel.class);
    }
   public static TemplateBasedViewModel getTemplateBasedViewModel(ViewModelStoreOwner viewModelStoreOwner){

        return new ViewModelProvider(viewModelStoreOwner).get(TemplateBasedViewModel.class);
    }


    public static UserViewModel getUserViewModel(ViewModelStoreOwner viewModelStoreOwner){

        return new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);

    }


    public static UserItem getUserItem(Activity context) {

        //Save User to Shared
        PreferenceManager prefManager = new PreferenceManager(context);
        UserItem item = new UserItem();
        item.setUserName(prefManager.getString(Constant.USER_NAME));
        item.setUserId(prefManager.getString(Constant.USER_ID));
        item.setEmail(prefManager.getString(Constant.USER_EMAIL));
        item.setPhone(prefManager.getString(Constant.USER_PHONE));
        item.setDesignation(prefManager.getString(Constant.USER_DESIGNATION));
        item.setPlanDuration(prefManager.getString(Constant.PLAN_DURATION));
        item.setPlanEndDate(prefManager.getString(Constant.PLAN_END_DATE));
        item.setStatus(prefManager.getString(Constant.STATUS));
        item.setUserImage(prefManager.getString(Constant.USER_IMAGE));
        item.setPlanName(prefManager.getString(Constant.PLAN_NAME));
        item.setLogin_type(prefManager.getString(Constant.LOGIN_TYPE));
        item.setPlanStartDate(prefManager.getString(Constant.PLAN_START_DATE));

        return item;
    }

    public static CharSequence getSpannableString(Context context, Typeface typeface, int i) {
        SpannableStringBuilder append = new SpannableStringBuilder().append(new SpannableString(context.getResources().getString(i)));
        return append.subSequence(0, append.length());
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri2, float f, float f2) throws IOException {
        int exifRotation;
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri2, "r");
            FileDescriptor fileDescriptor = openFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            if (f <= f2) {
                f = f2;
            }
            int i = (int) f;
            options2.inSampleSize = ImageUtils.getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options2);
            Matrix matrix = new Matrix();
            if (decodeFileDescriptor.getWidth() > i || decodeFileDescriptor.getHeight() > i) {
                BitmapFactory.Options resampling = ImageUtils.getResampling(decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFileDescriptor.getWidth()), ((float) resampling.outHeight) / ((float) decodeFileDescriptor.getHeight()));
            }
            String realPathFromURI = ImageUtils.getRealPathFromURI(uri2, context);
            if (Integer.parseInt(Build.VERSION.SDK) > 4 && (exifRotation = ExifUtils.getExifRotation(realPathFromURI)) != 0) {
                matrix.postRotate((float) exifRotation);
            }
            Bitmap createBitmap = Bitmap.createBitmap(decodeFileDescriptor, 0, 0, decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), matrix, true);
            openFileDescriptor.close();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Typeface getHeaderTypeface(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "font/Montserrat-SemiBold.ttf");
    }

    public static Typeface getTextTypeface(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "font/Montserrat-Medium.ttf");
    }

    public static Animation getAnimUp(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.slide_up);
    }

    public static Animation getAnimDown(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.slide_down);
    }


    public static File getSaveFileLocation(String str) {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(externalStoragePublicDirectory, ".Thumbnail Maker Stickers/" + str);
    }

    public static boolean saveBitmapObject(Activity activity, Bitmap bitmap2, String str) {
        Bitmap copy = bitmap2.copy(bitmap2.getConfig(), true);
        File file = new File(str);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean compress = copy.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            copy.recycle();
            activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
            return compress;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return false;
        }
    }

    public static String saveBitmapObject1(Bitmap bitmap2) {
        File saveFileLocation = getSaveFileLocation("category1");
        saveFileLocation.mkdirs();
        File file = new File(saveFileLocation, "raw1-" + System.currentTimeMillis() + ".png");
        String absolutePath = file.getAbsolutePath();
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return "";
        }
    }

    public static String saveBitmapObject(Activity activity, Bitmap bitmap2) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), ".Thumbnail Maker Stickers/Mydesigns");
        file.mkdirs();
        File file2 = new File(file, "thumb-" + System.currentTimeMillis() + ".png");
        if (file2.exists()) {
            file2.delete();
        }
        try {
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file2));
            return file2.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MAINACTIVITY", "Exception" + e.getMessage());
            Toast.makeText(activity, activity.getResources().getString(R.string.save_err), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static Bitmap getTiledBitmap(Activity activity, int i, Bitmap bitmap2) {
        Rect rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        Paint paint = new Paint();
        int progress = ThumbnailActivity.seek_tailys.getProgress() + 10;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        paint.setShader(new BitmapShader(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(activity.getResources(), i, options), progress, progress, true), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public static void showRecentHindDialog(View view, Activity activity) {
        View inflate = activity.getLayoutInflater().inflate(R.layout.tooltip_hint_layerview, null);
        ((TextView) inflate.findViewById(R.id.txthint)).setTypeface(getTextTypeface(activity));
        new EasyDialog(activity).setLayout(inflate).setBackgroundColor(activity.getResources().getColor(R.color.titlecolor)).setLocationByAttachedView(view).setGravity(3).setAnimationTranslationShow(0, 1000, -600.0f, 100.0f, -50.0f, 50.0f, 0.0f).setAnimationAlphaShow(1000, 0.3f, 1.0f).setAnimationTranslationDismiss(0, 500, -50.0f, 800.0f).setAnimationAlphaDismiss(500, 1.0f, 0.0f).setMatchParent(false).setMarginLeftAndRight(24, 24).setOutsideColor(activity.getResources().getColor(R.color.transparent)).show();
    }

    public static void showScrollLayerDialog(View view, Activity activity) {
        View inflate = activity.getLayoutInflater().inflate(R.layout.tooltip_hint_layerscroll, null);
        ((TextView) inflate.findViewById(R.id.txthint)).setTypeface(getTextTypeface(activity));
        new EasyDialog(activity).setLayout(inflate).setBackgroundColor(activity.getResources().getColor(R.color.titlecolor)).setLocationByAttachedView(view).setGravity(3).setAnimationTranslationShow(0, 1000, -600.0f, 100.0f, -50.0f, 50.0f, 0.0f).setAnimationAlphaShow(1000, 0.3f, 1.0f).setAnimationTranslationDismiss(0, 500, -50.0f, 800.0f).setAnimationAlphaDismiss(500, 1.0f, 0.0f).setMatchParent(false).setMarginLeftAndRight(24, 24).setOutsideColor(activity.getResources().getColor(R.color.transparent)).show();
    }

    public static Bitmap resizeBitmap(Bitmap bitmap2, int i, int i2) {
        float f;
        float f2 = (float) i;
        float f3 = (float) i2;
        float width = (float) bitmap2.getWidth();
        float height = (float) bitmap2.getHeight();
        Log.i("testings", f2 + "  " + f3 + "  and  " + width + "  " + height);
        float f4 = width / height;
        float f5 = height / width;
        if (width > f2) {
            f = f2 * f5;
            Log.i("testings", "if (wd > wr) " + f2 + "  " + f);
            if (f > f3) {
                f2 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
            }
            Log.i("testings", " in else " + f2 + "  " + f);
        } else {
            if (height > f3) {
                float f6 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f6 + "  " + f3);
                if (f6 > f2) {
                    f3 = f2 * f5;
                } else {
                    Log.i("testings", " in else " + f6 + "  " + f3);
                    f2 = f6;
                }
            } else if (f4 > 0.75f) {
                f3 = f2 * f5;
                Log.i("testings", " if (rat1 > .75f) ");
            } else if (f5 > 1.5f) {
                f2 = f3 * f4;
                Log.i("testings", " if (rat2 > 1.5f) ");
            } else {
                f = f2 * f5;
                Log.i("testings", " in else ");
                if (f > f3) {
                    f2 = f3 * f4;
                    Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                } else {
                    Log.i("testings", " in else " + f2 + "  " + f);
                }
            }
            return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
        }
        f3 = f;
        return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
    }
    public static Bitmap resizeBitmap2(Bitmap bitmap2, int i, int i2) {
        float f;
        float f2 = (float) i;
        float f3 = (float) i2;
        float width = (float) bitmap2.getWidth();
        float height = (float) bitmap2.getHeight();
        Log.i("testings", f2 + "  " + f3 + "  and  " + width + "  " + height);
        float f4 = width / height;
        float f5 = height / width;
        if (width > f2) {
            f = f2 * f5;
            Log.i("testings", "if (wd > wr) " + f2 + "  " + f);
            if (f > f3) {
                f2 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
            }
            Log.i("testings", " in else " + f2 + "  " + f);
        } else {
            if (height > f3) {
                float f6 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f6 + "  " + f3);
                if (f6 > f2) {
                    f3 = f2 * f5;
                } else {
                    Log.i("testings", " in else " + f6 + "  " + f3);
                    f2 = f6;
                }
            } else if (f4 > 0.75f) {
                f3 = f2 * f5;
                Log.i("testings", " if (rat1 > .75f) ");
            } else if (f5 > 1.5f) {
                f2 = f3 * f4;
                Log.i("testings", " if (rat2 > 1.5f) ");
            } else {
                f = f2 * f5;
                Log.i("testings", " in else ");
                if (f > f3) {
                    f2 = f3 * f4;
                    Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                } else {
                    Log.i("testings", " in else " + f2 + "  " + f);
                }
            }
            return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
        }
        f3 = f;
        return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
    }


    public static void addTemplateCreationCount(final int quoteId,String type) {

        ApiClient.getApiDataService().addCreated(quoteId, type).enqueue(new Callback<ModelCreated>() {
            @Override
            public void onResponse(Call<ModelCreated> call, retrofit2.Response<ModelCreated> response) {

                if (response.isSuccessful() && response.body() !=null){


                }

            }

            @Override
            public void onFailure(Call<ModelCreated> call, Throwable t) {

                t.printStackTrace();
            }
        });


    }
}
