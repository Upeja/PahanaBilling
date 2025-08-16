package com.pahana.pahanabilling.billing.dao;

import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.entity.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "upeja";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Saves the main bill record (without items) and returns the new bill's ID.
     */
    public int saveBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (customer_id, total_amount, date_time) VALUES (?, ?, ?)";
        int generatedId = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bill.getCustomerId());
            stmt.setDouble(2, bill.getTotalAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(bill.getDateTime()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                } else {
                    throw new SQLException("Creating bill failed, no ID obtained.");
                }
            }
        }
        return generatedId;
    }

    /**
     * Finds a single bill by its ID and also fetches all its associated items.
     */
    public Bill findById(int billId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        Bill bill = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setCustomerId(rs.getString("customer_id"));
                    bill.setTotalAmount(rs.getDouble("total_amount"));
                    bill.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());

                    // Now, fetch the associated items for this bill
                    bill.setItems(findItemsForBill(bill.getBillId(), conn));
                }
            }
        }
        return bill;
    }

    /**
     * Helper method to get all items for a specific bill ID.
     */
    private List<BillItem> findItemsForBill(int billId, Connection conn) throws SQLException {
        List<BillItem> items = new ArrayList<>();
        String sql = "SELECT * FROM bill_items WHERE bill_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setBillItemId(rs.getInt("bill_item_id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setItemId(rs.getString("item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setSubtotal(rs.getDouble("subtotal"));
                    items.add(item);
                }
            }
        }
        return items;
    }
}