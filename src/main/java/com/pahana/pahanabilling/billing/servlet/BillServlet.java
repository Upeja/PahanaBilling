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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        req.setCharacterEncoding("UTF-8");

        String customerId = req.getParameter("customerId"); // this is Customer.accountNumber
        String[] itemIds = req.getParameterValues("itemId");
        String[] quantities = req.getParameterValues("quantity");
        String[] unitPrices = req.getParameterValues("unitPrice");

        if (customerId == null || customerId.isEmpty()) {
            sendBackWithError("Customer is required.", req, resp);
            return;
        }
        if (itemIds == null || quantities == null || unitPrices == null
                || itemIds.length == 0
                || itemIds.length != quantities.length
                || itemIds.length != unitPrices.length) {
            sendBackWithError("Please add at least one item with valid quantity and price.", req, resp);
            return;
        }

        try {
            // Persist bill + items and get the saved bill (with items)
            Bill savedBill = billingService.createAndSaveBill(customerId, itemIds, quantities, unitPrices);

            // Generate PDF
            String pdfDir = getServletContext().getRealPath("/generatepdf");
            File pdfFolder = new File(pdfDir);
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }
            File pdfFile = PdfExportUtil.generateBillPDF(savedBill, pdfDir);

            // AJAX?
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setContentType("application/json");
                String json = String.format("{\"status\":\"OK\",\"billId\":%d,\"pdf\":\"%s\"}",
                        savedBill.getBillId(), req.getContextPath() + "/generatepdf/" + pdfFile.getName());
                resp.getWriter().write(json);
                return;
            }

            // Normal submit (your form opens in a new tab due to target="_blank")
            resp.sendRedirect(req.getContextPath() + "/generatepdf/" + pdfFile.getName());

        } catch (NumberFormatException nfe) {
            sendBackWithError("Invalid number format in quantity or price.", req, resp);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            sendBackWithError("Database error: " + sqle.getMessage(), req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            sendBackWithError("Failed to generate bill PDF: " + e.getMessage(), req, resp);
        }
    }

    private void loadData(HttpServletRequest req) {
        try {
            List<Customer> customers = customerService.listCustomers();
            List<Item> items = itemService.listItems();
            req.setAttribute("customers", customers);
            req.setAttribute("items", items);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendBackWithError(String msg, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        loadData(req);
        req.getRequestDispatcher("/bill.jsp").forward(req, resp);
    }
}