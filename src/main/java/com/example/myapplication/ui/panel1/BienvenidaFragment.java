package com.example.myapplication.ui.panel1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentBienvenidaBinding;
//Esto es el fragmento de la pantalla BIENVENIDA
public class BienvenidaFragment extends Fragment {

    private FragmentBienvenidaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BienvenidaModel homeViewModel =
                new ViewModelProvider(this).get(BienvenidaModel.class);

        binding = FragmentBienvenidaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}