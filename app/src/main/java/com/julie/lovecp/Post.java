package com.julie.lovecp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julie.lovecp.adapter.RecyclerViewAdapter2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class Post extends AppCompatActivity {


    EditText editTitle;
    EditText editPost;

    RecyclerView recyclerView;
    RecyclerViewAdapter2 recyclerViewAdapter2;
    ArrayList<Post> postArrayList = new ArrayList<>();
    Post posts;

    private AlertDialog dialog;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Post.this));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Alert", "response : " + response.toString());
                        for(int i = 0 ; i < response.length() ; i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                int userId = object.getInt("userId");
                                int id = object.getInt("id");
                                String title = object.getString("title");
                                String body = object.getString("body");

                                Post post = new Post(userId,id,title,body);
                                postArrayList.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapter2 = new RecyclerViewAdapter2(Post.this, postArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Alert", "error : " + error.toString());
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_add) {
            i = new Intent(Post.this,AddPosting.class);
            startActivityForResult(i,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_CANCELED){

        }
        if(requestCode == 0 && resultCode == RESULT_OK){
            // (새로운 방법) 액티비티간 정보를 객체(클래스 자체)로 전달
            // 1. ★intent 에서 보낸 "객체" 가져오기
            // intent 에 저장한 정보는 여기서는 data 로 가져오시면 된다고요... i로 getIntent(); 쓰지 말라고요..
            posts = (Post) data.getSerializableExtra("PostClass");
            // 2. 필요한 데이터 뽑기
            String title = posts.getTitle();
            String body = posts.getBody();
            // 3. 가져온 정보를 리사이클러뷰에 표시되도록 셋팅
            // 3-1. 뽑은 데이터 새 객체에 저장하기
            posts = new Post(title,body);
            // 3-2. 새로 만든 객체 ArrayList 에 추가하기
            postArrayList.add(posts);
            // 3-3. 갱신된 ArrayList 를 어댑터에 셋팅하기
            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post.this, postArrayList);
            // 3-4. 셋팅한 어댑터 리사이클러뷰에 넣어부리기
            recyclerView.setAdapter(recyclerViewAdapter2);

        }
    }
    private void createPopupDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Post.this);

        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        editTitle = alertView.findViewById(R.id.editTitle);
        editPost = alertView.findViewById(R.id.editPost);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = editTitle.getText().toString().trim();
                String post = editPost.getText().toString().trim();
                if(title.isEmpty() || post.isEmpty()) {
                    Toast.makeText(Post.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                posts = new Post(title,post);
                postArrayList.add(posts);

                recyclerViewAdapter2 = new RecyclerViewAdapter2(Post.this, postArrayList);
            }
        });
        alert.setNegativeButton("NO",null);

        alert.setView(alertView);
        alert.setCancelable(false);

        dialog = alert.create();
        dialog.show();
    }
}