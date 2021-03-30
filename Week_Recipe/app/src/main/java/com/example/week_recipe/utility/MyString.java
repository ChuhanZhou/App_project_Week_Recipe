package com.example.week_recipe.utility;

public class MyString {

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
}
