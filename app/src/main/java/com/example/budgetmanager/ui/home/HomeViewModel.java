package com.example.budgetmanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Double> income;
    private final MutableLiveData<Double> expense;
    private final MutableLiveData<Double> total;

    public HomeViewModel() {
        income = new MutableLiveData<>(0.0);
        expense = new MutableLiveData<>(0.0);
        total = new MutableLiveData<>(0.0);
    }

    public LiveData<Double> getIncome() {
        return income;
    }

    public LiveData<Double> getExpense() {
        return expense;
    }

    public LiveData<Double> getTotal() {
        return total;
    }

    public void updateIncome(double value) {
        income.setValue(value);
        calculateTotal();
    }

    public void updateExpense(double value) {
        expense.setValue(value);
        calculateTotal();
    }

    private void calculateTotal() {
        double currentIncome = income.getValue() != null ? income.getValue() : 0.0;
        double currentExpense = expense.getValue() != null ? expense.getValue() : 0.0;
        total.setValue(currentIncome - currentExpense);
    }
}
