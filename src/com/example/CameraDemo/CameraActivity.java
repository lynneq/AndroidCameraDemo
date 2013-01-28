package com.example.CameraDemo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static android.hardware.Camera.getNumberOfCameras;

public class CameraActivity extends Activity
{
    public static final String DEBUG_TAG = "CameraActivity";
    private ImageView imageView;
    private Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        takePitureWithFrontCamera();
    }

    public void takePitureWithFrontCamera() {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(this, "There is no camera on this device", Toast.LENGTH_LONG).show();
        }else {
            int cameraId = findFrontFacingCamera();
            if(cameraId<0){
                Toast.makeText(this, "No Front Facing Camera found", Toast.LENGTH_LONG).show();
            }else {
                camera = Camera.open(cameraId);
            }
        }
    }

    public void onClick(View view){//???
        camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
    }

    private int findFrontFacingCamera() {
        for(int i =0; i< getNumberOfCameras(); i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ){
                Log.d(DEBUG_TAG, "Camera found");
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onPause(){
        if(camera != null){
            camera.release();
            camera =null;
        }
        super.onPause();
    }
}
