package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class Login extends AppCompatActivity {

    EditText editEmail;
    EditText editPasswd;
    Button btnRegister;
    Button btnLogin;
    CheckBox checkAutoLogin;

    Intent i;

    String savedEmail;
    String savedPassed;
    // 쉐어드 프리퍼런스를 멤버변수로 뺀다.
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPasswd = findViewById(R.id.editPasswd);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        checkAutoLogin = findViewById(R.id.checkAutoLogin);

        sp = getSharedPreferences("regist_pref", MODE_PRIVATE);
        editor = sp.edit();
        savedEmail = sp.getString("email", null);
        savedPassed = sp.getString("passwd", null);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editEmail.getText().toString().trim();
                String passwd = editPasswd.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(Login.this, "형식에 맞지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.isEmpty() || passwd.length() < 4 || passwd.length() >12){
                    Toast.makeText(Login.this, "비밀번호가 형식에 맞지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(savedEmail != null && savedPassed != null &&
                        savedEmail.equals(email) && savedPassed.equals(passwd)) {
                    Toast.makeText(Login.this, "가입된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject body = new JSONObject();

                try{
                    body.put("email", email);
                    body.put("passwd", passwd);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASE_URL + "/api/v1/users/login",
                        body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String token = response.getString("token");
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
                );
                Volley.newRequestQueue(Login.this).add(request);
                if(checkAutoLogin.isChecked()){

                    editor.putBoolean("auto_login", true);
                }else{

                    editor.putBoolean("auto_login", false);
                }
                editor.apply();

                Intent i = new Intent(Login.this, First.class);
                startActivity(i);
                finish();
            }

        });

        //1. 자동로그인이 쉐어드프리퍼런스에 저장되어 있는지 정보 확인.
        boolean autoLogin = sp.getBoolean("auto_login", false);
        //2. 자동로그인이 true 로 되어있으면 token 이 있음.
        //3. 체크박스에도 체크표시
        if(autoLogin == true){
            editEmail.setText(savedEmail);
            editPasswd.setText(savedPassed);
            checkAutoLogin.setChecked(true);
        }

    }
}