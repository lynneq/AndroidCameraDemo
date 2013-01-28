package com.example.CameraDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Preview extends SurfaceView implements SurfaceHolder.Callback  {

    private final SurfaceHolder mHolder;
    private static final String DEBUG_TAG = "Previews";
    public Camera camera;

    public Preview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera =Camera.open();
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                        outStream.write(data);
                        outStream.close();
                        Log.d(DEBUG_TAG, "On PreviewFrame - Wrote bytes; "+ data.length);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                    }
                    Preview.this.invalidate();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        Paint paint = new Paint(Color.RED);
        Log.d(DEBUG_TAG, "draw");
        canvas.drawText("PREVIEW", canvas.getWidth()/2, canvas.getHeight()/2, paint);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera = null;
    }
}
