package com.example.myapplication.ui.panel5;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecyclerPolizaModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public RecyclerPolizaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prueba 1");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
