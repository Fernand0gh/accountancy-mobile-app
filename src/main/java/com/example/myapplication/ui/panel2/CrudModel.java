package com.example.myapplication.ui.panel2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
//Vista del crud
public class CrudModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CrudModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 2");
    }

    public LiveData<String> getText() {
        return mText;
    }
}