package com.pahana.pahanabilling.billing.dao;

import com.pahana.pahanabilling.billing.entity.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "upeja";

    // Load MySQL Driver once when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully (BillDAO)");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found (BillDAO)");
            e.printStackTrace();
        }
    }

    // 📝 Save new bill
    public void save(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (customer_id, item_id, units, unit_price, total_amount, date_time) " +
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

    // 📄 Get one bill by ID
    public Bill findById(int billId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";

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

    // 📋 Get all bills
    public List<Bill> getAllBills() throws SQLException {
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
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                ));
            }
        }

        return bills;
    }
}
