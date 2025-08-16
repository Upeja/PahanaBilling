package com.pahana.pahanabilling.billing.servlet;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.dao.BillItemDAO;
import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.billing.entity.BillItem;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/bill")
public class BillServlet extends HttpServlet {

    // Services used to load dropdown data
    private final CustomerService customerService = new CustomerService();
    private final ItemService itemService = new ItemService();

    // DAOs used to persist the bill and bill items
    private final BillDAO billDAO = new BillDAO();
    private final BillItemDAO billItemDAO = new BillItemDAO();

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

        String customerId = req.getParameter("customerId");

        // These come from hidden inputs created in bill.jsp addItem()
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
            sendBackWithError("Please add at least one item with valid quantity and unit price.", req, resp);
            return;
        }

        try {
            // 1) Compute total
            double totalAmount = 0.0;
            List<BillItem> itemsToSave = new ArrayList<>();

            for (int i = 0; i < itemIds.length; i++) {
                String itemId = itemIds[i];
                int qty = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(unitPrices[i]);

                if (qty <= 0 || price < 0) {
                    sendBackWithError("Invalid quantity or price for item: " + itemId, req, resp);
                    return;
                }

                double subtotal = qty * price;
                totalAmount += subtotal;

                BillItem bi = new BillItem();
                bi.setItemId(itemId);
                bi.setQuantity(qty);
                bi.setUnitPrice(price);
                bi.setSubtotal(subtotal);
                itemsToSave.add(bi);
            }

            // 2) Save bill
            Bill bill = new Bill();
            bill.setCustomerId(customerId);
            bill.setTotalAmount(totalAmount);
            bill.setDateTime(LocalDateTime.now());

            int billId = billDAO.saveBill(bill);

            // 3) Save each bill item with the generated billId
            for (BillItem bi : itemsToSave) {
                bi.setBillId(billId);
                billItemDAO.saveBillItem(bi);
            }

            // 4) Load the full bill (with items) and generate PDF
            Bill savedBill = billDAO.findById(billId);

            String pdfDir = getServletContext().getRealPath("/generatepdf");
            File dir = new File(pdfDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File pdfFile = PdfExportUtil.generateBillPDF(savedBill, pdfDir);

            // If you later use AJAX, you can return a small JSON payload
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setContentType("application/json");
                String json = String.format("{\"status\":\"OK\",\"billId\":%d,\"pdf\":\"%s\"}",
                        billId, req.getContextPath() + "/generatepdf/" + pdfFile.getName());
                resp.getWriter().write(json);
                return;
            }

            // Normal form submit: open the PDF (note your form uses target="_blank")
            resp.sendRedirect(req.getContextPath() + "/generatepdf/" + pdfFile.getName());

        } catch (NumberFormatException nfe) {
            sendBackWithError("Invalid number format for quantity or price.", req, resp);
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