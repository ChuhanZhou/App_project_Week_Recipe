package com.example.week_recipe.dao.converter;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.time.LocalDate;

public class LocalDateConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDate stringToLocalDate(String s)
    {
        if (s!=null)
        {
            String[] strings= s.split("-");
            return LocalDate.of(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String localDateToString(LocalDate localDate)
    {
        if (localDate!=null)
        {
            return localDate.getYear()+"-"+ String.format("%2d", localDate.getMonthValue()).replace(" ", "0") + "-" + String.format("%2d", localDate.getDayOfMonth()).replace(" ", "0");
        }
        return null;
    }
}
