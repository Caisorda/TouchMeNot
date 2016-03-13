package com.example.laptop.touchmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Laptop on 3/9/2016.
 */
public class MessageDBOpenHelper extends SQLiteOpenHelper {

    public static final String SCHEMA = "message";

    public MessageDBOpenHelper(Context context){
        super(context,SCHEMA,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Message.CONTENT_TABLE_NAME + " ("
                + Message.COLUMN_ID + " TEXT, "
                + Message.COLUMN_BODY + " TEXT) ";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + Message.CONTENT_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public int saveMessage(String message){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Message.COLUMN_BODY, message);

        return db.update(Message.CONTENT_TABLE_NAME, cv, " " + Message.COLUMN_ID + "= ?", new String[]{"" + "defmes"});
    }

    public String getMessage(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Message.CONTENT_TABLE_NAME, null, " " + Message.COLUMN_ID + "= ?", new String[]{"" + "defmes"}, null, null, null);

        if(c.moveToFirst()){
            return c.getString(c.getColumnIndex(Message.COLUMN_BODY));
        }else{
            return "";
        }
    }

    public long newMessage(){
        SQLiteDatabase dbWrite = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Message.COLUMN_ID, "defmes");
        cv.put(Message.COLUMN_BODY, "My Phone has been snatched! Current location is:");

        return dbWrite.insert(Message.CONTENT_TABLE_NAME, null, cv);

//        SQLiteDatabase db = getWritableDatabase();
//        return db.delete(Message.CONTENT_TABLE_NAME, " " + Message.COLUMN_ID + "= ?", new String[]{"" + "defmes"});
    }

}
