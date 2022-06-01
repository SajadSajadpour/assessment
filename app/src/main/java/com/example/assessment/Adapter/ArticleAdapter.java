package com.example.assessment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.assessment.Model.Doc;
import com.example.assessment.Model.Multimedium;
import com.example.assessment.Network.ApiService;
import com.example.assessment.R;
import com.example.assessment.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private ArrayList<Doc> mData;
    private Context mContext;

    public ArticleAdapter(ArrayList<Doc> data, Context context) {
        mData = data;
        mContext = context;
    }

    // Called when a new view for an item must be created. This method does not return the view of
    // the item, but a ViewHolder, which holds references to all the elements of the view.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // The view for the item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        // Create a ViewHolder for this view and return it
        return new ViewHolder(itemView);
    }

    // Populate the elements of the passed view (represented by the ViewHolder) with the data of
    // the item at the specified position.
    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Doc article = mData.get(position);

        vh.tvTitle.setText(getSafeString(article.getHeadline().getMain()));

        if (article.getPubDate() != null) {
            vh.tvDate.setVisibility(View.VISIBLE);
            DataUtil date = new DataUtil(article.getPubDate());
            vh.tvDate.setText(date.format1());
        } else
            vh.tvDate.setVisibility(View.GONE);

        ArrayList<Multimedium> multimedia = (ArrayList<Multimedium>) article.getMultimedia();
        String thumbUrl = "";
        for (Multimedium m : multimedia) {
            if (m.getType().equals("image") && m.getSubtype().equals("thumbnail")) {
                thumbUrl = ApiService.API_IMAGE_BASE_URL + m.getUrl();
                break;
            }
        }
//        if (!thumbUrl.isEmpty())
//            // TODO: Glide seems to not cache most of these images but load them from the URL each time
//            Glide.with(mContext)
//                    .load(thumbUrl)
//                    // Save original image in cache (less fetching from server)
////                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.placeholder_thumb)
//                    .error(R.drawable.error_thumb)
//                    .into(vh.ivThumb);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String getSafeString(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    public void clearArticles() {
        mData.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    public void appendArticles(List<Doc> articles) {
        int oldSize = getItemCount();
        mData.addAll(articles);
        notifyItemRangeInserted(oldSize, articles.size());
    }


    class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivThumb;
        TextView tvDate;
        TextView tvTitle;

        // Create a viewHolder for the passed view (item view)
        ViewHolder(View view) {
            super(view);
//            ivThumb = (ImageView) view.findViewById(R.id.ivThumb);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}
