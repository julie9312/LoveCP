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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.adapter.RecyclerViewAdapter;
import com.julie.lovecp.adapter.RecyclerViewAdapter2;
import com.julie.lovecp.model.News;
import com.julie.lovecp.model.Post;
import com.julie.lovecp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post_Main extends AppCompatActivity {

    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;
    ArrayList<Post> postArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    Button btnCreate;
    Button btnSync;

    //페이징처리를위한변수
    int offset=0;
    int limit=25;
    int cnt;
    String path = "/api/v1/posts";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Post_Main.this, AddPosting.class);
                startActivity(i);
                finish();
            }
        });

        btnSync = findViewById(R.id.btnSync);

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Post_Main.this));

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if( (lastPosition + 1) == totalCount){
                    if(cnt == limit) {
                        // 네트워크 통해서, 데이터를 더 불러오면 된다.
                        addNetworkData(path);
                    }
                }
            }
        });

        requestQueue = Volley.newRequestQueue(Post_Main.this);
        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

//        if(token != null){
            path = "/api/v1/posts/me";
//        }else{
//            path = "/api/v1/posts";
//        }

        getNetworkData(path);


    }



    private void getNetworkData(String path) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Utils.BASE_URL + path + "?offset=" + offset + "&limit=" + limit, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("bbb", "network token : " +token);

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i <items.length(); i++){
                                int id = items.getJSONObject(i).getInt("id");
                                String title = items.getJSONObject(i).getString("title");
                                String content = items.getJSONObject(i).getString("content");

                                Post post = new Post(id, title, content);
                                postArrayList.add(post);
                            }

                            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                            recyclerView2.setAdapter(recyclerViewAdapter2);

                            offset = offset +response.getInt("cnt");
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

    private void addNetworkData(String path) {

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
                            for(int i = 0; i <items.length(); i++){
                                int id = items.getJSONObject(i).getInt("id");
                                String title = items.getJSONObject(i).getString("title");
                                String content = items.getJSONObject(i).getString("content");

                                Post post = new Post(id, title, content);
                                postArrayList.add(post);
                            }

                            recyclerViewAdapter2.notifyDataSetChanged();

                            // 페이징을 위해서 오프셋을 변경시켜놔야 한다.
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
        Volley.newRequestQueue(Post_Main.this).add(request);

    }

    public void deletePost(int index){
        Post post = postArrayList.get(index);
        final int id = post.getId();

        //발리로 위의 메모 아이디 전송,

        JSONObject body = new JSONObject();
        try {
            body.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                Utils.BASE_URL + "/api/v1/posts/" +id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String token = response.getString("token");
                            Log.i("AAA", "network token : " + token);
                            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", token);
                            editor.apply();

                            Post post = new Post();
                            postArrayList.remove(post);

                            recyclerViewAdapter2.notifyDataSetChanged();

                            finish();
                            return;


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

        Volley.newRequestQueue(Post_Main.this).add(request);



        // 완료되면, postArrayList.remove(index);
        // adapter 노티파이.


    }

}


