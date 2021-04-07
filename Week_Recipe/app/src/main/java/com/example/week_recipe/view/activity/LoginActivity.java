package com.example.week_recipe.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.week_recipe.R;
import com.example.week_recipe.model.LoginModel;
import com.example.week_recipe.model.LoginModelManager;
import com.example.week_recipe.utility.MyPicture;


public class LoginActivity extends AppCompatActivity {

    private ImageView userPicture;
    private LoginModel loginModel;
    private EditText accountEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyPicture.setContext(getApplicationContext());
        init();
        setContentView(R.layout.activity_login);
        bind();
        setListener();

        loginModel.register("Email@gmail.com","0");
        accountEditText.setText("Email@gmail.com");
        passwordEditText.setText("0");
        clickLoginButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 1)
        {
            accountEditText.setText(data.getExtras().getString("email"));
            passwordEditText.setText(data.getExtras().getString("password"));
            clickLoginButton();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init()
    {
        loginModel = LoginModelManager.getLoginModelManager();
    }

    private void bind()
    {
        userPicture = findViewById(R.id.login_userPicture);
        accountEditText = findViewById(R.id.login_accountEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);
        loginButton = findViewById(R.id.login_loginButton);
        registerButton = findViewById(R.id.login_registerButton);
    }

    private void setListener()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoginButton();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegisterButton();
            }
        });
    }

    private void updateUserInfo(int id)
    {

    }

    private void clickRegisterButton()
    {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        //intent.putExtra("Intent",getIntent());
        //System.out.println(getIntent());
        startActivityForResult(intent,1);
    }

    private void clickLoginButton()
    {
        Context context = getApplicationContext();
        String result;
        if (!accountEditText.getText().toString().equals(""))
        {
            result = loginModel.login(accountEditText.getText().toString(),passwordEditText.getText().toString());
        }
        else
        {
            result = "Input the account number please.";
        }
        if (result==null)
        {
            Intent intent = new Intent(context,HomePageActivity.class);
            startActivity(intent);
            intent.putExtra("email",accountEditText.getText().toString());
            finish();
        }
        else
        {
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }
}
