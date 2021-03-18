package com.example.week_recipe.model;


import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;

public class LoginModelManager implements LoginModel {
    private static LoginModelManager loginModelManager;
    private AccountList accountList;

    private LoginModelManager()
    {
        accountList = new AccountList();
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
            SystemModelManager.getSystemModelManager().setAccount(""+email);
            return null;
        }
        return "Wrong account number or password!";
    }

    @Override
    public String register(String email, String password) {
        return accountList.addAccount(new Account(email, password));
    }
}
