package com.example.inventorymanagment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddItems extends AppCompatActivity {


    Button btn;
    EditText sac_hsn, itemName , price, stock;

    public void clear(){
        sac_hsn.setText("");
        itemName.setText("");
        price.setText("");
        stock.setText("");
    }

    public void show(){
        recyclerView.mData.clear();
        Cursor c = recyclerView.sql.rawQuery("SELECT * FROM item_list", null);

        if (c.moveToFirst()) {
            do {
                recyclerView.mData.add(new ListItems(c.getInt(0),
                        c.getInt(3),
                        c.getInt(4),
                        c.getString(1),
                        c.getString(2)));
            } while (c.moveToNext());
        }
        recyclerView.rv.setAdapter(recyclerView.itemAdapter);
        recyclerView.rv.setLayoutManager(new LinearLayoutManager(this));
        c.close();

    }

    public void add(View view){
        String sac = sac_hsn.getText().toString();
        String itemname = itemName.getText().toString();
        String pricee = price.getText().toString();
        String stockk = stock.getText().toString();


        recyclerView.sql.execSQL("INSERT INTO item_list(item_name , sac_hsn , price , stock) VALUES ('" + itemname + "' , '" + sac + "','" + pricee + "' , '" + stockk + "')");
        Toast.makeText(getApplicationContext() , "Added" , Toast.LENGTH_SHORT).show();

        show();
        clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        btn = findViewById(R.id.addBtn);
        sac_hsn = findViewById(R.id.sacEt);
        itemName = findViewById(R.id.itemnameEt);
        price = findViewById(R.id.priceEt);
        stock = findViewById(R.id.stockEt);

        btn.setVisibility(View.VISIBLE);


    }
}
