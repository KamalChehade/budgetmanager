package com.example.budgetmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName;
    private RadioGroup genderGroup;
    private RadioButton genderMale, genderFemale;
    private CheckBox checkboxSaving, checkboxExpenses, checkboxGoals;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences("BudgetBuddyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isRegistered", false)) {

            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_register);


        ImageView imageView = findViewById(R.id.image_expensemanager);
        inputName = findViewById(R.id.input_name);
        genderGroup = findViewById(R.id.gender_group);
        genderMale = findViewById(R.id.gender_male);
        genderFemale = findViewById(R.id.gender_female);
        checkboxSaving = findViewById(R.id.checkbox_saving);
        checkboxExpenses = findViewById(R.id.checkbox_expenses);
        checkboxGoals = findViewById(R.id.checkbox_goals);
        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(v -> {

            String name = inputName.getText().toString().trim();


            if (name.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected gender
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            String gender = "Not Specified";
            if (selectedGenderId == R.id.gender_male) {
                gender = "Male";
            } else if (selectedGenderId == R.id.gender_female) {
                gender = "Female";
            }


            boolean isSavingChecked = checkboxSaving.isChecked();
            boolean isExpensesChecked = checkboxExpenses.isChecked();
            boolean isGoalsChecked = checkboxGoals.isChecked();

             SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", name);
            editor.putString("gender", gender);
            editor.putBoolean("isSavingChecked", isSavingChecked);
            editor.putBoolean("isExpensesChecked", isExpensesChecked);
            editor.putBoolean("isGoalsChecked", isGoalsChecked);
            editor.putBoolean("isRegistered", true);
            editor.apply();


            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
