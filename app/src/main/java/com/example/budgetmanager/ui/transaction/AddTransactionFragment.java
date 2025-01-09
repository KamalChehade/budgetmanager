package com.example.budgetmanager.ui.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.budgetmanager.R;
import com.example.budgetmanager.databinding.FragmentAddTransactionBinding;
import com.example.budgetmanager.model.Transaction;

import java.util.Date;

public class AddTransactionFragment extends DialogFragment {

    private FragmentAddTransactionBinding binding;
    private AddTransactionViewModel addTransactionViewModel;
    private SharedTransactionViewModel sharedTransactionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewModels
        addTransactionViewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);
        sharedTransactionViewModel = new ViewModelProvider(requireActivity()).get(SharedTransactionViewModel.class); // SharedViewModel

        // Setup button listeners
        binding.incomeBtn.setOnClickListener(v -> {
            addTransactionViewModel.setTransactionType("Income");
            updateTransactionTypeUI(true); // Highlight Income button
        });

        binding.expenseBtn.setOnClickListener(v -> {
            addTransactionViewModel.setTransactionType("Expense");
            updateTransactionTypeUI(false); // Highlight Expense button
        });

        setupCategorySpinner();

        binding.saveTransactionBtn.setOnClickListener(v -> {
            saveTransaction();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set custom width for the dialog
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // Make the dialog fill the width
            window.setAttributes(params);
        }
    }

    private void updateTransactionTypeUI(boolean isIncome) {
        // Highlight Income or Expense button
        binding.incomeBtn.setBackgroundResource(isIncome ? R.drawable.selected_selector : R.drawable.default_selector);
        binding.expenseBtn.setBackgroundResource(isIncome ? R.drawable.default_selector : R.drawable.selected_selector);
    }

    private void setupCategorySpinner() {
        // Define categories and their icons
        String[] categories = getResources().getStringArray(R.array.category_array);
        int[] icons = {
                R.drawable.ic_placeholder, // Placeholder icon for "Choose Category"
                R.drawable.ic_salary,
                R.drawable.ic_loan,
                R.drawable.ic_food,
                R.drawable.ic_grocery,
                R.drawable.ic_other
        };

        // Set custom adapter
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(requireContext(), categories, icons);
        binding.categorySpinner.setAdapter(adapter);

        // Set default selection to "Choose Category" (index 0)
        binding.categorySpinner.setSelection(0);
        addTransactionViewModel.setCategory(null); // No category selected initially

        // Handle item selection
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // If "Choose Category" is selected, set category as null
                if (position == 0) {
                    addTransactionViewModel.setCategory(null);
                } else {
                    // Set selected category
                    String selectedCategory = categories[position];
                    addTransactionViewModel.setCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle if nothing is selected
            }
        });
    }

    private void saveTransaction() {
        // Validate and save transaction
        String transactionType = addTransactionViewModel.getTransactionType().getValue();
        String amountText = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = addTransactionViewModel.getCategory().getValue();
        boolean isRecurring = binding.recurringCheckbox.isChecked();

        if (transactionType == null || amountText.isEmpty() || category == null) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that amount is a valid number
        double amount = 0;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current date for the transaction as Date object
        Date currentDate = new Date(); // Get the current date as Date object

        // Prepare the transaction data string for SharedPreferences (serialize Date to timestamp)
        String transactionData = transactionType + "," + category + "," + amount + "," + note + "," + currentDate.getTime(); // Save the timestamp

        // Log the data to ensure it's correct
        Log.d("TransactionDebug", "Saving transaction: " + transactionData);  // Log the data being saved

        // Check if the transaction data is valid before saving
        if (transactionData.split(",").length != 5) {
            Log.e("TransactionDebug", "Invalid transaction data format: " + transactionData);
            return;
        }

        // Save transaction to SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("transactions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Retrieve the existing transactions from SharedPreferences
        String previousData = preferences.getString("allTransactions", "");
        previousData += transactionData + ";"; // Append the new transaction

        editor.putString("allTransactions", previousData);
        editor.apply(); // Apply changes

        // Create a Transaction object using the Date object and update the SharedViewModel
        Transaction transaction = new Transaction(transactionType, category, amount, note, currentDate); // Pass Date object
        sharedTransactionViewModel.addTransaction(transaction);

        // Show transaction saved message
        Toast.makeText(getContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();

        // Dismiss the dialog
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
