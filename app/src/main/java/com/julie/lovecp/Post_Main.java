package com.julie.lovecp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.julie.lovecp.adapter.RecyclerViewAdapter2;
import com.julie.lovecp.data.DatabaseHandler;
import com.julie.lovecp.model.Post;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Post_Main extends AppCompatActivity {

    Button btnSave;

    RequestQueue requestQueue;
    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;
    ArrayList<Post> postArrayList = new ArrayList<>();

    EditText editTitle;
    EditText editBody;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);
        btnSave = findViewById(R.id.btnSave);
        recyclerView2 = findViewById(R.id.recyclerView2);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Post_Main.this));



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Post_Main.this, AddPosting.class);
                startActivity(i);
                finish();
            }
        });


        requestQueue = Volley.newRequestQueue(Post_Main.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Utils.BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // 요기  String , int 값 안 먹음 추후 확인 일단 에러 제거
                                JSONObject object = response.getJSONObject(String.valueOf(i));
                                int id = object.getInt("id");
                                String title = object.getString("title");
                                String body = object.getString("body");
                                Post post = new Post(id, title, body);
                                postArrayList.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        DatabaseHandler dh = new DatabaseHandler(Post_Main.this);
                        postArrayList = dh.getAllPost();
                        recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                        recyclerView2.setAdapter(recyclerViewAdapter2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Alert", "error : " + error.toString());
                    }
                }
        );
        requestQueue.add(request);
    }
        @Override
        protected void onResume() {
            super.onResume();
            DatabaseHandler dh = new DatabaseHandler(Post_Main.this);
            postArrayList = dh.getAllPost();
            // 어댑터를 연결해야지 화면에 표시가 됨.
            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
            recyclerView2.setAdapter(recyclerViewAdapter2);
        }
    }
