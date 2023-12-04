package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {
    public static int version = 2;
    private String crearCATALOGO = "CREATE TABLE CATALOGO(CUENTA TEXT PRIMARY KEY, NOMBRE TEXT, CARGO INTEGER, ABONO INTEGER, NIVEL INTEGER)";
    private String crearPOLIZAS = "CREATE TABLE POLIZAS(POLIZA TEXT, FECHA TEXT, CUENTA TEXT, TIPOMOVTO INTEGER, IMPORTE INTEGER, PRIMARY KEY(POLIZA, CUENTA), FOREIGN KEY (CUENTA) REFERENCES CATALOGO(CUENTA))";
    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crearCATALOGO);
        db.execSQL(crearPOLIZAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CATALOGO");
        db.execSQL("DROP TABLE IF EXISTS POLIZAS");
    }
}
