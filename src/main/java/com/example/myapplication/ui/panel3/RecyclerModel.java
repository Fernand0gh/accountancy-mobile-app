package com.example.myapplication.ui.panel3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
//Vista de Recycler View Cuentas
public class RecyclerModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RecyclerModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 3");
    }

    public LiveData<String> getText() {
        return mText;
    }
}