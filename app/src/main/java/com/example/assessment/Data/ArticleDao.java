package com.example.assessment.Data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assessment.Model.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Article article);

    @Delete
    void delete(Article article);

    @Query("SELECT * from favorites_table")
    LiveData<List<Article>> getFavorites();
}
