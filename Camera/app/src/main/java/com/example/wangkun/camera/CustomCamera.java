package com.example.wangkun.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangkun on 2016/8/16.
 */
public class CustomCamera extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;

    private SurfaceView mPreView;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback=new Camera.PictureCallback() {
        //data存储整个照片的数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //在SDCard创建一个临时的文件
            File tempFile=new File("/sdcard/temp.png");
            //将整个data的数据写到文件
            try {
                FileOutputStream fos=new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                //拍照完成后跳到另一个Activity中
                Intent intent=new Intent(CustomCamera.this,ResultAty.class);
                intent.putExtra("picPath",tempFile.getAbsolutePath());
                startActivity(intent);
                CustomCamera.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);

        mPreView = (SurfaceView) findViewById(R.id.preview);
        mHolder = mPreView.getHolder();//获得SurfaceHolder对象
        mHolder.addCallback(this);
        //点击屏幕,自动对焦
        mPreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }

    /**
     * 拍摄的点击事件
     */
    public void capture(View v) {

        Camera.Parameters parameters=mCamera.getParameters();
        //设置拍照的格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //设置预览大小
        parameters.setPreviewSize(800,400);
        //设置对焦功能(自动对焦)
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //拍摄
        mCamera.autoFocus(new Camera.AutoFocusCallback() {//回调方法
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){//对焦准确
                    mCamera.takePicture(null,null,mPictureCallback);//拍摄一个画面
                }
            }
        });
    }

    /**
     * 加载Activity后绑定Camera
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            //预览相机的实时取景
            if (mHolder != null) {
                //将相机与sv进行绑定,同时预览相机的实时显示效果
                setStartPreView(mCamera, mHolder);
            }
        }
    }

    /**
     * 将Camera释放资源
     */
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

    }

    /**
     * 获取系统的一个Camera对象
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();//导入硬件中的Camera,获取Camera对象
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 在取景框中获取实时预览的图像
     */
    private void setStartPreView(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);//将holder对象传递进camera中,完成绑定
            camera.setDisplayOrientation(90);//预览角度转化为竖屏
            camera.startPreview();//开始在sv中获取camera中的图像

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机所占用的资源
     * 与Activity周期绑定
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);//将回调至空,取消sv与mCamera的关联操作
            mCamera.stopPreview();//停止取景效果
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreView(mCamera, mHolder);//开始经sv与camera进行绑定,开始相机的预览功能
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //重启整个功能
        mCamera.stopPreview();//关闭相机
        setStartPreView(mCamera, mHolder);//重启预览功能

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放所有资源
        releaseCamera();
    }
}
