package com.example.myapplication;

public class Cuenta implements Comparable<Cuenta> {
    private String cuenta;
    private String nombre;
    private int cargo;
    private int abono;
    private int nivel;

    public Cuenta(String cuenta, String nombre, int cargo, int abono, int nivel) {
        this.cuenta = cuenta;
        this.nombre = nombre;
        this.cargo = cargo;
        this.abono = abono;
        this.nivel = nivel;
    }

    public String getCuenta() {
        return cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCargo() {
        return cargo;
    }

    public int getAbono() {
        return abono;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public int compareTo(Cuenta o) {
        return this.cuenta.compareTo(o.getCuenta());
    }
}
