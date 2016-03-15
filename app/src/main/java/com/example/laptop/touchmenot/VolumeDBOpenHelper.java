package com.example.laptop.touchmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.regex.Pattern;

/**
 * Created by Laptop on 3/14/2016.
 */
public class VolumeDBOpenHelper extends SQLiteOpenHelper {
    public static final String SCHEMA = "volume";

    public VolumeDBOpenHelper(Context context){
        super(context,SCHEMA,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PatternKeys.TABLE_NAME_VOLUME + " ("
                + PatternKeys.COLUMN_ID + " TEXT, "
                + PatternKeys.COLUMN_CURRVOLUME + " INT) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + PatternKeys.TABLE_NAME_VOLUME;
        db.execSQL(sql);
        onCreate(db);
    }

    public int saveCurrentVolume(int volume){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PatternKeys.COLUMN_CURRVOLUME, volume);

        return db.update(PatternKeys.TABLE_NAME_VOLUME, cv, " " + PatternKeys.COLUMN_ID + "= ?", new String[]{"" + "curvol"});
    }

    public int getCurrentVolume(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(PatternKeys.TABLE_NAME_VOLUME, null, " " + PatternKeys.COLUMN_ID + "= ?", new String[]{"" + "curvol"}, null, null, null);

        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex(PatternKeys.COLUMN_CURRVOLUME));
        }else{
            return -1;
        }
    }

    public long newVolume(int volume){
        SQLiteDatabase dbWrite = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PatternKeys.COLUMN_ID, "curvol");
        cv.put(PatternKeys.COLUMN_CURRVOLUME, volume);

        return dbWrite.insert(PatternKeys.TABLE_NAME_VOLUME, null, cv);

    }
}
