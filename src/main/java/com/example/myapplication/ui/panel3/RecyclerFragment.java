package com.example.myapplication.ui.panel3;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRecyclerBinding;
import com.example.myapplication.Cuenta;

import java.util.ArrayList;
import java.util.Collections;

//FRAGMENTO DE LA PANTALLA DE CONSULTAS DE TODAS LAS CUENTAS
public class RecyclerFragment extends Fragment{
    private FragmentRecyclerBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Cuenta> cuentas;
    private String fragementoLanza;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerModel slideshowViewModel =
                new ViewModelProvider(this).get(RecyclerModel.class);

        binding = FragmentRecyclerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new AdaptadorCuenta());

        cuentas = new ArrayList<Cuenta>();
        //Saber desde donde se lanzo esta actividad
        //Si viene desde el fragmento 2 (CRUD) solo busca una cuenta
        //Si viene desde el fragmento 4 (Consultas) busca la cuenta y sus hijos
        Bundle bundle = getArguments();
        Cursor cursor = null;
        if (bundle != null) {
            fragementoLanza = bundle.getString("fragmentoLanza");
            String cuenta = bundle.getString("cuenta");
            System.out.println("Cuenta recibida en el bundle " + cuenta);
            if (cuenta.isEmpty()){
                cursor = MainActivity.getBD().rawQuery("SELECT * FROM CATALOGO", null);
            }else{
                cursor = MainActivity.getBD().rawQuery("SELECT * FROM CATALOGO WHERE CUENTA = " + cuenta, null);
            }
            if (fragementoLanza.equals("PREGUNTA_CONSULTA")){ //Se lanza desde la pestana de consultas
                try {
                    cursor.moveToFirst();
                    int nivel = cursor.getInt(4);
                    String padre = "";
                    if(nivel == 1){
                        padre = cuenta.substring(0, 2);
                    }
                    if(nivel == 2){
                        padre = cuenta.substring(0, 4);
                    }
                    if(nivel == 3){
                        padre = cuenta.substring(0, 6);
                    }
                    System.out.println("PADRE: " + padre);
                    cursor = MainActivity.getBD().rawQuery("SELECT * FROM CATALOGO WHERE CUENTA LIKE '" + padre + "%'", null);
                }catch (SQLiteException e){
                    Toast.makeText(getContext(), "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
                    return root;
                }
            }
        }

        while(cursor.moveToNext()){
            String cuenta = cursor.getString(0);
            String nombre = cursor.getString(1);
            int cargo = cursor.getInt(2);
            int abono = cursor.getInt(3);
            int nivel = cursor.getInt(4);
            cuentas.add(new Cuenta(cuenta, nombre, cargo, abono, nivel));
        }
        Collections.sort(cuentas);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class AdaptadorCuenta extends RecyclerView.Adapter<AdaptadorCuenta.AdaptadorCuentaHolder>{
        @NonNull
        @Override
        public AdaptadorCuentaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorCuentaHolder(getLayoutInflater().inflate(R.layout.layout_tarjeta, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorCuentaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return cuentas.size();
        }

        class AdaptadorCuentaHolder extends RecyclerView.ViewHolder{
            ImageView imgNivel;
            TextView txtCuenta;
            TextView txtNombre;
            TextView txtCargo;
            TextView txtAbono;
            public AdaptadorCuentaHolder(@NonNull View itemView) {
                super(itemView);
                imgNivel = itemView.findViewById(R.id.imgNivel);
                txtCuenta = itemView.findViewById(R.id.tvCuenta);
                txtNombre = itemView.findViewById(R.id.tvNombre);
                txtCargo = itemView.findViewById(R.id.tvCargo);
                txtAbono = itemView.findViewById(R.id.tvAbono);
            }
            public void imprimir(int position){
                txtCuenta.setText(cuentas.get(position).getCuenta());
                txtNombre.setText(cuentas.get(position).getNombre());
                txtCargo.setText("" + cuentas.get(position).getCargo());
                txtAbono.setText("" + cuentas.get(position).getAbono());
                int nivel = cuentas.get(position).getNivel();
                int margenCarta = 0;
                switch(nivel){
                    case 1:
                        imgNivel.setImageResource(R.drawable.numero1);
                        break;
                    case 2:
                        imgNivel.setImageResource(R.drawable.numero2);
                        margenCarta = 125;
                        break;
                    case 3:
                        imgNivel.setImageResource(R.drawable.numero3);
                        margenCarta = 225;
                        break;
                    default:
                        break;
                }
                if (getItemCount() > 1){
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
                    layoutParams.leftMargin = margenCarta;
                    itemView.setLayoutParams(layoutParams);
                }
            }
        }
    }
}