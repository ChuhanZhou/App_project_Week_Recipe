package com.example.week_recipe.model.domain.user;

import java.util.ArrayList;

public class AccountList {
    private ArrayList<Account> accounts;

    public AccountList()
    {
        accounts = new ArrayList<>();
    }

    public String addAccount(Account newAccount)
    {
        if (!hasAccount(newAccount.getEmail()))
        {
            accounts.add(newAccount);
            return null;
        }
        return "This account number is used!";
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
        for(int i=0;i<accounts.size();i++)
        {
            if (accounts.get(i).getEmail().equals(email))
            {
                return accounts.get(i);
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
                accounts.remove(getAccountByEmail(email));
            }
        }
    }
}
