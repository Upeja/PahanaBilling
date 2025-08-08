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
                List<Bill> bills = billingService.listAllBills();
                req.setAttribute("bills", bills);
                req.getRequestDispatcher("/WEB-INF/viewBills.jsp").forward(req, resp);
            } else {
                // Load dropdowns for form
                List<Customer> customers = customerService.listCustomers();
                List<Item> items = itemService.listItems();

                req.setAttribute("customers", customers);
                req.setAttribute("items", items);
                req.getRequestDispatcher("/WEB-INF/bill.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error: " + e.getMessage());
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
            // 🧾 Create and save bill
            Bill bill = billingService.generateBill(customerId, itemId, units, unitPrice);
            billingService.saveBill(bill);

            // ✅ Redirect to view
            resp.sendRedirect(req.getContextPath() + "/bills");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("Error saving bill: " + e.getMessage());
        }
    }
}
