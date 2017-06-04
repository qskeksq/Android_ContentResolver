package com.example.administrator.contacts;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

// 권한에 대한 내용은 따로 클래스로 빼낸다.
public class CheckPermissionActivity extends AppCompatActivity {

    private static final int REQ_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);


        // 0. 버전 호환성 처리 -- API level 이 23 이상일 경우만 살행
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // 설치 안드로이프 톤의 api level 가져오기
            checkPermission();
        } else {
            run();
        }
        // 아니면 그냥 run
    }


    @TargetApi(Build.VERSION_CODES.M)  // 이 함수를 사용하는데 필요한 api 를 정해준다.
    private void checkPermission(){
        // 1. 권한 체크 -- 특정권한이 있는지 시스템에 물어본다.
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            run();
        } else {
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            String[] permissions = {Manifest.permission.READ_CONTACTS};
            requestPermissions(permissions , REQ_PERMISSION); // 권한을 요구하는 팝업이 사용자에게 보여진다
        }
    }

    // 3. 사용자가 권한체크 팝업에서 권한을 승인 또는 거절하면 아래 메소드가 호출된다. 결과 처리를 여기서 한다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){ //첫번째로 넘긴 퍼미션의 결과가 승인이라면
                run();
            } else {
                cancel();
            }
        }
    }

    // 4. 권한 설정

    public void run(){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void cancel(){
        Toast.makeText(this, "권한 요청을 승인하셔야 앱을 사용할 수 있습니다", Toast.LENGTH_SHORT).show();
        finish();
    }




}
