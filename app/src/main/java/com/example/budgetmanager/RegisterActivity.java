package com.example.budgetmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName;
    private RadioGroup genderGroup;
    private RadioButton genderMale, genderFemale;
    private CheckBox checkboxSaving, checkboxExpenses, checkboxGoals, checkboxOther;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already registered
        SharedPreferences sharedPreferences = getSharedPreferences("BudgetBuddyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isRegistered", false)) {
            // If registered, navigate to MainActivity
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish(); // Prevent going back to the registration screen
        }

        setContentView(R.layout.activity_register);

        // Initialize Views
        inputName = findViewById(R.id.input_name);
        genderGroup = findViewById(R.id.gender_group);
        genderMale = findViewById(R.id.gender_male);
        genderFemale = findViewById(R.id.gender_female);
        checkboxSaving = findViewById(R.id.checkbox_saving);
        checkboxExpenses = findViewById(R.id.checkbox_expenses);
        checkboxGoals = findViewById(R.id.checkbox_goals);
        checkboxOther = findViewById(R.id.checkbox_other);
        registerButton = findViewById(R.id.register_button);

        // Register Button Click Listener
        registerButton.setOnClickListener(v -> {
            // Get the name entered by the user
            String name = inputName.getText().toString().trim();

            // Validate Name Input
            if (name.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected gender
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            String gender = "Not Specified"; // Default value
            if (selectedGenderId == R.id.gender_male) {
                gender = "Male";
            } else if (selectedGenderId == R.id.gender_female) {
                gender = "Female";
            }

            // Get the selected interests
            boolean isSavingChecked = checkboxSaving.isChecked();
            boolean isExpensesChecked = checkboxExpenses.isChecked();
            boolean isGoalsChecked = checkboxGoals.isChecked();
            boolean isOtherChecked = checkboxOther.isChecked();

            // Save data in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", name);
            editor.putString("gender", gender);
            editor.putBoolean("isSavingChecked", isSavingChecked);
            editor.putBoolean("isExpensesChecked", isExpensesChecked);
            editor.putBoolean("isGoalsChecked", isGoalsChecked);
            editor.putBoolean("isOtherChecked", isOtherChecked);
            editor.putBoolean("isRegistered", true); // Mark the user as registered
            editor.apply();

            // Show success message
            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Navigate to MainActivity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the RegisterActivity
        });
    }
}
