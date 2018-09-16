package com.example.rohangoyal2014.agrokart;

public class OrderModel {

    String itemName,qty,amt,purchaser;

    public OrderModel(String itemName, String qty, String amt, String purchaser) {
        this.itemName = itemName;
        this.qty = qty;
        this.amt = amt;
        this.purchaser = purchaser;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }
}
