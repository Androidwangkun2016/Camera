package com.example.wangkun.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {
    private static int REQ_1 = 1;
    private static int REQ_2 = 2;
    private ImageView mImageView;
    private String mFilePath;//记录文件的位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv);
        mFilePath = Environment.getExternalStorageDirectory().getPath();//获得SDCard路径
        mFilePath = mFilePath + "/" + "temp.png";//路劲加文件名
    }
    /**
     * 调用系统相机
     */
    public void startCamera1(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_1);
    }

    /**
     * 获得原图
     */
    public void startCamera2(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri=Uri.fromFile(new File(mFilePath));//将File路劲指定进来
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);//指定对应的文件,将系统存储的路劲更改为我们的指定的路劲
        startActivityForResult(intent, REQ_2);
    }

    public void customCamera(View v){
        startActivity(new Intent(this,CustomCamera.class));
    }
    /**
     * 接受从另一个Activity中返回的数据
     * data:返回的是一张缩略图
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_1) {//判断是上一个intent中返回的数据
                //获取数据
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");//取出bundle数据
                mImageView.setImageBitmap(bitmap);//获得的数据显示在ImageView中
            }else if(requestCode==REQ_2){
                FileInputStream fis = null;
                try {
                    fis=new FileInputStream(mFilePath);
                    Bitmap bitmap= BitmapFactory.decodeStream(fis);//解析为bitmao
                    mImageView.setImageBitmap(bitmap);//获得的数据显示在ImageView中
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
