package com.pahana.pahanabilling.customer.service;

import com.pahana.pahanabilling.customer.dao.CustomerDAO;
import com.pahana.pahanabilling.customer.entity.Customer;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();

    public void registerCustomer(Customer customer) {
        customerDAO.saveCustomer(customer);
    }
}
