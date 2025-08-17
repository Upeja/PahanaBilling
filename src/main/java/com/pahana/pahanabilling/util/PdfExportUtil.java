package com.pahana.pahanabilling.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.entity.BillItem;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfExportUtil {

    public static File generateBillPDF(Bill bill, String outputDir) throws Exception {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }

        File folder = new File(outputDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "bill_" + bill.getBillId() + ".pdf";
        File file = new File(folder, fileName);

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        // IMPORTANT: Close the document before the stream auto-closes
        try (FileOutputStream fos = new FileOutputStream(file)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Bill Receipt", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Bill details
            Font label = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font value = FontFactory.getFont(FontFactory.HELVETICA, 11);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Paragraph header = new Paragraph();
            header.add(new Chunk("Bill ID: ", label));
            header.add(new Chunk(String.valueOf(bill.getBillId()), value));
            header.add(Chunk.NEWLINE);
            header.add(new Chunk("Customer ID: ", label));
            header.add(new Chunk(bill.getCustomerId() != null ? bill.getCustomerId() : "-", value));
            header.add(Chunk.NEWLINE);
            header.add(new Chunk("Date: ", label));
            header.add(new Chunk(bill.getDateTime() != null ? bill.getDateTime().format(formatter) : "-", value));
            document.add(header);

            document.add(Chunk.NEWLINE);

            // Items table
            List<BillItem> items = bill.getItems();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5f);
            table.setWidths(new float[]{2.5f, 1.2f, 1.6f, 1.7f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
            Color headerBg = new Color(40, 55, 77);

            PdfPCell h1 = new PdfPCell(new Phrase("Item ID", headerFont));
            PdfPCell h2 = new PdfPCell(new Phrase("Qty", headerFont));
            PdfPCell h3 = new PdfPCell(new Phrase("Unit Price (LKR)", headerFont));
            PdfPCell h4 = new PdfPCell(new Phrase("Subtotal (LKR)", headerFont));
            for (PdfPCell h : new PdfPCell[]{h1, h2, h3, h4}) {
                h.setHorizontalAlignment(Element.ALIGN_CENTER);
                h.setBackgroundColor(headerBg);
                h.setPadding(6f);
                table.addCell(h);
            }

            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            double runningTotal = 0.0;

            if (items != null && !items.isEmpty()) {
                for (BillItem it : items) {
                    double subtotal = it.getSubtotal() != 0.0
                            ? it.getSubtotal()
                            : round(it.getQuantity() * it.getUnitPrice());
                    runningTotal += subtotal;

                    PdfPCell c1 = new PdfPCell(new Phrase(it.getItemId(), cellFont));
                    PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(it.getQuantity()), cellFont));
                    PdfPCell c3 = new PdfPCell(new Phrase(formatMoney(it.getUnitPrice()), cellFont));
                    PdfPCell c4 = new PdfPCell(new Phrase(formatMoney(subtotal), cellFont));

                    c1.setPadding(5f);
                    c2.setPadding(5f);
                    c3.setPadding(5f);
                    c4.setPadding(5f);

                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c4.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    table.addCell(c1);
                    table.addCell(c2);
                    table.addCell(c3);
                    table.addCell(c4);
                }
            } else {
                PdfPCell empty = new PdfPCell(new Phrase("No items", cellFont));
                empty.setColspan(4);
                empty.setHorizontalAlignment(Element.ALIGN_CENTER);
                empty.setPadding(8f);
                table.addCell(empty);
            }

            // Grand total row
            PdfPCell totalLabel = new PdfPCell(new Phrase("Grand Total (LKR)", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            totalLabel.setColspan(3);
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setPadding(6f);

            double totalFromBill = bill.getTotalAmount() != 0.0 ? bill.getTotalAmount() : runningTotal;

            PdfPCell totalValue = new PdfPCell(new Phrase(formatMoney(totalFromBill), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setPadding(6f);

            table.addCell(totalLabel);
            table.addCell(totalValue);

            document.add(table);

            document.add(Chunk.NEWLINE);
            Paragraph thanks = new Paragraph("Thank you for your purchase!", FontFactory.getFont(FontFactory.HELVETICA, 11));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            // Close BEFORE leaving the try-with-resources block
            document.close();
        }

        return file;
    }

    private static String formatMoney(double value) {
        return String.format("%.2f", value);
    }

    private static double round(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}