package com.example.CameraDemo;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {
    public static final String DEBUG_TAG = "CameraActivity";
    private Camera camera;
    private Preview preview;
    private Button button;
    private ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(DEBUG_TAG, "onCreate'd");
        }
    };

    private PictureCallback rawCallBack = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.d(DEBUG_TAG, "onPictureTaken - raw");
        }
    };

    private PictureCallback jpegCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                outputStream.write(data);
                outputStream.close();

                Log.d(DEBUG_TAG, "onPictureTaken - jpeg wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            Log.d(DEBUG_TAG, "onPictureTaken - jpeg");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        preview = new Preview(this);
        ((FrameLayout) findViewById(R.id.preview)).addView(preview);

        button = (Button) findViewById(R.id.captureFront);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview.camera.takePicture(shutterCallback, rawCallBack, jpegCallback);
            }
        });
        Log.d(DEBUG_TAG, "On create");

    }

    @Override
    public void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
}