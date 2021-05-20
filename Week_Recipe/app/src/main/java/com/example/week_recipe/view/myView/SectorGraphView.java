package com.example.week_recipe.view.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

public class SectorGraphView extends View {
    private static int[] allColors;
    private ArrayList<SectorGraphItemInfo> itemInfoList;

    private Paint sectorGraphPaint;
    private Paint textPaint;
    private Bitmap bitmap;
    private Canvas canvas;
    private RectF rectF;
    private RectF insideRectF;
    private float startAngle = -90;
    private float endAngle = 270;

    private static int[] getAllColors()
    {
        if (allColors==null)
        {
            int size = 0xE3-0x0D;
            allColors = new int[size*6];
            int[] position = new int[]{256,65536,1,256,65536,1};
            for (int x=0;x<6;x++)
            {
                for (int i=0;i<size;i++)
                {
                    if (x==0&&i==0)
                    {
                        allColors[0] = 0xFFE30D0D;
                    }
                    else
                    {
                        if (x%2==0)
                        {
                            allColors[x*size+i] = allColors[x*size+i-1] + position[x];
                        }
                        else
                        {
                            allColors[x*size+i] = allColors[x*size+i-1] - position[x];
                        }
                    }
                }
            }
        }
        return allColors;
    }

    public SectorGraphView(Context context) {
        super(context);
        itemInfoList = new ArrayList<>();
    }

    public SectorGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        itemInfoList = new ArrayList<>();
    }

    public SectorGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemInfoList = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SectorGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        itemInfoList = new ArrayList<>();
    }

    public SectorGraphView setItemInfo(SectorGraphItemInfo... itemInfo) {
        itemInfoList = new ArrayList<>();
        Collections.addAll(itemInfoList,itemInfo.clone());
        return this;
    }

    public SectorGraphView addItemInfo(SectorGraphItemInfo... itemInfo) {
        Collections.addAll(itemInfoList, itemInfo.clone());
        return this;
    }

    private int[] getColors(int size)
    {
        int[] colors = new int[size];
        int[] basic;
        if (size<16)
        {
            basic = new int[]{
                    0xFFF44336,
                    0xFFFF5722,
                    0xFFFF9800,
                    0xFFFFC107,
                    0xFFFFEB3B,
                    0xFFCDDC39,
                    0xFF8BC34A,
                    0xFF4CAF50,
                    0xFF009688,
                    0xFF00BCD4,
                    0xFF03A9F4,
                    0xFF2196F3,
                    0xFF3F51B5,
                    0xFF673AB7,
                    0xFF9C27B0,
                    0xFFE91E63};
        }
        else
        {
            basic = getAllColors();
        }

        int space = basic.length/size;
        for (int x=0;x<size;x++)
        {
            colors[x] = basic[x*space];
        }

        return colors;
    }

    public void draw(int testSize,boolean needHollow,int widthOfShow) {
        post(() -> {
            //prepareForDraw
            //automatic adaptation
            int w = getWidth();
            int h = Math.min(w-testSize*8,getHeight());

            int yPadding = testSize*2;
            int r = (h-yPadding*2)/2;
            int xPadding = (w-r*2)/2;
            int xOfCenter = w/2;
            int yOfCenter = h/2;

            sectorGraphPaint = new Paint();
            sectorGraphPaint.setAntiAlias(true);

            textPaint = new Paint();
            textPaint.setTextSize(testSize);
            textPaint.setColor(0xFF000000);

            rectF = new RectF(xPadding, yPadding, w-xPadding, h-yPadding);
            if (needHollow)
            {
                insideRectF = new RectF(xPadding+widthOfShow, yPadding+widthOfShow, w-xPadding-widthOfShow, h-yPadding-widthOfShow);
            }

            if (itemInfoList.size()>0)
            {
                int[] colours = getColors(itemInfoList.size());
                itemInfoList.get(0).setStartAngle(startAngle);
                itemInfoList.get(0).setColor(colours[0]);
                for (int x = 1; x < itemInfoList.size(); x++) {
                    itemInfoList.get(x).setStartAngle(itemInfoList.get(x-1).getEndAngle());
                    itemInfoList.get(x).setColor(colours[x]);
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);

            //draw
            new Thread(() -> {
                int printIndex = -1;
                while (startAngle <= endAngle) {
                    float printAngle = 0.2f;
                    sectorGraphPaint.setColor(0xFF888888);

                    for (int x = 0; x < itemInfoList.size(); x++)
                    {
                        if (itemInfoList.get(x).shouldPrint(startAngle))
                        {
                            if (itemInfoList.get(x).getEndAngle()-startAngle>=0.2)
                            {
                                printAngle = 0.2f;
                            }
                            else
                            {
                                printAngle = 0.1f;
                            }
                            sectorGraphPaint.setColor(itemInfoList.get(x).getColor());

                            if (printIndex!=x)
                            {
                                try {
                                    Thread.sleep(1000/itemInfoList.size());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                printIndex = x;
                            }
                            break;
                        }
                    }

                    canvas.drawArc(rectF, startAngle, printAngle, true, sectorGraphPaint);
                    if (needHollow&&getBackground()!=null)
                    {
                        //draw hollow
                        sectorGraphPaint.setColor(((ColorDrawable)getBackground()).getColor());
                        canvas.drawArc(insideRectF, 0, 360,  true, sectorGraphPaint);
                    }
                    postInvalidate();
                    startAngle+=0.1;
                }

                for (int x=0;x<itemInfoList.size();x++)
                {
                    float xOfText = xOfCenter+itemInfoList.get(x).getXOfPrintTextPrint(r);
                    float yOfText = yOfCenter-itemInfoList.get(x).getYOfPrintTextPrint(r);
                    String printText = itemInfoList.get(x).getTitle()+":"+itemInfoList.get(x).getPercent()+"%";

                    //change the start point of text
                    if (xOfText-xOfCenter>=0&&yOfText-yOfCenter>=0)
                    {
                        yOfText+=testSize;
                    }
                    else if(xOfText-xOfCenter<0)
                    {
                        xOfText-=printText.toCharArray().length*testSize/2;
                        xOfText = Math.max(xOfText,0);
                        if (yOfText-yOfCenter>=0)
                        {
                            yOfText+=testSize;
                        }
                    }
                    canvas.drawText(printText,xOfText,yOfText,textPaint);
                    postInvalidate();
                }
            }).start();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sectorGraphPaint == null)
        {
            super.onDraw(canvas);
        }
        else
        {
            canvas.drawBitmap(bitmap, 0, 0, sectorGraphPaint);
        }
    }
}
