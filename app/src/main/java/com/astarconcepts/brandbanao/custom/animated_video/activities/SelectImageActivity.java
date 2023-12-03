package com.astarconcepts.brandbanao.custom.animated_video.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.GalleryImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.ReplaceImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.SelectImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelImages;
import com.astarconcepts.brandbanao.custom.animated_video.model.SelectImageModel;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SelectImageActivity extends AppCompatActivity {
    public static ArrayList<ModelImages> al_images = new ArrayList<>();
    public static int selectedAlbumPosition = 0;
    public static ReplaceImageAdapter replaceImageAdapter;
    public static GalleryImageAdapter galleryImageAdapter;
    public static int selectedReplcePos = 0;
    public static ArrayList<SelectImageModel> replaceImageList;
    public static ArrayList<String> templateImagesPath;
    SelectImageAdapter selectImageAdapter;
    ArrayList<Integer> addedImage = new ArrayList<>();
    DrawerLayout drawerLayout;
    LinearLayout content;
    CardView ham_burger_icon;
    RecyclerView rv_gallery_album, rv_gallery_images, replaceImagerv;
    boolean boolean_folder;
    TextView selectedFoldername;
    String templateJSon;
    int numbersOfImages;
    private LinearLayout ll_save;
    private ImageView back;
    private ProgressBar progressBar;


    public static String file2String(String filePath) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } finally {
            reader.close();
        }

        String returnStr = stringBuilder.toString();
        return returnStr;
    }

    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        initView();

        back.setOnClickListener(view -> onBackPressed());

        replaceImageList = new ArrayList<>();
        templateImagesPath = new ArrayList<>();

        selectedReplcePos = 0;

        ham_burger_icon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        templateJSon = getIntent().getStringExtra("template");

        Log.d("ParsingErrorCheck", "" + templateJSon);

        Log.d("ParsingErrorCheck", "" + templateJSon);
        String jsonForTemplate = "";
        jsonForTemplate = templateJSon;

        try {
            templateImagesPath.clear();
            replaceImageList.clear();

            JSONObject jsonObject = new JSONObject(jsonForTemplate);

            JSONArray jsonArray = jsonObject.getJSONArray("elements");

            numbersOfImages = jsonArray.length();

            for (int i = 0; i < numbersOfImages; i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                JSONArray jsonArray_Size = object.getJSONArray("size");

                int editable = object.getInt("editable");

                if (editable == 1) {
                    SelectImageModel selectImageModel = new SelectImageModel();
                    selectImageModel.setWidth((int) (Float.parseFloat(jsonArray_Size.get(0).toString())));
                    selectImageModel.setHeight((int) (Float.parseFloat(jsonArray_Size.get(1).toString())));
                    if (getIntent().hasExtra("fromDownloading")) {

                        selectImageModel.setPath("");

                    } else {

                        selectImageModel.setPath(getIntent().getStringExtra("filepath") + "/" + getIntent().getStringExtra("code") + "/" + object.getString("image"));

                    }
                    templateImagesPath.add(getIntent().getStringExtra("filepath") + "/" + getIntent().getStringExtra("code") + "/" + object.getString("image"));
                    replaceImageList.add(selectImageModel);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(this::loadImages).start();

        replaceImageAdapter = new ReplaceImageAdapter(getApplicationContext(), replaceImageList, selectedReplcePos);
        replaceImagerv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        replaceImagerv.setAdapter(replaceImageAdapter);

        ll_save.setOnClickListener(view -> {

            if (getIntent().hasExtra("fromDownloading")) {

                finish();

            } else {

                Intent intent = new Intent(SelectImageActivity.this, VideoEditorActivity.class);
                intent.putExtra("filepath", MyUtils.getFolderPath(SelectImageActivity.this, "ae"));
                intent.putExtra("code", getIntent().getStringExtra("code"));
                startActivity(intent);
                finish();
            }
            progressBar.setVisibility(View.VISIBLE);

        });

    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN};
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        cursor = activity.getContentResolver().query(uri, projection, null,
                null, orderBy);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    public ArrayList<ModelImages> loadImages() {
        al_images.clear();
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN};

        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);


            for (int i = 0; i < al_images.size(); i++) {
                try {
                    if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                        boolean_folder = true;
                        int_position = i;
                        break;
                    } else {
                        boolean_folder = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }


            if (boolean_folder) {

                ArrayList<String> alPath = new ArrayList<>();
                alPath.addAll(al_images.get(int_position).getAlImagepath());
                alPath.add(absolutePathOfImage);
                al_images.get(int_position).setAlImagepath(alPath);

            } else {
                ArrayList<String> alPath = new ArrayList<>();
                alPath.add(absolutePathOfImage);
                ModelImages obj_model = new ModelImages();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAlImagepath(alPath);

                al_images.add(obj_model);
            }

        }


        al_images.add(0, new ModelImages("All", getAllShownImagesPath(SelectImageActivity.this)));


        new Handler(getMainLooper()).post(() -> {

            progressBar.setVisibility(View.GONE);

            galleryImageAdapter = new GalleryImageAdapter(SelectImageActivity.this, al_images, replaceImageList);
            rv_gallery_images.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            rv_gallery_images.setAdapter(galleryImageAdapter);
            selectImageAdapter = new SelectImageAdapter(getApplicationContext(), al_images, galleryImageAdapter, drawerLayout, selectedFoldername);
            rv_gallery_album.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            rv_gallery_album.setAdapter(selectImageAdapter);

        });

        return al_images;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            if (data != null) {

                replaceImageAdapter.updateImage(selectedReplcePos, getPathFromURI(SelectImageActivity.this, UCrop.getOutput(data)));

                if (!addedImage.contains(selectedReplcePos)) {

                    addedImage.add(selectedReplcePos);

                }

                if (addedImage.size() == replaceImageList.size()) {
                    ll_save.setVisibility(View.VISIBLE);
                }

                if (selectedReplcePos < numbersOfImages - 1) {
                    selectedReplcePos++;
                }
                replaceImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        content = findViewById(R.id.content);
        ham_burger_icon = findViewById(R.id.ham_burger_icon);
        rv_gallery_images = findViewById(R.id.rv_gallery_images);
        rv_gallery_album = findViewById(R.id.rv_gallery_album);
        selectedFoldername = findViewById(R.id.selectedFoldername);
        replaceImagerv = findViewById(R.id.replaceImagerv);
        ll_save = findViewById(R.id.ll_save);
        progressBar = findViewById(R.id.progressBar);

        back = findViewById(R.id.back);
    }
}