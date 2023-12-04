package com.example.myapplication;

public class Poliza implements Comparable<Poliza>{
    private String poliza, fecha, cuenta;
    private int tipo, importe;
    public Poliza(String poliza, String fecha, String cuenta, int tipo, int importe){
        this.poliza = poliza;
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.tipo = tipo;
        this.importe = importe;
    }
    public String getPoliza() {
        return poliza;
    }
    public String getFecha() {
        return fecha;
    }
    public String getCuenta() {
        return cuenta;
    }
    public int getTipo() {
        return tipo;
    }
    public int getImporte() {
        return importe;
    }
    public String toString(){
        return "Poliza " + poliza + " Fecha " + fecha + " Cuenta " + cuenta + " Tipo " + tipo + " Importe " + importe;
    }
    @Override
    public int compareTo(Poliza o) {
        return this.poliza.compareTo(o.poliza);
    }
}
