package com.example.aztek.baza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper
{
    public final static int VERSION =1;
    public final static String ID="_id";
    public final static String DATABASE_NAME ="markaTelefonowBD2";
    public final static String PHONES_TABLE ="telefony";
    public final static String PRODUCER_COLUMN ="producent";
    public final static String VERSION_COLUMN ="wersja";
    public final static String WWW_COLUMN ="www";
    public final static String MODEL_COLUMN ="model";
    public final static String CREATE_DATABASE ="CREATE TABLE "+ PHONES_TABLE + "("+ID+" integer primary key autoincrement, "+ MODEL_COLUMN +" text not null, "+ PRODUCER_COLUMN +" text, "+ VERSION_COLUMN +" text, "+ WWW_COLUMN +");";
    public final static String DROP_DATABASE ="DROP TABLE IF EXISTS"+ PHONES_TABLE;

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_DATABASE);
        onCreate(db);
    }
}
