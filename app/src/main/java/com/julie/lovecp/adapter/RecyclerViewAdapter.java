package com.julie.lovecp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.julie.lovecp.R;
import com.julie.lovecp.model.News;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<News> newsArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {

        News news = newsArrayList.get(position);
        String title = news.getTitle();
        String desc = news.getDescription();
        String urlToImage = news.getUrlToImage();
        String author = news.getAuthor();
        String publishedAt = news.getPublishedAt();

        holder.txtTitle.setText(title);
        holder.txtDesc.setText(desc);
        holder.txtAuthor.setText(author);
        holder.txtPublishedAt.setText(publishedAt);

        Glide.with(context).load(urlToImage).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = newsArrayList.get(position).getUrl();
                Uri webpage = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, webpage);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDesc;
        public ImageView img;
        public TextView txtAuthor;
        public TextView txtPublishedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            img = itemView.findViewById(R.id.img);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtPublishedAt = itemView.findViewById(R.id.txtPublishedAt);

        }
    }

}
