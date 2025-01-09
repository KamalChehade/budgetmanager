
package com.example.budgetmanager.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTransactionViewModel extends ViewModel {

    private final MutableLiveData<String> transactionType = new MutableLiveData<>();
    private final MutableLiveData<Double> amount = new MutableLiveData<>();
    private final MutableLiveData<String> note = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRecurring = new MutableLiveData<>();
    private final MutableLiveData<String> category = new MutableLiveData<>();



    public LiveData<String> getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String type) {
        transactionType.setValue(type);
    }
    public LiveData<String> getCategory() {
        return category;
    }

    public void setCategory(String categoryName) {
        category.setValue(categoryName);
    }
    public LiveData<Double> getAmount() {
        return amount;
    }

    public void setAmount(Double value) {
        amount.setValue(value);
    }

    public LiveData<String> getNote() {
        return note;
    }

    public void setNote(String noteText) {
        note.setValue(noteText);
    }

    public LiveData<Boolean> getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean recurring) {
        isRecurring.setValue(recurring);
    }
}