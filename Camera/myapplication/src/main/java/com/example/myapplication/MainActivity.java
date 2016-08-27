package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**启动相机*/
    public void testCamera(View v){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//隐式Intent
        startActivity(intent);
    }
}
