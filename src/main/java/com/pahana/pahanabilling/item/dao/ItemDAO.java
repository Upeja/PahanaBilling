package com.pahana.pahanabilling.item.dao;

import java.sql.*;
import java.util.*;
import com.pahana.pahanabilling.item.entity.Item;

public class ItemDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "upeja"; // change this if needed

    // ✅ Static block to ensure MySQL driver is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found. Please add mysql-connector-j to your classpath.", e);
        }
    }

    // 🔽 Save New Item
    public void save(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_id, name, price) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemId());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();
        }
    }

    // 🔽 Get All Items
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new Item(
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        }
        return items;
    }

    // 🔽 Get One Item by ID
    public Item findById(String itemId) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
            }
        }
        return null;
    }

    // 🔽 Update Existing Item
    public void update(Item item) throws SQLException {
        String sql = "UPDATE items SET name = ?, price = ? WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setString(3, item.getItemId());
            stmt.executeUpdate();
        }
    }

    // 🔽 Delete Item
    public void delete(String itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemId);
            stmt.executeUpdate();
        }
    }

    // ✅ Check if item exists
    public boolean exists(String itemId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM items WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // 🔍 Search items by name
    public List<Item> searchByName(String keyword) throws SQLException {
        List<Item> results = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Item(
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        }
        return results;
    }
}
