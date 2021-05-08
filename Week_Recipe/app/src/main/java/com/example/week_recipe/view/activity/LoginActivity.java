package com.example.week_recipe.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.week_recipe.R;
import com.example.week_recipe.model.LoginModel;
import com.example.week_recipe.model.LoginModelManager;
import com.example.week_recipe.utility.MyPicture;
import com.example.week_recipe.utility.MyString;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private LoginModel loginModel;
    private static final int FirebaseUILogin = 11;
    private String email;
    private String password;
    private String userName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_login);
        checkAutoLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FirebaseUILogin)
        {
            if (resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                email = user.getEmail();
                password = ""+email.hashCode();
                userName = user.getDisplayName();
                clickLoginButton();
            }
            else if (resultCode == RESULT_CANCELED)
            {
                onBackPressed();
            }
            else
            {
                openLoginView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init()
    {
        MyPicture.setContext(getApplicationContext());
        MyString.setContext(getApplicationContext());
        loginModel = LoginModelManager.getLoginModelManager(getApplicationContext());
    }

    private void checkAutoLogin()
    {
        String email = MyString.readStringFromInternalStorage("AutoLogin");
        if (email!=null)
        {
            this.email = email;
            password = ""+email.hashCode();
            clickLoginButton();
        }
        else
        {
            openLoginView();
        }
    }

    public void openLoginView() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        @SuppressLint("ResourceType")
        Intent loginIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.Theme_Week_Recipe)
                .build();
        startActivityForResult(loginIntent, FirebaseUILogin);
    }

    private void clickLoginButton()
    {
        Context context = getApplicationContext();
        loginModel.addAccountInfo(email,password,userName);
        Intent intent = new Intent(context,HomePageActivity.class);
        startActivity(intent);
        intent.putExtra("email",email);
        finish();
    }
}
