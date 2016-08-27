package com.example.wangkun.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by wangkun on 2016/8/16.
 */
public class ResultAty extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        String path=getIntent().getStringExtra("picPath");//获得临时存储
        ImageView imageView= (ImageView) findViewById(R.id.pic);
        try {
            FileInputStream fis=new FileInputStream(path);
            Bitmap bitmap= BitmapFactory.decodeStream(fis);
            Matrix matrix=new Matrix();
            matrix.setRotate(90);//进行角度的设置
            bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//对显示的图片进行调整
            //将图像设置个imageView
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Bitmap bitmap=BitmapFactory.decodeFile(path);
//        imageView.setImageBitmap(bitmap);//显示图片
    }
}
