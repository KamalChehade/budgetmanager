package com.example.budgetmanager.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.budgetmanager.R;
import com.example.budgetmanager.adapters.TransactionAdapter;
import com.example.budgetmanager.databinding.FragmentHomeBinding;
import com.example.budgetmanager.model.Transaction;
import com.example.budgetmanager.ui.transaction.AddTransactionFragment;
import com.example.budgetmanager.ui.transaction.SharedTransactionViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private SharedTransactionViewModel sharedTransactionViewModel;
    private TransactionAdapter transactionAdapter;

    private double totalIncome = 0.0;
    private double totalExpense = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedTransactionViewModel = new ViewModelProvider(requireActivity()).get(SharedTransactionViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.floatingActionButton.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getParentFragmentManager(), "AddTransactionFragment");
        });

        transactionAdapter = new TransactionAdapter(getContext());
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.transactionsList.setAdapter(transactionAdapter);

        sharedTransactionViewModel.getTransactionList().observe(getViewLifecycleOwner(), transactions -> {
            transactionAdapter.setTransactions(transactions);
            calculateTotals(transactions);  // Calculate totals when transactions are updated
        });

        loadTransactions();  // Load transactions from SharedPreferences when fragment is created

        return root;
    }

    private void loadTransactions() {
        SharedPreferences preferences = requireContext().getSharedPreferences("transactions", Context.MODE_PRIVATE);
        String allTransactions = preferences.getString("allTransactions", "");

        if (!allTransactions.isEmpty()) {
            String[] transactions = allTransactions.split(";");
            List<Transaction> transactionList = new ArrayList<>();
            for (String transaction : transactions) {
                String[] details = transaction.split(",");
                if (details.length == 5) {
                    String transactionType = details[0];
                    String category = details[1];
                    double amount = 0;
                    try {
                        amount = Double.parseDouble(details[2]);
                    } catch (NumberFormatException e) {
                        Log.e("TransactionDebug", "Invalid amount format: " + details[2]);
                        continue;
                    }
                    String note = details[3];
                    long dateTimestamp = Long.parseLong(details[4]);
                    Date date = new Date(dateTimestamp);

                    transactionList.add(new Transaction(transactionType, category, amount, note, date));
                } else {
                    Log.e("TransactionDebug", "Invalid transaction data: " + transaction);
                }
            }

            sharedTransactionViewModel.setTransactionList(transactionList);
            calculateTotals(transactionList);  // Calculate totals after loading transactions
            transactionAdapter.setTransactions(transactionList);
        } else {
            Log.d("TransactionDebug", "No transactions found in SharedPreferences");
        }
    }

    private void calculateTotals(List<Transaction> transactions) {
        totalIncome = 0.0;
        totalExpense = 0.0;

        for (Transaction transaction : transactions) {
            if ("Income".equals(transaction.getTransactionType())) {
                totalIncome += transaction.getAmount();
            } else if ("Expense".equals(transaction.getTransactionType())) {
                totalExpense += transaction.getAmount();
            }
        }

        double total = totalIncome - totalExpense;

        // Update the UI with the calculated values
        binding.incomeLbl.setText(String.format("$%.2f", totalIncome));
        binding.expenseLbl.setText(String.format("$%.2f", totalExpense));
        binding.totalLbl.setText(String.format("$%.2f", total));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
