package com.example.budgetmanager.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.budgetmanager.R;
import com.example.budgetmanager.adapters.TransactionAdapter;
import com.example.budgetmanager.databinding.FragmentHomeBinding;
import com.example.budgetmanager.model.Transaction;
import com.example.budgetmanager.ui.transaction.AddTransactionFragment;
import com.example.budgetmanager.ui.transaction.SharedTransactionViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements TransactionAdapter.OnTransactionClickListener {

    private FragmentHomeBinding binding;
    private SharedTransactionViewModel sharedTransactionViewModel;
    private TransactionAdapter transactionAdapter;

    private double totalIncome = 0.0;
    private double totalExpense = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedTransactionViewModel = new ViewModelProvider(requireActivity()).get(SharedTransactionViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.floatingActionButton.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getParentFragmentManager(), "AddTransactionFragment");
        });

         transactionAdapter = new TransactionAdapter(getContext(), this);
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.transactionsList.setAdapter(transactionAdapter);

        sharedTransactionViewModel.getTransactionList().observe(getViewLifecycleOwner(), transactions -> {
            String selectedDate = sharedTransactionViewModel.getSelectedDate().getValue();
            filterTransactionsByDate(transactions, selectedDate);
        });

         sharedTransactionViewModel.getSelectedDate().observe(getViewLifecycleOwner(), selectedDate -> {
            loadTransactions(selectedDate);
        });

        return root;
    }

    private void loadTransactions(String selectedDate) {
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
            filterTransactionsByDate(transactionList, selectedDate);
        } else {
            Log.d("TransactionDebug", "No transactions found in SharedPreferences");
        }
    }

    private void filterTransactionsByDate(List<Transaction> transactions, String selectedDate) {
        if (selectedDate != null) {
            boolean isYearly = selectedDate.split(" ").length == 1;
            boolean isMonthly = selectedDate.split(" ").length == 2;
            SimpleDateFormat sdf;
            List<Transaction> filteredTransactions = new ArrayList<>();

            if (isYearly) {

                sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
            } else if (isMonthly) {

                sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            } else {
                // Format to "MMMM dd, yyyy" for daily filtering
                sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            }

            for (Transaction transaction : transactions) {
                String transactionDateFormatted = sdf.format(transaction.getDate());
                if (transactionDateFormatted.equals(selectedDate)) {
                    filteredTransactions.add(transaction);
                }
            }

            if (!filteredTransactions.isEmpty()) {
                transactionAdapter.setTransactions(filteredTransactions);
                calculateTotals(filteredTransactions);
            } else {
                transactionAdapter.setTransactions(new ArrayList<>());
                binding.incomeLbl.setText("$0.00");
                binding.expenseLbl.setText("$0.00");
                binding.totalLbl.setText("$0.00");
            }
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


        binding.incomeLbl.setText(String.format("$%.2f", totalIncome));
        binding.expenseLbl.setText(String.format("$%.2f", totalExpense));
        binding.totalLbl.setText(String.format("$%.2f", total));
    }

    @Override
    public void onTransactionLongClick(Transaction transaction) {

         new AlertDialog.Builder(getContext())
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteTransaction(transaction);
                })
                .setNegativeButton("No", (dialog, which) -> {

                    transactionAdapter.notifyDataSetChanged();
                })
                .show();
    }

    private void deleteTransaction(Transaction transaction) {

        SharedPreferences preferences = requireContext().getSharedPreferences("transactions", Context.MODE_PRIVATE);
        String allTransactions = preferences.getString("allTransactions", "");
        String newTransactions = allTransactions.replace(transaction.getTransactionType() + "," + transaction.getCategory() + "," + transaction.getAmount() + "," + transaction.getNote() + "," + transaction.getDate().getTime() + ";", "");


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("allTransactions", newTransactions);
        editor.apply();


        List<Transaction> currentList = sharedTransactionViewModel.getTransactionList().getValue();
        if (currentList != null) {
            currentList.remove(transaction);
            sharedTransactionViewModel.setTransactionList(currentList);
        }


        Toast.makeText(getContext(), "Transaction deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
