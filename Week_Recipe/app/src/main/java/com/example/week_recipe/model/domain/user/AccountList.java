package com.example.week_recipe.model.domain.user;

import java.util.ArrayList;

public class AccountList {
    private ArrayList<Account> accountList;

    public AccountList()
    {
        accountList = new ArrayList<>();
    }

    public String addAccount(Account newAccount)
    {
        if (newAccount!=null)
        {
            if (!hasAccount(newAccount.getEmail()))
            {
                accountList.add(newAccount);
                return null;
            }
            return "This email is used!";
        }
        return "Can't input null";
    }

    public boolean checkPassword(String email,String password)
    {
        if (hasAccount(email))
        {
            return getAccountByEmail(email).checkPassword(password);
        }
        return false;
    }

    public boolean hasAccount(String email)
    {
        return getAccountByEmail(email)!=null;
    }

    private Account getAccountByEmail(String email)
    {
        for(int i = 0; i< accountList.size(); i++)
        {
            if (accountList.get(i).getEmail().equals(email))
            {
                return accountList.get(i);
            }
        }
        return null;
    }

    public String changePassword(String email,String oldPassword,String newPassword)
    {
        if (hasAccount(email))
        {
            return getAccountByEmail(email).changePassword(oldPassword,newPassword);
        }
        return "Can't find the account!";
    }

    public void removeAccount(String email,String password)
    {
        if (hasAccount(email))
        {
            if (getAccountByEmail(email).checkPassword(password))
            {
                accountList.remove(getAccountByEmail(email));
            }
        }
    }
}
