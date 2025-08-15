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
        int units = Integer.parseInt(req.getParameter("units"));
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));

        try {
            // Create and save bill in DB
            Bill bill = billingService.generateBill(customerId, itemId, units, unitPrice);
            billingService.saveBill(bill);

            // Generate PDF using OpenPDF
            String pdfDir = getServletContext().getRealPath("/generatepdf");
            String pdfPath = PdfExportUtil.generateBillPDF(bill, pdfDir);

            // If AJAX request, send updated bill table HTML
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                List<Bill> bills = billingService.listAllBills();
                StringBuilder tableHtml = new StringBuilder();
                for (Bill b : bills) {
                    tableHtml.append("<tr>")
                            .append("<td>").append(b.getBillId()).append("</td>")
                            .append("<td>").append(b.getCustomerId()).append("</td>")
                            .append("<td>").append(b.getItemId()).append("</td>")
                            .append("<td>").append(b.getUnits()).append("</td>")
                            .append("<td>").append(b.getUnitPrice()).append("</td>")
                            .append("<td>").append(b.getTotalAmount()).append("</td>")
                            .append("<td>").append(b.getDateTime()).append("</td>")
                            .append("<td><a href='")
                            .append(req.getContextPath())
                            .append("/generatepdf/")
                            .append(new File(pdfPath).getName())
                            .append("' target='_blank'>View PDF</a></td>")
                            .append("</tr>");
                }
                resp.setContentType("text/html");
                resp.getWriter().write(tableHtml.toString());
                return;
            }

            // Normal form submit → reload page
            req.setAttribute("success", "✅ Bill generated successfully!");
            loadData(req);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Error saving bill: " + e.getMessage());
            loadData(req);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ PDF generation failed: " + e.getMessage());
            loadData(req);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
        }
    }
    private void loadData(HttpServletRequest req) {
        try {
            List<Customer> customers = customerService.listCustomers();
            List<Item> items = itemService.listItems();
            List<Bill> bills = billingService.listAllBills();
            req.setAttribute("customers", customers); req.setAttribute("items", items);
            req.setAttribute("bills", bills);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}