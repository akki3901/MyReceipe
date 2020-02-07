package com.webtomob.myreceipe.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.webtomob.myreceipe.model.Receipe;

public class AddReceipeViewModel extends ViewModel {
    private MutableLiveData<Receipe> userMutableLiveData;

    public MutableLiveData<Receipe> getReceipeData() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        Receipe receipe = new Receipe("", "", "", "", "45 mins", "");

        userMutableLiveData.setValue(receipe);

    }

}
