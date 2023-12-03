package com.astarconcepts.brandbanao.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;

@Database(entities = {FavoriteList.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.app_name)).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        }

        return INSTANCE;

    }

    public abstract MyDAO myDao();

}
 