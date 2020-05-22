package com.example.inventorymanagment;

public class ListItems {

    private int id, price, stock;
    private String itemName, sachsn;


    ListItems(int id, int price, int stock, String itemName, String sachsn) {
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.itemName = itemName;
        this.sachsn = sachsn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSachsn() {
        return sachsn;
    }

    public void setSachsn(String sachsn) {
        this.sachsn = sachsn;
    }
}
