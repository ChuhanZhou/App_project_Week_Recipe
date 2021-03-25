package com.example.week_recipe.model;


import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserDataList;

public class LoginModelManager implements LoginModel {
    private static LoginModelManager loginModelManager;
    private AccountList accountList;
    private UserDataList userDataList;

    private LoginModelManager()
    {
        accountList = new AccountList();
        userDataList = new UserDataList();
    }

    public static LoginModelManager getLoginModelManager() {
        if (loginModelManager==null)
        {
            loginModelManager = new LoginModelManager();
        }
        return loginModelManager;
    }

    @Override
    public String login(String email, String password) {
        if (accountList.checkPassword(email, password))
        {
            SystemModelManager.getSystemModelManager().setUserData(userDataList.getByEmail(email));
            return null;
        }
        return "Wrong account number or password!";
    }

    @Override
    public String register(String email, String password) {
        Account newAccount = new Account(email, password);
        String result =  accountList.addAccount(newAccount);
        if (result==null)
        {
            result = userDataList.add(new UserData(email,"User" + newAccount.hashCode()));
        }
        return result;
    }
}
