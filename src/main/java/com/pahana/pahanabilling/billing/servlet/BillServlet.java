package com.pahana.pahanabilling.billing.servlet;

import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.service.BillingService;
import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;
import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;
import com.pahana.pahanabilling.util.PdfExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/bill", "/bills"})
public class BillServlet extends HttpServlet {

    private final BillingService billingService = new BillingService();
    private final CustomerService customerService = new CustomerService();
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        try {
            if ("/bills".equals(path)) {
                // View bills page
                List<Bill> bills = billingService.listAllBills();
                req.setAttribute("bills", bills);
                req.getRequestDispatcher("/viewBills.jsp").forward(req, resp);
            } else {
                // Generate bill page
                List<Customer> customers = customerService.listCustomers();
                List<Item> items = itemService.listItems();

                req.setAttribute("customers", customers);
                req.setAttribute("items", items);
                req.getRequestDispatcher("/bill.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Error loading data: " + e.getMessage());
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String customerId = req.getParameter("customerId");
        String itemId = req.getParameter("itemId");
        int units = Integer.parseInt(req.getParameter("units"));
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));

        try {
            // Save bill to DB
            Bill bill = billingService.generateBill(customerId, itemId, units, unitPrice);
            billingService.saveBill(bill);

            // Generate PDF
            String pdfDir = getServletContext().getRealPath("/generatepdf");
            PdfExportUtil.generateBillPDF(bill, pdfDir);

            req.setAttribute("success", "✅ Bill generated successfully!");

            // Reload bill form data
            List<Customer> customers = customerService.listCustomers();
            List<Item> items = itemService.listItems();

            req.setAttribute("customers", customers);
            req.setAttribute("items", items);

            req.getRequestDispatcher("/bill.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Error saving bill: " + e.getMessage());
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ PDF generation failed: " + e.getMessage());
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        }
    }
}
