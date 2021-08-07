package com.example.week_recipe.utility.qrCodeInfo;

import com.example.week_recipe.model.domain.food.Food;

public class QRCodeFoodInfo extends QRCodeInfo{

    public QRCodeFoodInfo(Food food)
    {
        super(food,QRCodeInfoType.FOOD);
    }

    public Food getFood()
    {
        return gson.fromJson(info,Food.class);
    }

    @Override
    public String getJson() {
        return gson.toJson(this);
    }
}
