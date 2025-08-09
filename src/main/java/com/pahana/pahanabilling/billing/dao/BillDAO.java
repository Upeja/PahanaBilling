package com.pahana.pahanabilling.billing.dao;

import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "upeja";

    // 📝 Save new bill
    public void save(Bill bill) throws SQLException {
        String sql = "INSERT INTO bill (customer_id, item_id, units, unit_price, total_amount, date_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bill.getCustomerId());
            stmt.setString(2, bill.getItemId());
            stmt.setInt(3, bill.getUnits());
            stmt.setDouble(4, bill.getUnitPrice());
            stmt.setDouble(5, bill.getTotalAmount());
            stmt.setTimestamp(6, Timestamp.valueOf(bill.getDateTime()));

            stmt.executeUpdate();
        }
    }

    // 📄 Get one bill by ID (for PDF export)
    public Bill findById(int billId) throws SQLException {
        String sql = "SELECT * FROM bill WHERE bill_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Bill(
                        rs.getInt("bill_id"),
                        rs.getString("customer_id"),
                        rs.getString("item_id"),
                        rs.getInt("units"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                );
            }
        }

        return null;
    }

    // 📋 Get all bills (for viewing)
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bill ORDER BY date_time DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getString("customer_id"),
                        rs.getString("item_id"),
                        rs.getInt("units"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                );
                bills.add(bill);
            }
        }

        return bills;
    }
}
