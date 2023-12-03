package com.astarconcepts.brandbanao.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static com.astarconcepts.brandbanao.utils.MyUtils.getPathFromURI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.SavedAdapter;
import com.astarconcepts.brandbanao.custom.poster.activity.ShareImageActivity;
import com.astarconcepts.brandbanao.databinding.FragmentDownloadBinding;
import com.astarconcepts.brandbanao.model.DownloadItem;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedPosterFragment extends Fragment {

    FragmentDownloadBinding binding;
    SavedAdapter adapter;
    List<DownloadItem> uriList;
    int DELETE_REQUEST_URI_R = 11;
    int DELETE_REQUEST_URI_Q = 12;
    DownloadItem downloadItem;
    UniversalDialog universalDialog;

    public SavedPosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDownloadBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uriList = new ArrayList<>();
        universalDialog = new UniversalDialog(getActivity(), false);
        setData();

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            setData();
        });


    }

    private void setData() {
        new LoadImages().execute();
    }

    private void goToNextActivity(Uri path) {

        Intent intent = new Intent(getContext(), ShareImageActivity.class);
        intent.putExtra("uri", (getPathFromURI(getActivity(), path)));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
    }

    public Uri getImageContentUri(File imageFile, boolean isVideo) {
        if (isVideo) {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID},
                    MediaStore.Video.Media.DATA + "=? ",
                    new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
            }
        } else {
            try {
                String filePath = imageFile.getAbsolutePath();
                Cursor cursor = getActivity().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media._ID},
                        MediaStore.Images.Media.DATA + "=? ",
                        new String[]{filePath}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    cursor.close();
                    return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                }
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DELETE_REQUEST_URI_R) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), getResources().getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == DELETE_REQUEST_URI_Q) {
            if (resultCode == RESULT_OK) {
                int delete = getContext().getContentResolver().delete(downloadItem.uri, null, null);
                if (delete == 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.error_delete), Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class LoadImages extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (adapter == null) {
                adapter = new SavedAdapter(getActivity(), uriList, (item) -> {
                    downloadItem = item;
                    goToNextActivity(downloadItem.getUri());
                });
                binding.rvDownload.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (uriList.size() > 0) {
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.rvDownload.setVisibility(View.VISIBLE);
                binding.llNotFound.setVisibility(View.GONE);

            } else {
                binding.shimmerViewContainer.stopShimmer();
                binding.rvDownload.setVisibility(View.GONE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.llNotFound.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            uriList.clear();

            try {


                File root = new File(MyUtils.getStoreVideoExternalStorage(getActivity()));

                if (root.exists()) {

                    File[] files = root.listFiles();

                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().contains("jpg") || files[i].getName().contains("jpeg") || files[i].getName().contains("png")) {
                            uriList.add(new DownloadItem(getImageContentUri(files[i], false), false));
                            Util.showLog("URI IMAGE: " + getImageContentUri(files[i], false));
                        }

                    }
                    Collections.reverse(uriList);
                }
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
            }
            return null;
        }
    }
}