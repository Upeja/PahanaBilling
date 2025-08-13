package com.pahana.pahanabilling.billing.service;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BillingService {
    private final BillDAO billDAO = new BillDAO();

    /**
     * Create a bill object from inputs
     */
    public Bill generateBill(String customerId, String itemId, int units, double unitPrice) {
        double totalAmount = unitPrice * units;
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setItemId(itemId);
        bill.setUnits(units);
        bill.setUnitPrice(unitPrice);
        bill.setTotalAmount(totalAmount);
        bill.setDateTime(LocalDateTime.now());
        return bill;
    }

    /**
     * Save bill to database
     */
    public void saveBill(Bill bill) throws SQLException {
        billDAO.save(bill);
    }

    /**
     * Get all bills
     */
    public List<Bill> listAllBills() throws SQLException {
        return billDAO.getAllBills();
    }

    /**
     * Get a single bill by ID
     */
    public Bill getBillById(int billId) throws SQLException {
        return billDAO.findById(billId);
    }
}
