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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModels
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedTransactionViewModel = new ViewModelProvider(requireActivity()).get(SharedTransactionViewModel.class); // SharedViewModel

        // Inflate the layout using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set FloatingActionButton click listener to show AddTransactionFragment
        binding.floatingActionButton.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getParentFragmentManager(), "AddTransactionFragment");
        });

        // Setup RecyclerView for transactions
        transactionAdapter = new TransactionAdapter(getContext());
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.transactionsList.setAdapter(transactionAdapter);

        // Observe the transaction list from SharedViewModel
        sharedTransactionViewModel.getTransactionList().observe(getViewLifecycleOwner(), transactions -> {
            // Update the RecyclerView with the new list of transactions
            transactionAdapter.setTransactions(transactions);
        });


        // Load transactions from SharedPreferences
        loadTransactions();

        return root;
    }

    private void loadTransactions() {
        // Retrieve data from SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("transactions", Context.MODE_PRIVATE);
        String allTransactions = preferences.getString("allTransactions", "");

        if (!allTransactions.isEmpty()) {
            // Process and display transactions
            String[] transactions = allTransactions.split(";");
            List<Transaction> transactionList = new ArrayList<>();
            for (String transaction : transactions) {
                // Ensure transaction data has the correct number of parts
                String[] details = transaction.split(",");
                if (details.length == 5) {
                    String transactionType = details[0];
                    String category = details[1];
                    double amount = 0;
                    try {
                        amount = Double.parseDouble(details[2]);
                    } catch (NumberFormatException e) {
                        Log.e("TransactionDebug", "Invalid amount format: " + details[2]);
                        continue;  // Skip this transaction if amount is invalid
                    }
                    String note = details[3];

                    // Parse the date from the timestamp (long)
                    long dateTimestamp = Long.parseLong(details[4]); // Get the timestamp
                    Date date = new Date(dateTimestamp); // Convert timestamp to Date object

                    // Create a Transaction object and add it to the list
                    transactionList.add(new Transaction(transactionType, category, amount, note, date));

                    // Log the loaded transaction data
                    Log.d("TransactionDebug", "Loaded transaction: " + category + ", " + amount + ", " + date);
                } else {
                    Log.e("TransactionDebug", "Invalid transaction data: " + transaction); // Log invalid data
                }
            }

            // Update the SharedViewModel with the loaded transactions
            sharedTransactionViewModel.setTransactionList(transactionList);

            // Add all transactions to RecyclerView
            transactionAdapter.setTransactions(transactionList);
        } else {
            Log.d("TransactionDebug", "No transactions found in SharedPreferences"); // Log if no transactions are found
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
