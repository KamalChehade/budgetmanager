package com.example.budgetmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.budgetmanager.ui.transaction.SharedTransactionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.budgetmanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView toolbarTitle, currentDate, dailyToggle, monthlyToggle, yearlyToggle;
    private ImageView previousDate, nextDate;
    private View dailyUnderline, monthlyUnderline, yearlyUnderline;
    private Calendar calendar;
    private SharedTransactionViewModel sharedTransactionViewModel;
    private boolean isMonthlyView = false;  // Flag to track monthly view
    private boolean isYearlyView = false;   // Flag to track yearly view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedTransactionViewModel = new ViewModelProvider(this).get(SharedTransactionViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        toolbarTitle = findViewById(R.id.toolbar_title);
        currentDate = findViewById(R.id.current_date);
        previousDate = findViewById(R.id.previous_date);
        nextDate = findViewById(R.id.next_date);
        dailyToggle = findViewById(R.id.daily_toggle);
        monthlyToggle = findViewById(R.id.monthly_toggle);
        yearlyToggle = findViewById(R.id.yearly_toggle);
        dailyUnderline = findViewById(R.id.daily_underline);
        monthlyUnderline = findViewById(R.id.monthly_underline);
        yearlyUnderline = findViewById(R.id.yearly_underline);

        calendar = Calendar.getInstance();
        updateDateDisplay();

        previousDate.setOnClickListener(v -> {
            if (isYearlyView) {
                calendar.add(Calendar.YEAR, -1);  // Move to the previous year
            } else if (isMonthlyView) {
                calendar.add(Calendar.MONTH, -1);  // Move to the previous month
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, -1);  // Move to the previous day
            }
            updateDateDisplay();
        });

        nextDate.setOnClickListener(v -> {
            if (isYearlyView) {
                calendar.add(Calendar.YEAR, 1);  // Move to the next year
            } else if (isMonthlyView) {
                calendar.add(Calendar.MONTH, 1);  // Move to the next month
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);  // Move to the next day
            }
            updateDateDisplay();
        });

        dailyToggle.setOnClickListener(v -> showDailyToggle());
        monthlyToggle.setOnClickListener(v -> showMonthlyToggle());
        yearlyToggle.setOnClickListener(v -> showYearlyToggle());

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home) {
                toolbarTitle.setText("Home");
            } else if (destination.getId() == R.id.navigation_profile) {
                toolbarTitle.setText("Profile");
            }
        });

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void updateDateDisplay() {
        String formattedDate;
        if (isYearlyView) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());  // Show year only
            formattedDate = sdf.format(calendar.getTime());
        } else if (isMonthlyView) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());  // Show month and year
            formattedDate = sdf.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());  // Show full date
            formattedDate = sdf.format(calendar.getTime());
        }

        currentDate.setText(formattedDate);

        // Save the current date to SharedViewModel for HomeFragment
        sharedTransactionViewModel.setSelectedDate(formattedDate);
    }

    private void showDailyToggle() {
        isMonthlyView = false;
        isYearlyView = false;

        dailyUnderline.setVisibility(View.VISIBLE);
        monthlyUnderline.setVisibility(View.GONE);
        yearlyUnderline.setVisibility(View.GONE);

        dailyToggle.setTextColor(getResources().getColor(android.R.color.white));
        monthlyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        yearlyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));

        updateDateDisplay();  // Update the date display for daily view
    }

    private void showMonthlyToggle() {
        isMonthlyView = true;
        isYearlyView = false;

        monthlyUnderline.setVisibility(View.VISIBLE);
        dailyUnderline.setVisibility(View.GONE);
        yearlyUnderline.setVisibility(View.GONE);

        monthlyToggle.setTextColor(getResources().getColor(android.R.color.white));
        dailyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        yearlyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));

        updateDateDisplay();  // Update the date display for monthly view
    }

    private void showYearlyToggle() {
        isMonthlyView = false;
        isYearlyView = true;

        yearlyUnderline.setVisibility(View.VISIBLE);
        dailyUnderline.setVisibility(View.GONE);
        monthlyUnderline.setVisibility(View.GONE);

        yearlyToggle.setTextColor(getResources().getColor(android.R.color.white));
        dailyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        monthlyToggle.setTextColor(getResources().getColor(android.R.color.darker_gray));

        updateDateDisplay();  // Update the date display for yearly view
    }
}
