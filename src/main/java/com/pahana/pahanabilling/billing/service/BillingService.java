package com.pahana.pahanabilling.billing.service;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BillingService {
    private final BillDAO billDAO = new BillDAO();

    // 🧮 Calculate total
    private double calculateTotal(int units, double unitPrice) {
        return units * unitPrice;
    }

    // 📦 Create bill object
    public Bill generateBill(String customerId, String itemId, int units, double unitPrice) {
        double total = calculateTotal(units, unitPrice);
        return new Bill(0, customerId, itemId, units, unitPrice, total, LocalDateTime.now());
    }

    // 💾 Save bill to DB
    public void saveBill(Bill bill) throws SQLException {
        billDAO.save(bill);
    }

    // 📋 View all bills
    public List<Bill> listAllBills() throws SQLException {
        return billDAO.getAllBills();
    }
}
