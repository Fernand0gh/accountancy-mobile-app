package com.example.myapplication.ui.panel1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
//Esta es la vista de bienvenida
public class BienvenidaModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BienvenidaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 1");
    }

    public LiveData<String> getText() {
        return mText;
    }
}