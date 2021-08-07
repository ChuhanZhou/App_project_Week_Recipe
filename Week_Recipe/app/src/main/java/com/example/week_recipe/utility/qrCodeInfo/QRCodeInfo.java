package com.example.week_recipe.utility.qrCodeInfo;

import com.google.gson.Gson;

import java.io.Serializable;

public class QRCodeInfo {
    QRCodeInfoType infoType;
    String info;
    static Gson gson = new Gson();

    public QRCodeInfo(Object info,QRCodeInfoType infoType)
    {
        this.info = gson.toJson(info);
        this.infoType = infoType;
    }

    public String getInfo() {
        return info;
    }

    public QRCodeInfoType getInfoType() {
        return infoType;
    }

    public String getJson()
    {
        return gson.toJson(this);
    }
}
