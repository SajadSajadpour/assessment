package com.example.assessment.Model;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assessment.Data.DataProvider;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    private DataProvider mDataProvider;
    private LiveData<List<Article>> mFavorites;
    private LiveData<List<Article>> mMostViewed;
    private LiveData<List<Article>> mMostEmailed;
    private LiveData<List<Article>> mMostShared;
    private LiveData<String> mErrorMessage;

    public ArticleViewModel(Application application) {
        super(application);
        mDataProvider = new DataProvider(application);
        mFavorites = mDataProvider.getFavorites();
        mErrorMessage = mDataProvider.getErrorMessage();

        updateData();
    }

    public LiveData<List<Article>> getFavorites() {
        return mFavorites;
    }

    public LiveData<List<Article>> getMostEmailed() {
        return mMostEmailed;
    }

    public LiveData<List<Article>> getMostShared() {
        return mMostShared;
    }

    public LiveData<List<Article>> getMostViewed() {
        return mMostViewed;
    }

    public void insertFavorite(Article article) {
        mDataProvider.insertFavorite(article);
    }

    public void deleteFavorite(Article article) {
        mDataProvider.deleteFavorite(article);
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void updateData() {
        mMostEmailed = mDataProvider.getMostEmailed();
        mMostViewed = mDataProvider.getMostViewed();
        mMostShared = mDataProvider.getMostShared();
    }
}
