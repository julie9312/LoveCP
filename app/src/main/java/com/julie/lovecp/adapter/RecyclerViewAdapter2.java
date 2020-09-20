package com.julie.lovecp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julie.lovecp.R;
import com.julie.lovecp.model.Post;

import java.util.ArrayList;


public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;

    public RecyclerViewAdapter2(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.ViewHolder holder, int position) {
        Post post = postArrayList.get(position);

        String title = post.getTitle();
        String body = post.getBody();
        holder.txtTitle.setText(title);
        holder.txtBody.setText(body);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }



        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView txtTitle;
            public TextView txtBody;
            public ImageView imgDelete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.txtTitle);
                txtBody = itemView.findViewById(R.id.txtBody);
                imgDelete = itemView.findViewById(R.id.imgDelete);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = getAdapterPosition();
                        postArrayList.remove(index);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }