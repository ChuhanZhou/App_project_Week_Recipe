package com.example.week_recipe.model.domain.user;

import java.util.ArrayList;

public class UserDataList {

    private ArrayList<UserData> userDataList;

    public UserDataList()
    {
        userDataList = new ArrayList<>();
    }

    public boolean hasUserData(String email)
    {
        return getByEmail(email)!=null;
    }

    public String add(UserData newUserData)
    {
        if (newUserData!=null)
        {
            if (!hasUserData(newUserData.getEmail()))
            {
                userDataList.add(newUserData);
                return null;
            }
            return "This email is used!";
        }
        return "Can't input null";
    }

    public UserData getByEmail(String email)
    {
        for(int i=0;i<userDataList.size();i++)
        {
            if (userDataList.get(i).getEmail().equals(email))
            {
                return userDataList.get(i);
            }
        }
        return null;
    }

    public void remove(UserData userData)
    {
        if (hasUserData(userData.getEmail()))
        {
            userDataList.remove(getByEmail(userData.getEmail()));
        }
    }
}
