package com.example.week_recipe.dao.realtimeDBS;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.loader.content.AsyncTaskLoader;

import com.example.week_recipe.dao.converter.LocalDateTimeConverter;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;
import com.example.week_recipe.model.domain.recipe.RecipeList;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.model.domain.user.UserSetting;
import com.example.week_recipe.utility.MyPicture;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RealtimeDBController {
    private static RealtimeDBController controller;
    private FirebaseDatabase database;
    private DatabaseReference userDataRef;
    private DatabaseReference imageDataRef;
    private Gson gson;
    private boolean databaseOnline;
    private boolean updateImage;
    private boolean useOnlineDatabase;

    public static RealtimeDBController getController()
    {
        if (controller==null)
        {
            controller=new RealtimeDBController();
        }
        return controller;
    }

    public void setUseOnlineDatabase(boolean useOnlineDatabase) {
        this.useOnlineDatabase = useOnlineDatabase;
    }

    private RealtimeDBController()
    {
        databaseOnline = true;
        updateImage = false;
        useOnlineDatabase = true;
        database = FirebaseDatabase.getInstance("https://week-recipe-default-rtdb.europe-west1.firebasedatabase.app/");
        //database.setPersistenceEnabled(true);
        userDataRef = database.getReference("/userData");
        imageDataRef = database.getReference("/imageList");

        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseOnline = false;
            }
        });

        gson = new Gson();
    }

    public UserData getUserData(String email)
    {
        if (databaseOnline&&useOnlineDatabase)
        {
            Task<DataSnapshot> getDataTask = userDataRef.child(String.valueOf(email.hashCode())).get();
            if (!getDataTask.isCanceled())
            {
                while (true)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getDataTask.isComplete())
                    {
                        DataSnapshot data = getDataTask.getResult();
                        email = data.child("email").getValue(String.class);
                        String userName = data.child("userName").getValue(String.class);
                        String userImageId = data.child("userImageId").getValue(String.class);
                        RecipeList myDailyRecipeList = gson.fromJson(data.child("myDailyRecipeList").getValue(String.class),RecipeList.class);
                        FoodList favoriteFoodList = gson.fromJson(data.child("favoriteFoodList").getValue(String.class),FoodList.class);
                        FavouriteWeekRecipeList favoriteWeekRecipeList = gson.fromJson(data.child("favoriteWeekRecipeList").getValue(String.class),FavouriteWeekRecipeList.class);
                        String updateTime = data.child("updateTime").getValue(String.class);
                        UserSetting setting = gson.fromJson(data.child("setting").getValue(String.class),UserSetting.class);
                        UserData userData = new UserData(userImageId,email,userName,myDailyRecipeList,favoriteWeekRecipeList,favoriteFoodList,updateTime,setting);
                        return userData;
                    }
                }
            }
        }
        return null;
    }

    public LocalDateTime getUpdateTime(String email)
    {
        if (databaseOnline&&useOnlineDatabase)
        {
            Task<DataSnapshot> getDataTask = userDataRef.child(String.valueOf(email.hashCode())).child("updateTime").get();
            if (!getDataTask.isCanceled())
            {
                while (true)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getDataTask.isComplete())
                    {
                        String updateTime = getDataTask.getResult().getValue(String.class);
                        System.out.println(updateTime);
                        return LocalDateTimeConverter.stringToLocalDateTime(updateTime);
                    }
                }
            }
        }
        return null;
    }

    public void updateUserData(UserData userData)
    {
        if (databaseOnline&&useOnlineDatabase)
        {
            DatabaseReference userRef = userDataRef.child(String.valueOf(userData.getEmail().hashCode()));
            userRef.child("email").setValue(userData.getEmail());
            userRef.child("userName").setValue(userData.getUserName());
            userRef.child("userImageId").setValue(userData.getUserImageId());
            userRef.child("myDailyRecipeList").setValue(gson.toJson(userData.getMyDailyRecipeList()));
            userRef.child("favoriteFoodList").setValue(gson.toJson(userData.getFavoriteFoodList()));
            userRef.child("favoriteWeekRecipeList").setValue(gson.toJson(userData.getFavoriteWeekRecipeList()));
            userRef.child("updateTime").setValue(userData.getUpdateTime());
            userRef.child("setting").setValue(gson.toJson(userData.getSetting()));
        }
    }

    public byte[] getImageById(String id)
    {
        if (databaseOnline&&updateImage&&useOnlineDatabase)
        {
            Task<DataSnapshot> getDataTask = imageDataRef.child(String.valueOf(id)).get();
            if (!getDataTask.isCanceled())
            {
                while (true)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getDataTask.isComplete())
                    {
                        byte[] bytes = gson.fromJson(getDataTask.getResult().getValue(String.class),byte[].class);
                        return bytes;
                    }
                }
            }
        }
        return null;
    }

    public boolean hasImage(String id)
    {
        if (databaseOnline&&updateImage&&useOnlineDatabase)
        {
            Task<DataSnapshot> getDataTask = imageDataRef.get();
            if (!getDataTask.isCanceled())
            {
                while (true)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getDataTask.isComplete())
                    {
                        return getDataTask.getResult().hasChild(id);
                    }
                }
            }
        }
        return false;
    }

    public void updateImageById(String id)
    {
        if (MyPicture.hasImage(id)&&databaseOnline&&updateImage&&useOnlineDatabase)
        {
            byte[] bytes = MyPicture.bitmapToByte(MyPicture.getBitmapByImageId(id));
            imageDataRef.child(id).setValue(gson.toJson(bytes));
        }
    }

    public void updateImageById(String id,byte[] bytes)
    {
        if (databaseOnline&&updateImage&&useOnlineDatabase)
        {
            imageDataRef.child(id).setValue(gson.toJson(bytes));
        }
    }
}
