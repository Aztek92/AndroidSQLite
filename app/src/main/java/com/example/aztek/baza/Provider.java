package com.example.aztek.baza;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class Provider extends ContentProvider
{

    private SQLite mDB;
    private static final String ID = "com.example.aztek.baza.Provider";
    public static final Uri URI_CONTENT = Uri.parse("content://"+ ID +"/"+ SQLite.PHONES_TABLE);
    private static final int TABLE =1;
    private static final int SELECTED_ROW =2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(ID, SQLite.PHONES_TABLE, TABLE);
        sUriMatcher.addURI(ID, SQLite.PHONES_TABLE + "/#" , SELECTED_ROW);
    }


    @Override
    public boolean onCreate()
    {
       mDB = new SQLite(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        int typUri= sUriMatcher.match(uri);
        SQLiteDatabase mDatabase= mDB.getWritableDatabase();
        Cursor mCursor=null;
        switch(typUri)
        {
            case TABLE:
                mCursor= mDatabase.query(false, SQLite.PHONES_TABLE,projection,selection,selectionArgs,null,null,sortOrder,null,null);
                break;
            case SELECTED_ROW:
                mCursor= mDatabase.query(false, SQLite.PHONES_TABLE, projection, selectionAddId(selection,uri), selectionArgs, null, null, sortOrder, null, null);
                break;
        }
        mCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return mCursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        int typUri= sUriMatcher.match(uri);
        SQLiteDatabase mDatabase= mDB.getWritableDatabase();
        long addedId=0;
        switch(typUri)
        {
            case TABLE:
            {
                addedId= mDatabase.insert(SQLite.PHONES_TABLE,null,values);
            }
            break;
            default:
                throw new IllegalArgumentException("Nieznane URI: "+uri);


        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(SQLite.PHONES_TABLE + "/" + addedId);
    }

    private String selectionAddId(String selection, Uri uri)
    {
        if (selection !=null && !selection.equals(""))
            selection= selection+ " and "+ SQLite.ID + "="+uri.getLastPathSegment();
        else
            selection= SQLite.ID+ "="+ uri.getLastPathSegment();
        return selection;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int uriType= sUriMatcher.match(uri);
        SQLiteDatabase mDatabase= mDB.getWritableDatabase();
        int amountOfDeleted=0;
        switch(uriType)
        {
            case TABLE:
                amountOfDeleted= mDatabase.delete(SQLite.PHONES_TABLE, selectionAddId(selection,uri),selectionArgs);
                break;
            case SELECTED_ROW:
                amountOfDeleted= mDatabase.delete(SQLite.PHONES_TABLE, selectionAddId(selection,uri),selectionArgs);
            break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return amountOfDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int uriType= sUriMatcher.match(uri);
        SQLiteDatabase mDatabase= mDB.getWritableDatabase();
        int amountOfUpdated=0;
        switch(uriType)
        {
            case TABLE:
                amountOfUpdated= mDatabase.update(SQLite.PHONES_TABLE, values, selection, selectionArgs);
                break;
            case SELECTED_ROW:
                amountOfUpdated= mDatabase.update(SQLite.PHONES_TABLE,values, selectionAddId(selection,uri),selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return amountOfUpdated;
    }
}
