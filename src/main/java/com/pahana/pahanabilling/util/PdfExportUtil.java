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

        document.add(new Paragraph("Pahana Billing Receipt", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Customer: " + bill.getCustomerId()));
        document.add(new Paragraph("Item: " + bill.getItemId()));
        document.add(new Paragraph("Units: " + bill.getUnits()));
        document.add(new Paragraph("Unit Price: Rs. " + bill.getUnitPrice()));
        document.add(new Paragraph("Total: Rs. " + bill.getTotalAmount()));
        document.add(new Paragraph("Date: " + bill.getDateTime().toString()));

        document.close();
    }
}
