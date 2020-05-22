package com.example.inventorymanagment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class recyclerView extends AppCompatActivity {

    static SQLiteDatabase sql;
    static RecyclerView rv;
    static RecyclerViewAdapter itemAdapter;
    static List<ListItems> mData;
    FloatingActionButton fab;


    public void show() {

        mData = new ArrayList<>();
        itemAdapter = new RecyclerViewAdapter(this, mData);

        Cursor c = sql.rawQuery("SELECT * FROM item_list", null);

        if (c.moveToFirst()) {
            do {
                //   int id, int price, int stock, String itemName, String sachsn

                mData.add(new ListItems(c.getInt(0),
                        c.getInt(3),
                        c.getInt(4),
                        c.getString(1),
                        c.getString(2)));

            } while (c.moveToNext());
        }
        rv.setAdapter(itemAdapter);
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_items, menu);

        MenuItem svi = menu.findItem(R.id.searchView);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(svi);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        fab = findViewById(R.id.floatingActionButton);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        sql = this.openOrCreateDatabase("inventory", MODE_PRIVATE, null);
        sql.execSQL("CREATE TABLE IF NOT EXISTS item_list (Id INTEGER PRIMARY KEY," +
                "item_name VARCHAR," +
                "sac_hsn VARCHAR," +
                "price INT," +
                "stock INT)");

        show();

        fab.setOnClickListener((v) -> {
            Intent i = new Intent(getApplicationContext(), AddItems.class);
            startActivity(i);

        });

    }
}
