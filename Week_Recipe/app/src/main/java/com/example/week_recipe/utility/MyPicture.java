package com.example.week_recipe.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.realtimeDBS.RealtimeDBController;
import com.example.week_recipe.model.SystemModelManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        byte[] output = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();
        return output;
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

    public static Bitmap bitmapCompressionOnSize(Bitmap bitmap,float maxMB)
    {
        Bitmap output = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        output.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        float bitmapSize = byteArrayOutputStream.size();
        //System.out.println(bitmapSize);

        while (bitmapSize/1024/1024>maxMB/0.999)
        {
            byteArrayOutputStream.reset();

            float scale = (float) Math.sqrt(maxMB/(bitmapSize/1024f/1024f));

            //System.out.println(maxMB/(bitmapSize/1024f/1024f));
            //System.out.println(scale);

            Matrix matrix = new Matrix();
            matrix.postScale(scale,scale);

            output = Bitmap.createBitmap(output, 0, 0, output.getWidth(), output.getHeight(), matrix, true);

            output.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            bitmapSize = byteArrayOutputStream.size();
            //System.out.println(bitmapSize);
        }

        byteArrayOutputStream.reset();
        return output;
    }

    public static Bitmap bitmapCompressionOnQuality(Bitmap bitmap,float maxMB)
    {
        Bitmap output = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        output.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        float bitmapSize = byteArrayOutputStream.size();
        //System.out.println(bitmapSize);

        while (bitmapSize/1024/1024>maxMB/0.999)
        {
            byteArrayOutputStream.reset();

            int quality = (int) (100*maxMB/(bitmapSize/1024f/1024f));
            //System.out.println(quality);
            output.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

            output = byteToBitmap(byteArrayOutputStream.toByteArray());

            bitmapSize = byteArrayOutputStream.size();
            //System.out.println(bitmapSize);
        }
        byteArrayOutputStream.reset();
        return output;
    }

    @SuppressLint("NewApi")
    public static Bitmap getGrayScaleBitmapByPixel(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);
        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                int r = (pixel[index] >> 16) & 0xff;
                int g = (pixel[index] >> 8) & 0xff;
                int b = pixel[index] & 0xff;
                int grayValue = (int) (0.3f * r + 0.59f * g + 0.11f * b);
                pixel[index] = Color.rgb(grayValue,grayValue,grayValue);
            }
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        output.setPixels(pixel,0,width,0,0,width,height);
        return output;
    }

    public static Bitmap getGrayScaleBitmapByPaint(Bitmap bitmap){
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

    public static Bitmap getBinarizationBitmapByPixel(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[][] gray = new int[width][height];
        long sumGray = 0;
        int aveGray = 0;
        int[] pixel = new int[area];

        bitmap.getPixels(pixel,0,width,0,0,width,height);
        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                int r = (pixel[index] >> 16) & 0xff;
                int g = (pixel[index] >> 8) & 0xff;
                int b = pixel[index] & 0xff;
                int grayValue = (int) (0.3f * r + 0.59f * g + 0.11f * b);

                gray[x][y] = grayValue;
                sumGray += grayValue;
            }
        }

        aveGray = (int) (sumGray/area);

        //System.out.println("ave:"+aveGray+"sum:"+sumGray+"area:"+area);

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                if (gray[x][y]>aveGray)
                {
                    pixel[index] = Color.rgb(255,255,255);
                }
                else
                {
                    pixel[index] = Color.rgb(0,0,0);
                }
            }
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        output.setPixels(pixel,0,width,0,0,width,height);
        return output;
    }

    public static Bitmap getBinarizationBitmapBaseOnOriginalBitmap(Bitmap bitmap)
    {
        bitmap = getGrayScaleBitmapByPaint(bitmap);//灰度化
        bitmap = bitmapCompressionOnSize(bitmap,1f);//压缩至1MB左右

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);

        int[] brightnessBackground = getBrightnessBackgroundBaseOnGray(pixel,width,height,5);//获得图片亮度背景
        pixel = brightnessAdjustmentBaseOnGray(pixel,width,height,brightnessBackground);//基于图片亮度背景调整亮度

        //二值化
        int[][] gray = new int[width][height];
        long sumGray = 0;
        int aveGray = 0;

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                int grayValue = pixel[index] & 0xff;

                gray[x][y] = grayValue;
                sumGray += grayValue;
            }
        }

        aveGray = (int) (sumGray/area);

        //System.out.println("ave:"+aveGray+"sum:"+sumGray+"area:"+area);

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                if (gray[x][y]>aveGray)
                {
                    pixel[index] = Color.rgb(255,255,255);
                }
                else
                {
                    pixel[index] = Color.rgb(0,0,0);
                }
            }
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        output.setPixels(pixel,0,width,0,0,width,height);
        return output;
    }

    public static Bitmap medianFiltering(Bitmap bitmap,int sampleRadius)
    {
        if (sampleRadius<=0)
        {
            sampleRadius = 1;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);

        pixel = medianFiltering(pixel,width,height,sampleRadius);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        output.setPixels(pixel,0,width,0,0,width,height);
        return output;
    }

    private static int[] medianFiltering(int[] pixel,int width,int height,int sampleRadius)
    {
        if (sampleRadius<=0)
        {
            sampleRadius = 1;
        }
        int[] output = new int[width*height];
        int sampleLength = sampleRadius*2+1;
        int[][] filteringArea = new int[3][sampleLength*sampleLength];

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                if (x>=sampleRadius&&x<width-sampleRadius&&y>=sampleRadius&&y<height-sampleRadius)
                {
                    int startIndex = x - sampleRadius + (y - sampleRadius) * width;
                    for (int sampleX=0;sampleX<sampleLength;sampleX++)
                    {
                        for (int sampleY=0;sampleY<sampleLength;sampleY++)
                        {
                            int sampleIndex = sampleX + sampleY * sampleLength;
                            int sampleIndexInPixel = startIndex + sampleX + sampleY * width;
                            int r = (pixel[sampleIndexInPixel] >> 16) & 0xff;
                            int g = (pixel[sampleIndexInPixel] >> 8) & 0xff;
                            int b = pixel[sampleIndexInPixel] & 0xff;
                            filteringArea[0][sampleIndex] = r;
                            filteringArea[1][sampleIndex] = g;
                            filteringArea[2][sampleIndex] = b;
                        }
                    }
                    Arrays.sort(filteringArea[0]);
                    Arrays.sort(filteringArea[1]);
                    Arrays.sort(filteringArea[2]);
                    int medianIndex = filteringArea[0].length/2+1;
                    output[index] = Color.rgb(filteringArea[0][medianIndex],filteringArea[1][medianIndex],filteringArea[2][medianIndex]);
                }
                else
                {
                    output[index] = pixel[index];
                }
            }
        }
        return output;
    }

    private static int[] medianFilteringBaseOnGray(int[] pixel,int width,int height,int sampleRadius)
    {
        if (sampleRadius<=0)
        {
            sampleRadius = 1;
        }
        int[] output = new int[width*height];
        int sampleLength = sampleRadius*2+1;
        int[] filteringArea = new int[sampleLength*sampleLength];

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                int index = x + y * width;
                if (x>=sampleRadius&&x<width-sampleRadius&&y>=sampleRadius&&y<height-sampleRadius)
                {
                    int startIndex = x - sampleRadius + (y - sampleRadius) * width;
                    for (int sampleX=0;sampleX<sampleLength;sampleX++)
                    {
                        for (int sampleY=0;sampleY<sampleLength;sampleY++)
                        {
                            int sampleIndex = sampleX + sampleY * sampleLength;
                            int sampleIndexInPixel = startIndex + sampleX + sampleY * width;
                            filteringArea[sampleIndex] = pixel[sampleIndexInPixel] & 0xff;
                        }
                    }
                    Arrays.sort(filteringArea);
                    int medianIndex = filteringArea.length/2+1;
                    output[index] = Color.rgb(filteringArea[medianIndex],filteringArea[medianIndex],filteringArea[medianIndex]);
                }
                else
                {
                    output[index] = pixel[index];
                }
            }
        }
        return output;
    }

    public static Bitmap getBrightnessBackground(Bitmap bitmap,int sampleRadius)
    {
        if (sampleRadius<0)
        {
            sampleRadius = 1;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);

        pixel = getBrightnessBackground(pixel,width,height,sampleRadius);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        output.setPixels(pixel,0,width,0,0,width,height);
        return output;
    }

    //base on http://www.javashuo.com/article/p-xxqdgcxk-nr.html
    private static int[] getBrightnessBackground(int[] pixel,int width,int height,int sampleRadius)
    {
        if (sampleRadius<0)
        {
            sampleRadius = 1;
        }
        int[] output = new int[width*height];
        int sampleLength = sampleRadius*2+1;
        int[][] filteringArea = new int[3][sampleLength*sampleLength];
        int index;
        int sampleIndex;
        int sampleIndexInPixel;
        int r;
        int g;
        int b;
        int grayValue;
        int[] brightness = new int[3];

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                index = x + y * width;
                for (int sampleX=0;sampleX<sampleLength;sampleX++)
                {
                    for (int sampleY=0;sampleY<sampleLength;sampleY++)
                    {
                        sampleIndex = sampleX + sampleY * sampleLength;
                        int sampleXInPixel = x - sampleRadius + sampleX;
                        int sampleYInPixel = y - sampleRadius + sampleY;
                        sampleIndexInPixel = sampleXInPixel + sampleYInPixel * width;
                        r = -1;
                        g = -1;
                        b = -1;
                        if (sampleXInPixel>=0&&sampleXInPixel<width&&sampleYInPixel>=0&&sampleYInPixel<height)
                        {
                            r = (pixel[sampleIndexInPixel] >> 16) & 0xff;
                            g = (pixel[sampleIndexInPixel] >> 8) & 0xff;
                            b = pixel[sampleIndexInPixel] & 0xff;
                        }
                        filteringArea[0][sampleIndex] = r;
                        filteringArea[1][sampleIndex] = g;
                        filteringArea[2][sampleIndex] = b;
                    }
                }

                for (int i=0;i<3;i++)
                {
                    Arrays.sort(filteringArea[i]);
                }

                for (int i=0;i<3;i++)
                {
                    brightness[i] = 0;
                    int i1;
                    for (i1=0;i1<6;i1++)
                    {
                        brightness[i] += filteringArea[i][filteringArea[i].length-2-i1];
                        if (filteringArea[i][filteringArea[i].length-2-i1-1]==-1)
                        {
                            break;
                        }
                    }
                    brightness[i] = brightness[i]/(i1+1);
                }

                output[index] = Color.rgb(brightness[0],brightness[1],brightness[2]);

            }
        }
        return output;
    }

    public static int[] getBrightnessBackgroundBaseOnGray(int[] pixel,int width,int height,int sampleRadius)
    {
        if (sampleRadius<0)
        {
            sampleRadius = 1;
        }
        int[] output = new int[width*height];
        int sampleLength = sampleRadius*2+1;
        int[] filteringArea = new int[sampleLength*sampleLength];
        int index;
        int sampleIndex;
        int sampleIndexInPixel;
        int grayValue;
        int brightness;

        for (int x=0;x<width;x++)
        {
            for (int y=0;y<height;y++)
            {
                index = x + y * width;
                for (int sampleX=0;sampleX<sampleLength;sampleX++)
                {
                    for (int sampleY=0;sampleY<sampleLength;sampleY++)
                    {
                        sampleIndex = sampleX + sampleY * sampleLength;
                        int sampleXInPixel = x - sampleRadius + sampleX;
                        int sampleYInPixel = y - sampleRadius + sampleY;
                        sampleIndexInPixel = sampleXInPixel + sampleYInPixel * width;
                        grayValue = -1;
                        if (sampleXInPixel>=0&&sampleXInPixel<width&&sampleYInPixel>=0&&sampleYInPixel<height)
                        {
                            grayValue = pixel[sampleIndexInPixel] & 0xff;
                        }
                        filteringArea[sampleIndex] = grayValue;
                    }
                }

                Arrays.sort(filteringArea);

                brightness = 0;
                int i;
                for (i=0;i<6;i++)
                {
                    brightness += filteringArea[filteringArea.length-2-i];
                    if (filteringArea[filteringArea.length-2-i-1]==-1)
                    {
                        break;
                    }
                }
                brightness = brightness/(i+1);

                output[index] = Color.rgb(brightness,brightness,brightness);
            }
        }
        return output;
    }

    public static int[] brightnessAdjustmentBaseOnGray(int[] pixel,int width,int height,int[] brightnessBackground)
    {
        int area = width * height;
        int[] output = new int[area];

        if (brightnessBackground==null||brightnessBackground.length!=pixel.length)
        {
            brightnessBackground = getBrightnessBackgroundBaseOnGray(pixel,width,height,1);
        }

        for (int x=0;x<area;x++)
        {
            int backgroundBrightness = brightnessBackground[x] & 0xff;
            int originalBrightness;
            int afterAdjustment;
            originalBrightness = pixel[x] & 0xff;

            if (backgroundBrightness<=originalBrightness)
            {
                afterAdjustment = 255;
            }
            else
            {
                float k;
                float b1 = 2.5f;
                float b2 = 1.0f;
                if (backgroundBrightness<20)
                {
                    k = b1;
                }
                else if (backgroundBrightness<=100)
                {
                    k = 1+(b1-1)*(100-backgroundBrightness)/80;
                }
                else if (backgroundBrightness<200)
                {
                    k = 1;
                }
                else
                {
                    k = 1+b2*(backgroundBrightness-220)/35;
                }
                afterAdjustment = (int) Math.max(255*0.75,255-k*(backgroundBrightness-originalBrightness));
            }

            output[x] = Color.rgb(afterAdjustment,afterAdjustment,afterAdjustment);
        }
        return output;
    }

    //base on http://www.javashuo.com/article/p-xxqdgcxk-nr.html
    public static Bitmap brightnessAdjustment(Bitmap bitmap,Bitmap background)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        int[] brightnessBackground = new int[area];
        int[] after = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);

        if (background==null||background.getWidth()!=width||background.getHeight()!=height)
        {
            brightnessBackground = getBrightnessBackground(pixel,width,height,1);
        }
        else
        {
            background.getPixels(brightnessBackground,0,width,0,0,width,height);
        }

        for (int x=0;x<area;x++)
        {
            int backgroundBrightness = brightnessBackground[x] & 0xff;
            int[] originalBrightness = new int[3];
            int[] afterAdjustment = new int[3];
            originalBrightness[0] = (pixel[x] >> 16) & 0xff; //r
            originalBrightness[1] = (pixel[x] >> 8) & 0xff; //g
            originalBrightness[2] = pixel[x] & 0xff; //b

            for (int i=0;i<3;i++)
            {
                if (backgroundBrightness<=originalBrightness[i])
                {
                    afterAdjustment[i] = 255;
                }
                else
                {
                    float k;
                    float b1 = 2.5f;
                    float b2 = 1.0f;
                    if (backgroundBrightness<20)
                    {
                        k = b1;
                    }
                    else if (backgroundBrightness<=100)
                    {
                        k = 1+(b1-1)*(100-backgroundBrightness)/80;
                    }
                    else if (backgroundBrightness<200)
                    {
                        k = 1;
                    }
                    else
                    {
                        k = 1+b2*(backgroundBrightness-220)/35;
                    }
                    afterAdjustment[i] = (int) Math.max(255*0.75,255-k*(backgroundBrightness-originalBrightness[i]));
                }
            }

            after[x] = Color.rgb(afterAdjustment[0],afterAdjustment[1],afterAdjustment[2]);
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        output.setPixels(after,0,width,0,0,width,height);
        return output;
    }
}
