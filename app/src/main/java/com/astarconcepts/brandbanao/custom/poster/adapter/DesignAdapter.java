package com.astarconcepts.brandbanao.custom.poster.adapter;

import static com.astarconcepts.brandbanao.ui.stickers.RelStickerView.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.create.BitmapDataObject;
import com.astarconcepts.brandbanao.custom.poster.create.DatabaseHandler;
import com.astarconcepts.brandbanao.custom.poster.create.TemplateInfo;
import com.astarconcepts.brandbanao.custom.poster.listener.ClickListener;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


public class DesignAdapter extends Adapter<DesignAdapter.MyViewHolder> {
    public LottieAnimationView deleteAnim;
    String catName;
    Activity context;
    int height;
    ClickListener<TemplateInfo> listener;
    UniversalDialog universalDialog;
    private List<TemplateInfo> arrayList;


    public DesignAdapter(Activity context2, List<TemplateInfo> arrayList, ClickListener<TemplateInfo> listener, String str, int i) {
        this.context = context2;
        this.catName = str;
        this.height = i;
        this.listener = listener;
        this.arrayList = arrayList;

    }

    public int getItemCount() {
        return this.arrayList.size();
    }


    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        MyViewHolder myViewHolders = (MyViewHolder) myViewHolder;

        universalDialog = new UniversalDialog(context, false);


        final TemplateInfo templateInfo = (TemplateInfo) this.arrayList.get(i);

        if (this.catName.equals("MY_TEMP")) {
            myViewHolders.delete.setVisibility(View.VISIBLE);
            try {
                if (templateInfo.getTHUMB_URI().contains("thumb")) {
                    Glide.with(this.context).load(new File(templateInfo.getTHUMB_URI()).getAbsoluteFile()).fitCenter().apply(((new RequestOptions().dontAnimate()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(myViewHolders.myimage);
                } else if (templateInfo.getTHUMB_URI().contains("raw")) {
                    Glide.with(this.context).load(getBitmapDataObject(Uri.parse(templateInfo.getTHUMB_URI()).getPath()).imageByteArray).fitCenter().apply(((new RequestOptions().dontAnimate()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(myViewHolders.myimage);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                myViewHolders.myimage.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.spaceholder));
            }
        } else {
            Glide.with(this.context).load(this.context.getResources().getIdentifier(templateInfo.getTHUMB_URI(), "drawable", this.context.getPackageName())).fitCenter().apply(((new RequestOptions().dontAnimate()).placeholder(R.drawable.spaceholder)).error(R.drawable.spaceholder)).into(myViewHolders.myimage);
        }
        myViewHolders.delete.setOnClickListener(view1 -> showDeleteDialog(i));
        myViewHolders.itemView.setOnClickListener(v -> listener.onClick(templateInfo, i));

    }

    private BitmapDataObject getBitmapDataObject(String str) {
        try {
            return (BitmapDataObject) new ObjectInputStream(new FileInputStream(str)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void showDeleteDialog(final int i) {
        universalDialog.showDeleteDialog(context.getString(R.string.delete), context.getString(R.string.sure_delete), context.getString(R.string.delete), context.getString(R.string.cancel));
        universalDialog.show();

        universalDialog.okBtn.setOnClickListener(v -> {

            universalDialog.cancel();

            TemplateInfo templateInfo = DesignAdapter.this.arrayList.get(i);
            DatabaseHandler dbHandler = DatabaseHandler.getDbHandler(context);
            boolean deleteTemplateInfo = dbHandler.deleteTemplateInfo(templateInfo.getTEMPLATE_ID());
            dbHandler.close();
            if (deleteTemplateInfo) {
                deleteFile(Uri.parse(templateInfo.getTHUMB_URI()));
                arrayList.remove(templateInfo);
                notifyDataSetChanged();

                return;
            }
        });
    }


    public boolean deleteFile(Uri uri) {
        boolean z = false;
        try {
            File file = new File(uri.getPath());
            z = file.delete();
            if (file.exists()) {
                try {
                    z = file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    z = this.context.getApplicationContext().deleteFile(file.getName());
                }
            }
            Context context2 = this.context;
            Context context3 = this.context;
            context2.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, FileProvider.getUriForFile(context3, context.getApplicationContext().getPackageName() + ".provider", file)));
        } catch (Exception e2) {
            Log.e(TAG, "deleteFile: " + e2);
        }
        return z;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View inflate;


        inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_download, viewGroup, false);

        return new MyViewHolder(inflate);
    }

    public static class MyViewHolder extends ViewHolder {
        private final ImageView myimage;
        private final ImageView delete;


        public MyViewHolder(View view) {
            super(view);

            this.myimage = view.findViewById(R.id.iv_post);
            this.delete = view.findViewById(R.id.delete);
        }
    }
}
