package com.example.week_recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.week_recipe.model.LoginModel;
import com.example.week_recipe.model.LoginModelManager;

public class RegisterActivity extends AppCompatActivity {
    private LoginModel loginModel;
    private EditText accountEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button registerButton;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_register);
        bind();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init()
    {
        loginModel = LoginModelManager.getLoginModelManager();
    }

    private void bind()
    {
        accountEditText = findViewById(R.id.register_EditText_accountNumber);
        passwordEditText = findViewById(R.id.register_EditText_password);
        confirmEditText = findViewById(R.id.register_EditText_ConfirmPassword);
        registerButton = findViewById(R.id.register_registerButton);
        backButton = findViewById(R.id.register_backButton);
    }

    private void setListener()
    {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegisterButton();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackButton();
            }
        });
    }

    private void clickRegisterButton()
    {
        Context context = getApplicationContext();
        String result;
        if (!accountEditText.getText().toString().equals(""))
        {
            String email = accountEditText.getText().toString();
            if (!passwordEditText.getText().toString().equals(""))
            {
                String password = passwordEditText.getText().toString();
                if (password.equals(confirmEditText.getText().toString()))
                {
                    result = loginModel.register(email,password);
                }
                else
                {
                    result = "Input the same password in Confirm please.";
                }
            }
            else
            {
                result = "Input a password please.";
            }
        }
        else
        {
            result = "Input an account number please.";
        }
        if (result==null)
        {
            Intent intent = new Intent();
            intent.putExtra("account",accountEditText.getText().toString());
            intent.putExtra("password",passwordEditText.getText().toString());
            setResult(1,intent);
            finish();
        }
        else
        {
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }

    private void clickBackButton()
    {
        finish();
    }
}
