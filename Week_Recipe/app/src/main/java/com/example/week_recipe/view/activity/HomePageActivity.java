package com.example.week_recipe.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.*;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.week_recipe.R;
import com.example.week_recipe.model.SystemModel;
import com.example.week_recipe.model.SystemModelManager;
import com.example.week_recipe.model.domain.user.UserData;
import com.example.week_recipe.view.fragment.DailyRecipeFragment;
import com.example.week_recipe.view.fragment.RecipeForWeekFragment;
import com.example.week_recipe.view.fragment.RecipeWithDateFragment;
import com.example.week_recipe.viewModel.HomePageViewModel;
import com.example.week_recipe.viewModel.UserInformationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomePageActivity extends AppCompatActivity {

    private HomePageViewModel viewModel;
    private Toolbar toolbar;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout layout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationDrawer;
    private NavHostFragment navHostFragment;
    private TextView userNameTextView;
    private ImageView userImageView;
    private MenuItem reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        bind();
        updateInfo(viewModel.getUserData().getValue());
        setListener();
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

    private void init()
    {
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    private void bind()
    {
        layout = findViewById(R.id.homepage_constraintLayout);
        navHostFragment = FragmentManager.findFragment(findViewById(R.id.homePage_fragment));
        //navigation
        toolbar = findViewById(R.id.homePage_toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.homePage_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_todayRecipeFragment,
                R.id.nav_weekRecipeFragment,
                R.id.nav_userInformationFragment)
                .setOpenableLayout(layout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //bottom navigation
        bottomNavigationView = findViewById(R.id.homePage_bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //navigation drawer
        navigationDrawer = findViewById(R.id.homePage_navigationDrawer);
        userNameTextView = navigationDrawer.getHeaderView(0).findViewById(R.id.navigationDrawerHeader_userNameTextView);
        userImageView = navigationDrawer.getHeaderView(0).findViewById(R.id.navigationDrawerHeader_userImage);
        NavigationUI.setupWithNavController(navigationDrawer, navController);
    }

    private void setListener()
    {
        viewModel.getUserData().observe(this, new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                updateInfo(userData);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                switch (item.getItemId())
                {
                    case R.id.nav_item_reset:
                        if (fragment.getClass().equals(RecipeWithDateFragment.class))
                        {
                            ((RecipeWithDateFragment) fragment).setShowDate(LocalDate.now());
                        }
                        else if (fragment.getClass().equals(RecipeForWeekFragment.class))
                        {
                            ((RecipeForWeekFragment) fragment).setShowDate(LocalDate.now());
                        }
                        break;
                        
                }
                return false;
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (reset==null)
                {
                    reset = toolbar.getMenu().findItem(R.id.nav_item_reset);
                }
                if (toolbar.getMenu().size()>0)
                {
                    switch (destination.getId())
                    {
                        case R.id.nav_todayRecipeFragment:
                            if (reset!=null)
                            {
                                reset.setVisible(true);
                                reset.setTitle(R.string.title_resetDate);
                            }
                            break;
                        case R.id.nav_weekRecipeFragment:
                            if (reset!=null)
                            {
                                reset.setVisible(true);
                                reset.setTitle(R.string.title_resetWeek);
                            }
                            break;
                        default:
                            if (reset!=null)
                            {
                                reset.setVisible(false);
                            }
                    }
                }
            }
        });
    }

    private void updateInfo(UserData userData)
    {
        if (userData!=null)
        {
            userNameTextView.setText(userData.getUserName());
            if(userData.hasUserImage())
            {
                userImageView.setImageBitmap(userData.getUserImage());

            }
            else
            {
                userImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
            userImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void setBottomNavVisibility()
    {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("NonConstantResourceId")
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
