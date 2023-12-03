package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.TextDelegate;
import com.astarconcepts.brandbanao.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextChangeAdapter extends RecyclerView.Adapter<TextChangeAdapter.MyViewHolder> {


    private final TextDelegate textDelegate;
    Activity context;
    List<String> nameList;
    LottieAnimationView lottieAnimationView;
    private EditText textchangeinput;


    public TextChangeAdapter(Activity context, List<String> nameList, LottieAnimationView lottieAnimationView) {
        this.context = context;
        this.nameList = nameList;
        this.lottieAnimationView = lottieAnimationView;
        textDelegate = new TextDelegate(lottieAnimationView);

    }


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_template_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        textchangeinput.setHint(nameList.get(position));

        textchangeinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textDelegate.setText(nameList.get(position), charSequence.toString());
                lottieAnimationView.setFontAssetDelegate(new FontAssetDelegate() {

                    @Override
                    public Typeface fetchFont(String fontFamily) {

                        return Typeface.createFromFile(context.getIntent().getStringExtra("filepath") + "/" + context.getIntent().getStringExtra("code") + "/res/fonts/" + fontFamily + ".ttf");

                    }


                });


                lottieAnimationView.setTextDelegate(textDelegate);


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    private void initView(View view) {
        textchangeinput = view.findViewById(R.id.textchangeinput);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            initView(itemView);
        }
    }
}
