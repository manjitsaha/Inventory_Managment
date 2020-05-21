package com.example.inventorymanagment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class UpdateItems extends AppCompatActivity {
    Button updateBtn;
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

    public void setData(){
        Intent i = getIntent();
        int id = i.getIntExtra("Id" , -1);
        Cursor c = recyclerView.sql.rawQuery("SELECT * FROM item_list WHERE Id = " + id + "", null);

        if(c.moveToFirst()){
            do{
                itemName.setText(c.getString(1));
                sac_hsn.setText(c.getString(2));
                price.setText(String.valueOf(c.getInt(3)));
                stock.setText(String.valueOf(c.getInt(4)));
            }while (c.moveToNext());
        }
        c.close();
    }


    public void update(View view){


        String sac = sac_hsn.getText().toString();
        String itemname = itemName.getText().toString();
        String pricee = price.getText().toString();
        String stockk = stock.getText().toString();

        Intent i = getIntent();
        int id = i.getIntExtra("Id" , -1);
        if(id != -1){
            recyclerView.sql.execSQL("UPDATE item_list SET item_name = '" + itemname + "', sac_hsn = '" + sac + "'," +
                                                                        "price = "+pricee+", stock = "+stockk+" WHERE ID = "+id+"");
            //(id ,item_name , sac_hsn , price , stock)

            Toast.makeText(getApplicationContext() , "Updated" , Toast.LENGTH_SHORT).show();
        }
        show();
        clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);

        updateBtn = findViewById(R.id.updateBtn);
        sac_hsn = findViewById(R.id.sacEt);
        itemName = findViewById(R.id.itemnameEt);
        price = findViewById(R.id.priceEt);
        stock = findViewById(R.id.stockEt);

        setData();

    }
}
