package com.example.week_recipe;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.*;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity {

    private SystemModel systemModel;
    private Toolbar toolbar;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout layout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationDrawer;
    private TextView userNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        bind();
        updateInfo();
        setBottomNavVisibility();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.homePage_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration)||super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        System.out.println(2);
        super.onBackPressed();
    }

    private void init()
    {
        systemModel = SystemModelManager.getSystemModelManager();
    }

    private void bind()
    {
        layout = findViewById(R.id.homepage_constraintLayout);
        //navigation
        toolbar = findViewById(R.id.homePage_toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.homePage_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_todayRecipeFragment,R.id.nav_userInformationFragment)
                .setOpenableLayout(layout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //bottom navigation
        bottomNavigationView = findViewById(R.id.homePage_bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //navigation drawer
        navigationDrawer = findViewById(R.id.homePage_navigationDrawer);
        userNameTextView = navigationDrawer.getHeaderView(0).findViewById(R.id.navigationDrawerHeader_userNameTextView);
        NavigationUI.setupWithNavController(navigationDrawer, navController);
    }

    private void updateInfo()
    {
        userNameTextView.setText(systemModel.getUserData().getUserName());
    }

    private void setBottomNavVisibility()
    {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId())
                {
                    case R.id.nav_todayRecipeFragment:
                    case R.id.nav_weekRecipeFragment:
                    case R.id.nav_userInformationFragment:
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

}
