package com.astarconcepts.brandbanao.utils;

import static com.astarconcepts.brandbanao.utils.MyUtils.unzip;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.model.Sticker_info;
import com.astarconcepts.brandbanao.custom.poster.model.Text_infoposter;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.FrameModel;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class FrameUtils {

    public static String realX = "";
    public static String realY = "";
    public static String calcWidth = "";
    public static String calcHeight = "";
    public static ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    public static ArrayList<Text_infoposter> textInfoArrayList = new ArrayList<>();
    static int templateRealWidth = 0;
    static int templateRealHeight = 0;
    public static OnFrameStatus listner;

    static PreferenceManager preferenceManager;


    private static ArrayList<String> fontNames = new ArrayList<>();
    private static int downloadId;


    public void getOnFrameStatus(OnFrameStatus status) {

        listner = status;
    }


    public FrameUtils(Activity context, FrameModel frameModel, OnFrameStatus onFrameStatus) {

        preferenceManager = new PreferenceManager(context);

        listner = onFrameStatus;

        File path = new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_frame)), frameModel.getTitle());


        if (path.exists()) {

            proccessFrame(context, frameModel, path.getAbsolutePath(), listner);

        } else {


            downloadTemplate(context, frameModel);

        }


    }


    private static void downloadTemplate(Activity context, FrameModel frameModel) {

    //    TemplateUtils.setupProgress(context);

        downloadId = PRDownloader.download(frameModel.getFrame_zip(), MyUtils.getFolderPath(context, context.getString(R.string.poster_frame)), frameModel.getTitle() + ".zip").build()
                .setOnProgressListener(progress -> {

                }).start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        try {

                            unzip(new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_frame)), frameModel.getTitle() + ".zip"), new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_frame))), path -> {

                            proccessFrame(context, frameModel, new File(path, frameModel.getTitle()).getAbsolutePath(), listner);


                            });

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(Error error) {

                        Toast.makeText(context, "Download Failed.", Toast.LENGTH_SHORT).show();

                        PRDownloader.cancel(downloadId);
                      //  TemplateUtils.dismissDialog();

                    }
                });


    }


    public static void proccessFrame(Activity context, FrameModel model, String path, OnFrameStatus onFrameStatus) {


        stickerInfoArrayList.clear();
        textInfoArrayList.clear();

        fontNames.clear();

        listner = onFrameStatus;

        try {

            JSONObject jsonObject = new JSONObject(MyUtils.readFromFile(new File(path, "json/" + model.getTitle() + ".json").getAbsolutePath()));

            JSONArray jsonArrayLayers = jsonObject.getJSONArray("layers");

            for (int i = 0; i < jsonArrayLayers.length(); i++) {

                JSONObject jsonObject1 = jsonArrayLayers.getJSONObject(i);

                processJson(context, model, i, jsonObject1);

                File file = new File(Configure.GetFileDir(context).getPath() + File.separator + "font");

                // find fonts and copy to font directory.

                for (int i1 = 0; i1 < fontNames.size(); i1++) {

                    try {

                        File sourceFile = new File(path, "fonts/" + fontNames.get(i1) + ".ttf");

                        File dest = new File(file.getPath(), fontNames.get(i1) + ".ttf");

                        if (sourceFile.exists()) {

                            FileUtils.copyFile(sourceFile, dest);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }

            }

           /// TemplateUtils.dismissDialog();

            listner.onFrameLoaded(stickerInfoArrayList, textInfoArrayList);
        } catch (Exception e) {

            e.printStackTrace();
            Log.d("onFrameLoaded__", e.getMessage());
        }
    }

    public interface OnLogoDownloadListener {
        void onLogoDownloaded(String logoPath);

        void onLogoDownloadError();
    }

    public static void processJson(Activity context, FrameModel model, int i, JSONObject jsonObject1) throws Exception {


        String businessLogo = preferenceManager.getString(Constant.BUSINESS_LOGO_PATH);

        String type = jsonObject1.getString("type");
        String name = jsonObject1.getString("name");

        String width = String.valueOf(jsonObject1.getInt("width"));
        String height = String.valueOf(jsonObject1.getInt("height"));

        if (i == 0) {
            templateRealWidth = Integer.parseInt(width);
            templateRealHeight = Integer.parseInt(height);
        }

        String x = String.valueOf(jsonObject1.getInt("x"));
        String y = String.valueOf(jsonObject1.getInt("y"));



     String   calcWidth = "";
        String calcHeight = "";

        String   realX = String.valueOf(((int) Math.round(Float.parseFloat(x)) * 100) / templateRealWidth);
        String  realY = String.valueOf((int) Math.round(Float.parseFloat(y) * 100) / templateRealHeight);

        calcWidth = String.valueOf(Integer.parseInt(width) * 100 / templateRealWidth + Integer.parseInt(realX));
        calcHeight = String.valueOf(Integer.parseInt(height) * 100 / templateRealHeight + Integer.parseInt(realY));

        if (type != null) {

            if (type.contains("image")) {

                Sticker_info info = new Sticker_info();
                info.setSticker_id(String.valueOf(i));

                String stickerUrl = MyUtils.getFolderPath(context, context.getString(R.string.poster_frame)) + File.separator + model.getTitle() + File.separator + jsonObject1.getString("src").replace("../", "");


               if (name.equals("logo")) {

                   if (model.getFrame_category().equals("personal")) {

                       info.setSt_image(preferenceManager.getString(Constant.USER_IMAGE_PATH));

                   } else {

                       info.setSt_image(businessLogo);
                   }

                }else{
                   info.setSt_image(stickerUrl);
               }

               info.setNAME(name);



                info.setSt_order(String.valueOf(i));
                info.setSt_height(calcHeight);
                info.setSt_width(calcWidth);
                info.setSt_x_pos(realX);
                info.setSt_y_pos(realY);
                info.setSt_rotation("0");
                stickerInfoArrayList.add(info);

            } else if (type.contains("text")) {

                String color = jsonObject1.getString("color");
                String text = jsonObject1.getString("text");
                String size = jsonObject1.getString("size");

                String weight = "";
                if (jsonObject1.has("weight")) {
                    weight = jsonObject1.getString("weight");
                }

                String justification = jsonObject1.getString("justification");

                String font = jsonObject1.getString("font");

                fontNames.add(font);

               if (!jsonObject1.has("rotation")) {

                //    jsonObject1.put("size", Integer.parseInt(size) + 15);
                  //  jsonObject1.put("y", Integer.parseInt(y) + 2);


                   jsonObject1.put("size", (int) Math.round(Integer.parseInt(size) +Integer.parseInt(size)*0.5));
                   jsonObject1.put("y",  (int) Math.round(Integer.parseInt(y) +Integer.parseInt(y)* 0.002));

                    y = jsonObject1.getString("y");

                    size = jsonObject1.getString("size");
                    String calSizeHeight = String.valueOf(Integer.parseInt(size) - Integer.parseInt(height)).replace("-", "");
                    String calRealY = String.valueOf(Integer.parseInt(y) - Integer.parseInt(calSizeHeight));
                    realY = String.valueOf((int) Math.round(Float.parseFloat(calRealY) * 100) / templateRealHeight);
                    calcHeight = String.valueOf(Integer.parseInt(size) * 100 / templateRealHeight + Integer.parseInt(realY));

                }

                String rotation = "0";
                if (jsonObject1.has("rotation")) {
                    rotation = jsonObject1.getString("rotation");
                }

                Text_infoposter textInfo = new Text_infoposter();
                textInfo.setText_id(String.valueOf(i));
                textInfo.setText(text);


                BusinessItem businessItem = Constant.getBusinessItem(context);


                if (name.equals("email")) {
                    if (model.getFrame_category().equals("personal")) {
                        textInfo.setText(preferenceManager.getString(Constant.USER_EMAIL));
                    } else {
                        textInfo.setText(businessItem != null ? businessItem.getEmail() : "");

                    }
                } else if (name.equals("address")) {
                    textInfo.setText(businessItem != null ? businessItem.getAddress() : "");
                } else if (name.equals("website")) {
                    textInfo.setText(businessItem != null ? businessItem.getWebsite() : "");
                } else if (name.equals("company")) {
                    textInfo.setText(businessItem != null ? businessItem.getName() : "");
                } else if (name.equals("name")) {
                    if (model.getFrame_category().equals("personal")) {
                        textInfo.setText(preferenceManager.getString(Constant.USER_NAME));
                    } else {

                        textInfo.setText(businessItem != null ? businessItem.getName() : "");
                    }
                } else if (name.equals("number") || name.equals("phone") || name.equals("mobile")) {
                    if (model.getFrame_category() != null && model.getFrame_category().equals("personal")) {
                        textInfo.setText(preferenceManager.getString(Constant.USER_PHONE));
                    } else {
                        textInfo.setText(businessItem != null ? businessItem.getPhone() : "");
                    }
                } else if (name.equals("whatsapp")) {

                    //Whatsapp
                    textInfo.setText(preferenceManager.getString(Constant.USER_PHONE));

                } else if (name.equals("facebook")) {

                     textInfo.setText(businessItem != null ? businessItem.getSocial_facebook() : "");

                } else if (name.equals("twitter")) {

                    textInfo.setText(businessItem != null ? businessItem.getSocial_twitter() : "");


                } else if (name.equals("youtube")) {

                    textInfo.setText(businessItem != null ? businessItem.getSocial_youtube() : "");

                }else if (name.equals("instagram")){

                    textInfo.setText(businessItem != null ? businessItem.getSocial_instagram() : "");


                }


                textInfo.setTxt_height(calcHeight);
                textInfo.setTxt_width(calcWidth);
                textInfo.setTxt_x_pos(realX);

                textInfo.setTxt_y_pos(realY);
                textInfo.setTxt_rotation(rotation);
                textInfo.setTxt_color(color.replace("0x", "#"));
                textInfo.setTxt_order("" + i);
                textInfo.setFont_family(font);
                textInfo.setTxt_weight(weight);
                textInfo.setTxt_justification(justification);

                textInfoArrayList.add(textInfo);

            }
        }
    }

    public interface OnFrameStatus {
        void onFrameLoaded(ArrayList<Sticker_info> stickerInfos, ArrayList<Text_infoposter> textInfos);
    }


}
