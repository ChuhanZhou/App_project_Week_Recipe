package com.example.week_recipe.view.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.MyQRCode;
import com.example.week_recipe.utility.MyUnit;
import com.example.week_recipe.utility.UiDataCache;
import com.example.week_recipe.utility.qrCodeInfo.QRCodeFoodInfo;
import com.example.week_recipe.view.fragment.FoodInformationFragment;
import com.example.week_recipe.view.fragment.PopupQRCodeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class FoodInformationActivity extends AppCompatActivity {

    public static final String foodMenuKey = "foodMenu";
    private ConstraintLayout layout;
    private Button backButton;
    private FloatingActionButton editButton;
    private FloatingActionButton showQRCodeButton;
    private FoodInformationFragment fragment;
    private Food showFood;
    private boolean clickEditButton;
    private PopupQRCodeFragment popupQRCodeFragment;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        bind();
        setListener();
    }

    @Override
    public void onBackPressed() {
        setResult(1,getIntent());
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clickEditButton = false;
        }).start();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == 1)
        {
            showFood = (Food) UiDataCache.getData(data.getExtras().getString("showFood"));
            fragment.bind(showFood);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bind()
    {
        layout = findViewById(R.id.foodInformation_layout);
        View fragmentView = findViewById(R.id.foodInformation_fragment);
        backButton = findViewById(R.id.foodInformation_backButton);
        editButton = findViewById(R.id.foodInformation_editFoodButton);
        showQRCodeButton = findViewById(R.id.foodInformation_showQRCodeButton);
        fragment = FragmentManager.findFragment(fragmentView);
        this.showFood = (Food) UiDataCache.getData(getIntent().getExtras().getString("showFood"));
        fragment.bind(showFood);
    }

    private void setListener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackButton();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditButton();
            }
        });
        showQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickShowQRCodeButton();
            }
        });
    }

    private void clickBackButton()
    {
        finish();
    }

    private void clickEditButton()
    {
        if (!clickEditButton)
        {
            clickEditButton = true;
            Intent intent = new Intent(getApplicationContext(),EditFoodInformationActivity.class);
            intent.putExtra("editFood", UiDataCache.putData("editFood",showFood));
            startActivityForResult(intent,2);
        }
    }

    private void clickShowQRCodeButton()
    {

        int length = Math.min(layout.getWidth(),layout.getHeight());
        String json = new QRCodeFoodInfo(showFood).getJson();
        Bitmap QRCode = MyQRCode.createQRCode(json,length,length);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.fragment_popup_q_r_code, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        WindowManager.LayoutParams wlp =dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.show();
        dialog.getWindow().setLayout(length,length);
        popupQRCodeFragment = new PopupQRCodeFragment(view,QRCode);
    }
}