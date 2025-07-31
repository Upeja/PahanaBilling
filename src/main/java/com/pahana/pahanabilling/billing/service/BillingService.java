package com.pahana.pahanabilling.billing.service;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BillingService {
    private final BillDAO billDAO = new BillDAO();

    // 💰 Calculate total
    public double calculateTotal(int units, double pricePerUnit) {
        return units * pricePerUnit;
    }

    // 🧾 Create bill object
    public Bill generateBill(String customerId, String itemId, int units, double unitPrice) {
        double total = calculateTotal(units, unitPrice);
        return new Bill(0, customerId, itemId, units, total, LocalDateTime.now());
    }

    // 💾 Save bill to DB
    public void saveBill(Bill bill) throws SQLException {
        billDAO.save(bill);
    }

    // 📋 View previous bills
    public List<Bill> listAllBills() throws SQLException {
        return billDAO.getAll();
    }
}
