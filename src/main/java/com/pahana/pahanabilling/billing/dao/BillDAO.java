package com.pahana.pahanabilling.billing.dao;

import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password"; // update this

    // 🧾 Save new bill
    public void save(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (customer_id, item_id, units, total_amount, date_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bill.getCustomerId());
            stmt.setString(2, bill.getItemId());
            stmt.setInt(3, bill.getUnits());
            stmt.setDouble(4, bill.getTotalAmount());
            stmt.setTimestamp(5, Timestamp.valueOf(bill.getDateTime()));
            stmt.executeUpdate();
        }
    }

    // 📋 View all bills
    public List<Bill> getAll() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY date_time DESC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bills.add(new Bill(
                        rs.getInt("bill_id"),
                        rs.getString("customer_id"),
                        rs.getString("item_id"),
                        rs.getInt("units"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                ));
            }
        }
        return bills;
    }
}
