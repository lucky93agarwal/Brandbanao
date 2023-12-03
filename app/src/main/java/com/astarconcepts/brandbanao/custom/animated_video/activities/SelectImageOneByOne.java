package com.astarconcepts.brandbanao.custom.animated_video.activities;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.OneByOneGalleryImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.SelectImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelImages;

import java.util.ArrayList;

public class SelectImageOneByOne extends AppCompatActivity {
    public static ArrayList<ModelImages> alImages = new ArrayList<>();
    RecyclerView rv_gallery_album;
    RecyclerView rv_gallery_images;
    OneByOneGalleryImageAdapter oneByOneGalleryImageAdapter;
    SelectImageAdapter selectImageAdapter;
    DrawerLayout drawerLayout;
    LinearLayout content;
    CardView hamBurgerIcon;
    TextView selectedFoldername;
    boolean boolean_folder;
    Activity context;
    private ImageView back;
    private ProgressBar progressBar;

    public ArrayList<ModelImages> loadImages() {
        alImages.clear();
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data;
        int column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN};

        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);


            for (int i = 0; i < alImages.size(); i++) {
                try {
                    if (alImages.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
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

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(alImages.get(int_position).getAlImagepath());
                al_path.add(absolutePathOfImage);
                alImages.get(int_position).setAlImagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                ModelImages obj_model = new ModelImages();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAlImagepath(al_path);
                alImages.add(obj_model);
            }

        }


        alImages.add(0, new ModelImages("All", getAllShownImagesPath(SelectImageOneByOne.this)));


        new Handler(getMainLooper()).post(() -> {

            progressBar.setVisibility(View.GONE);

            oneByOneGalleryImageAdapter = new OneByOneGalleryImageAdapter(SelectImageOneByOne.this, alImages);
            rv_gallery_images.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            rv_gallery_images.setAdapter(oneByOneGalleryImageAdapter);
            selectImageAdapter = new SelectImageAdapter(getApplicationContext(), alImages, oneByOneGalleryImageAdapter, drawerLayout, selectedFoldername);
            rv_gallery_album.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            rv_gallery_album.setAdapter(selectImageAdapter);

        });

        return alImages;
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_one_by_one);
        initView();

        context = this;

        back.setOnClickListener(view -> onBackPressed());

        new Thread(() -> loadImages()).start();


        hamBurgerIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        initView();

    }

    private void initView() {

        drawerLayout = findViewById(R.id.drawerLayout);
        content = findViewById(R.id.content);
        hamBurgerIcon = findViewById(R.id.ham_burger_icon);
        rv_gallery_images = findViewById(R.id.rv_gallery_images);
        rv_gallery_album = findViewById(R.id.rv_gallery_album);
        selectedFoldername = findViewById(R.id.selectedFoldername);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);

    }
}