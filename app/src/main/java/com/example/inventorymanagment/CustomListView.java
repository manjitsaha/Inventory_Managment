package com.example.inventorymanagment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomListView extends AppCompatActivity {

    static SQLiteDatabase sql;
    FloatingActionButton fab;

    static ListView lv;
    SearchView sv;
    static ArrayList midArr = new ArrayList();
    static ArrayList mitemNameArr = new ArrayList();
    static ArrayList msacPriceStockArr = new ArrayList();
    static myAdapter mad;


    public void search(String searchText) {
        ArrayList localIdArr = new ArrayList();
        ArrayList localItemNameArrr = new ArrayList();
        ArrayList localSacPriceStockArrr = new ArrayList();

        myAdapter localMad = new myAdapter(getApplicationContext(), localIdArr, localItemNameArrr, localSacPriceStockArrr);
        Log.i("Query ", searchText);
        Cursor c = sql.rawQuery("SELECT * FROM item_list WHERE item_name LIKE '%" + searchText + "%'", null);

        if (c.moveToFirst()) {
            do {
                localIdArr.add(c.getInt(0));
                localItemNameArrr.add(c.getString(1));
                localSacPriceStockArrr.add("Sac/Hsn : " + c.getString(2) + "  "
                        + "Price : " + c.getInt(3) + "   "
                        + "Stock : " + c.getInt(4));
                //(id ,item_name , sac_hsn , price , stock)
            } while (c.moveToNext());
        }


        lv.setAdapter(localMad);
        c.close();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), UpdateItems.class);
                i.putExtra("Id", Integer.parseInt(localIdArr.get(position).toString()));
                startActivity(i);

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete(Integer.parseInt(localIdArr.get(position).toString()));
                lv.setSelection(position);
                return false;
            }
        });
    }


    public void show() {
        midArr.clear();
        mitemNameArr.clear();
        msacPriceStockArr.clear();
        mad = new myAdapter(this , midArr ,mitemNameArr ,msacPriceStockArr);

        Cursor c = sql.rawQuery("SELECT * FROM item_list", null);

        if (c.moveToFirst()) {
            do {
                midArr.add(c.getInt(0));
                mitemNameArr.add(c.getString(1));
                msacPriceStockArr.add("Sac/Hsn : "+c.getString(2) + "  "
                        + "Price : "+c.getInt(3) + "   "
                        + "Stock : "+c.getInt(4));
                //(id ,item_name , sac_hsn , price , stock)
            } while (c.moveToNext());
        }
        lv.setAdapter(mad);
        c.close();


    }

    public void delete(int position){

       sql.execSQL("DELETE FROM item_list where ID = " + position + "");
        show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_items , menu);

        MenuItem svi = menu.findItem(R.id.searchView);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(svi);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.about:
                new AlertDialog.Builder(this).setTitle("Credit")
                        .setMessage("THIS APP IS DEVELOPED BY MANJIT SAHA")
                        .setPositiveButton("Close" , null).show();

                return true;

            default:
                return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view);


        lv = findViewById(R.id.customListView);
        fab = findViewById(R.id.floatingActionButton);

        //    sv = findViewById(R.id.searchView);

        sql = this.openOrCreateDatabase("inventory", MODE_PRIVATE, null);
        sql.execSQL("CREATE TABLE IF NOT EXISTS item_list (Id INTEGER PRIMARY KEY," +
                "item_name VARCHAR," +
                "sac_hsn VARCHAR," +
                "price INT," +
                "stock INT)");


        show();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AddItems.class);
                startActivity(i);

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext() , UpdateItems.class);
                i.putExtra("Id" , Integer.parseInt(midArr.get(position).toString()));
                startActivity(i);
              }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete(Integer.parseInt(midArr.get(position).toString()));
                lv.setSelection(position);
                return true;
            }
        });


    }


}


class myAdapter extends ArrayAdapter{

    Context context;
    ArrayList ridArr = new ArrayList();
    ArrayList ritemNameArr = new ArrayList();
    ArrayList rsacPriceStockArr = new ArrayList();

    myAdapter(Context c , ArrayList id , ArrayList name , ArrayList sps){
        super(c , R.layout.row  , R.id.idTv , id);

        this.context = c;
        this.ridArr = id;
        this.ritemNameArr = name;
        this.rsacPriceStockArr = sps;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = layoutInflater.inflate(R.layout.row , parent , false);

        TextView idt = row.findViewById(R.id.idTv);
        TextView itemname = row.findViewById(R.id.itemNameRow);
        TextView sps = row.findViewById(R.id.spsRow);

        idt.setText(ridArr.get(position).toString());
        itemname.setText(ritemNameArr.get(position).toString());
        sps.setText(rsacPriceStockArr.get(position).toString());

        return row;
    }


}