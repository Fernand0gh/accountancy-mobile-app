package com.example.myapplication.ui.panel6;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.databinding.FragmentRegPolizaBinding;
import com.example.myapplication.ui.panel2.CrudFragment;
import com.example.myapplication.ui.panel3.RecyclerModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgregarPolizaFragment extends Fragment implements View.OnClickListener{

    private @NonNull FragmentRegPolizaBinding binding;
    private EditText txtPoliza;
    private EditText[] txtCuentas;
    private EditText[] txtCargos;
    private EditText[] txtAbonos;
    private Button btnGrabarPoliza;
    private Calendar calendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerModel slideshowViewModel =
                new ViewModelProvider(this).get(RecyclerModel.class);

        binding = FragmentRegPolizaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtCuentas = new EditText[5];
        txtCargos = new EditText[5];
        txtAbonos = new EditText[5];

        txtPoliza = binding.txtPoliza;

        txtCuentas[0] = binding.txtCuenta1;
        txtCuentas[1] = binding.txtCuenta2;
        txtCuentas[2] = binding.txtCuenta3;
        txtCuentas[3] = binding.txtCuenta4;
        txtCuentas[4] = binding.txtCuenta5;
        txtCargos[0] = binding.txtCargo1;
        txtCargos[1] = binding.txtCargo2;
        txtCargos[2] = binding.txtCargo3;
        txtCargos[3] = binding.txtCargo4;
        txtCargos[4] = binding.txtCargo5;
        txtAbonos[0] = binding.txtAbono1;
        txtAbonos[1] = binding.txtAbono2;
        txtAbonos[2] = binding.txtAbono3;
        txtAbonos[3] = binding.txtAbono4;
        txtAbonos[4] = binding.txtAbono5;

        btnGrabarPoliza = binding.btnGrabarPoliza;
        btnGrabarPoliza.setOnClickListener(this);

        calendar = Calendar.getInstance();

        //BORARR ESTO
        Cursor cursor = MainActivity.getBD().rawQuery("SELECT * FROM POLIZAS", null);
        while (cursor.moveToNext()){
            System.out.println("Poliza: " + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInt(3) + " " + cursor.getInt(4));
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnGrabarPoliza.getId()) {
            String poliza = binding.txtPoliza.getText().toString();
            String cuentas[] = new String[5];
            int importes[] = new int[5];
            ArrayList<Integer> movimientos = new ArrayList<>(5);
            if(poliza.isEmpty()){
                Toast.makeText(getContext(), "Ingrese una póliza", Toast.LENGTH_SHORT).show();
                return;
            }
            if (validarExistenciaPoliza(poliza)) {
                Toast.makeText(getContext(), "La póliza ya existe", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < 4; i++){
                if(!txtCuentas[i].getText().toString().isEmpty()){
                    System.out.println("Cuenta: " + txtCuentas[i].getText().toString());
                    if (!CrudFragment.validarExistencia(txtCuentas[i].getText().toString())) {
                        Toast.makeText(getContext(), "La cuenta " + txtCuentas[i].getText().toString() + " no existe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (txtCuentas[i].getText().toString().substring(4, 6).equals("00")) {
                        Toast.makeText(getContext(), txtCuentas[i].getText().toString() + " no es una subsubcuenta", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (txtCargos[i].getText().toString().isEmpty() && txtAbonos[i].getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Ingrese un cargo o un abono para cada cuenta", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!txtCargos[i].getText().toString().isEmpty() && !txtAbonos[i].getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Un movimiento solo puede ser cargo o abono", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cuentas[i] = txtCuentas[i].getText().toString();
                    if (!txtCargos[i].getText().toString().isEmpty()) {
                        movimientos.add(1);
                    }
                    if (!txtAbonos[i].getText().toString().isEmpty()) {
                        movimientos.add(2);
                    }
                }
            }
            System.out.println("Movimientos :" + movimientos.size());
            if (movimientos.size() < 2) {
                Toast.makeText(getContext(), "Ingrese al menos dos movimientos", Toast.LENGTH_SHORT).show();
                return;
            }
            int cargos = 0;
            int abonos = 0;
            for(int i = 0; i < 4; i++){
                if (!txtCargos[i].getText().toString().isEmpty()) {
                    cargos += Integer.parseInt(txtCargos[i].getText().toString());
                    importes[i] = Integer.parseInt(txtCargos[i].getText().toString());
                }
                if (!txtAbonos[i].getText().toString().isEmpty()) {
                    abonos += Integer.parseInt(txtAbonos[i].getText().toString());
                    importes[i] = Integer.parseInt(txtAbonos[i].getText().toString());
                }
            }
            if (cargos != abonos) {
                Toast.makeText(getContext(), "La póliza no está cuadrada", Toast.LENGTH_SHORT).show();
                return;
            }

            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = simpleDateFormat.format(date);

            for (int i = 0; i < movimientos.size(); i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put("POLIZA", poliza);
                contentValues.put("FECHA", fecha);
                contentValues.put("CUENTA", cuentas[i]);
                contentValues.put("TIPOMOVTO", movimientos.get(i));
                contentValues.put("IMPORTE", importes[i]);
                MainActivity.getBD().insert("POLIZAS", null, contentValues);

                actualizarSaldoCuenta(cuentas[i], importes[i], movimientos.get(i));
                String cuentaPadre = cuentas[i].substring(0, 4) + "00";
                actualizarSaldoCuenta(cuentaPadre, importes[i], movimientos.get(i));
                String cuentaAbuelo = cuentaPadre.substring(0, 2) + "0000";
                actualizarSaldoCuenta(cuentaAbuelo, importes[i], movimientos.get(i));
            }
        }
    }

    private void actualizarSaldoCuenta(String cuenta, int importe, int movimiento){
        System.out.println("Se intenta checar la cuenta " + cuenta);
        Cursor cursor = MainActivity.getBD().rawQuery("SELECT CARGO, ABONO FROM CATALOGO WHERE cuenta = " + cuenta, null);
        cursor.moveToFirst();
        int cargo = cursor.getInt(0);
        int abono = cursor.getInt(1);
        ContentValues contentValues = new ContentValues();
        if (movimiento == 1) {
            cargo += importe;
            contentValues.put("CARGO", cargo);
        } else {
            abono += importe;
            contentValues.put("ABONO", abono);
        }
        MainActivity.getBD().update("CATALOGO", contentValues, "CUENTA = " + cuenta, null);
    }
    private boolean validarExistenciaPoliza(String poliza){
        Cursor cursor = MainActivity.getBD().rawQuery("SELECT * FROM POLIZAS WHERE poliza = " + poliza, null);
        return cursor.moveToFirst();
    }
}
