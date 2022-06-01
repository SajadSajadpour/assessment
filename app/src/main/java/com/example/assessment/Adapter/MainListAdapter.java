package com.example.assessment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.assessment.Activity.ArticleActivity;
import com.example.assessment.Model.Article;
import com.example.assessment.Model.ArticleViewModel;
import com.example.assessment.R;
import com.example.assessment.Util.Constants;

import java.util.List;

public class MainListAdapter extends ArrayAdapter<Article> {

    private List<Article> mArticles; // cached copy of articles
    private List<Article> mFavorites; // cached copy of favorites
    private ArticleViewModel mArticleViewModel;
//    private MainActivity mMainActivity;

    final String LOG_TAG = this.getClass().getSimpleName();

    public MainListAdapter(Context context) {
        super(context, R.layout.list_view_article);
//        mMainActivity = (MainActivity) context;
        mArticleViewModel = ViewModelProviders.of((AppCompatActivity) context).get(ArticleViewModel.class);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_article, parent, false);
        }

        if (mArticles != null) {
            final Article article = mArticles.get(position);

            TextView title = convertView.findViewById(R.id.article_title);
            TextView date = convertView.findViewById(R.id.article_date);
            TextView author = convertView.findViewById(R.id.article_author);
//            final ImageView img = convertView.findViewById(R.id.image_add_favorite);
//            final ProgressBar progressBar = convertView.findViewById(R.id.article_progressBar);

            title.setText(article.getTitle());
            author.setText(article.getAuthor());
            date.setText(article.getPublishedDate());

            final long id = article.getId();
            final boolean isFavorite = isFavorite(article);
//            img.setImageResource(getImgId(isFavorite));
            // Add onClickListeners
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
////                    mMainActivity.showProgressBar(true);
////                    progressBar.setVisibility(View.VISIBLE);
////                    showArticle(article);
////                    progressBar.setVisibility(View.GONE);
//                }
//            });

            // Add/Remove article to/from favorites
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    img.setImageResource(getImgId(!isFavorite));
//                    if(isFavorite) {
//                        Toast.makeText(getContext(), "Remove id: " + id + " from favorites", Toast.LENGTH_SHORT).show();
//                        mArticleViewModel.deleteFavorite(article);
//                    } else {
//                        Toast.makeText(getContext(), "Add id: " + id + " to favorites", Toast.LENGTH_SHORT).show();
//                        mArticleViewModel.insertFavorite(article);
//                    }
//                }
//            });

        }

        return convertView;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    public void setFavorites(List<Article> favorites) {
        mFavorites = favorites;
        notifyDataSetChanged();
    }

    private void showArticle(Article article) {
        Intent intent = new Intent(getContext(), ArticleActivity.class);
        intent.putExtra(Constants.IntentArticleExtras.articleUrl, article.getUrl());
        getContext().startActivity(intent);
    }

    private boolean isFavorite(Article article) {
        if(mFavorites != null) {
            for (Article fav : mFavorites) {
                if(article.getId() == fav.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return mArticles != null ? mArticles.size() : 0;
    }

    private int getImgId(boolean isFavorite) {
        return isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
    }
}
