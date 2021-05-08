package com.example.week_recipe.model;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.Repository;
import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.utility.MyString;
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
            new Thread(()->{
                SystemModelManager.getSystemModelManager().setUserData(repository.getUserDataByEmail(email));
                MyString.saveStringToInternalStorage(email,"AutoLogin");
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

    @Override
    public void addAccountInfo(String email,String password,String userName) {
        Account account = new Account(email, password);
        if (!accountList.hasAccount(email))
        {
            accountList.addAccount(account);
            repository.insertAccount(account);
            repository.insertUserData(new UserData(email,userName));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (accountList.checkPassword(email, password))
        {
            new Thread(()->{
                SystemModelManager.getSystemModelManager().setUserData(repository.getUserDataByEmail(email));
            }).start();
            MyString.saveStringToInternalStorage(email,"AutoLogin");
        }
    }
}
