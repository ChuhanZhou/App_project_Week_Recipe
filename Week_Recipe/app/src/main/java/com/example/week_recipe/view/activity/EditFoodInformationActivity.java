package com.example.week_recipe.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;
import com.example.week_recipe.utility.qrCodeInfo.QRCodeFoodInfo;
import com.example.week_recipe.utility.qrCodeInfo.QRCodeInfo;
import com.example.week_recipe.view.fragment.QRCodeScanFragment;
import com.example.week_recipe.view.fragment.SetFoodInformationFragment;
import com.example.week_recipe.viewModel.EditFoodInformationViewModel;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.utility.UiDataCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EditFoodInformationActivity extends AppCompatActivity implements QRCodeScanFragment.DecodeQRCodeListener{

    public static final String foodMenuKey = "foodMenu";
    private final static int NEED_BACK = 1001;
    private EditFoodInformationViewModel viewModel;
    private Button backButton;
    private FloatingActionButton saveButton;
    private FloatingActionButton qrCodeScanButton;
    private Food oldFood;
    private SetFoodInformationFragment fragment;
    private QRCodeScanFragment qrCodeScanFragment;
    private boolean isScanning;
    private boolean needQRCodeInfo;
    private boolean clickSaveButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NEED_BACK) {
                clickBackButton();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_information);

        //init
        clickSaveButton = false;
        isScanning = false;
        needQRCodeInfo = false;
        viewModel = new ViewModelProvider(this).get(EditFoodInformationViewModel.class);

        bind();
        setListener();
    }

    @Override
    public void onBackPressed() {
        if (isScanning)
        {
            isScanning = false;
            fragment.getView().setVisibility(View.VISIBLE);
            qrCodeScanButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            qrCodeScanFragment.stopCamera();
            qrCodeScanFragment.getView().setVisibility(View.INVISIBLE);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (isScanning)
        //{
        //    qrCodeScanFragment.stopCamera();
        //    qrCodeScanFragment.openCamera(this);
        //    needQRCodeInfo = true;
        //}
    }

    private void toastPrint(String information)
    {
        if (information!=null)
        {
            Context context = getApplicationContext();
            Toast.makeText(context,information,Toast.LENGTH_SHORT).show();
        }
    }

    private void bind()
    {
        backButton = findViewById(R.id.editFoodInformation_backButton);
        saveButton = findViewById(R.id.editFoodInformation_saveButton);
        qrCodeScanButton = findViewById(R.id.editFoodInformation_qrCodeScanButton);
        fragment = FragmentManager.findFragment(findViewById(R.id.editFoodInformation_fragment));
        qrCodeScanFragment = FragmentManager.findFragment(findViewById(R.id.editFoodInformation_QRCodeSacnFragment));
        oldFood = (Food) UiDataCache.getData(getIntent().getExtras().getString("editFood"));
        fragment.bind(oldFood.copy(),false);

        qrCodeScanFragment.getView().setVisibility(View.INVISIBLE);
    }

    private void setListener()
    {
        qrCodeScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickQRCodeScanButton();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackButton();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveButton();
            }
        });
    }

    private void clickQRCodeScanButton()
    {
        if (isScanning)
        {

        }
        else
        {
            isScanning = true;
            needQRCodeInfo = true;

            fragment.getView().setVisibility(View.GONE);
            qrCodeScanButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);

            qrCodeScanFragment.getView().setVisibility(View.VISIBLE);
            qrCodeScanFragment.openCamera(this);
        }
    }

    private void clickBackButton()
    {
        onBackPressed();
    }

    private void clickSaveButton()
    {
        if (!clickSaveButton)
        {
            clickSaveButton = true;
            Food newFood = fragment.getNewFood();

            String result;
            FoodList foodMenu = (FoodList) UiDataCache.getData(foodMenuKey);
            if (foodMenu==null)
            {
                //for update food home page
                result = viewModel.updateFoodInformation(oldFood,newFood);
            }
            else
            {
                //for update food in favourite week recipe
                result = foodMenu.update(oldFood,newFood);
                if (result==null)
                {
                    viewModel.updateFoodInformation(oldFood,newFood);
                }
            }

            toastPrint(result);
            if (result==null)
            {
                Intent intent = new Intent();
                intent.putExtra("showFood",UiDataCache.putData("showFood",newFood));
                setResult(1,intent);
                finish();
            }
            else
            {
                clickSaveButton = false;
            }
        }
    }

    @Override
    public void onDecodeNewInfo(String info) {
        try {
            QRCodeInfo qrCodeInfo = new Gson().fromJson(info, QRCodeInfo.class);
            switch (qrCodeInfo.getInfoType())
            {
                case FOOD:
                    if (needQRCodeInfo) {
                        needQRCodeInfo = false;
                        QRCodeFoodInfo foodInfo = new Gson().fromJson(info, QRCodeFoodInfo.class);
                        Food food = foodInfo.getFood();
                        food = new Food(food.getName(),food.getType(),food.getIngredientsList(),oldFood.getImageId());
                        fragment.setFood(food);

                        Message message = new Message();
                        message.what = NEED_BACK;
                        handler.sendMessage(message);
                    }
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}