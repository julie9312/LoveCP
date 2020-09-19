package com.julie.lovecp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julie.lovecp.model.Translations;
import com.julie.lovecp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Papago extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText editBefore;
    TextView txtAfter;
    Button btnTrans;

    String before;

    Translations new_translations;

    JSONObject jsonObject;
    JSONObject messageObject;
    JSONObject resultObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papago);

        editBefore = findViewById(R.id.editBefore);
        txtAfter = findViewById(R.id.txtAfter);
        btnTrans = findViewById(R.id.btnTrans);


        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                before = editBefore.getText().toString().trim();

                if(before.isEmpty()){
                    Toast.makeText(Papago.this, "번역할 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                setRequestQueue();
            }
        });
    }

    private void setRequestQueue() {
        requestQueue = Volley.newRequestQueue(Papago.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.Trans_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Papago","response : "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject message = jsonObject.getJSONObject("message");
                            JSONObject result = message.getJSONObject("result");
                            String after = result.getString("translatedText");

                            txtAfter.setText(after);

                            new_translations = new Translations(before, after);
                            Log.i("trans", new_translations.getAfter());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Papago","error : "+error);

                    }
                }
        ) {
            // 네이버 API 헤더 셋팅 부분을 여기에 작성한다.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                params.put("X-Naver-Client-Id", "Tziw5pHAM3K5KyoEXgXC");
                params.put("X-Naver-Client-Secret", "7A3RfXoHrJ");
                return params;
            }

            // 네이버에 요청할 파라미터를 셋팅한다.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("source", "en");
                params.put("target", "ko");
                params.put("text", before);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}