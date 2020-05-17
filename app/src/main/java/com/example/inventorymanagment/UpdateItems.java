package com.example.inventorymanagment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

        ArrayList midArr = new ArrayList();
        ArrayList mitemNameArr = new ArrayList();
        ArrayList msacPriceStockArr = new ArrayList();
        myAdapter mad = new myAdapter(this , midArr ,mitemNameArr ,msacPriceStockArr);

        Cursor c = CustomListView.sql.rawQuery("SELECT * FROM item_list", null);

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
        CustomListView.lv.setAdapter(mad);
        c.close();

    }

    public void setData(){
        Intent i = getIntent();
        int id = i.getIntExtra("Id" , -1);
        Cursor c = CustomListView.sql.rawQuery("SELECT * FROM item_list WHERE Id = "+id+"" , null);

        if(c.moveToFirst()){
            do{
                itemName.setText(c.getString(1));
                sac_hsn.setText(c.getString(2));
                price.setText(Integer.toString(c.getInt(3)));
                stock.setText(Integer.toString(c.getInt(4)));
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
            CustomListView.sql.execSQL("UPDATE item_list SET item_name = '"+itemname+"', sac_hsn = '"+sac+"'," +
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
