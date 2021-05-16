package com.example.week_recipe.utility;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UiDataCache {
    private static final Map<String,Object> dataCache = new HashMap<>();
    //private static final Stack<Activity> activityStack = new Stack<>();

    public static Object getData(String key)
    {
        return dataCache.get(key);
    }

    public static String putData(String key,Object data)
    {
        dataCache.put(key, data);
        return key;
    }

    public boolean containsKey(String key) {
        return dataCache.containsKey(key);
    }

    //public static void pushActivity(Activity item)
    //{
    //    activityStack.push(item);
    //}
//
    //public static Activity popActivity()
    //{
    //    if (!activityStack.empty())
    //    {
    //        Activity activity = activityStack.pop();
    //        return activity;
    //    }
    //    return null;
    //}
}
