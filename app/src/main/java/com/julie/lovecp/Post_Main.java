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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julie.lovecp.adapter.RecyclerViewAdapter2;
import com.julie.lovecp.model.Post;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Post_Main extends AppCompatActivity {


    RequestQueue requestQueue;


    RecyclerView recyclerView;
    RecyclerViewAdapter2 recyclerViewAdapter2;
    ArrayList<Post> postArrayList = new ArrayList<>();

    private AlertDialog dialog;
    EditText editTitle;
    EditText editBody;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Post_Main.this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });


        requestQueue = Volley.newRequestQueue(Post_Main.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Utils.BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Alert", "response : " + response.toString());

                        for(int i = 0 ; i < response.length() ; i++){
                            try {
                                // 요기  String , int 값 안 먹음 추후 확인 일단 에러 제거
                                JSONObject object = response.getJSONObject(String.valueOf(i));
                                int id = object.getInt("id");
                                String title = object.getString("title");
                                String body = object.getString("body");

                                Post post = new Post(id,title,body);
                                postArrayList.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                        recyclerView.setAdapter(recyclerViewAdapter2);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.menu_add) {
            i = new Intent(Post_Main.this,AddPosting.class);
            startActivityForResult(i,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Post_Main.this);

        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        editTitle = alertView.findViewById(R.id.editTitle);
        editBody = alertView.findViewById(R.id.editBody);

        alert.setView(alertView);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();

                if(title.isEmpty() || body.isEmpty()){
                    Toast.makeText(Post_Main.this,
                            "글자를 꼭 입력하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Post post = new Post( 1, title, body);
                postArrayList.add(post);
                recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
                recyclerView.setAdapter(recyclerViewAdapter2);
            }
        });
        alert.setNegativeButton("NO", null);

        alert.setCancelable(false);

        dialog = alert.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            Post post = (Post) data.getSerializableExtra("PostClass");

            postArrayList.add(post);

            recyclerViewAdapter2 = new RecyclerViewAdapter2(Post_Main.this, postArrayList);
            recyclerView.setAdapter(recyclerViewAdapter2);

        }

    }
}

