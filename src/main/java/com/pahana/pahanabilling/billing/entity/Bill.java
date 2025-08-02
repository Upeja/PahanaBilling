package com.pahana.pahanabilling.billing.entity;

import java.time.LocalDateTime;

public class Bill {
    private int billId;
    private String customerId;
    private String itemId;
    private int units;
    private double unitPrice;
    private double totalAmount;
    private LocalDateTime dateTime;

    public Bill() {}

    public Bill(int billId, String customerId, String itemId, int units, double unitPrice, double totalAmount, LocalDateTime dateTime) {
        this.billId = billId;
        this.customerId = customerId;
        this.itemId = itemId;
        this.units = units;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.dateTime = dateTime;
    }

    // Getters and Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", customerId='" + customerId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", units=" + units +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", dateTime=" + dateTime +
                '}';
    }
}
