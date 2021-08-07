package com.example.week_recipe.view.myView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.week_recipe.R;
import com.example.week_recipe.utility.CameraInterface;
import com.example.week_recipe.utility.MyPicture;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, CameraInterface.CameraOpenOverCallback {

    private Context context;
    private SurfaceHolder surfaceHolder;
    private boolean isOpen;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        surfaceHolder = holder;
        if (isOpen)
        {
            CameraInterface.getInstance().startPreview(surfaceHolder,true);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        CameraInterface.getInstance().stopCamera();
    }

    private void init() {
        context = getContext();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public void openCamera()
    {
        CameraInterface.getInstance().openCamera(this);
    }

    public void stopCamera(){
        CameraInterface.getInstance().stopCamera();
        isOpen = false;
    }

    public boolean checkCamera()
    {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    public Bitmap getPreview() {
        return CameraInterface.getInstance().getPreview();
    }

    @Override
    public void cameraHasOpened() {
        isOpen = true;
        CameraInterface.getInstance().startPreview(surfaceHolder,true);

        Camera.Size size = CameraInterface.getInstance().getPreviewSize();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        float previewRatio = (float) size.width / size.height;
        layoutParams.width = getWidth();
        layoutParams.height = (int) (getWidth()*previewRatio);
        setLayoutParams(layoutParams);
    }
}
