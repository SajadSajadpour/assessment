package com.example.assessment.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessment.Model.LandingListData;
import com.example.assessment.R;

public class LandingListAdapter extends RecyclerView.Adapter<LandingListAdapter.ViewHolder>{

    private final LandingListData[] listdata;
    private OnItemClickListener mOnItemClickListener;

    // RecyclerView recyclerView;
    public LandingListAdapter(LandingListData[] listdata, OnItemClickListener onItemClickListener) {
        this.listdata = listdata;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.landing_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final LandingListData landingListData = listdata[position];
        holder.textView.setText(listdata[position].getBtnName());
        holder.imageView.setImageResource(listdata[position].getBtnId());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
