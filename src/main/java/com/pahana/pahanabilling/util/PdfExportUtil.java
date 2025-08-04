package com.pahana.pahanabilling.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.pahana.pahanabilling.billing.entity.Bill;

import java.io.OutputStream;

public class PdfExportUtil {
    public static void generateBillPDF(Bill bill, OutputStream out) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("🧾 Pahana Billing Receipt", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table layout
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Add rows
        table.addCell(new Phrase("Customer ID", labelFont));
        table.addCell(new Phrase(bill.getCustomerId(), valueFont));

        table.addCell(new Phrase("Item ID", labelFont));
        table.addCell(new Phrase(bill.getItemId(), valueFont));

        table.addCell(new Phrase("Units", labelFont));
        table.addCell(new Phrase(String.valueOf(bill.getUnits()), valueFont));

        table.addCell(new Phrase("Unit Price (LKR)", labelFont));
        table.addCell(new Phrase("Rs. " + bill.getUnitPrice(), valueFont));

        table.addCell(new Phrase("Total Amount (LKR)", labelFont));
        table.addCell(new Phrase("Rs. " + bill.getTotalAmount(), valueFont));

        table.addCell(new Phrase("Date & Time", labelFont));
        table.addCell(new Phrase(bill.getDateTime().toString(), valueFont));

        document.add(table);

        // Footer
        Paragraph thankYou = new Paragraph("Thank you for using Pahana Billing System.", valueFont);
        thankYou.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(thankYou);

        document.close();
    }
}
