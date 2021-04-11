package com.example.week_recipe.dao;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.week_recipe.dao.converter.FoodTypeConverter;
import com.example.week_recipe.dao.converter.IngredientsListConverter;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.user.Account;
import com.example.week_recipe.model.domain.user.AccountList;
import com.example.week_recipe.model.domain.user.UserData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@RequiresApi(api = Build.VERSION_CODES.O)
public class Repository implements PropertyChangeListener {
    private static Repository instance;

    private SystemModel systemModel;

    private AccountDao accountDao;
    private UserDataDao userDataDao;
    //private FoodDao foodDao;
    //private FoodListDao foodListDao;
    //private FoodListFoodJoinDao foodListFoodJoinDao;

    private ExecutorService executorService;

    private Repository(Context context)
    {
        SystemDatabase systemDatabase = SystemDatabase.getInstance(context);

        accountDao = systemDatabase.accountDao();
        userDataDao = systemDatabase.userDataDao();
        //foodDao = systemDatabase.foodDao();
        //foodListDao = systemDatabase.foodListDao();
        //foodListFoodJoinDao = systemDatabase.foodListFoodJoinDao();

        executorService = Executors.newFixedThreadPool(2);

        systemModel = SystemModelManager.getSystemModelManager();
        systemModel.addListener("updateDailyRecipeList",this);
        systemModel.addListener("updateFavoriteFoodList",this);
        systemModel.addListener("updateFood",this);
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
        return userDataDao.getUserDataByEmail(email);
    }

    public void updateUserData(UserData userData)
    {
        executorService.execute(() -> userDataDao.update(userData));
    }

    public void deleteUserData(UserData userData)
    {
        executorService.execute(() -> userDataDao.delete(userData));
    }

    //public void insertFood(Food food) {
    //    executorService.execute(() -> foodDao.insert(food));
    //}
//
    //public void updateFood(Food oldFood,Food newFood) {
    //    executorService.execute(() -> foodDao.update(oldFood.getId(),newFood.getId(),newFood.getUserEmail(),newFood.getName(), FoodTypeConverter.FoodTypeToString(newFood.getType()), IngredientsListConverter.ingredientsListToString(newFood.getIngredientsList()),newFood.getImageId()));
    //}
//
    //public List<Food> getFoodByEmail(String email) {
    //    return foodDao.getFoodByEmail(email);
    //}
//
    //public void clearUselessFood(FoodList usefulFood,String userEmail)
    //{
    //    executorService.execute(() ->{
    //        List<Food> foodList = foodDao.getFoodByEmail(userEmail);
    //        for (int x=0;x<foodList.size();x++)
    //        {
    //            if (!usefulFood.hasFood(foodList.get(x)))
    //            {
    //                foodDao.delete(foodList.get(x));
    //            }
    //        }
    //    });
    //}
//
    //public void insertFoodList(FoodList foodList) {
    //    executorService.execute(() -> {
    //        foodListDao.insert(foodList);
//
    //    });
//
    //}
//
    //public List<FoodList> getAllFoodList()
    //{
    //    List<FoodList> listOfFoodList = foodListDao.getAllFoodList();
    //    for (int x=0;x<listOfFoodList.size();x++)
    //    {
    //        putFoodToFoodList(listOfFoodList.get(x));
    //    }
    //    return listOfFoodList;
    //}
//
    //private void putFoodToFoodList(FoodList foodList)
    //{
    //    List<Food> list = foodListFoodJoinDao.getFoodByFoodListId(foodList.getId());
    //    for (int x=0;x<foodList.getSize();x++)
    //    {
    //        foodList.add(list.get(x));
    //    }
    //}
//
    //public FoodList getFoodListByFoodListId(int foodListId)
    //{
    //    FoodList foodList = new FoodList(foodListId);
    //    putFoodToFoodList(foodList);
    //    return foodList;
    //}
//
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateUserData(systemModel.getUserData());
    }
}
