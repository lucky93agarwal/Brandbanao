package com.astarconcepts.brandbanao.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.astarconcepts.brandbanao.room.entity.FavoriteList;

import java.util.List;

@Dao
public interface MyDAO {
    @Insert
    public void addData(FavoriteList favoriteList);

    @Query("select * from favoritelist where template_type=:template_type")
    public List<FavoriteList> getFavoriteData(String template_type);

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE poster_id=:poster_id)")
    public int isFavorite(int poster_id);

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE video_id=:video_id)")
    public int isFavoriteVideo(int video_id);

    @Delete
    public void delete(FavoriteList favoriteList);
    @Query("DELETE FROM favoritelist WHERE video_id=:video_id")
    void deleteVideo( int video_id);

    @Query("DELETE FROM favoritelist WHERE poster_id=:poster_id")
    void deletePoster( int poster_id);

}