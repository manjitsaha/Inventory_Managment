package com.example.inventorymanagment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddItems extends AppCompatActivity {


     Button btn;
    EditText sac_hsn, itemName , price, stock;

    public void clear(){
        sac_hsn.setText("");
        itemName.setText("");
        price.setText(" ");
        stock.setText(" ");
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
                msacPriceStockArr.add(c.getString(2) + "   "
                        + c.getInt(3) + "   "
                        + c.getInt(4));
                //(id ,item_name , sac_hsn , price , stock)
            } while (c.moveToNext());
        }
        CustomListView.lv.setAdapter(mad);
        c.close();

    }

    public void add(View view){
        String sac = sac_hsn.getText().toString();
        String itemname = itemName.getText().toString();
        String pricee = price.getText().toString();
        String stockk = stock.getText().toString();


        CustomListView.sql.execSQL("INSERT INTO item_list(item_name , sac_hsn , price , stock) VALUES ('"+itemname+"' , '"+sac+"','"+pricee+"' , '"+stockk+"')");
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
