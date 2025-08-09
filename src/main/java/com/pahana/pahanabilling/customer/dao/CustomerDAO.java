package com.pahana.pahanabilling.customer.dao;

import com.pahana.pahanabilling.customer.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "upeja"; // 🔁 Replace with your real password

    // ➕ Save customer
    public void save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (account_number, name, address, phone, units_consumed) VALUES (?, ?, ?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ✅ Load MySQL Driver
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customer.getAccountNumber());
                stmt.setString(2, customer.getName());
                stmt.setString(3, customer.getAddress());
                stmt.setString(4, customer.getPhone());
                stmt.setInt(5, customer.getUnitsConsumed());
                stmt.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    // 📋 View all customers
    public List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    customers.add(new Customer(
                            rs.getString("account_number"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getInt("units_consumed")
                    ));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return customers;
    }

    // 🔍 Get customer by account number
    public Customer findById(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM customers WHERE account_number = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Customer(
                            rs.getString("account_number"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getInt("units_consumed")
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return null;
    }

    // ✏️ Update customer
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, address = ?, phone = ?, units_consumed = ? WHERE account_number = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customer.getName());
                stmt.setString(2, customer.getAddress());
                stmt.setString(3, customer.getPhone());
                stmt.setInt(4, customer.getUnitsConsumed());
                stmt.setString(5, customer.getAccountNumber());
                stmt.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    // ❌ Delete customer
    public void delete(String accountNumber) throws SQLException {
        String sql = "DELETE FROM customers WHERE account_number = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                stmt.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    // ✅ Check if account number exists
    public boolean exists(String accountNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE account_number = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
