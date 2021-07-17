package com.example.week_recipe.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.realtimeDBS.RealtimeDBController;
import com.example.week_recipe.model.SystemModelManager;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyPicture {
    private static final Map<String,Bitmap> bitmapCache = new HashMap<>();
    private static Context context;
    private static RealtimeDBController realtimeDBController;
    private static ExecutorService executorService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setContext(Context context) {
        MyPicture.context = context;
        realtimeDBController = RealtimeDBController.getController();
        executorService = Executors.newFixedThreadPool(2);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getDrawableById(int id) {
        return context.getResources().getDrawable(id);
    }

    public static Bitmap getBitmapById(int id) {
        return drawableToBitmap(getDrawableById(id));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static byte[] drawableToByte(Drawable drawable) {
        return bitmapToByte(drawableToBitmap(drawable));
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public static Drawable byteToDrawable(byte[] imageByte) {
        return bitmapToDrawable(byteToBitmap(imageByte));
    }

    public static Bitmap byteToBitmap(byte[] imageByte) {
        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void saveBitmapToInternalStorage(Bitmap bitmap, String imageId) {
        String fileName = SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode() + imageId + ".png";
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        byte[] bytes = bitmapToByte(bitmap);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        putBitmapToOnlineDatabase(imageId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Bitmap readBitmapFromInternalStorage(String imageId) {
        String fileName = SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode() + imageId + ".png";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] read = new byte[fis.available()];
            fis.read(read);
            return byteToBitmap(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean hasImage(String imageId)
    {
        String fileName = SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode() + imageId + ".png";
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        return bitmapCache.containsKey(imageId)||file.exists();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Bitmap getBitmapFromOnlineDatabase(String imageId)
    {
        return byteToBitmap(realtimeDBController.getImageById(imageId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putBitmapToOnlineDatabase(String imageId)
    {
        executorService.execute(() ->realtimeDBController.updateImageById(imageId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putBitmapToOnlineDatabase(String imageId,byte[] bytes)
    {
        executorService.execute(() ->realtimeDBController.updateImageById(imageId,bytes));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap getBitmapByImageId(String imageId)
    {
        if (!bitmapCache.containsKey(imageId))
        {
            bitmapCache.put(imageId,readBitmapFromInternalStorage(imageId));
        }
        return bitmapCache.get(imageId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putBitmapByImageId(String imageId, Bitmap bitmap)
    {
        if (!bitmap.equals(bitmapCache.get(imageId)))
        {
            bitmapCache.put(imageId,bitmap);
            new Thread(()->{saveBitmapToInternalStorage(bitmap, imageId);}).start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void loadAllBitmapToCache(ArrayList<String> imageIdList)
    {
        new Thread(()->{
            for (int x=0;x<imageIdList.size();x++)
            {
                if (hasImage(imageIdList.get(x)))
                {
                    getBitmapByImageId(imageIdList.get(x));
                    if (!realtimeDBController.hasImage(imageIdList.get(x)))
                    {
                        putBitmapToOnlineDatabase(imageIdList.get(x));
                    }
                }
                else if (realtimeDBController.hasImage(imageIdList.get(x)))
                {
                    bitmapCache.put(imageIdList.get(x),getBitmapFromOnlineDatabase(imageIdList.get(x)));
                    saveBitmapToInternalStorage(bitmapCache.get(imageIdList.get(x)),imageIdList.get(x));
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void clearUselessBitmapInInternalStorage(ArrayList<String> imageIdList)
    {
        new Thread(()->{
            String emailHashCode = ""+SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode();
            ArrayList<String> usefulFileList = new ArrayList<>();
            for (int x=0;x<imageIdList.size();x++)
            {
                usefulFileList.add(emailHashCode + imageIdList.get(x) + ".png");
            }

            String[] fileList = context.fileList();

            for (String fileName : fileList) {
                if (!usefulFileList.contains(fileName))
                {
                    String[] part = fileName.split("_");
                    if (part[0].equals(emailHashCode)&&part[1].equals("foodImage"))
                    {
                        context.deleteFile(fileName);
                    }
                }
            }
        }).start();
    }

    @SuppressLint("NewApi")
    public static Bitmap getGrayBitmapByPixel(Bitmap bitmap)
    {
        Bitmap output = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        int width = output.getWidth();
        int height = output.getHeight();
        int threadNum = Math.min(width,200);
        new Thread(()->{
            for (int x=0;x<threadNum;x++)
            {
                int threadId = x;
                new Thread(()->{
                    int dealWithX = threadId;
                    while (true)
                    {
                        for (int y=0;y<height;y++)
                        {
                            Color pointColor = output.getColor(dealWithX,y);
                            float grayValue = 0.3f * pointColor.red() + 0.59f * pointColor.green() + 0.11f * pointColor.blue();
                            int grayId = Color.argb(pointColor.alpha(),grayValue,grayValue,grayValue);
                            output.setPixel(dealWithX,y,grayId);
                        }
                        dealWithX+=threadNum;
                        if (dealWithX>=width)
                        {
                            break;
                        }
                    }
                }).start();
            }
        }).start();
        return output;
    }

    public static Bitmap getGrayBitmapByPaint(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output  = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(colorFilter);

        canvas.drawBitmap(bitmap, 0,0, paint);
        return output;
    }
}
