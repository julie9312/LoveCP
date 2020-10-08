package com.julie.lovecp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText editEmail;
    EditText editPasswd1;
    EditText editPasswd2;
    EditText editPasswdHint;
    Button btnRegister;

    RequestQueue requestQueue;

    private String token;
    private AlertDialog.Builder finishAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPasswd1 = findViewById(R.id.editPasswd1);
        editPasswd2 = findViewById(R.id.editPasswd2);
        editPasswdHint = findViewById(R.id.editPasswdHint);
        btnRegister = findViewById(R.id.btnRegister);

        requestQueue = Volley.newRequestQueue(Register.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editEmail.getText().toString().trim();
                String passwd = editPasswd1.getText().toString().trim();
                String passwd2 = editPasswd2.getText().toString().trim();
                String passwd_hint = editPasswdHint.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(Register.this, "이메일 입력은 필수입니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.contains("@") || !email.contains(".")) {
                    Toast.makeText(Register.this, "@와 .을 전부 포함한 이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwd.isEmpty()) {
                    Toast.makeText(Register.this, "비밀번호 입력은 필수입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() < 4 || passwd.length() > 12) {
                    Toast.makeText(Register.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwd.equalsIgnoreCase(passwd2) == false) {
                    Toast.makeText(Register.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd_hint.isEmpty()) {
                    Toast.makeText(Register.this, "힌트는 필수로 입력하셔야 합니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    body.put("passwd", passwd);
                    body.put("passwd_hint", passwd_hint);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder finishAlert = new AlertDialog.Builder(Register.this);
                finishAlert.setTitle("회원가입완료");
                finishAlert.setMessage("완료하시겠습니까?");
                finishAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.POST,
                                Utils.BASE_URL + "/api/v1/users",
                                body,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String token = response.getString("token");
                                            Log.i("AAA", "networktoken : " + token);
                                            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("token", token);
                                            editor.apply();

                                            Intent i = new Intent(Register.this, Login.class);
                                            i.putExtra("token", token);
                                            startActivity(i);
                                            finish();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Register.this, "이미 가입된 이메일입니다.", Toast.LENGTH_LONG).show();
                                        return;

                                    }
                                }
                        );
                        Volley.newRequestQueue(Register.this).add(request);
                    }
                });
                finishAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(Register.this, "취소하였습니다", Toast.LENGTH_SHORT).show();

                    }
                });
                finishAlert.setCancelable(false);
                finishAlert.show();
            }
        });
    }

}


