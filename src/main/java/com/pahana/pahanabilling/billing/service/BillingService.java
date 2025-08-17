package com.pahana.pahanabilling.billing.service;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.dao.BillItemDAO;
import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.entity.BillItem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillingService {

    private final BillDAO billDAO = new BillDAO();
    private final BillItemDAO billItemDAO = new BillItemDAO();

    public Bill createAndSaveBill(String customerId, List<BillItem> items) throws SQLException {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("customerId is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("At least one bill item is required");
        }

        double totalAmount = 0.0;
        for (BillItem bi : items) {
            if (bi.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for item: " + bi.getItemId());
            }
            if (bi.getUnitPrice() < 0) {
                throw new IllegalArgumentException("Invalid unit price for item: " + bi.getItemId());
            }
            double subtotal = bi.getSubtotal() != 0.0 ? bi.getSubtotal() : (bi.getQuantity() * bi.getUnitPrice());
            bi.setSubtotal(subtotal);
            totalAmount += subtotal;
        }

        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(totalAmount);
        bill.setDateTime(LocalDateTime.now());

        int billId = billDAO.saveBill(bill);
        bill.setBillId(billId);

        for (BillItem bi : items) {
            bi.setBillId(billId);
            billItemDAO.saveBillItem(bi);
        }

        Bill saved = billDAO.findById(billId);
        return saved != null ? saved : bill;
    }

    public Bill createAndSaveBill(String customerId, String[] itemIds, String[] quantities, String[] unitPrices) throws SQLException {
        if (itemIds == null || quantities == null || unitPrices == null) {
            throw new IllegalArgumentException("itemIds, quantities, and unitPrices are required");
        }
        if (itemIds.length == 0 || itemIds.length != quantities.length || itemIds.length != unitPrices.length) {
            throw new IllegalArgumentException("Mismatched item arrays.");
        }

        List<BillItem> items = new ArrayList<>(itemIds.length);
        for (int i = 0; i < itemIds.length; i++) {
            BillItem bi = new BillItem();
            bi.setItemId(itemIds[i]);
            bi.setQuantity(Integer.parseInt(quantities[i]));
            bi.setUnitPrice(Double.parseDouble(unitPrices[i]));
            items.add(bi);
        }
        return createAndSaveBill(customerId, items);
    }

    public Bill getBillById(int billId) throws SQLException {
        return billDAO.findById(billId);
    }
}