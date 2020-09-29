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
import com.julie.lovecp.model.Post;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePosting extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText editTitle;
    EditText editContent;
    Button btnUpdate;
    int id;

    String token;
    String path = "/api/v1/posts/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_posting);

        requestQueue = Volley.newRequestQueue(UpdatePosting.this);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

//        if (token != null) {
//            path = "/api/v1/posts/auth";
//        } else {
//            path = "/api/v1/posts";
//        }

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnUpdate = findViewById(R.id.btnUpdate);


        // 어댑터의 카드뷰 클릭하면, 여기서 데이터를 받아준다.
        id = getIntent().getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        // 화면에 표시
        editTitle.setText(title);
        editContent.setText(content);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                JSONObject body = new JSONObject();
                try {
                    body.put("post_id", id);
                    body.put("title", title);
                    body.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                        Utils.BASE_URL + "/api/v1/posts/" +id, body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String token = response.getString("token");
                                    Log.i("AAA" , "network token : " +token);
                                    SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("token", token);
                                    editor.apply();

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

                Volley.newRequestQueue(UpdatePosting.this).add(request);

                Intent i = new Intent(UpdatePosting.this, Post_Main.class);
                startActivity(i);
                finish();

            }
        });
    }
}