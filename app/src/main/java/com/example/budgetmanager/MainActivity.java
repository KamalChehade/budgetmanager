package com.example.budgetmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.budgetmanager.databinding.ActivityMainBinding;
import com.example.budgetmanager.ui.transaction.SharedTransactionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView toolbarTitle, currentDate, dailyToggle, monthlyToggle, yearlyToggle;
    private ImageView previousDate, nextDate;
    private View dailyUnderline, monthlyUnderline, yearlyUnderline;
    private Calendar calendar;
    private SharedTransactionViewModel sharedTransactionViewModel;
    private boolean isMonthlyView = false;
    private boolean isYearlyView = false;

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
                calendar.add(Calendar.YEAR, -1);
            } else if (isMonthlyView) {
                calendar.add(Calendar.MONTH, -1);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            updateDateDisplay();
        });

        nextDate.setOnClickListener(v -> {
            if (isYearlyView) {
                calendar.add(Calendar.YEAR, 1);
            } else if (isMonthlyView) {
                calendar.add(Calendar.MONTH, 1);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
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


        ImageView gearIcon = findViewById(R.id.gear_icon);
        gearIcon.setOnClickListener(this::showSettingsMenu);
    }

    private void updateDateDisplay() {
        String formattedDate;
        if (isYearlyView) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
            formattedDate = sdf.format(calendar.getTime());
        } else if (isMonthlyView) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            formattedDate = sdf.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            formattedDate = sdf.format(calendar.getTime());
        }

        currentDate.setText(formattedDate);
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

        updateDateDisplay();
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

        updateDateDisplay();
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

        updateDateDisplay();
    }

    private void showSettingsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.settings_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onSettingsMenuItemClicked);
        popupMenu.show();
    }

    private boolean onSettingsMenuItemClicked(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_reset_transactions) {
            resetAllTransactions();
            return true;
        }
        return false;
    }

    private void resetAllTransactions() {

          new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Reset Transactions")
                .setMessage("Are you sure you want to reset all transactions? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {

                     SharedPreferences preferences = getSharedPreferences("transactions", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();


                    if (sharedTransactionViewModel.getTransactionList().getValue() != null) {
                        sharedTransactionViewModel.setTransactionList(new ArrayList<>());
                    }


                    Toast.makeText(this, "All transactions have been reset.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
