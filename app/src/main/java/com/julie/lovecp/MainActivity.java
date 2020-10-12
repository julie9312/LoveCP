package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    Button btnRegister;
    Button btnStart;
    RequestQueue requestQueue;
    String token;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// 자동로그인 1 sp 저장한게 이리로 오게 하는거
        btnRegister = findViewById(R.id.btnRegister);
        btnStart = findViewById(R.id.btnStart);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        SharedPreferences sp = getSharedPreferences("lcpapp", MODE_PRIVATE);
        token = sp.getString("token", null);
        if(token != null){
            Log.i("baba", token);
            Intent i = new Intent(MainActivity.this, First.class);
            startActivity(i);
            finish();

        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
                finish();

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        });


    }
}