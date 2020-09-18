package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        btnRegister = findViewById(R.id.btnRegister);
        btnStart = findViewById(R.id.btnStart);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        SharedPreferences sp = getSharedPreferences("regist_pref", MODE_PRIVATE);
        String token = sp.getString("token", null);
        if(token != null){
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