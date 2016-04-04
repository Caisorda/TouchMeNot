package com.example.laptop.touchmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Laptop on 3/11/2016.
 */
public class GracePeriodDBOpenHelper extends SQLiteOpenHelper {
    public static final String SCHEMA = "graceperiod";

    public GracePeriodDBOpenHelper(Context context){
        super(context,SCHEMA,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Message.GRACE_PERIOD_TABLE_NAME + " ("
                + Message.COLUMN_ID + " TEXT, "
                + Message.COLUMN_GRACE_PERIOD + " INT) ";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + Message.GRACE_PERIOD_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public int saveGracePeriod(int graceperiod){
        SQLiteDatabase db = getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(Message.COLUMN_GRACE_PERIOD, graceperiod);

        return db.update(Message.GRACE_PERIOD_TABLE_NAME, cv, " " + Message.COLUMN_ID + "= ?", new String[]{"" + "defint"});
    }

    public int getGracePeriod(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Message.GRACE_PERIOD_TABLE_NAME, null, " " + Message.COLUMN_ID + "= ?", new String[]{"" + "defint"}, null, null, null);

        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex(Message.COLUMN_GRACE_PERIOD));
        }else{
            return 0;
        }
    }

    public long newGracePeriod(){
        SQLiteDatabase dbWrite = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Message.COLUMN_ID, "defint");
        cv.put(Message.COLUMN_GRACE_PERIOD, 5);

        return dbWrite.insert(Message.GRACE_PERIOD_TABLE_NAME, null, cv);

    }
}
