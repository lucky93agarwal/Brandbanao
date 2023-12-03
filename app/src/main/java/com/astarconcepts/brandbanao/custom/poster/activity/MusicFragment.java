package com.astarconcepts.brandbanao.custom.poster.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.MusicAdapter;
import com.astarconcepts.brandbanao.adapters.MusicPagerAdapter;
import com.astarconcepts.brandbanao.model.MusicModel;

import java.util.ArrayList;
import java.util.List;


public class MusicFragment extends Fragment {

    MusicPagerAdapter.OnMusicSelect listner;
    List<MusicModel> musics;
    RecyclerView recyclerView;
    private MusicAdapter musicAdapter;

    public MusicFragment(List<MusicModel> musics, MusicPagerAdapter.OnMusicSelect onMusicSelect) {
        this.listner = onMusicSelect;
        this.musics = musics;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.recyclerView = view.findViewById(R.id.rv_songList);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setHasFixedSize(true);

        EditText editText = view.findViewById(R.id.et_search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString() != null) {
                    filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //code
            }
        });


        editText.setOnEditorActionListener((textView, i, keyEvent) -> {


            if (i == EditorInfo.IME_ACTION_SEARCH) {

                if (editText.getText() != null && !editText.getText().toString().isEmpty()) {

                    filter(editText.getText().toString());

                } else {
                    Toast.makeText(getContext(), "Please enter text", Toast.LENGTH_SHORT).show();
                }


            }

            return false;
        });

        musicAdapter = new MusicAdapter(getActivity(), musics, new MusicPagerAdapter.OnMusicSelect() {
            @Override
            public void onSelect(String path) {
                listner.onSelect(path);
            }

            @Override
            public void onPlay(String path) {
                listner.onPlay(path);
            }

            @Override
            public void onStop() {
                listner.onStop();
            }
        });

        recyclerView.setAdapter(musicAdapter);


        if (musics != null && musics.isEmpty()) {
            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);

        }
    }

    void filter(String text) {
        ArrayList<MusicModel> temp = new ArrayList();
        for (MusicModel d : musics) {

            if (d != null) {

                if (d.getTitle().contains(text)) {
                    temp.add(d);
                }
            }

        }
        musicAdapter.updateList(temp);
    }
}