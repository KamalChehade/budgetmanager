package com.example.budgetmanager.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanager.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Views
        TextView displayName = root.findViewById(R.id.user_display_name);
        TextView gender = root.findViewById(R.id.user_gender);
        TextView interests = root.findViewById(R.id.user_interests);

        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("BudgetBuddyPrefs", getContext().MODE_PRIVATE);
        String name = sharedPreferences.getString("userName", "John Doe");
        String genderValue = sharedPreferences.getString("gender", "Not Specified");
        boolean isSavingChecked = sharedPreferences.getBoolean("isSavingChecked", false);
        boolean isExpensesChecked = sharedPreferences.getBoolean("isExpensesChecked", false);
        boolean isGoalsChecked = sharedPreferences.getBoolean("isGoalsChecked", false);

        // Populate Views
        displayName.setText(name);
        gender.setText(genderValue);

        // Construct Interests String
        StringBuilder interestsString = new StringBuilder();
        if (isSavingChecked) interestsString.append("Saving Money, ");
        if (isExpensesChecked) interestsString.append("Tracking Expenses, ");
        if (isGoalsChecked) interestsString.append("Achieving Financial Goals");

        // Remove trailing comma and space if needed
        if (interestsString.length() > 0 && interestsString.charAt(interestsString.length() - 2) == ',') {
            interestsString.setLength(interestsString.length() - 2);
        }

        interests.setText(interestsString.toString());

        return root;
    }
}
