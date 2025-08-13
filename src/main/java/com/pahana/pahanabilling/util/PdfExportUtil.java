package com.pahana.pahanabilling.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pahana.pahanabilling.billing.entity.Bill;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class PdfExportUtil {

    /**
     * Generates a PDF for a Bill, saves it to a specified directory,
     * and returns the full path to the saved file.
     * @param bill The bill data to include in the PDF.
     * @param outputDir The directory on the server where the PDF will be saved.
     * @return The absolute path to the newly created PDF file.
     * @throws Exception if there is an error during PDF generation or file I/O.
     */
    public static String generateBillPDF(Bill bill, String outputDir) throws Exception {
        System.out.println(bill + outputDir);
        // Create the output directory if it doesn't already exist
        File folder = new File(outputDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Define the file path for the new PDF
        String fileName = "bill_" + bill.getBillId() + ".pdf";
        String filePath = outputDir + File.separator + fileName;
        System.out.println(fileName + filePath);
        // Create a new A4-sized document
        Document document = new Document(PageSize.A4);

        // Get a PdfWriter instance that writes to a file
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        // Open the document to begin adding content
        document.open();

        // --- Add Content to the Document ---

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Bill Receipt", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Add a blank line for spacing

        // Bill Details
        document.add(new Paragraph("Bill ID: " + bill.getBillId()));
        document.add(new Paragraph("Customer ID: " + bill.getCustomerId()));

        // Format the date and time for better readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        document.add(new Paragraph("Date: " + bill.getDateTime().format(formatter)));
        document.add(new Paragraph(" "));

        // Table for bill items
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // Table Headers
        table.addCell("Item ID");
        table.addCell("Units");
        table.addCell("Unit Price (LKR)");
        table.addCell("Total (LKR)");

        // Table Data
        table.addCell(bill.getItemId());
        table.addCell(String.valueOf(bill.getUnits()));
        table.addCell(String.format("%.2f", bill.getUnitPrice()));
        table.addCell(String.format("%.2f", bill.getTotalAmount()));

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank you for your purchase!"));

        // Close the document to save the file.
        document.close();
        System.out.println(filePath);
        // Return the full path to the created file
        return filePath;
    }
}