package com.julie.lovecp;


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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AfterLogin extends AppCompatActivity {

    TextView txtValue;
    Button btnLogout2;
    String token;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        txtValue = findViewById(R.id.txtValue);
        btnLogout2 = findViewById(R.id.btnLogout2);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);

        token = sp.getString("token", null);

        requestQueue = Volley.newRequestQueue(AfterLogin.this);

        btnLogout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                        Utils.BASE_URL + "/api/v1/users/logout", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("ZZZ", response.toString());

                                        SharedPreferences preferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("token", null);
                                        editor.apply();
                                        Intent i = new Intent(AfterLogin.this, Login.class);
                                        startActivity(i);
                                        finish();


                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("error", "ERROR : " + error.toString());
                                Toast.makeText(AfterLogin.this,"로그아웃실패2", Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String token = sp.getString("token", null);
                        params.put("Authorization", "Bearer "+token);
                        return params;
                    }
                };

                Volley.newRequestQueue(AfterLogin.this).add(request);

            }
        });
    }
}
