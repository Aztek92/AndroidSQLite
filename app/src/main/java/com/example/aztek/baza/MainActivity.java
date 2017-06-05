package com.example.aztek.baza;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapterCursor;
    private ListView mList;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView) findViewById(R.id.List);
        loadContent();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("TRYB",1);
                intent.putExtra("POZYCJA", id);
                startActivity(intent);

            }
        });


        mList.setMultiChoiceModeListener(new MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int a = 1;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater pump = mode.getMenuInflater();
                pump.inflate(R.menu.context_bar_list, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                int a = 1;
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clear_menu: {
                        deleteSelected();
                        return true;
                    }

                }
                return false;
            }

                    private void deleteSelected() {
                        long selected[] = mList.getCheckedItemIds();
                        for (int i = 0; i < selected.length; i++) {
                            getContentResolver().delete(ContentUris.withAppendedId(Provider.URI_CONTENT, selected[i]), null, null);
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        int a = 1;
                    }
                });
        loadContent();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {SQLite.ID, SQLite.PRODUCER_COLUMN, SQLite.MODEL_COLUMN};
        CursorLoader mCursorLoader = new CursorLoader(this, Provider.URI_CONTENT, projection, null, null, null);
        return mCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showData(data.getCount());
        mAdapterCursor.swapCursor(data);
    }

    void showData(int elements) {
        if (elements <= 0) {
            ((TextView) findViewById(R.id.noData)).setVisibility(View.VISIBLE);
            ((ListView) findViewById(R.id.List)).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.noData)).setVisibility(View.GONE);
            ((ListView) findViewById(R.id.List)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterCursor.swapCursor(null);
    }

    private void loadContent() {
        getLoaderManager().initLoader(0, null, this);
        String[] mapFrom = new String[]{SQLite.PRODUCER_COLUMN, SQLite.MODEL_COLUMN};
        int[] mapTo = new int[]{R.id.producer, R.id.model};
        mAdapterCursor = new SimpleCursorAdapter(this, R.layout.list_element, null, mapFrom, mapTo, 0);
        mList.setAdapter(mAdapterCursor);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addElements();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void addElements() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("TRYB", -1);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.aztek.baza/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.aztek.baza/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
