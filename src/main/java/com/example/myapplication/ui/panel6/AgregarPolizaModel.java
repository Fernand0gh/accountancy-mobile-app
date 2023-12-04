package com.example.myapplication.ui.panel6;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AgregarPolizaModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AgregarPolizaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 1");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
