package com.pahana.pahanabilling.billing.service;

import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BillingService {

    private final BillDAO billDAO = new BillDAO();

    public Bill generateBill(String customerId, String itemId, int units, double unitPrice) {
        double total = units * unitPrice;
        return new Bill(0, customerId, itemId, units, unitPrice, total, java.time.LocalDateTime.now());
    }

    public void saveBill(Bill bill) throws SQLException {
        billDAO.save(bill);
    }

    public List<Bill> listAllBills() throws SQLException {
        return billDAO.getAllBills();
    }

    /**
     * Generate PDF for given bills.
     */
    public void generatePDF(List<Bill> bills, String filePath) throws IOException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Bill Receipt", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            document.add(new Paragraph("Generated On: " + java.time.LocalDateTime.now()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 3, 2, 2, 2});

            table.addCell("Customer ID");
            table.addCell("Item ID");
            table.addCell("Units");
            table.addCell("Unit Price");
            table.addCell("Total");

            for (Bill bill : bills) {
                table.addCell(bill.getCustomerId());
                table.addCell(bill.getItemId());
                table.addCell(String.valueOf(bill.getUnits()));
                table.addCell(String.format("%.2f", bill.getUnitPrice()));
                table.addCell(String.format("%.2f", bill.getTotalAmount()));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);
            double grandTotal = bills.stream().mapToDouble(Bill::getTotalAmount).sum();
            document.add(new Paragraph("Grand Total: Rs. " + String.format("%.2f", grandTotal)));

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF: " + e.getMessage(), e);
        } finally {
            document.close();
        }
    }
}
