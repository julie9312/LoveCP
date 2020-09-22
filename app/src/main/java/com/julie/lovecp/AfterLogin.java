package com.julie.lovecp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AfterLogin extends AppCompatActivity implements View.OnClickListener{

    String token;
    Button btnLogout2;
    String query = "";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        btnLogout2 = findViewById(R.id.btnLogout2);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);

        token = sp.getString("token", null);

        requestQueue = Volley.newRequestQueue(AfterLogin.this);

        btnLogout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == btnLogout2){
            Log.i("로그아웃", "버튼클릭 : 로그아웃");
            logoutRequest(Request.Method.DELETE, "/api/v1/users/logout", null);
        }

    }
    public void logoutRequest(int method, final String api_url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(method, Utils.BASE_URL + api_url + query, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("response",Utils.BASE_URL + api_url + query);

//                        String token = response.getString("token");
//                        String user_id = response.getString("user_id");


                        try {
                            boolean success = response.getBoolean("success");
                            if(success){

                            SharedPreferences preferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", null);
                            editor.apply();

                                Intent i = new Intent(AfterLogin.this, Login.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(AfterLogin.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AfterLogin.this,"로그아웃실패2", Toast.LENGTH_SHORT).show();
                        Log.i("error", "ERROR : " + error.toString());
                        return;
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
        Volley.newRequestQueue(AfterLogin.this).add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(AfterLogin.this, Login.class);
        startActivity(i);
        finish();
    }
}