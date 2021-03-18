package com.example.week_recipe;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.*;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;

public class HomePageActivity extends AppCompatActivity {

    private SystemModel systemModel;
    private TextView accountTextView;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private com.example.week_recipe.HomePageMainFragment mainFragment;
    private com.example.week_recipe.HomePageSecondFragment secondFragment;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        bind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void init()
    {
        systemModel = SystemModelManager.getSystemModelManager();
        fragmentManager = getSupportFragmentManager();
    }

    private void bind()
    {
        layout = findViewById(R.id.homePage_layout);

        navController = Navigation.findNavController(this,R.id.homePage_fragment);
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homePageMainFragment,
                R.id.homePageSecondFragment).setOpenableLayout(layout).build();

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //NavigationUI.setupWithNavController(navigationDrawer, navController);

        accountTextView = findViewById(R.id.homePage_fragment_main_account);
        accountTextView.setText(systemModel.getAccount());
        toolbar = findViewById(R.id.homePage_toolbar);
        setSupportActionBar(toolbar);
        showMainFragment();
        //showMainFragment();
        //showSecondFragment();
    }

    private void showMainFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mainFragment==null)
        {
            //mainFragment = HomePageMainFragment.();
            fragmentTransaction.add(R.id.homePage_fragment,mainFragment);
        }
        fragmentTransaction.show(mainFragment);
        fragmentTransaction.commit();
    }

    private void showSecondFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (secondFragment==null)
        {
            secondFragment = com.example.week_recipe.HomePageSecondFragment.newInstance();
            fragmentTransaction.add(R.id.homePage_fragment,secondFragment);
        }
        if (mainFragment!=null)
        {
            fragmentTransaction.remove(mainFragment);
            mainFragment = null;
        }
        fragmentTransaction.replace(R.id.homePage_fragment,mainFragment);
        fragmentTransaction.commit();
    }
}
