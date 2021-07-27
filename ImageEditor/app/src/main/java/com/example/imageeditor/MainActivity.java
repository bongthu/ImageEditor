package com.example.imageeditor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

public class MainActivity extends AppCompatActivity {

    //변수 초기화
    Button btn_pick;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //변수 할당
        btn_pick = findViewById(R.id.btn_pick);
        imageView = findViewById(R.id.image_view);

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //변수 생성
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        //권한 초기화
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //CHECK CONDITION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 디바이스 버전이 버전 10보다 더 높을때
            //변수 생성
            pickImage();
        } else {
            //디바이스 버전이 10버전 아래일 때
            //컨디션 체크
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //권한이 부여되지 않았을 때
                // 권한을 요구한다
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {
                // 권한이 주어졌을 때
                // 호출 방법
                pickImage();
            }
        }
    }

    private void pickImage() {
        //initialize intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set type
        intent.setType("image/*");
        //strat activity for result
        startActivityForResult(intent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check Condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //권한이 주어졌을 때
            //Call method
            pickImage();
        } else {
            //권한이 주어지지 않았을 때 디스플레이 토스트
            Toast.makeText(getApplicationContext(),
                    "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check condition
        if (resultCode == RESULT_OK) {
            // when result is ok
            // initialize uri
            Uri uri = data.getData();
            switch (requestCode) {
                case 100:
                    //when request code id equal to 100
                    //initialize intent
                    Intent intent = new Intent(MainActivity.this, DsPhotoEditorActivity.class);
                    //set data
                    intent.setData(uri);
                    //set output directory name
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "images");
                    //set toolbar color
                    intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));
                    //set background color
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));
                    //hide tools
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                            new int[]{
                                    DsPhotoEditorActivity.TOOL_WARMTH, DsPhotoEditorActivity.TOOL_PIXELATE});
                    //start activity for result
                    startActivityForResult(intent, 101);
                    break;
                case 101:
                    //when requestcode is equal to 100
                    //set image on image view
                    imageView.setImageURI(uri);
                    // display toast
                    Toast.makeText(getApplicationContext(), "Photo Saved", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}