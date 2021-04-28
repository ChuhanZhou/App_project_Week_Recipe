package com.example.week_recipe.dao.converter;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(String s) {
        if (s != null) {
            String[] date = s.split(" ")[0].split("-");
            String[] time = s.split(" ")[1].split(":");
            return LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.getYear() + "-" + String.format("%2d", localDateTime.getMonthValue()).replace(" ", "0") + "-" + String.format("%2d", localDateTime.getDayOfMonth()).replace(" ", "0") + " " + String.format("%2d", localDateTime.getHour()).replace(" ", "0") + ":" + String.format("%2d", localDateTime.getMinute()).replace(" ", "0") + ":" + String.format("%2d", localDateTime.getSecond()).replace(" ", "0");
        }
        return null;
    }
}
