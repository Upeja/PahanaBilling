package com.pahana.pahanabilling.customer.service;

import com.pahana.pahanabilling.customer.dao.CustomerDAO;
import com.pahana.pahanabilling.customer.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    // ➕ Add new customer
    public void registerCustomer(Customer customer) throws SQLException {
        customerDAO.save(customer);
    }

    // 📋 View all customers
    public List<Customer> listCustomers() throws SQLException {
        return customerDAO.getAll();
    }

    // 🔍 Get customer by ID
    public Customer getCustomerById(String accountNumber) throws SQLException {
        return customerDAO.findById(accountNumber);
    }

    // ✏️ Update existing customer
    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.update(customer);
    }

    // ❌ Delete customer
    public void deleteCustomer(String accountNumber) throws SQLException {
        customerDAO.delete(accountNumber);
    }

    // ✅ Check if account number already exists
    public boolean customerExists(String accountNumber) throws SQLException {
        return customerDAO.exists(accountNumber);
    }
}
