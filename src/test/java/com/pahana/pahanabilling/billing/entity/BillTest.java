package com.pahana.pahanabilling.billing.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillItemTest {

    @Test
    void testBillItemConstructorAndSubtotal() {
        BillItem billItem = new BillItem("I001", 3, 100.0);

        assertEquals("I001", billItem.getItemId());
        assertEquals(3, billItem.getQuantity());
        assertEquals(100.0, billItem.getUnitPrice());
        assertEquals(300.0, billItem.getSubtotal()); // 3 * 100.0
    }

    @Test
    void testSettersAndGetters() {
        BillItem billItem = new BillItem();
        billItem.setBillItemId(1);
        billItem.setBillId(10);
        billItem.setItemId("I002");
        billItem.setQuantity(2);
        billItem.setUnitPrice(50.0);
        billItem.setSubtotal(100.0);

        assertEquals(1, billItem.getBillItemId());
        assertEquals(10, billItem.getBillId());
        assertEquals("I002", billItem.getItemId());
        assertEquals(2, billItem.getQuantity());
        assertEquals(50.0, billItem.getUnitPrice());
        assertEquals(100.0, billItem.getSubtotal());
    }

    @Test
    void testUpdateQuantityRecalculateSubtotal() {
        BillItem billItem = new BillItem("I003", 5, 20.0);

        // Change quantity
        billItem.setQuantity(10);
        billItem.setSubtotal(billItem.getQuantity() * billItem.getUnitPrice());

        assertEquals(10, billItem.getQuantity());
        assertEquals(200.0, billItem.getSubtotal()); // 10 * 20.0
    }

    @Test
    void testUpdateUnitPriceRecalculateSubtotal() {
        BillItem billItem = new BillItem("I004", 2, 30.0);

        // Change price
        billItem.setUnitPrice(40.0);
        billItem.setSubtotal(billItem.getQuantity() * billItem.getUnitPrice());

        assertEquals(40.0, billItem.getUnitPrice());
        assertEquals(80.0, billItem.getSubtotal()); // 2 * 40.0
    }
}
