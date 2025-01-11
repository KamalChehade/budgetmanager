package com.example.budgetmanager.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.budgetmanager.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SharedTransactionViewModel extends ViewModel {

    private final MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>(); // Format: "MMMM dd, yyyy" for daily or "MMMM yyyy" for monthly

    // LiveData for the transaction list
    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    // LiveData for the selected date
    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        List<Transaction> currentList = transactionList.getValue();
        if (currentList != null) {
            currentList.add(transaction);
            transactionList.setValue(currentList); // Update the transaction list
        }
    }

    // Set the transaction list from SharedPreferences or any other data source
    public void setTransactionList(List<Transaction> transactions) {
        transactionList.setValue(transactions); // Update the transaction list with the new data
    }

    // Set the selected date (either full date or month)
    public void setSelectedDate(String date) {
        selectedDate.setValue(date); // Update the selected date
    }

    // Filter transactions based on the selected date
    public List<Transaction> filterTransactionsByDate(List<Transaction> transactions, String selectedDate) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        // Check if selectedDate contains the full date or just month and year
        if (selectedDate != null) {
            boolean isMonthly = selectedDate.split(" ").length == 2;  // Check if it contains only month and year ("MMMM yyyy")
            for (Transaction transaction : transactions) {
                String transactionDate = formatDate(transaction.getDate(), isMonthly);
                if (transactionDate.equals(selectedDate)) {
                    filteredTransactions.add(transaction);
                }
            }
        }

        return filteredTransactions;
    }

    // Helper method to format date as either "MMMM dd, yyyy" (daily) or "MMMM yyyy" (monthly)
    private String formatDate(Date date, boolean isMonthly) {
        SimpleDateFormat sdf;
        if (isMonthly) {
            sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());  // Format as "MMMM yyyy"
        } else {
            sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());  // Format as "MMMM dd, yyyy"
        }
        return sdf.format(date); // Convert Date to the required format
    }
}
