package com.julie.lovecp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.julie.lovecp.data.DatabaseHandler;
import com.julie.lovecp.model.Post;

public class AddPosting extends AppCompatActivity {


    EditText editTitle;
    EditText editBody;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 1. ★툴바 왼쪽에 백버튼이 생김. 액션은 별개임.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 다른 액티비티에 앱바 설정하는 과정
        // (1) activity_main.xml 에 있는 앱바레이아웃, 툴바 설정을 복사해서 이 액티비티와 연결된 .xml 파일에 붙여넣기
        // (2) 앱바 만들 액티비티에서 Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar); 설정
        // (3) 매니페스트 파일에 등록된 이 액티비티에 android:theme="@style/AppTheme.NoActionBar" 설정 추가
        // (4) 없이도 됨. ^.^.. 위에 세 단계 다 셋팅하지 않고 onCreateOptionsMenu() 여기서
        // 메뉴 주소(R.menu.~, menu) 설정을 이 액티비티 전용 menu_~.xml 로 바꿔주면 됨.

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();
                if(title.isEmpty() || body.isEmpty()){
                    Toast.makeText(AddPosting.this,
                            "데이터를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //저장
                DatabaseHandler dh = new DatabaseHandler(AddPosting.this);
                Post post = new Post();
                post.setTitle(title);
                post.setBody(body);
                dh.addPost(post);

                // 잘 저장했다고 토스트
                Toast.makeText(AddPosting.this, "잘 저장되었습니다.",
                        Toast.LENGTH_SHORT).show();

                // 메인액티비티 다시 보이도록

                finish();

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}