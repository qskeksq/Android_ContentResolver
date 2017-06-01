package com.example.administrator.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// 권한에 대한 내용은 따로 클래스로 빼낸다.
public class CheckPermissionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
    }


}
