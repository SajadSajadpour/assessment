package com.example.assessment.Activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.assessment.Adapter.ArticleAdapter;
import com.example.assessment.Decor.SpacesItemDecoration;
import com.example.assessment.Dialog.FilterDialogFragment;
import com.example.assessment.Listener.EndlessRecyclerViewScrollListener;
import com.example.assessment.Listener.RecyclerViewItemClickSupport;
import com.example.assessment.Model.ApiResponse;
import com.example.assessment.Model.Doc;
import com.example.assessment.Network.ApiServiceSingleton;
import com.example.assessment.R;
import com.example.assessment.Util.Util;
import com.example.assessment.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_ARTICLE_URL = "ArticleUrl";
    public static final String TAG_FILTER_DIALOG = "filter";

    // General
    Activity mActivity;
    SharedPreferences mPref;
    ActivitySearchBinding b;

    // RecyclerView
    ArrayList<Doc> mArticles;
    ArticleAdapter mAdapter;
    ProgressDialog mProgressDialog;
    EndlessRecyclerViewScrollListener mScrollListener;

    // API requests
    String mQuery;

    // UI
    MenuItem mFilterMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_search);
//        setContentView(R.layout.activity_search);

        mActivity = this;
        mPref = getPreferences(MODE_PRIVATE);
        mArticles = new ArrayList<>();
        mProgressDialog = setupProgressDialog();
        mAdapter = new ArticleAdapter(mArticles, this);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("EXTRA_MESSAGE");

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle("Article");


        // Set up RecyclerView
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadData(page);
            }
        };
        b.recyclerView.setAdapter(mAdapter);
        b.recyclerView.setLayoutManager(layoutManager);
        b.recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        b.recyclerView.addOnScrollListener(mScrollListener);

        RecyclerViewItemClickSupport.addTo(b.recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            // TODO: pass Doc object instead of only URL (make DOC extend Parcelable)
//            Intent intent = new Intent(mActivity, DetailActivity.class);
//            intent.putExtra(EXTRA_ARTICLE_URL, mArticles.get(position).getWebUrl());
//            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setupSearchView(menu);
        // Save reference to the "Filter" menu item and tint icon if any filters are set
        mFilterMenuItem = menu.findItem(R.id.action_filter);
        if (isAnyFilterSet()) tintFilterIcon(true);
        return super.onCreateOptionsMenu(menu);
    }

    // Change color of filter menu icon to accent color (addTint=true) or white (addTint=false)
    public void tintFilterIcon(boolean addTint) {
        Drawable drawable = mFilterMenuItem.getIcon();
        if (drawable != null) {
            drawable.mutate();
            if (addTint)
                drawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            else
                drawable.setColorFilter(null);
        }
    }


    // Return true if any filter is enabled in the SharedPreferences, and false otherwise
    public boolean isAnyFilterSet() {
        return !mPref.getString(getString(R.string.pref_begin_date), "").isEmpty()
                || !mPref.getString(getString(R.string.pref_end_date), "").isEmpty()
                || !mPref.getString(getString(R.string.pref_sort_order), "").isEmpty()
                || !mPref.getString(getString(R.string.pref_news_desk), "").isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                DialogFragment newFragment = new FilterDialogFragment();
                newFragment.show(getFragmentManager(), TAG_FILTER_DIALOG);
                return true;
            default:
                return false;
        }
    }

    // TODO: check if using ProgressBar is better
    private ProgressDialog setupProgressDialog() {
        ProgressDialog p =  new ProgressDialog(mActivity);
        p.setIndeterminate(true);
        p.setMessage(getString(R.string.progress_loading));
        return p;
    }

    // Read filters from SharedPreferences, and construct and execute the API query
    private void loadData(int page) {
        // Read query parameter values from SharedPreferences
        String beginDate = nullify(mPref.getString(getString(R.string.pref_begin_date), ""));
        String endDate = nullify(mPref.getString(getString(R.string.pref_end_date), ""));
        String sortOrder = nullify(mPref.getString(getString(R.string.pref_sort_order), ""));
        String newsDesk = nullify(mPref.getString(getString(R.string.pref_news_desk), ""));
        String query = nullify(mQuery);

        // The "fq" parameter uses Lucene query syntax:
        //     field:value
        // If the value contains whitespace, enclose it in double quotes:
        //     field:"my value"
        // For specifying multiple values for a field enclose them in parentheses:
        //     field:("value 1" "value 2")
        // The default operator for multiple values is OR. It's possible to specify AND:
        //     field:("value 1" AND "value 2")
        // Multiple fields can be specified by separating them with a space:
        //     field1:("value 1" "value_2") field2:"value 3"
        // The default operator for multiple fields is OR. It's possible to specify AND:
        //     field1:("value 1" "value 2") AND field2:"value 3"
        //
        // Notes:
        //   - If an invalid token is submitted (e.g. invalid field name or news_desk category),
        //     then it is just ignored (no error returned). An error is only returned if the
        //     Lucene query syntax is invalid.
        //   - In general, the "q" parameter is optional (might make sense if, for example,
        //     searching all the articles of today with begin_date=...&end_date=...)
        if (newsDesk != null) {
            ArrayList<String> a = new ArrayList<>(Arrays.asList(newsDesk.split(":")));
            Iterator itr = a.iterator();
            newsDesk = "news_desk:(";
            while (itr.hasNext()) {
                newsDesk += "\"" + itr.next() + "\"";
                if (itr.hasNext()) newsDesk += " ";
                else newsDesk += ")";
            }
        }

        query(query, newsDesk, beginDate, endDate, sortOrder, page);
    }

    // Return null for an empty string, and the original string for an non-empty string
    private String nullify(String str) {
        return (str.isEmpty()) ? null : str;
    }

    private void setupSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Called when query is submitted (by pressing "Search" button on keyboard)
            // Note: empty search queries detected by the SearchView itself and ignored
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.clearArticles();
                mScrollListener.resetState();
                mQuery = query;
                loadData(0);
                loadData(1);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void query(String q, String fq, String beginDate, String endDate, String sort, Integer page) {
        mProgressDialog.show();
        Call<ApiResponse> call = ApiServiceSingleton.getInstance().query(q, fq, beginDate, endDate, sort, page);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                // API rate limits: 1000 requests per day, 1 request per second (check X-RateLimit
                // fields in HTTP response).
                System.out.println("articles --->"+response);

                if (response.code() == 429) {
                    Log.v(LOG_TAG, response.code() + ": rate limit exceeded");
                    return;
                }
                try {
                    ArrayList<Doc> articles = (ArrayList<Doc>) response.body().getResponse().getDocs();
                    System.out.println("articlessss --->"+articles);
                    if (articles.isEmpty()) {}
                    //Util.toastLong(mActivity, getString(R.string.toast_no_results));
                    else
                        mAdapter.appendArticles(articles);
                }
                catch (NullPointerException e) {
                    fail(e);
                }
                mProgressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                fail(t);
            }

            private void fail(Throwable t) {
                // TODO: check for SocketTimeoutException (if connection is slow)
                // TODO: check for UnknownHostException (if there is no Internet connection)
                Util.toastLong(mActivity, "Query failed: " + t.getClass().getSimpleName());
                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
