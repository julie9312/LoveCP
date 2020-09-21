package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.julie.lovecp.data.DatabaseHandler;
import com.julie.lovecp.model.Post;

public class UpdatePosting extends AppCompatActivity {

    EditText editTitle;
    EditText editBody;
    Button btnUpdate;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_posting);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        btnUpdate = findViewById(R.id.btnUpdate);


        // 어댑터의 카드뷰 클릭하면, 여기서 데이터를 받아준다.
        id = getIntent().getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        // 화면에 표시
        editTitle.setText(title);
        editBody.setText(body);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();

                Post post = new Post(id, title, body);
                DatabaseHandler dh = new DatabaseHandler(UpdatePosting.this);
                dh.updatePost(post);
                Toast.makeText(UpdatePosting.this, "수정완료", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(UpdatePosting.this, Post_Main.class);
                startActivity(i);
                finish();

            }
        });
    }
}