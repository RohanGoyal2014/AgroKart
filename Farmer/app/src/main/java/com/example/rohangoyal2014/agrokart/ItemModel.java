package com.example.rohangoyal2014.agrokart;

public class ItemModel {

    String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ItemModel(String name, String qty, String cost, String unit) {
        this.name = name;
        this.qty = qty;
        this.cost = cost;

        this.unit = unit;
    }

    String qty;

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getCost() {
        return cost;
    }

    public String getUnit() {
        return unit;
    }

    String cost;
    String unit;

}
