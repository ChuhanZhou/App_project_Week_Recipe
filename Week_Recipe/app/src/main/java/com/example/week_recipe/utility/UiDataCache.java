package com.example.week_recipe.utility;

import com.example.week_recipe.model.domain.food.Food;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UiDataCache {
    private static final Map<String,Object> dataCache = new HashMap<>();

    public static Object getData(String key)
    {
        return dataCache.get(key);
    }

    public static String putData(String key,Object data)
    {
        Gson gson = new Gson();
        System.out.println(gson.toJson(data));
        dataCache.put(key, gson.fromJson(gson.toJson(data), Food.class));
        return key;
    }
}
