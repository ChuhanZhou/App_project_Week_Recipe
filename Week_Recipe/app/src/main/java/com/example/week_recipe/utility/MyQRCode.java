package com.example.week_recipe.utility;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Hashtable;

public class MyQRCode {
    private static final String CHARSET = "utf-8";

    public static Bitmap createQRCode(String info,int w,int h)
    {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(info, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];

            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[x + y * w] = 0xff000000;
                    }
                    else
                    {
                        pixels[x + y * w] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeQRCode(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int[] pixel = new int[area];
        bitmap.getPixels(pixel,0,width,0,0,width,height);

        LuminanceSource source = new RGBLuminanceSource(width,height,pixel);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = null;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        try {
            result = new MultiFormatReader().decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        if (result!=null)
        {
            return result.getText();
        }
        else
        {
            return null;
        }
    }
}
