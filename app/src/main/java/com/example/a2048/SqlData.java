package com.example.a2048;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlData extends SQLiteOpenHelper {

    private static final String DB_NAME = "GameDataBase";
    private static final int DB_VERSION = 1;

    public SqlData(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS usuario ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT, 'usuario' TEXT, 'contra' TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS data2048 ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'user_id' INTEGER,'dimension' INTEGER, 'puntos' INTEGER ,FOREIGN KEY (user_id) REFERENCES usuario(id))");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS datalightout ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'user_id' INTEGER,'dimension' INTEGER, 'pasos_restante' INTEGER, 'tiempo_restante' INTEGER ,FOREIGN KEY (user_id) REFERENCES usuario(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addUsuario(String usuario,String contra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", usuario);
        contentValues.put("contra", contra);
        db.insert("usuario", null, contentValues);
        db.close();
    }

    @SuppressLint("Range")
    public Usuario getUsuario(String username) {
        String [] arg= {username};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from usuario where usuario = ? ",arg);
        Usuario u = new Usuario();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            u.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
            u.setContra(cursor.getString(cursor.getColumnIndex("contra")));
        }
        db.close();
        return u;
    }

}
