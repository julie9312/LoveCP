package com.julie.lovecp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.julie.lovecp.AddPosting;
import com.julie.lovecp.R;
import com.julie.lovecp.UpdatePosting;
import com.julie.lovecp.data.DatabaseHandler;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtBody;
        public ImageView imgDelete;
        public CardView cardView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView2 = itemView.findViewById(R.id.cardView2);
            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();

                    Post post = postArrayList.get(index);
                    int id = post.getId();
                    String title = post.getTitle();
                    String body = post.getBody();

                    Intent i = new Intent(context, UpdatePosting.class);
                    i.putExtra("id", id);
                    i.putExtra("title", title);
                    i.putExtra("body", body);
                    context.startActivity(i);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(context);
                    deleteAlert.setTitle("메모 삭제");
                    deleteAlert.setMessage("정말 삭제하시겠습니까??");
                    deleteAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            int index = getAdapterPosition();
                            Post post = postArrayList.get(index);
                            DatabaseHandler dh = new DatabaseHandler(context);
                            dh.deletePost(post);

                            postArrayList = dh.getAllPost();
                            notifyDataSetChanged();
                        }
                    });
                    deleteAlert.setNegativeButton("NO", null);
                    deleteAlert.setCancelable(false);
                    deleteAlert.show();


                }
            });

        }
    }
}
