package com.example.assessment.Data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.assessment.Model.Article;
import com.example.assessment.Model.DataWrapper;
import com.example.assessment.Network.ApiService;
import com.example.assessment.Util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;

public class DataProvider {
    private static LocalDatabase mDB;
    private Call<DataWrapper> mCall;
    private ApiService mNytapiService;

    private MutableLiveData<List<Article>> mMostViewed = new MutableLiveData<>();
    private MutableLiveData<List<Article>> mMostEmailed = new MutableLiveData<>();
    private MutableLiveData<List<Article>> mMostShared = new MutableLiveData<>();
    private MutableLiveData<String> mErrorMessage = new MutableLiveData<>();

    public DataProvider(Application application) {
        mDB = LocalDatabase.getLocalDBInstance(application);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Retrofit.NYTBaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNytapiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<Article>> getMostEmailed() {
        mErrorMessage.setValue(null);
        mCall = mNytapiService.getMostEmailed(Constants.Retrofit.NYTApiKey);
        CallbackReceiver callbackReceiver = new CallbackReceiver(Constants.Tabs.MOST_EMAILED_TAB);
        mCall.enqueue(callbackReceiver);
        return mMostEmailed;
    }

    public LiveData<List<Article>> getMostShared() {
        mErrorMessage.setValue(null);
        mCall = mNytapiService.getMostShared(Constants.Retrofit.NYTApiKey);
        CallbackReceiver callbackReceiver = new CallbackReceiver(Constants.Tabs.MOST_SHARED_TAB);
        mCall.enqueue(callbackReceiver);
        return mMostShared;
    }

    public LiveData<List<Article>> getMostViewed() {
        mErrorMessage.setValue(null);
        mCall = mNytapiService.getMostViewed(Constants.Retrofit.NYTApiKey);
        CallbackReceiver callbackReceiver = new CallbackReceiver(Constants.Tabs.MOST_VIEWED_TAB);
        mCall.enqueue(callbackReceiver);
        return mMostViewed;
    }

//    @Override
//    public void onResponse(Call<DataWrapper> call, Response<DataWrapper> response) {
//
//    }
//
//    @Override
//    public void onFailure(Call<DataWrapper> call, Throwable t) {
//
//    }

    private class CallbackReceiver implements Callback<DataWrapper> {
        private int mQueryType;
        CallbackReceiver(int queryType) {
            mQueryType = queryType;
        }

        @Override
        public void onResponse(Call<DataWrapper> call, Response<DataWrapper> response) {
            if (response.isSuccessful()) {
                DataWrapper dataWrapper = response.body();
                if (dataWrapper != null) {
                    List<Article> articles = dataWrapper.getArticles();
                    for(Article article : articles) {
                        article.generateId();
                    }
                    switch(mQueryType) {
                        case Constants.Tabs.MOST_EMAILED_TAB:
                            DataProvider.this.mMostEmailed.setValue(articles);
                            break;
                        case Constants.Tabs.MOST_VIEWED_TAB:
                            DataProvider.this.mMostViewed.setValue(articles);
                            break;
                        case Constants.Tabs.MOST_SHARED_TAB:
                            DataProvider.this.mMostShared.setValue(articles);
                            break;
                    }

                } else {
                    DataProvider.this.mErrorMessage.setValue("No data received.");
                }
            } else {
                System.out.println(response.errorBody());
                DataProvider.this.mErrorMessage.setValue("Can't parse received data.");
            }
        }

        @Override
        public void onFailure(Call<DataWrapper> call, Throwable t) {
            Log.d(DataProvider.this.getClass().getSimpleName(), t.getMessage());
            t.printStackTrace();
            DataProvider.this.mErrorMessage.setValue("Failed to get articles from server.");
        }
    }

    public LiveData<List<Article>> getFavorites() {
        return mDB.articleDao().getFavorites();
    }

    public void insertFavorite(Article article) {
        new InsertAsyncTask().execute(article);
    }

    public void deleteFavorite(Article article) {
        new DeleteAsyncTask().execute(article);
    }

    private static class InsertAsyncTask extends AsyncTask<Article, Void, Void> {
        @Override
        protected Void doInBackground(Article... articles) {
            mDB.articleDao().insert(articles[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Article, Void, Void> {
        @Override
        protected Void doInBackground(Article... articles) {
            mDB.articleDao().delete(articles[0]);
            return null;
        }
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }
}
