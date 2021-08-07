package com.example.week_recipe.utility;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.Image;
import android.os.Build;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraInterface {
    private Camera camera;
    private Camera.Parameters parameters;
    private boolean isPreviewing = false;
    private boolean gettingPreview = false;
    private Camera.Size previewSize;
    private Camera.Size pictureSize;
    private static CameraInterface cameraInterface;
    private Camera.PreviewCallback previewCallback;
    private Camera.PictureCallback pictureCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera.AutoFocusCallback autoFocusCallback;
    private byte[] previewByte;
    private Bitmap preview;
    private Bitmap picture;
    private int numOfUse;

    public static CameraInterface getInstance(){
        if(cameraInterface == null){
            cameraInterface = new CameraInterface();
        }
        return cameraInterface;
    }

    private CameraInterface()
    {
        numOfUse = 0;
        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };
        previewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (data!=null&&!gettingPreview)
                {
                    camera.setPreviewCallback(null);
                    gettingPreview = true;
                    previewByte = data;
                    gettingPreview = false;
                    camera.setPreviewCallback(previewCallback);
                }
            }
        };
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if (data!=null)
                {
                    camera.stopPreview();
                    isPreviewing = false;
                    picture = byteFromCameraToBitmap(data,pictureSize.width,pictureSize.height);
                }
                camera.startPreview();
                isPreviewing = true;
            }
        };
        autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

            }
        };
    }

    public void openCamera(CameraOpenOverCallback callback) {
        numOfUse++;
        if (camera != null)
        {
            stopCamera();
        }
        camera = Camera.open();
        parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
        callback.cameraHasOpened();
    }

    public void stopCamera(){
        if (numOfUse>0)
        {
            numOfUse--;
        }
        if(camera != null&&numOfUse==0)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            isPreviewing = false;
            camera.release();
            camera = null;
            previewByte = null;
            preview = null;
            picture = null;
        }
    }

    public void startPreview(SurfaceHolder holder,boolean startAutoFocus)
    {
        if (isPreviewing)
        {
            camera.stopPreview();
            isPreviewing = false;
        }

        if(camera != null)
        {
            Camera.Size maxSize = parameters.getSupportedPreviewSizes().get(0);
            parameters.setPreviewSize(maxSize.width/2,maxSize.height/2);
            camera.setParameters(parameters);
            previewSize = parameters.getPreviewSize();
            pictureSize = parameters.getPictureSize();

            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            for (int x=0;x<sizes.size();x++)
            {
                System.out.println(x+":"+sizes.get(x).width+"x"+sizes.get(x).height);
            }

            try {
                camera.setPreviewDisplay(holder);
                camera.setPreviewCallback(previewCallback);
                camera.setDisplayOrientation(90);
                camera.startPreview();
                if (startAutoFocus)
                {
                    new Thread(()->{
                        while (camera!=null)
                        {
                            autoFocus();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                new Thread(()->{
                    while (camera!=null)
                    {
                        if (previewByte!=null)
                        {
                            byte[] data = previewByte;
                            previewByte = null;
                            preview = byteFromCameraToBitmap(data,previewSize.width,previewSize.height);
                        }
                        else
                        {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            isPreviewing = true;
        }
    }

    public void autoFocus()
    {
        if (camera!=null&&isPreviewing)
        {
            try {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                    }
                });
            }
            catch (RuntimeException e)
            {

            }
        }
    }

    public void takePicture(){
        if(isPreviewing && camera != null){
            picture = null;
            camera.takePicture(shutterCallback, null, pictureCallback);
        }
    }

    public Bitmap takePictureAndGetPhoto(){
        takePicture();
        while (picture==null)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public Bitmap getPreview() {
        return preview;
    }

    public Camera.Size getPreviewSize() {
        return previewSize;
    }

    public Camera.Size getPictureSize() {
        return pictureSize;
    }

    private Bitmap byteFromCameraToBitmap(byte[] data,int width,int height)
    {
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, outputStream);
        Bitmap output = MyPicture.byteToBitmap(outputStream.toByteArray());

        Matrix matrix = new Matrix();
        matrix.setRotate(90,(float) output.getWidth() / 2, (float) output.getHeight() / 2);
        return Bitmap.createBitmap(output, 0, 0, output.getWidth(), output.getHeight(), matrix, true);
    }

    public interface CameraOpenOverCallback{
        void cameraHasOpened();
    }
}
