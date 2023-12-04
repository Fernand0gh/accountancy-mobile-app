package com.example.myapplication.ui.panel4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
//Esta es la vista del CRUD de Cuentas
public class PregConsultaModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PregConsultaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 1");
    }

    public LiveData<String> getText() {
        return mText;
    }
}