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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        addTransactionViewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);
        sharedTransactionViewModel = new ViewModelProvider(requireActivity()).get(SharedTransactionViewModel.class);

        binding.incomeBtn.setOnClickListener(v -> {
            addTransactionViewModel.setTransactionType("Income");
            updateTransactionTypeUI(true);
        });

        binding.expenseBtn.setOnClickListener(v -> {
            addTransactionViewModel.setTransactionType("Expense");
            updateTransactionTypeUI(false);
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

        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    private void updateTransactionTypeUI(boolean isIncome) {
        // Highlight Income or Expense button
        binding.incomeBtn.setBackgroundResource(isIncome ? R.drawable.selected_selector : R.drawable.default_selector);
        binding.expenseBtn.setBackgroundResource(isIncome ? R.drawable.default_selector : R.drawable.selected_selector);
    }

    private void setupCategorySpinner() {
        String[] categories = getResources().getStringArray(R.array.category_array);
        int[] icons = {
                R.drawable.ic_placeholder,
                R.drawable.ic_salary,
                R.drawable.ic_loan,
                R.drawable.ic_food,
                R.drawable.ic_grocery,
                R.drawable.ic_other
        };

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(requireContext(), categories, icons);
        binding.categorySpinner.setAdapter(adapter);

        binding.categorySpinner.setSelection(0);
        addTransactionViewModel.setCategory(null);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    addTransactionViewModel.setCategory(null);
                } else {
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
        String transactionType = addTransactionViewModel.getTransactionType().getValue();
        String amountText = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = addTransactionViewModel.getCategory().getValue();

        if (transactionType == null || amountText.isEmpty() || category == null) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = 0;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedDate = sharedTransactionViewModel.getSelectedDate().getValue();
        Date transactionDate;
        try {
            if (selectedDate != null) {
                boolean isMonthly = selectedDate.split(" ").length == 2;
                boolean isYearly = selectedDate.split(" ").length == 1;

                if (!isMonthly && !isYearly) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                    transactionDate = sdf.parse(selectedDate);
                } else {
                    transactionDate = new Date();
                }
            } else {
                transactionDate = new Date();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid selected date.", Toast.LENGTH_SHORT).show();
            return;
        }

        String transactionData = transactionType + "," + category + "," + amount + "," + note + "," + transactionDate.getTime();

        SharedPreferences preferences = requireContext().getSharedPreferences("transactions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String previousData = preferences.getString("allTransactions", "");
        previousData += transactionData + ";";

        editor.putString("allTransactions", previousData);
        editor.apply();

        Transaction transaction = new Transaction(transactionType, category, amount, note, transactionDate);
        sharedTransactionViewModel.addTransaction(transaction);

        Toast.makeText(getContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();

        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
