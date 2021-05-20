package com.example.week_recipe.view.myView;

import java.text.DecimalFormat;

public class SectorGraphItemInfo {
    private String title;
    private int color;
    private float proportion;
    private float startAngle;
    private float endAngle;

    public SectorGraphItemInfo(String title,float proportion)
    {
        this.title = title;
        this.proportion = proportion;
    }

    public SectorGraphItemInfo(String title,int color,float proportion)
    {
        this.title = title;
        this.color = color;
        this.proportion = proportion;
    }

    public SectorGraphItemInfo(String title,int color,float proportion,float startAngle)
    {
        this.title = title;
        this.color = color;
        this.proportion = proportion;
        this.startAngle = startAngle;
        endAngle = startAngle+360*proportion;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }

    public float getProportion() {
        return proportion;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public float getEndAngle() {
        return endAngle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        endAngle = startAngle+360*proportion;
    }

    public boolean shouldPrint(float angle)
    {
        return startAngle<=angle && angle<getEndAngle();
    }

    public float getXOfPrintTextPrint(float r)
    {
        double halfOfAngle = Math.toRadians(startAngle+(endAngle-startAngle)/2+90);
        return (float) (Math.sin(halfOfAngle)*r);
    }

    public float getYOfPrintTextPrint(float r)
    {
        double halfOfAngle = Math.toRadians(startAngle+(endAngle-startAngle)/2+90);
        return (float) (Math.cos(halfOfAngle)*r);
    }

    public float getPercent()
    {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Float.parseFloat(decimalFormat.format(proportion*100));
    }
}
