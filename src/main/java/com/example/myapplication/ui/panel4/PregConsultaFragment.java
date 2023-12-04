package com.example.myapplication.ui.panel4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentPregconsultaBinding;

//Esto es el fragmento de la pantalla 1 (CRUD de Cuentas)
public class PregConsultaFragment extends Fragment implements View.OnClickListener {
    private Button btnCuenta, btnPoliza;
    private TextView txtID;
    private FragmentPregconsultaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PregConsultaModel homeViewModel =
                new ViewModelProvider(this).get(PregConsultaModel.class);

        binding = FragmentPregconsultaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnCuenta = binding.btnCuenta;
        btnPoliza = binding.btnPoliza;
        txtID = binding.txtID;
        btnCuenta.setOnClickListener(this);
        btnPoliza.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (txtID.getText().toString().equals("")){
            Toast.makeText(getContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
            return;
        }
        if(v.getId() == btnCuenta.getId()){
            if (txtID.getText().toString().length() != 6){
                Toast.makeText(getContext(), "Ingrese una cuenta valida", Toast.LENGTH_SHORT).show();
                return;
            }
            //Lanzar la pantalla de consulta de cuentas
            Bundle bundle = new Bundle();
            bundle.putString("fragmentoLanza", "PREGUNTA_CONSULTA");
            bundle.putString("cuenta", txtID.getText().toString());

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_3, bundle);
            return;
        }
        if(v.getId() == btnPoliza.getId()){
            //Lanzar la pantalla de consulta de cuentas
            Bundle bundle = new Bundle();
            bundle.putString("poliza", txtID.getText().toString());

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_5, bundle);
            return;
        }
    }
}