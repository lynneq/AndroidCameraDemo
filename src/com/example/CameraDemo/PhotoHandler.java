package com.example.CameraDemo;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = getDir();
        if (!pictureFileDir.exists() && !pictureFileDir.mkdir()) {
            Log.d(CameraActivity.DEBUG_TAG, "Can't create directory to save image.");
            Toast.makeText(context, "Can't create directory to save image.", Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddmmss");
        createImageFileWithDateAsName(data, pictureFileDir, dateFormat);

    }

    private void createImageFileWithDateAsName(byte[] data, File pictureFileDir, SimpleDateFormat dateFormat) {
        String date = dateFormat.format(new Date());
        String photoName = "Image_" + date + ".jpg";
        String filename = pictureFileDir.getPath() + File.separator + photoName;

        File img = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(img);
            fos.write(data);
            fos.close();
            Toast.makeText(context, "New Image saved:" + photoName,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(CameraActivity.DEBUG_TAG, "File" + filename + "not saved: "
                    + error.getMessage());
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraRollDemo");
    }
}
