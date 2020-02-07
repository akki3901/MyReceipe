package com.webtomob.myrecipe.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.webtomob.myrecipe.model.Recipe;

public class AddRecipeViewModel extends ViewModel {
    private MutableLiveData<Recipe> userMutableLiveData;

    public MutableLiveData<Recipe> getRecipeData() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        Recipe recipe = new Recipe("", "", "", "", "45 mins", "");

        userMutableLiveData.setValue(recipe);

    }

}
