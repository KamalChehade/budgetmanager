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
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();

     public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

     public LiveData<String> getSelectedDate() {
        return selectedDate;
    }


    public void addTransaction(Transaction transaction) {
        List<Transaction> currentList = transactionList.getValue();
        if (currentList != null) {
            currentList.add(transaction);
            transactionList.setValue(currentList);
        }
    }


    public void setTransactionList(List<Transaction> transactions) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactionList.setValue(transactions);
    }


     public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

     public List<Transaction> filterTransactionsByDate(List<Transaction> transactions, String selectedDate) {
        List<Transaction> filteredTransactions = new ArrayList<>();

         if (selectedDate != null) {
            boolean isMonthly = selectedDate.split(" ").length == 2;
            for (Transaction transaction : transactions) {
                String transactionDate = formatDate(transaction.getDate(), isMonthly);
                if (transactionDate.equals(selectedDate)) {
                    filteredTransactions.add(transaction);
                }
            }
        }

        return filteredTransactions;
    }

     private String formatDate(Date date, boolean isMonthly) {
        SimpleDateFormat sdf;
        if (isMonthly) {
            sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        }
        return sdf.format(date);
    }
}
