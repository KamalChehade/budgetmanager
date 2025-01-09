package com.example.budgetmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.budgetmanager.databinding.ActivityMainBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView toolbarTitle, currentDate, dailyToggle, monthlyToggle;
    private ImageView previousDate, nextDate;
    private View dailyUnderline, monthlyUnderline;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Pass each menu ID as a set of top-level destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Initialize toolbar components
        toolbarTitle = findViewById(R.id.toolbar_title);
        currentDate = findViewById(R.id.current_date);
        previousDate = findViewById(R.id.previous_date);
        nextDate = findViewById(R.id.next_date);
        dailyToggle = findViewById(R.id.daily_toggle);
        monthlyToggle = findViewById(R.id.monthly_toggle);
        dailyUnderline = findViewById(R.id.daily_underline);
        monthlyUnderline = findViewById(R.id.monthly_underline);

        // Set up Calendar and Initial Date
        calendar = Calendar.getInstance();
        updateDateDisplay();

        // Handle Previous Date Click
        previousDate.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
        });

        // Handle Next Date Click
        nextDate.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateDisplay();
        });

        // Handle Daily Toggle Click
        dailyToggle.setOnClickListener(v -> {
            showDailyToggle();
        });

        // Handle Monthly Toggle Click
        monthlyToggle.setOnClickListener(v -> {
            showMonthlyToggle();
        });

        // Observe destination changes and update the custom TextView title
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home) {
                toolbarTitle.setText("Home");
            } else if (destination.getId() == R.id.navigation_dashboard) {
                toolbarTitle.setText("Dashboard");
            } else if (destination.getId() == R.id.navigation_notifications) {
                toolbarTitle.setText("Notifications");
            }
        });

        // Set up BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        currentDate.setText(sdf.format(calendar.getTime()));
    }

    private void showDailyToggle() {
        // Show Daily underline, hide Monthly underline
        dailyUnderline.setVisibility(View.VISIBLE);
        monthlyUnderline.setVisibility(View.GONE);

        // Update text colors for active state
        dailyToggle.setTextColor(getResources().getColor(android.R.color.white));
        monthlyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void showMonthlyToggle() {
        // Show Monthly underline, hide Daily underline
        monthlyUnderline.setVisibility(View.VISIBLE);
        dailyUnderline.setVisibility(View.GONE);

        // Update text colors for active state
        monthlyToggle.setTextColor(getResources().getColor(android.R.color.white));
        dailyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }
}
