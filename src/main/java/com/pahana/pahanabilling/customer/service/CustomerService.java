package com.pahana.pahanabilling.customer.service;

import com.pahana.pahanabilling.customer.dao.CustomerDAO;
import com.pahana.pahanabilling.customer.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public void addCustomer(Customer customer) throws SQLException {
        customerDAO.save(customer);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.update(customer);
    }

    public void deleteCustomer(String accountNumber) throws SQLException {
        customerDAO.delete(accountNumber);
    }

    public List<Customer> listCustomers() throws SQLException {
        return customerDAO.getAll();
    }

    public Customer getCustomerById(String accountNumber) throws SQLException {
        return customerDAO.findById(accountNumber);
    }

    public boolean customerExists(String accountNumber) throws SQLException {
        return customerDAO.exists(accountNumber);
    }
}
