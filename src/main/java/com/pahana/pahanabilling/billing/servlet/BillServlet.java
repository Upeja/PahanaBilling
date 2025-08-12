package com.pahana.pahanabilling.billing.servlet;

import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.service.BillingService;
import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;
import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.io.FileOutputStream;



@WebServlet(urlPatterns = "/bill")
public class BillServlet extends HttpServlet {

    private final BillingService billingService = new BillingService();
    private final CustomerService customerService = new CustomerService();
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        loadData(req);
        req.getRequestDispatcher("/bill.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String customerId = req.getParameter("customerId");
        String itemId = req.getParameter("itemId");
        String unitsStr = req.getParameter("units");
        String unitPriceStr = req.getParameter("unitPrice");

        if (customerId == null || itemId == null || unitsStr == null || unitPriceStr == null
                || customerId.isEmpty() || itemId.isEmpty() || unitsStr.isEmpty() || unitPriceStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing required fields");
            return;
        }

        try {
            int units = Integer.parseInt(unitsStr);
            double unitPrice = Double.parseDouble(unitPriceStr);

            // Generate bill object
            Bill bill = billingService.generateBill(customerId, itemId, units, unitPrice);

            // Save bill in DB (this should set bill.billId if you applied change above)
            billingService.saveBill(bill);

            // Create a folder under webapp so it's reachable: /generatedpdfs
            String pdfFolderPath = getServletContext().getRealPath("/generatedpdfs");
            File folder = new File(pdfFolderPath);
            if (!folder.exists()) folder.mkdirs();

            // file name
            String fileName = "Bill_" + bill.getBillId() + ".pdf";
            File pdfFile = new File(folder, fileName);

            // Use PdfExportUtil to generate the pdf (it expects an OutputStream)
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                com.pahana.pahanabilling.util.PdfExportUtil.generateBillPDF(bill, fos);
            }

            // respond with updated table rows (AJAX expects HTML rows)
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            List<Bill> bills = billingService.listAllBills();
            for (Bill b : bills) {
                out.println("<tr>");
                out.println("<td>" + b.getBillId() + "</td>");
                out.println("<td>" + b.getCustomerId() + "</td>");
                out.println("<td>" + b.getItemId() + "</td>");
                out.println("<td>" + b.getUnits() + "</td>");
                out.println("<td>" + b.getUnitPrice() + "</td>");
                out.println("<td>" + b.getTotalAmount() + "</td>");
                // link to PDF (saved under /generatedpdfs)
                out.println("<td><a target='_blank' href='" + req.getContextPath()
                        + "/generatedpdfs/Bill_" + b.getBillId() + ".pdf'>View PDF</a></td>");
                out.println("</tr>");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid number format");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error saving bill: " + e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("PDF generation error: " + e.getMessage());
        }
    }


    private void loadData(HttpServletRequest req) {
        try {
            List<Customer> customers = customerService.listCustomers();
            List<Item> items = itemService.listItems();
            List<Bill> bills = billingService.listAllBills();

            req.setAttribute("customers", customers);
            req.setAttribute("items", items);
            req.setAttribute("bills", bills);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading data: " + e.getMessage());
        }
    }
}
