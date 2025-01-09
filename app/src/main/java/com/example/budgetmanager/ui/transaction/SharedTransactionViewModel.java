package com.example.budgetmanager.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.budgetmanager.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SharedTransactionViewModel extends ViewModel {

    private final MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    public void addTransaction(Transaction transaction) {
        List<Transaction> currentList = transactionList.getValue();
        if (currentList != null) {
            currentList.add(transaction);
            transactionList.setValue(currentList); // Update LiveData
        }
    }

    public void setTransactionList(List<Transaction> transactions) {
        transactionList.setValue(transactions); // Set new list from SharedPreferences
    }
}
