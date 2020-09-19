package com.julie.lovecp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.julie.lovecp.AddPosting;
import com.julie.lovecp.Post;
import com.julie.lovecp.R;

import java.util.ArrayList;


public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;
    RecyclerViewAdapter2 recyclerViewAdapter2;

    public RecyclerViewAdapter2(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Post post = postArrayList.get(position);

        String title = post.getTitle();
        String content = post.getContent();

        holder.txtTitle.setText(title);
        holder.txtContent.setText("    "+content);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtContent;
        public ImageView imgDelete;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.editTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();

                    Post post = postArrayList.get(index);
                    int id = post.getId();
                    String title = post.getTitle();
                    String content = post.getContent();

                    Intent i = new Intent(context, AddPosting.class);
                    i.putExtra("id", id);
                    i.putExtra("title", title);
                    i.putExtra("content", content);
                    context.startActivity(i);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("포스트 삭제");
                    alert.setMessage("정말 삭제하시겠습니까?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int index = getAdapterPosition();
                            postArrayList.remove(index);

                            notifyDataSetChanged();
                        }
                    });
                    alert.setNegativeButton("No", null);
                    alert.setCancelable(false);
                    alert.show();

                }
            });
        }
    }
}
