package com.pahana.pahanabilling.billing.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Bill {
    private int billId;
    private String customerId;
    private double totalAmount;
    private LocalDateTime dateTime;

    // A bill now contains a list of its items.
    private List<BillItem> items;

    public Bill() {}

    // Constructor for creating a new bill
    public Bill(String customerId, double totalAmount, LocalDateTime dateTime, List<BillItem> items) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.dateTime = dateTime;
        this.items = items;
    }

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }
}