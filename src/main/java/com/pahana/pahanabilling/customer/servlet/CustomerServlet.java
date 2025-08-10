package com.pahana.pahanabilling.customer.servlet;

import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        loadCustomerList(req);
        req.getRequestDispatcher("/customer.jsp").forward(req, resp);
    }

    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String accountNumber = req.getParameter("accountNumber");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        int unitsConsumed = Integer.parseInt(req.getParameter("unitsConsumed"));

        try {
            if ("edit".equalsIgnoreCase(action)) {
                customerService.updateCustomer(
                        new Customer(accountNumber, name, address, phone, unitsConsumed)
                );
                req.setAttribute("success", "✅ Customer updated successfully!");

            } else if ("delete".equalsIgnoreCase(action)) {
                customerService.deleteCustomer(accountNumber);
                req.setAttribute("success", "🗑️ Customer deleted successfully!");

            } else { // Add new
                if (customerService.customerExists(accountNumber)) {
                    req.setAttribute("error", "⚠️ Account number already exists!");
                } else {
                    customerService.addCustomer(
                            new Customer(accountNumber, name, address, phone, unitsConsumed)
                    );
                    req.setAttribute("success", "✅ Customer added successfully!");
                }
            }

            // Reload updated data
            req.setAttribute("customers", customerService.listCustomers());
            req.getRequestDispatcher("/customer.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Error: " + e.getMessage());
            req.getRequestDispatcher("/customer.jsp").forward(req, resp);
        }
    }


    private void loadCustomerList(HttpServletRequest req) {
        try {
            List<Customer> customers = customerService.listCustomers();
            req.setAttribute("customers", customers);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading customers: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
