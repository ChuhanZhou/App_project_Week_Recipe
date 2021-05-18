package com.example.week_recipe.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
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
import com.example.week_recipe.utility.UiDataCache;
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
    private CardView favouriteFoodCardView;
    private CardView favouriteRecipeCardView;
    private MenuItem reset;
    private MenuItem addToFavourite;
    private MenuItem setByFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        bind();
        updateInfo(viewModel.getUserData().getValue());
        setListener();
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
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    private void bind() {
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
        favouriteFoodCardView = navigationDrawer.getHeaderView(0).findViewById(R.id.navigationDrawerHeader_favouriteFoodCardView);
        favouriteRecipeCardView = navigationDrawer.getHeaderView(0).findViewById(R.id.navigationDrawerHeader_favouriteRecipeCardView);
        NavigationUI.setupWithNavController(navigationDrawer, navController);

    }

    private void setListener() {
        viewModel.getUserData().observe(this, new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                updateInfo(userData);
            }
        });

        favouriteFoodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, FavouriteFoodActivity.class);
                startActivity(intent);
            }
        });

        favouriteRecipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                switch (item.getItemId()) {
                    case R.id.nav_item_setByFavourite:
                        if (fragment.getClass().equals(RecipeForWeekFragment.class)) {
                            ((RecipeForWeekFragment) fragment).setWeekRecipeByFavourite();
                        }
                        break;
                    case R.id.nav_item_addToFavourite:
                        if (fragment.getClass().equals(RecipeForWeekFragment.class)) {
                            ((RecipeForWeekFragment) fragment).addFavouriteWeekRecipe();
                        }
                        break;
                    case R.id.nav_item_reset:
                        if (fragment.getClass().equals(RecipeWithDateFragment.class)) {
                            ((RecipeWithDateFragment) fragment).setShowDate(LocalDate.now());
                        } else if (fragment.getClass().equals(RecipeForWeekFragment.class)) {
                            ((RecipeForWeekFragment) fragment).setShowDate(LocalDate.now());
                        }
                        break;
                    case R.id.nav_settingsFragment:
                        navController.navigate(R.id.nav_settingsFragment);
                        break;
                }
                return false;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (reset == null||addToFavourite == null||setByFavourite==null) {
                    reset = toolbar.getMenu().findItem(R.id.nav_item_reset);
                    addToFavourite = toolbar.getMenu().findItem(R.id.nav_item_addToFavourite);
                    setByFavourite = toolbar.getMenu().findItem(R.id.nav_item_setByFavourite);
                }
                if (toolbar.getMenu().size() > 0) {
                    switch (destination.getId()) {
                        case R.id.nav_todayRecipeFragment:
                            setByFavourite.setVisible(false);
                            addToFavourite.setVisible(false);
                            reset.setVisible(true);
                            reset.setTitle(R.string.title_resetDate);

                            break;
                        case R.id.nav_weekRecipeFragment:
                            setByFavourite.setVisible(true);
                            addToFavourite.setVisible(true);
                            reset.setVisible(true);
                            reset.setTitle(R.string.title_resetWeek);
                            break;
                        default:
                            setByFavourite.setVisible(false);
                            addToFavourite.setVisible(false);
                            reset.setVisible(false);
                    }
                }

                //setBottomNavVisibility
                switch (destination.getId()) {
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

    private void updateInfo(UserData userData) {
        if (userData != null) {
            userNameTextView.setText(userData.getUserName());
            if (userData.hasUserImage()) {
                userImageView.setImageBitmap(userData.getUserImage());

            } else {
                userImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
            userImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
