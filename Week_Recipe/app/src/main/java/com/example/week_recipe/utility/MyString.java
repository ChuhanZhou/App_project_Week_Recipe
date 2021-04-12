package com.example.week_recipe.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class MyString {
    private static Context context;

    public static void setContext(Context context) {
        MyString.context = context;
    }

    public static void saveStringToInternalStorage(String s, String fileId) {
        String fileName = fileId + ".txt";
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readStringFromInternalStorage(String fileId) {
        String fileName = fileId + ".txt";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] read = new byte[fis.available()];
            fis.read(read);
            return new String(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean haveOrInside(String searchWord, String basicWord)
    {
        char[] searchList = searchWord.toCharArray();
        char[] basicList = basicWord.toCharArray();
        for (int x=0;x<basicList.length;x++)
        {
            if (searchList[0]==basicList[x])
            {
                for (int i=0;i<searchList.length;i++)
                {
                    if (x+i>=basicList.length)
                    {
                        break;
                    }
                    if (searchList[i]!=basicList[x+i])
                    {
                        break;
                    }
                    else if (i==searchList.length-1)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNullOrEmptyOrFullOfSpace(String s)
    {
        if (s==null)
        {
            return true;
        }
        if (s.isEmpty())
        {
            return true;
        }
        char[] charsOfS = s.toCharArray();
        for (char charsOf : charsOfS) {
            if (charsOf != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(String s)
    {
        if (s==null)
        {
            return true;
        }
        return s.isEmpty();
    }
}
