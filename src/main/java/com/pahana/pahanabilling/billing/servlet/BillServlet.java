package com.pahana.pahanabilling.billing.servlet;

import com.google.gson.Gson;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try {
            String customerId = req.getParameter("customerId");
            String[] itemIds = req.getParameterValues("itemId[]");
            String[] unitPrices = req.getParameterValues("unitPrice[]");
            String[] units = req.getParameterValues("units[]");

            if (itemIds == null || unitPrices == null || units == null) {
                out.print(gson.toJson(new Response(false, "No items found", null)));
                return;
            }

            List<Bill> bills = new ArrayList<>();
            for (int i = 0; i < itemIds.length; i++) {
                double price = Double.parseDouble(unitPrices[i]);
                int qty = Integer.parseInt(units[i]);

                Bill bill = billingService.generateBill(customerId, itemIds[i], qty, price);
                billingService.saveBill(bill);
                bills.add(bill);
            }

            // Generate PDF for the latest bill set
            String pdfPath = generateBillPDF(bills);
            String pdfUrl = req.getContextPath() + "/generated-pdfs/" + new File(pdfPath).getName();

            out.print(gson.toJson(new Response(true, "Bill generated successfully", pdfUrl)));

        } catch (SQLException e) {
            e.printStackTrace();
            out.print(gson.toJson(new Response(false, "Database error: " + e.getMessage(), null)));
        } catch (Exception e) {
            e.printStackTrace();
            out.print(gson.toJson(new Response(false, "Error: " + e.getMessage(), null)));
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
            e.printStackTrace();
        }
    }

    private String generateBillPDF(List<Bill> bills) throws IOException {
        // TODO: Replace this with your actual PDF generation logic
        // Example: PDF file is generated in "generated-pdfs" folder under webapp
        File folder = new File(getServletContext().getRealPath("/generated-pdfs"));
        if (!folder.exists()) folder.mkdirs();

        String fileName = "bill_" + LocalDateTime.now().toString().replace(":", "-") + ".pdf";
        File pdfFile = new File(folder, fileName);

        // Your existing BillingService.generatePDF(bills, pdfFile) logic goes here
        billingService.generatePDF(bills, pdfFile.getAbsolutePath());

        return pdfFile.getAbsolutePath();
    }

    static class Response {
        boolean success;
        String message;
        String pdfUrl;

        Response(boolean success, String message, String pdfUrl) {
            this.success = success;
            this.message = message;
            this.pdfUrl = pdfUrl;
        }
    }
}
