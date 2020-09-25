package com.julie.lovecp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post_Main extends AppCompatActivity {

    Button btnSave;

    RequestQueue requestQueue;
    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;
    ArrayList<Post> postArrayList = new ArrayList<>();

    EditText editTitle;
    EditText editBody;

    Intent i;

    // 페이징 처리를 위한 변수
    int offset = 0;
    int limit = 25;
    int cnt;
    String path = "/api/v1/posts";
    String token;

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
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView2.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();

                int totalCount = recyclerView2.getAdapter().getItemCount();

                if ((lastPosition + 1) == totalCount) {
                    if (cnt == limit) {
                        addNetworkData(path);
                    }
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Post_Main.this, AddPosting.class);
                startActivity(i);
                finish();
            }
        });


        requestQueue = Volley.newRequestQueue(Post_Main.this);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        if (token != null) {
            path = "/api/v1/posts/auth";
        } else {
            path = "/api/v1/posts";
        }
        getNetworkData(path);

    }

    private void getNetworkData(String path) {

        Log.i("AAA", Utils.BASE_URL + path + "?offset="+offset+"&limit="+limit);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Utils.BASE_URL + path + "?offset=" + offset + "&limit=" + limit, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                int id = items.getJSONObject(i).getInt("id");
                                String title = items.getJSONObject(i).getString("title");
                                String body = items.getJSONObject(i).getString("body");

                                Post post = new Post(id, title, body);
                                postArrayList.add(post);

                            }

                            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                            recyclerView2.setAdapter(recyclerViewAdapter2);

                            // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시
                            // 호출할때, 해당 offset 으로 서버에 요청이 가능하다.
                            offset = offset + response.getInt("cnt");
                            cnt = response.getInt("cnt");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    DatabaseHandler dh = new DatabaseHandler(Post_Main.this);
                    postArrayList =dh.getAllPost();
                    recyclerViewAdapter2 =new

                    RecyclerViewAdapter2(Post_Main .this, postArrayList);
                    recyclerView2.setAdapter(recyclerViewAdapter2);
                }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void addNetworkData(String path) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Utils.BASE_URL + path + path + "?offset=" + offset + "&limit=" + limit, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean success = response.getBoolean("success");
                            if(success == false){
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i <items.length(); i++){
                                int id = items.getJSONObject(i).getInt("id");
                                String title = items.getJSONObject(i).getString("title");
                                String body = items.getJSONObject(i).getString("body");

                                Post post = new Post(id, title, body);
                                postArrayList.add(post);

                            }

                            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                            recyclerView2.setAdapter(recyclerViewAdapter2);

                            // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시
                            // 호출할때, 해당 offset 으로 서버에 요청이 가능하다.
                            offset = offset + response.getInt("cnt");
                            cnt = response.getInt("cnt");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };

        requestQueue.add(request);
    }

}
    

