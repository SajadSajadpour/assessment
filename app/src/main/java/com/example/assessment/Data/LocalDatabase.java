package com.example.assessment.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assessment.Model.Article;
import com.example.assessment.Util.Constants;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();

    private static LocalDatabase DB_INSTANCE;

    public static LocalDatabase getLocalDBInstance(Context context) {
        if (DB_INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = Room.databaseBuilder(context, LocalDatabase.class, Constants.DB.localDBName)
                            .build();
                }
            }

        }
        return DB_INSTANCE;
    }
}
