package com.example.week_recipe.dao;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.realtimeDBS.RealtimeDBController;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserSetting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@RequiresApi(api = Build.VERSION_CODES.O)
public class Repository implements PropertyChangeListener {
    private static Repository instance;

    private SystemModel systemModel;

    private AccountDao accountDao;
    private UserDataDao userDataDao;

    private ExecutorService executorService;

    private RealtimeDBController realtimeDatabaseController;

    private Repository(Context context)
    {
        SystemDatabase systemDatabase = SystemDatabase.getInstance(context);

        accountDao = systemDatabase.accountDao();
        userDataDao = systemDatabase.userDataDao();
        //foodDao = systemDatabase.foodDao();
        //foodListDao = systemDatabase.foodListDao();
        //foodListFoodJoinDao = systemDatabase.foodListFoodJoinDao();
        executorService = Executors.newFixedThreadPool(2);

        realtimeDatabaseController = RealtimeDBController.getController();

        systemModel = SystemModelManager.getSystemModelManager();
        systemModel.addListener("updateUserBasicInformation",this);
        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
        systemModel.addListener("updateFavoriteWeekRecipe",this);
        systemModel.addListener("updateUserSetting",this);
    }

    public static synchronized Repository getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new Repository(context);
        }
        return instance;
    }

    public void insertAccount(Account account)
    {
        executorService.execute(() ->  accountDao.insert(account));
    }

    public AccountList getAllAccount()
    {
        AccountList accountList = new AccountList();
        List<Account> list =  accountDao.getAllAccount();
        for (int x=0;x<list.size();x++)
        {
            accountList.addAccount(list.get(x));
        }
        return accountList;
    }

    public void updateAccount(Account account)
    {
        executorService.execute(() -> accountDao.update(account));
    }

    public void deleteAccount(Account account)
    {
        executorService.execute(() -> accountDao.delete(account));
    }

    public void insertUserData(UserData userData)
    {
        executorService.execute(() ->  userDataDao.insert(userData));
    }

    public UserData getUserDataByEmail(String email)
    {
        UserData localData = userDataDao.getUserDataByEmail(email);
        if (localData.getSetting().isUseOnlineDatabase())
        {
            LocalDateTime onlineUpdateTime = realtimeDatabaseController.getUpdateTime(email);
            if (onlineUpdateTime!=null&&onlineUpdateTime.isAfter(localData.getUpdateTimeInLocalDateTime()))
            {
                return realtimeDatabaseController.getUserData(email);
            }
            else
            {
                realtimeDatabaseController.updateUserData(localData);
                return localData;
            }
        }
        else
        {
            realtimeDatabaseController.setUseOnlineDatabase(false);
            return localData;
        }
    }

    public void updateUserData(UserData userData)
    {
        executorService.execute(() -> userDataDao.update(userData));
        realtimeDatabaseController.updateUserData(userData);
    }

    public void deleteUserData(UserData userData)
    {
        executorService.execute(() -> userDataDao.delete(userData));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("updateUserSetting"))
        {
            realtimeDatabaseController.setUseOnlineDatabase(((UserSetting)evt.getNewValue()).isUseOnlineDatabase());
        }
        updateUserData(systemModel.getUserData());
    }
}
