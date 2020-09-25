package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.data.DatabaseHandler;
import com.julie.lovecp.model.Post;
import com.julie.lovecp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPosting extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText editTitle;
    EditText editBody;
    Button btnSave;

    String token;
    String path = "/api/v1/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        if (token != null) {
            path = "/api/v1/posts/auth";
        } else {
            path = "/api/v1/posts";
        }

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        btnSave = findViewById(R.id.btnSave);

        requestQueue = Volley.newRequestQueue(AddPosting.this);




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();
                if (title.isEmpty() || body.isEmpty()) {
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