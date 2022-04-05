package com.cst2335.finalproject.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Know more about our APP" +
                "                       This application is the final project for the CST2335 Course");
    }

    public LiveData<String> getText() {
        return mText;
    }
}