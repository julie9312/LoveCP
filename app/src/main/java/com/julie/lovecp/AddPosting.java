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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPosting extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText editTitle;
    EditText editContent;
    Button btnSave;

    String token;
    String path = "/api/v1/posts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);

        requestQueue = Volley.newRequestQueue(AddPosting.this);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);


        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddPosting.this,
                            "데이터를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject body = new JSONObject();
                try {
                    body.put("title", title);
                    body.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        Utils.BASE_URL + path, body,
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
                                Log.i("test",error+"");
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
                Volley.newRequestQueue(AddPosting.this).add(request);
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