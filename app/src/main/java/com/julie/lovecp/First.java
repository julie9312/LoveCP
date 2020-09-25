package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.adapter.RecyclerViewAdapter;
import com.julie.lovecp.model.News;
import com.julie.lovecp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class First extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<News> newsArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    Button btnA;
    Button btnB;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(First.this));

        btnA = findViewById(R.id.btnA);
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(First.this, Papago.class);
                startActivity(i);
            }
        });

        btnB = findViewById(R.id.btnB);
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(First.this, Post_Main.class);
                startActivity(i);
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(First.this, AfterLogin.class);
                startActivity(i);
                finish();
            }
        });


        requestQueue = Volley.newRequestQueue(First.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Utils.NEWS_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());

                            try {
                                JSONArray articles = response.getJSONArray("articles");

                                for (int i = 0; i < articles.length(); i++) {
                                    JSONObject jsonObject = articles.getJSONObject(i);
                                    String author = jsonObject.getString("author");
                                    String title = jsonObject.getString("title");
                                    String desc = jsonObject.getString("description");
                                    String url = jsonObject.getString("url");
                                    String urlToImage = jsonObject.getString("urlToImage");
                                    String publishedAt = jsonObject.getString("publishedAt");

                                    News news = new News(author, title, desc, url, urlToImage, publishedAt);
                                    newsArrayList.add(news);
                                }
                                recyclerViewAdapter = new RecyclerViewAdapter(First.this, newsArrayList);
                                recyclerView.setAdapter(recyclerViewAdapter);

                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}
