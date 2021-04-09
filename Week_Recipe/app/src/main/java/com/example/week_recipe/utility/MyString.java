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
