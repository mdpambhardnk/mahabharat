package com.jorjoto.mahabharat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseClass extends SQLiteOpenHelper {
    private static String DB_NAME = "mahabharat";

    SQLiteDatabase sqLiteDatabase;
    Context context;

    public DataBaseClass(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ads (ad_id  INTEGER,ad_type TEXT)");
        db.execSQL("create table closeads (ad_id  INTEGER,ad_type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ads ");
        db.execSQL("drop table if exists closeads ");
        onCreate(db);
    }

    public Cursor getadsData() {
        Cursor cursor = null;
        sqLiteDatabase = this.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("select * from ads", null);
        return cursor;
    }

    public void insertadsData(String ad_id, String ad_type) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_id", ad_id);
        contentValues.put("ad_type", ad_type);
        sqLiteDatabase.insert("ads", null, contentValues);
    }

    public void deleteadsData() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from ads");
    }


    public Cursor isadsadded(String ad_id) {
        Cursor cursor = null;
        sqLiteDatabase = this.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select * from ads where ad_id = '" + ad_id + "'", null);
        return cursor;
    }


    public void insertcloseadsData(String ad_id, String ad_type) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_id", ad_id);
        contentValues.put("ad_type", ad_type);
        sqLiteDatabase.insert("closeads", null, contentValues);
    }
    public void deletecloseadsData() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from closeads");
    }

    public Cursor iscloseadsadded(String ad_id) {
        Cursor cursor = null;
        sqLiteDatabase = this.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select * from closeads where ad_id = '" + ad_id + "'", null);
        return cursor;
    }
}
