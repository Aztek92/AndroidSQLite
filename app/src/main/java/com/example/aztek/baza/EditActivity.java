package com.example.aztek.baza;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity
{
    private int mode =-1;
    private long rowNumber;
    private SimpleCursorAdapter mAdapterCursor;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_edit);
        Bundle mBundle = getIntent().getExtras();

        mode = mBundle.getInt("TRYB");
        if (mode !=-1)
        {
            rowNumber = mBundle.getLong("POZYCJA");
            TextView producer = (TextView)findViewById(R.id.editText);
            TextView model= (TextView)findViewById(R.id.editText2);
            TextView version = (TextView)findViewById(R.id.editText3);
            TextView www = (TextView)findViewById(R.id.editText4);


            Cursor mCursor = getContentResolver().query(Provider.URI_CONTENT, new String[]{SQLite.ID, SQLite.PRODUCER_COLUMN, SQLite.MODEL_COLUMN, SQLite.VERSION_COLUMN, SQLite.WWW_COLUMN}, SQLite.ID + "=" + rowNumber, null, null);
            mCursor.moveToFirst();
            String producerName = mCursor.getString(1);
            producer.setText(producerName);
            model.setText(mCursor.getString(2));
            version.setText(mCursor.getString(3));
            www.setText(mCursor.getString(4));

            String data = mCursor.getString(mCursor.getColumnIndex(SQLite.ID));


        }


    }

    public void saveModel(View view)
    {
        String producer = ((TextView)findViewById(R.id.editText)).getText().toString();
        String model = ((TextView)findViewById(R.id.editText2)).getText().toString();
        String version = ((TextView)findViewById(R.id.editText3)).getText().toString();
        String www = ((TextView)findViewById(R.id.editText4)).getText().toString();
        if (validation(producer,model,www,version))
        {
            ContentValues values= new ContentValues();
            values.put(SQLite.PRODUCER_COLUMN, producer);
            values.put(SQLite.MODEL_COLUMN, model);
            values.put(SQLite.VERSION_COLUMN, version);
            values.put(SQLite.WWW_COLUMN, www);
            if (mode ==-1)
            {
                Toast.makeText(this,"Dodano nowy obiekt", Toast.LENGTH_SHORT).show();
                Uri mNewUri = getContentResolver().insert(Provider.URI_CONTENT,values);
            }
            else
            {
                Toast.makeText(this,"Edytowano obiekt", Toast.LENGTH_SHORT).show();
                int mNewUri=getContentResolver().update(Provider.URI_CONTENT, values, SQLite.ID+"="+String.valueOf(rowNumber),null);
            }


        }
        else
        {
            return;
        }

    }

    public void openPage (View view)
    {
        String address=((TextView)findViewById(R.id.editText4)).getText().toString();
        if (wwwValidation(address))
        {
            Intent intentWww= new Intent("android.intent.action.VIEW",Uri.parse(address));
            startActivity(intentWww);
        }
        else
        {
            Toast.makeText(this,"Strona www jest nieprawidłowa", Toast.LENGTH_SHORT).show();
        }

    }
    public void cancelOperation(View view)
    {
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    boolean wwwValidation(String adres)
    {
        if(adres.startsWith("http://www."))
            return true;
        else
            return false;
    }
    boolean modelValidation(String modelOrProducer)
    {
        if(!modelOrProducer.isEmpty() && modelOrProducer.length()>2)
            return true;
        else
            return false;
    }

    boolean versionValidation(String version)
    {
        if(version.isEmpty() )
            return false;
        for (int i=0;i<version.length();i++)
        {
            char character = version.charAt(i);
            if (character=='.' || character=='0' ||character=='1' ||character=='2' ||character=='3' ||character=='4' ||character=='5' ||character=='6' ||character=='7' ||character=='8' ||character=='9' )
            {

            }
            else
            {
                return false;
            }
        }
        return true;
    }


    boolean validation(String producer, String model, String address, String version)
    {
        if (!wwwValidation(address))
        {
            Toast.makeText(this,"Strona www jest nieprawidłowa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!versionValidation(version))
        {
            Toast.makeText(this,"Wersja jest nieprawidłowa", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!modelValidation(producer))
        {
            Toast.makeText(this,"Nazwa producenta jest nieprawidłowa", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!modelValidation(model))
        {
            Toast.makeText(this,"Nazwa modelu jest nieprawidłowa", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
