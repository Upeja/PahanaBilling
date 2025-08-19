package com.pahana.pahanabilling.customer.dao;

import com.pahana.pahanabilling.customer.entity.Customer;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {

    private static CustomerDAO customerDAO;

    @BeforeAll
    static void setup() {
        customerDAO = new CustomerDAO();
    }

    @Test
    void testAddCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer("CUST100", "Test User", "123 Main St", "0771234567", 50);

        // Act
        customerDAO.save(customer);

        // Assert
        assertTrue(customerDAO.exists("CUST100"), "Customer should be saved successfully");

        // Cleanup
        customerDAO.delete("CUST100");
    }

    @Test
    void testGetCustomerById() throws SQLException {
        // Arrange
        Customer customer = new Customer("CUST101", "Jane Doe", "456 Market St", "0712345678", 75);
        customerDAO.save(customer);

        // Act
        Customer retrieved = customerDAO.findById("CUST101");

        // Assert
        assertNotNull(retrieved, "Customer should be returned");
        assertEquals("Jane Doe", retrieved.getName(), "Customer name should match");

        // Cleanup
        customerDAO.delete("CUST101");
    }

    @Test
    void testDeleteCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer("CUST102", "John Smith", "789 High St", "0759876543", 30);
        customerDAO.save(customer);

        // Act
        customerDAO.delete("CUST102");

        // Assert
        assertFalse(customerDAO.exists("CUST102"), "Customer should be removed from DB");
    }
}
