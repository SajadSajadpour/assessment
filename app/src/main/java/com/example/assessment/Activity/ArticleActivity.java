package com.example.assessment.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assessment.R;
import com.example.assessment.Util.Constants;

public class ArticleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Add back button to activity on toolbar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }

        String articleUrl = getUrl();
        if(articleUrl != null && !articleUrl.isEmpty()) {
            showArticle(articleUrl);
        }
    }

    private String getUrl() {
        return this.getIntent().getStringExtra(Constants.IntentArticleExtras.articleUrl);
    }

    private void showArticle(String url) {
        WebView webView = findViewById(R.id.article_webview);
        webView.loadUrl(url);
    }
}
