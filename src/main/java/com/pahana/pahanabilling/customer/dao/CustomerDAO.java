package com.pahana.pahanabilling.customer.dao;

import com.pahana.pahanabilling.util.DBConnection;
import com.pahana.pahanabilling.customer.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CustomerDAO {
    public void saveCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO customer (account_number, name, address, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhone());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
