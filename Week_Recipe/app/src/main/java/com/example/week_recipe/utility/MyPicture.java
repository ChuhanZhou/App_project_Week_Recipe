package com.example.week_recipe.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.week_recipe.model.SystemModelManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPicture {
    private static final Map<String,Bitmap> bitmapCache = new HashMap<>();
    private static Context context;

    public static void setContext(Context context) {
        MyPicture.context = context;
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

    private static void saveBitmapToInternalStorage(Bitmap bitmap, String imageId) {
        String fileName = SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode() + imageId + ".png";
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapToByte(bitmap));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static boolean hasImage(String imageId)
    {
        String fileName = SystemModelManager.getSystemModelManager().getUserData().getEmail().hashCode() + imageId + ".png";
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        return bitmapCache.containsKey(imageId)||file.exists();
    }

    public static Bitmap getBitmapByImageId(String imageId)
    {
        if (!bitmapCache.containsKey(imageId))
        {
            bitmapCache.put(imageId,readBitmapFromInternalStorage(imageId));
        }
        return bitmapCache.get(imageId);
    }

    public static void putBitmapByImageId(String imageId,Bitmap bitmap)
    {
        bitmapCache.put(imageId,bitmap);
        new Thread(()->{saveBitmapToInternalStorage(bitmap, imageId);}).start();
    }

    public static void clearUselessBitmapInInternalStorage(ArrayList<String> imageIdList)
    {
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
                String[] part = fileName.split("\\.");
                if (part[0].split("_")[0].equals(emailHashCode)&&part[part.length - 1].equals("png"))
                {
                    context.deleteFile(fileName);
                }
            }
        }
    }
}
