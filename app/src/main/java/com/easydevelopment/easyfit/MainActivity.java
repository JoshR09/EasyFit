package com.easydevelopment.easyfit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.easydevelopment.easyfit.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Handle navigation item clicks
        navView.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.navigation_workouts:
                    navController.navigate(R.id.navigation_workouts);
                    return true;
                case R.id.navigation_history:
                    navController.navigate(R.id.navigation_history);
                    return true;
                default:
                    return false;
            }
        });
    }
}
