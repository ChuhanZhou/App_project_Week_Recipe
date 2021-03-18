package com.example.week_recipe;

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

import com.example.week_recipe.model.LoginModel;
import com.example.week_recipe.model.LoginModelManager;


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
        init();
        setContentView(R.layout.activity_login);
        bind();
        setListener();

        loginModel.register("0","0");
        accountEditText.setText("0");
        passwordEditText.setText("0");
        clickLoginButton();

        //System.out.println(userPicture.getDrawable().g.toString());

        //Resources resources = this.getBaseContext().getResources();
        //userPicture.setImageDrawable(resources.getDrawable(R.drawable.user_picture));

        //Bitmap bitmap = ((BitmapDrawable)userPicture.getDrawable()).getBitmap();
        //userPicture.setImageBitmap(TransformPicture.getRoundedCornerBitmap(bitmap));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 1)
        {
            accountEditText.setText(data.getExtras().getString("account"));
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
            intent.putExtra("account",Integer.parseInt(accountEditText.getText().toString()));
            finish();
        }
        else
        {
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }
}