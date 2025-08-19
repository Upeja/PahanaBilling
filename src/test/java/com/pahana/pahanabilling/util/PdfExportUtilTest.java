package com.pahana.pahanabilling.util;

import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.entity.BillItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PdfExportUtilTest {

    private final String outputDir = "test-pdfs";

    @AfterEach
    void cleanUp() {
        // Delete test PDF files after each test
        File folder = new File(outputDir);
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                file.delete();
            }
            folder.delete();
        }
    }

    @Test
    void testGeneratePdf() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setCustomerId("C001");
        bill.setDateTime(LocalDateTime.now());
        bill.setItems(Arrays.asList(
                new BillItem("ItemA", 2, 100.0),
                new BillItem("ItemB", 1, 200.0)
        ));
        bill.setTotalAmount(400.0);

        // Act
        File pdf = PdfExportUtil.generateBillPDF(bill, outputDir);

        // Assert
        assertNotNull(pdf);
        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0, "Generated PDF should not be empty");
    }

    @Test
    void testGeneratePdfInvalidBill() {
        // Arrange
        Bill bill = null;

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                PdfExportUtil.generateBillPDF(bill, outputDir)
        );
        assertEquals("Bill cannot be null", ex.getMessage());
    }
}
