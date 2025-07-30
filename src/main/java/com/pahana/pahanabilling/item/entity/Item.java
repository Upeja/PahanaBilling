package com.pahana.pahanabilling.item.entity;

public class Item {
    private String itemId;
    private String name;
    private double price;

    public Item() {}

    public Item(String itemId, String name, double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public boolean isValid() {
        return itemId != null && !itemId.isEmpty()
                && name != null && !name.isEmpty()
                && price >= 0;
    }
}