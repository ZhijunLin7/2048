package com.example.a2048;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    // Datos de usuario
    public void insertUsuario(String usuario, String contra) {
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
            u.setId(cursor.getInt(cursor.getColumnIndex("id")));
            u.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
            u.setContra(cursor.getString(cursor.getColumnIndex("contra")));
        }
        db.close();
        return u;
    }

    // Datos de lightout
    public void adddatalightout(String usuario,int dimension,int pasos_restante, int tiempo_restante) {
        ContentValues contentValues = new ContentValues();
        Usuario u =this.getUsuario(usuario);
        Datalightout datalightout = getDatalightoutById(u.getId(),dimension);
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updata = false;
        if (datalightout.getUser_id() == 0) {
            contentValues.put("user_id", u.getId());
            contentValues.put("dimension", dimension);
            contentValues.put("pasos_restante", pasos_restante);
            contentValues.put("tiempo_restante", tiempo_restante);
            db.insert("datalightout", null, contentValues);
        }else {
            if (pasos_restante > datalightout.getPasos_restante() ) {
                contentValues.put("pasos_restante", pasos_restante);
                updata=true;
            }
            if (tiempo_restante > datalightout.getTiempo_restante()) {
                contentValues.put("tiempo_restante", tiempo_restante);
                updata=true;
            }
            if (updata) {
                String [] arg= {String.valueOf(u.getId()), String.valueOf(dimension)};
                db.update("datalightout", contentValues, "user_id=? and dimension = ?" , arg);
            }
        }
        db.close();
    }

    @SuppressLint("Range")
    public Datalightout getDatalightoutById(int id, int dimension) {
        String [] arg= {String.valueOf(id), String.valueOf(dimension)};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from datalightout where user_id = ? and dimension = ? ",arg);
        Datalightout datalightout = new Datalightout();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            datalightout.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            datalightout.setDimension(cursor.getInt(cursor.getColumnIndex("dimension")));
            datalightout.setPasos_restante(cursor.getInt(cursor.getColumnIndex("pasos_restante")));
            datalightout.setTiempo_restante(cursor.getInt(cursor.getColumnIndex("tiempo_restante")));
        }
        db.close();
        return datalightout;
    }

    public long countdatalightout(int dimension){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            db = getReadableDatabase();
        }
        String [] arg= {String.valueOf(dimension)};
        return DatabaseUtils.queryNumEntries(db, "datalightout","dimension= ?",arg);
    }

    @SuppressLint("Range")
    public Datalightout rankinglightout(int position,int dimension) {
        String query = "SELECT  * FROM  datalightout INNER JOIN usuario ON usuario.id=datalightout.user_id where dimension= "+dimension+" ORDER BY pasos_restante ,tiempo_restante DESC " + "LIMIT " + position + ",1";

        Cursor cursor = null;
        Datalightout entry = new Datalightout();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                entry.setName(cursor.getString(cursor.getColumnIndex("usuario")));
                entry.setDimension(cursor.getInt(cursor.getColumnIndex("dimension")));
                entry.setPasos_restante(cursor.getInt(cursor.getColumnIndex("pasos_restante")));
                entry.setTiempo_restante(cursor.getInt(cursor.getColumnIndex("tiempo_restante")));
                cursor.close();
            }
        } catch (Exception e) {
            Log.d("test", "QUERY EXCEPTION! " + e.getMessage());
        } finally {

            return entry;
        }
    }

    // Datos de 2048
    public void adddata2048(String usuario,int dimension,int puntos) {
        ContentValues contentValues = new ContentValues();
        Usuario u =this.getUsuario(usuario);
        Data2048 data2048 = getData2048ById(u.getId(),dimension);
        SQLiteDatabase db = this.getWritableDatabase();
        if (data2048.getUser() == 0) {
            contentValues.put("user_id", u.getId());
            contentValues.put("dimension", dimension);
            contentValues.put("puntos", puntos);
            db.insert("data2048", null, contentValues);
        }else {
            if (puntos > data2048.getPuntos() ) {
                contentValues.put("puntos", puntos);

                String [] arg= {String.valueOf(u.getId()), String.valueOf(dimension)};
                db.update("data2048", contentValues, "user_id=? and dimension = ?" , arg);
            }

        }
        db.close();
    }

    @SuppressLint("Range")
    public Data2048 getData2048ById(int id, int dimension) {
        String [] arg= {String.valueOf(id), String.valueOf(dimension)};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from data2048 where user_id = ? and dimension = ? ",arg);
        Data2048 data2048 = new Data2048();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            data2048.setUser(cursor.getInt(cursor.getColumnIndex("user_id")));
            data2048.setDimension(cursor.getInt(cursor.getColumnIndex("dimension")));
            data2048.setPuntos(cursor.getInt(cursor.getColumnIndex("puntos")));
        }
        db.close();
        return data2048;
    }

    @SuppressLint("Range")
    public String getMaxpuntosByname(String usuario,int dimension) {
        Usuario u =this.getUsuario(usuario);
        String [] arg= {String.valueOf(u.getId()), String.valueOf(dimension)};
        String maxPuntos = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from data2048 where user_id = ? and dimension = ? ",arg);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            maxPuntos= cursor.getString(cursor.getColumnIndex("puntos"));
        }
        db.close();
        return maxPuntos;
    }

    public long countdata2048(int dimension){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            db = getReadableDatabase();
        }
        String [] arg= {String.valueOf(dimension)};
        return DatabaseUtils.queryNumEntries(db, "data2048","dimension= ?",arg);
    }

    @SuppressLint("Range")
    public Data2048 ranking2048(int position,int dimension) {
        String query = "SELECT  * FROM  data2048 INNER JOIN usuario ON usuario.id=data2048.user_id where dimension= "+dimension+" ORDER BY puntos DESC " + "LIMIT " + position + ",1";

        Cursor cursor = null;
        Data2048 entry = new Data2048();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                entry.setName(cursor.getString(cursor.getColumnIndex("usuario")));
                entry.setDimension(cursor.getInt(cursor.getColumnIndex("dimension")));
                entry.setPuntos(cursor.getInt(cursor.getColumnIndex("puntos")));
            }
        } catch (Exception e) {
            Log.d("test", "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }

}
