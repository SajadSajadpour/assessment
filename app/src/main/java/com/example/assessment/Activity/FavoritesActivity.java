package com.example.assessment.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.assessment.Adapter.MainListAdapter;
import com.example.assessment.Model.Article;
import com.example.assessment.Model.ArticleViewModel;
import com.example.assessment.R;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ArticleViewModel mArticleViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add back button on toolbar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }

        final MainListAdapter adapter = new MainListAdapter(this);
        ListView listView = findViewById(R.id.articles_list_view);
        listView.setAdapter(adapter);

        mArticleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        mArticleViewModel.getFavorites().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                adapter.setFavorites(articles);
                adapter.setArticles(articles);
            }
        });
    }
}
