package com.example.week_recipe.model;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.Repository;
import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserDataList;
import com.google.gson.Gson;
@RequiresApi(api = Build.VERSION_CODES.O)
public class LoginModelManager implements LoginModel {
    private static LoginModelManager loginModelManager;
    private AccountList accountList;
    //private UserDataList userDataList;
    private Repository repository;

    private LoginModelManager(Context context)
    {
        repository = Repository.getInstance(context);
        accountList = new AccountList();
        new Thread(()->{
            accountList = repository.getAllAccount();
            System.out.println(new Gson().toJson(accountList));
        }).start();
        //userDataList = new UserDataList();
    }

    public static LoginModelManager getLoginModelManager(Context context) {
        if (loginModelManager==null)
        {
            loginModelManager = new LoginModelManager(context);
        }
        return loginModelManager;
    }

    @Override
    public String login(String email, String password) {
        if (accountList.checkPassword(email, password))
        {
            //SystemModelManager.getSystemModelManager().setUserData(userDataList.getByEmail(email));
            new Thread(()->{

                SystemModelManager.getSystemModelManager().setUserData(repository.getUserDataByEmail(email));
                System.out.println(new Gson().toJson(repository.getUserDataByEmail(email)));
            }).start();
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
            repository.insertAccount(newAccount);
            repository.insertUserData(new UserData(email,"User" + newAccount.hashCode()));
            //result = userDataList.add(new UserData(email,"User" + newAccount.hashCode()));
        }
        return result;
    }
}
