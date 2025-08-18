package com.pahana.pahanabilling.item.dao;

import com.pahana.pahanabilling.item.entity.Item;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

    private static ItemDAO itemDAO;

    @BeforeAll
    static void setup() {
        itemDAO = new ItemDAO();
    }

    @Test
    void testAddItem() throws SQLException {
        // Arrange
        Item item = new Item("ITEM100", "Test Pen", 45.50);

        // Act
        itemDAO.save(item);

        // Assert
        assertTrue(itemDAO.exists("ITEM100"), "Item should be stored successfully");

        // Cleanup
        itemDAO.delete("ITEM100");
    }

    @Test
    void testGetItemById() throws SQLException {
        // Arrange
        Item item = new Item("ITEM101", "Test Book", 250.00);
        itemDAO.save(item);

        // Act
        Item retrieved = itemDAO.findById("ITEM101");

        // Assert
        assertNotNull(retrieved, "Item should be returned");
        assertEquals("Test Book", retrieved.getName(), "Item name should match");
        assertEquals(250.00, retrieved.getPrice(), 0.001, "Item price should match");

        // Cleanup
        itemDAO.delete("ITEM101");
    }

    @Test
    void testUpdateItemPrice() throws SQLException {
        // Arrange
        Item item = new Item("ITEM102", "Test Notebook", 120.00);
        itemDAO.save(item);

        // Act - update price
        item = new Item("ITEM102", "Test Notebook", 200.00);
        itemDAO.update(item);

        // Retrieve updated
        Item updated = itemDAO.findById("ITEM102");

        // Assert
        assertNotNull(updated, "Item should exist after update");
        assertEquals(200.00, updated.getPrice(), 0.001, "Price should be updated correctly");

        // Cleanup
        itemDAO.delete("ITEM102");
    }
}
