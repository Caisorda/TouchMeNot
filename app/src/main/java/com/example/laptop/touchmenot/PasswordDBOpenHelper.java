package com.example.laptop.touchmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bryan on 13/03/2016.
 */
public class PasswordDBOpenHelper extends SQLiteOpenHelper {

    public static final String SCHEMA = "password021";
    public static int index = 1;

    public PasswordDBOpenHelper(Context context){
        super(context, SCHEMA, null, 1);

    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Password.CONTENT_TABLE_NAME + " ("
                + Password.COLUMN_ID + " INTEGER, "
                + Password.COLUMN_PASSWORD + " TEXT, "
                + Password.COLUMN_CONFIRMPASSWORD + " TEXT) ";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + Password.CONTENT_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public long insertPassword(Password p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Password.COLUMN_PASSWORD, p.getPassword());
        cv.put(Password.COLUMN_CONFIRMPASSWORD, p.getConfirmpassword());
        cv.put(Password.COLUMN_ID, index);
        long id = db.insert(Password.CONTENT_TABLE_NAME, null, cv);

        return id;

    }

    public int savePassword(Password pass){
        SQLiteDatabase db = getWritableDatabase();
        //pass.setID(index);
        ContentValues cv = new ContentValues();
        cv.put(Password.COLUMN_PASSWORD, pass.getPassword());
        cv.put(Password.COLUMN_CONFIRMPASSWORD, pass.getConfirmpassword());
        return db.update(Password.CONTENT_TABLE_NAME, cv, " " + Password.COLUMN_ID + "= ?", new String[]{String.valueOf(pass.getID())});
        //return db.update(Password.CONTENT_TABLE_NAME, cv, " " + Password.COLUMN_ID + "= ?", new String[]{"" + pass});
    }

    public Cursor queryAllPassword(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Password.CONTENT_TABLE_NAME, null, null, null, null, null, null);

        return c;
    }

    public Password getPassword(int id){
        Password pass = new Password();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Password.CONTENT_TABLE_NAME, null, " " + Password.COLUMN_ID + "=? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if(c.moveToFirst()){
            pass.setPassword(c.getString(c.getColumnIndex(Password.COLUMN_PASSWORD)));
            pass.setConfirmpassword(c.getString(c.getColumnIndex(Password.COLUMN_CONFIRMPASSWORD)));
            pass.setID(id);
        }
        else
        {
            pass = null;
        }

        return pass;
    }

    public int getID(String password){
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Password.CONTENT_TABLE_NAME, null, " " + Password.COLUMN_PASSWORD + "=? ",
                new String[]{String.valueOf(password)}, null, null, null);
        if(c.moveToFirst()){
            id = c.getInt(c.getColumnIndex(Password.COLUMN_ID));
        }


        return id;
    }

}
