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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
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
        String unitsStr = req.getParameter("units");
        String unitPriceStr = req.getParameter("unitPrice");

        // Basic validation for required parameters
        if (customerId == null || customerId.isEmpty() ||
                itemId == null || itemId.isEmpty() ||
                unitsStr == null || unitsStr.isEmpty() ||
                unitPriceStr == null || unitPriceStr.isEmpty()) {

            req.setAttribute("error", "❌ Missing required fields: customerId, itemId, units, or unitPrice.");
            loadData(req);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
            return;
        }

        int units;
        double unitPrice;
        try {
            units = Integer.parseInt(unitsStr);
            unitPrice = Double.parseDouble(unitPriceStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "❌ Invalid number format for units or unit price.");
            loadData(req);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
            return;
        }

        try {
            // Generate and save bill
            Bill bill = billingService.generateBill(customerId, itemId, units, unitPrice);
            billingService.saveBill(bill);

            // Create PDF file path
            String pdfFolderPath = getServletContext().getRealPath("/generated-pdfs");
            File pdfFolder = new File(pdfFolderPath);
            if (!pdfFolder.exists()) pdfFolder.mkdirs();

            String pdfFileName = "bill_" + LocalDateTime.now().toString().replace(":", "-") + ".pdf";
            String pdfFilePath = pdfFolderPath + File.separator + pdfFileName;

            // Generate PDF for this single bill
            billingService.generatePDF(Collections.singletonList(bill), pdfFilePath);

            req.setAttribute("success", "✅ Bill generated successfully!");
            req.setAttribute("pdfPath", "generated-pdfs/" + pdfFileName);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Error saving bill: " + e.getMessage());
        }

        // Reload all data
        loadData(req);
        req.getRequestDispatcher("/bill.jsp").forward(req, resp);
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
}
