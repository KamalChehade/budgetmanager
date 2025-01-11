package com.example.budgetmanager.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> title;

    public ProfileViewModel() {
        title = new MutableLiveData<>();
        title.setValue("Profile");
    }

    public LiveData<String> getTitle() {
        return title;
    }
}
