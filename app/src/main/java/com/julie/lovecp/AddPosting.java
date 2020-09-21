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

public class AddPosting extends AppCompatActivity {

    EditText editTitle;
    EditText editBody;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();
                if(title.isEmpty() || body.isEmpty()){
                    Toast.makeText(AddPosting.this,
                            "데이터를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //저장
                DatabaseHandler dh = new DatabaseHandler(AddPosting.this);
                Post post = new Post();
                post.setTitle(title);
                post.setBody(body);
                dh.addPost(post);

                // 잘 저장했다고 토스트
                Toast.makeText(AddPosting.this, "잘 저장되었습니다.",
                        Toast.LENGTH_SHORT).show();

                // 메인액티비티 다시 보이도록
                Intent i = new Intent(AddPosting.this, Post_Main.class);
                startActivity(i);
                finish();

            }
        });


    }
}