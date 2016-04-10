package com.example.laptop.touchmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by Laptop on 4/6/2016.
 */
public class PressedDBOpenHelper extends SQLiteOpenHelper {
    public static final String SCHEMA = "pressed";

    public PressedDBOpenHelper(Context context){
        super(context, SCHEMA, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PressedButton.TABLE_NAME + " ("
                + PressedButton.COLUMN_ID + " INT, "
                + PressedButton.COLUMN_BUTTON + " TEXT, "
                + PressedButton.COLUMN_TIME +" TEXT) ";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + PressedButton.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public PatternKeys getKey(int order){
        PatternKeys key = new PatternKeys();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(PatternKeys.TABLE_NAME_PATTERN, null, " " + PatternKeys.COLUMN_ID + "= ?", new String[]{"" + order}, null, null, null);

        if(c.moveToFirst()){
            key.setOrder(c.getInt(c.getColumnIndex(PatternKeys.COLUMN_ID)));
            key.setKey(c.getString(c.getColumnIndex(PatternKeys.COLUMN_KEY)));
        }else{
            key = null;
        }

        return key;
    }

    public ArrayList<PatternKeys> getPattern(){
        ArrayList<PatternKeys> keys = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PatternKeys.TABLE_NAME_PATTERN, null,null,null,null,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    PatternKeys key = new PatternKeys();
                    key.setOrder(cursor.getInt(cursor.getColumnIndex(PatternKeys.COLUMN_ID)));
                    key.setKey(cursor.getString(cursor.getColumnIndex(PatternKeys.COLUMN_KEY)));
                    keys.add(key);
                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return keys;
    }

//    public long insertPressedButton(PressedButton button){
//        SQLiteDatabase db = getWritableDatabase();
//
//        System.out.println(button.getOrder());
//
//        ContentValues cv = new ContentValues();
//        cv.put(PressedButton.COLUMN_ID, button.getOrder());
//        cv.put(PressedButton.COLUMN_KEY, button.getKey());
//
//        return db.insert(PatternKeys.TABLE_NAME_PATTERN,null,cv);
//    }

    public int getButtonPosition(){
        SQLiteDatabase db = getReadableDatabase();
        final SQLiteStatement stmt = db
                .compileStatement("SELECT MAX(_id) FROM pattern");

        int i = (int) stmt.simpleQueryForLong();
        if(i != 0){
            return i;
        }else{
            return 0;
        }
    }

    public int clearPattern(){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(PatternKeys.TABLE_NAME_PATTERN,null,null);
    }
}
