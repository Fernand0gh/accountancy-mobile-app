package com.example.myapplication.ui.panel5;
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
import com.example.myapplication.Poliza;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRecycler2Binding;
import com.example.myapplication.ui.panel3.RecyclerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

public class RecyclerPolizaFragment extends Fragment {
    private @NonNull FragmentRecycler2Binding binding;
    private RecyclerView recyclerView;
    private ArrayList<Poliza> polizas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerModel slideshowViewModel =
                new ViewModelProvider(this).get(RecyclerModel.class);

        binding = FragmentRecycler2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView2;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerPolizaFragment.AdaptadorPoliza());

        polizas = new ArrayList<Poliza>(5);

        Bundle bundle = getArguments();
        String poliza = bundle.getString("poliza");

        try{
            Cursor cursor = MainActivity.getBD().rawQuery("SELECT * FROM POLIZAS WHERE POLIZA = " + poliza, null);
            int i = 0;
            while (cursor.moveToNext()){
                String fecha = cursor.getString(1);
                String cuenta = cursor.getString(2);
                int tipo = cursor.getInt(3);
                int importe = cursor.getInt(4);
                polizas.add(new Poliza(poliza, fecha, cuenta, tipo, importe));
                //System.out.println(polizas[i].getPoliza() + " " + polizas[i].getFecha() + " " + polizas[i].getCuenta() + " " + polizas[i].getTipo() + " " + polizas[i].getImporte());
                i++;
            }
        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al buscar la poliza", Toast.LENGTH_SHORT).show();
        }

        System.out.println("HOLA HOLA HOLA");
        System.out.println(polizas.size());
        for (Poliza p: polizas){
            System.out.println(p.toString());
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class AdaptadorPoliza extends RecyclerView.Adapter<RecyclerPolizaFragment.AdaptadorPoliza.AdaptadorPolizaHolder>{
        @NonNull
        @Override
        public RecyclerPolizaFragment.AdaptadorPoliza.AdaptadorPolizaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerPolizaFragment.AdaptadorPoliza.AdaptadorPolizaHolder(getLayoutInflater().inflate(R.layout.layout_tarjeta_poliza, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerPolizaFragment.AdaptadorPoliza.AdaptadorPolizaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return polizas.size();
        }

        class AdaptadorPolizaHolder extends RecyclerView.ViewHolder{
            ImageView imgTipo;
            TextView tvPoliza, tvFecha, tvCuenta, tvMovimiento, tvImporte;
            public AdaptadorPolizaHolder(@NonNull View itemView) {
                super(itemView);
                imgTipo = itemView.findViewById(R.id.imgTipo);
                tvCuenta = itemView.findViewById(R.id.tvCuentaPoliza);
                tvPoliza = itemView.findViewById(R.id.tvPoliza);
                tvFecha = itemView.findViewById(R.id.tvFecha);
                tvMovimiento = itemView.findViewById(R.id.tvMovimiento);
                tvImporte = itemView.findViewById(R.id.tvImporte);
            }
            public void imprimir(int position){
                if (polizas.get(position).getTipo() == 1){
                    imgTipo.setImageResource(R.drawable.cargo);
                    tvMovimiento.setText("Cargo");
                }else{
                    imgTipo.setImageResource(R.drawable.abono);
                    tvMovimiento.setText("Abono");
                }
                tvPoliza.setText(polizas.get(position).getPoliza());
                tvFecha.setText(polizas.get(position).getFecha());
                tvCuenta.setText(polizas.get(position).getCuenta());
                tvImporte.setText(polizas.get(position).getImporte() + "");
            }
        }
    }
}
