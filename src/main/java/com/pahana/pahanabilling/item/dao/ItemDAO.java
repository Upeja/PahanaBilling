package com.pahana.pahanabilling.item.dao;

import java.sql.*;
import java.util.*;
import com.pahana.pahanabilling.item.entity.Item;


public class ItemDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_billing";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password"; // change this

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
}
