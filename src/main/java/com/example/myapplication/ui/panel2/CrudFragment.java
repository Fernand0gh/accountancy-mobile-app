package com.example.myapplication.ui.panel2;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCrudBinding;

//Fragmento de la pantalla 2 CRUD
public class CrudFragment extends Fragment implements View.OnClickListener {
    private FragmentCrudBinding binding;
    private EditText txtCuenta, txtNombre, txtCargo, txtAbono;
    private Button btnRecuperar, btnGrabar, btnModificar, btnBorrar, btnConsultar, btnLimpiar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CrudModel galleryViewModel =
                new ViewModelProvider(this).get(CrudModel.class);

        binding = FragmentCrudBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Enlazar los componentes del xml a java
        txtCuenta = binding.txtCuenta;
        txtNombre = binding.txtNombre;
        txtCargo = binding.txtCargo;
        txtAbono = binding.txtAbono;

        btnRecuperar = binding.btnRecuperar;
        btnGrabar = binding.btnGrabar;
        btnModificar = binding.btnModificar;
        btnBorrar = binding.btnBorrar;
        btnConsultar = binding.btnConsultar;
        btnLimpiar = binding.btnLimpiar;

        btnRecuperar.setOnClickListener(this);
        btnGrabar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnRecuperar){
            recuperar();
            return;
        }
        if(v.getId() == R.id.btnGrabar){
            insertar();
            return;
        }
        if(v.getId() == R.id.btnBorrar){
            borrar();
            return;
        }
        if(v.getId() == R.id.btnLimpiar){
            txtCuenta.setText("");
            txtNombre.setText("");
            txtCargo.setText("");
            txtAbono.setText("");
            return;
        }
        if (v.getId() == R.id.btnModificar){
            actualizar();
            return;
        }
        if (v.getId() == R.id.btnConsultar){
            //Lanzar el fragmento 3 (Consulta)
            //Indicarle que se lazo desde el fragmento 2
            Bundle bundle = new Bundle();
            bundle.putString("fragmentoLanza", "CRUD_CUENTAS");
            bundle.putString("cuenta", txtCuenta.getText().toString());

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_3, bundle);
        }

    }
    public static boolean validarExistencia(String cuenta){
        Cursor cursor = MainActivity.getBD().rawQuery("SELECT * FROM CATALOGO WHERE CUENTA =" + cuenta, null);
        return cursor.moveToFirst();
    }
    private void recuperar(){
        String cuenta = txtCuenta.getText().toString();
        String query = "SELECT * FROM CATALOGO WHERE CUENTA =" + cuenta;
        Cursor resultado = MainActivity.getBD().rawQuery(query, null);
        if(resultado.moveToFirst()){
            txtNombre.setText(resultado.getString(1));
            txtCargo.setText(resultado.getInt(2) + "");
            txtAbono.setText(resultado.getInt(3) + "");
        }else{
            Toast.makeText(getActivity(), "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
        }
    }
    private void insertar(){
        String cuenta = txtCuenta.getText().toString();
        String nombre = txtNombre.getText().toString();
        //Validar que no exista la cuenta
        if(validarExistencia(cuenta)){
            Toast.makeText(getActivity(), "La cuenta ya existe", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validar datos no vacios
        if(cuenta.isEmpty() || nombre.isEmpty() || txtCargo.getText().toString().isEmpty()
                || txtAbono.getText().toString().isEmpty() ){
            Toast.makeText(getActivity(), "Datos incompletos", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validar que longitud de cuenta
        if(cuenta.length() != 6 || cuenta.substring(0, 2).equals("00")){
            Toast.makeText(getActivity(), "Cuenta no Válida", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validar que este en ceros
        int cargo = Integer.parseInt(txtCargo.getText().toString());
        int abono = Integer.parseInt(txtAbono.getText().toString());
        if(cargo != 0 || abono != 0){
            Toast.makeText(getActivity(), "La cuenta debe crearse en ceros", Toast.LENGTH_SHORT).show();
            return;
        }
        int nivel = 0;
        if(!cuenta.substring(0, 2).equals("00")){
            nivel = 1;
        }
        if(!cuenta.substring(2, 4).equals("00")){
            nivel = 2;
        }
        if(!cuenta.substring(4, 6).equals("00")){
            nivel = 3;
        }

        //Validar que exista la cuenta padre
        if(nivel > 1){
            String cuentaPadre = "";
            if(nivel == 2){
                cuentaPadre = cuenta.substring(0, 2);
            }else if(nivel == 3){
                cuentaPadre = cuenta.substring(0, 4);
            }
            System.out.println("Cuenta padre: " + cuentaPadre);
            Cursor cursor = MainActivity.getBD().rawQuery("SELECT * FROM CATALOGO WHERE CUENTA LIKE '" + cuentaPadre + "%'", null);

            if(!cursor.moveToFirst()){
                Toast.makeText(getActivity(), "La cuenta padre no existe", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        System.out.println("Nivel: " + nivel);
        ContentValues valoresInsercion = new ContentValues();
        valoresInsercion.put("CUENTA", cuenta);
        valoresInsercion.put("NOMBRE", nombre);
        valoresInsercion.put("CARGO", cargo);
        valoresInsercion.put("ABONO", abono);
        valoresInsercion.put("NIVEL", nivel);
        try {
            MainActivity.getBD().insert("CATALOGO", null, valoresInsercion);
            Toast.makeText(getActivity(), "Grabado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al grabar", Toast.LENGTH_SHORT).show();
        }
    }

    private void borrar(){
//        MainActivity.getBD().execSQL("DELETE FROM CATALOGO");
//        Toast.makeText(getActivity(), "Borrado", Toast.LENGTH_SHORT).show();
        try{
            String cuenta = txtCuenta.getText().toString();
            if(!validarExistencia(cuenta)){
                Toast.makeText(getActivity(), "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            //Validar que no tenga subcuentas
            Cursor cursorSubcuentas = MainActivity.getBD().rawQuery("SELECT NIVEL FROM CATALOGO WHERE CUENTA = " + cuenta, null);
            cursorSubcuentas.moveToFirst();
            int nivel = cursorSubcuentas.getInt(0);
            System.out.println("Nivel: " + nivel);
            if(nivel < 3) {
                String cuentaPadre = "";
                if(nivel == 1){
                    cuentaPadre = cuenta.substring(0, 2);
                }else if(nivel == 2){
                    cuentaPadre = cuenta.substring(0, 4);
                }
                String query = "SELECT * FROM CATALOGO WHERE CUENTA LIKE '" + cuentaPadre + "%' AND CUENTA != " + cuenta;
                cursorSubcuentas = MainActivity.getBD().rawQuery(query, null);
                if(cursorSubcuentas.moveToFirst()){
                    Toast.makeText(getActivity(), "No se puede borrar la cuenta porque tiene subcuentas", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //Validar que no tenga movimientos
            Cursor cursorMovimientos = MainActivity.getBD().rawQuery("SELECT CARGO, ABONO FROM CATALOGO WHERE CUENTA ='" + cuenta + "'", null);
            cursorMovimientos.moveToFirst();
            int cargo = cursorMovimientos.getInt(0);
            int abono = cursorMovimientos.getInt(1);
            System.out.println("Cargo: " + cargo + " Abono: " + abono);
            if(cargo != 0 || abono != 0){
                Toast.makeText(getActivity(), "No se puede borrar la cuenta porque tiene movimientos", Toast.LENGTH_SHORT).show();
                return;
            }
            MainActivity.getBD().execSQL("DELETE FROM CATALOGO WHERE CUENTA =" + cuenta);

            txtCuenta.setText("");
            txtNombre.setText("");
            txtCargo.setText("");
            txtAbono.setText("");

            Toast.makeText(getActivity(), "Borrado con éxito", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Error al borrar", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }
    private void actualizar(){
        String cuenta = txtCuenta.getText().toString();
        String nombre = txtNombre.getText().toString();

        if (cuenta.isEmpty() || nombre.isEmpty()){
            Toast.makeText(getActivity(), "Datos incompletos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarExistencia(cuenta)){
            Toast.makeText(getActivity(), "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues valoresActualizacion = new ContentValues();
        valoresActualizacion.put("NOMBRE", nombre);
        try {
            MainActivity.getBD().update("CATALOGO", valoresActualizacion, "CUENTA=" + cuenta, null);
            Toast.makeText(getActivity(), "Actualizado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }
}