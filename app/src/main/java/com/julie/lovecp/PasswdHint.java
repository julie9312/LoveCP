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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswdHint extends AppCompatActivity {

    EditText editEmail3;
    EditText editPasswdHint2;
    Button btnLoginHint;
    String newPasswd;
    String passwd_hint;

    RequestQueue requestQueue;

    AlertDialog.Builder finishAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd_hint);

        requestQueue = Volley.newRequestQueue(PasswdHint.this);

        editEmail3 = findViewById(R.id.editEmail3);
        editPasswdHint2 = findViewById(R.id.editPasswdHint2);
        btnLoginHint = findViewById(R.id.btnLoginHint);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        newPasswd = sp.getString("passwd", null);
        passwd_hint = sp.getString("passwd_hint", null);

        btnLoginHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String savedemail = editEmail3.getText().toString().trim();
                String passwd_hint = editPasswdHint2.getText().toString().trim();

                if(savedemail != null && savedemail.equals(editEmail3)){
                    Toast.makeText(PasswdHint.this, "가입된정보가 없습니다.",
                            Toast.LENGTH_SHORT).show();
                    Log.i("email", savedemail);
                    return;
                }

                if(passwd_hint.equals(passwd_hint) == false){
                    Toast.makeText(PasswdHint.this, "정답이 일치 하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject body = new JSONObject();
                try {
                    body.put("email", savedemail);
                    body.put("passwd_hint", passwd_hint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, Utils.BASE_URL + "/api/v1/users", body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    boolean success = response.getBoolean("success");
                                    if(success) {
                                        String passwd = response.getString("passwd");
                                        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("passwd", passwd);
                                        editor.apply();
                                    } else {
                                        Log.i("error", "ERROR : " + toString());
                                        Toast.makeText(PasswdHint.this, "비밀번호 찾기 실패", Toast.LENGTH_SHORT).show();
                                    }
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
                );
                Volley.newRequestQueue(PasswdHint.this).add(request);


                AlertDialog.Builder finishAlert = new AlertDialog.Builder(PasswdHint.this);
                finishAlert.setTitle("정답이 일치 합니다");
                finishAlert.setMessage("새로운비번은 :"+ newPasswd);
                finishAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        Intent i = new Intent(PasswdHint.this, Login.class);
                        startActivity(i);

                    }
                });
                finishAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface  dialogInterface, int which) {
                        Toast.makeText(PasswdHint.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                finishAlert.setCancelable(false);
                finishAlert.show();
            }
        });

    }
}