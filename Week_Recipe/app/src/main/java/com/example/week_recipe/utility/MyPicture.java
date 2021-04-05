package com.example.week_recipe.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.util.Base64;
import android.widget.ImageView;

import com.example.week_recipe.model.domain.recipe.DailyRecipe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MyPicture {

    private static Context context;

    public static void setContext(Context context)
    {
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

    public static byte[] drawableToByte(Drawable drawable)
    {
        return bitmapToByte(drawableToBitmap(drawable));
    }

    public static byte[] bitmapToByte(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Drawable byteToDrawable(byte[] imageByte)
    {
        return bitmapToDrawable(byteToBitmap(imageByte));
    }

    public static Bitmap byteToBitmap(byte[] imageByte)
    {
        return BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
    }

}
