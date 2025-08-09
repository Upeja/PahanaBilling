package com.pahana.pahanabilling.customer.servlet;

import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/customers", "/customers/edit", "/customers/delete"})
public class CustomerServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        String accountNumber = req.getParameter("accountNumber");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        int unitsConsumed = Integer.parseInt(req.getParameter("unitsConsumed"));

        Customer customer = new Customer(accountNumber, name, address, phone, unitsConsumed);

        try {
            if ("/customers/edit".equals(path)) {
                customerService.updateCustomer(customer);
            } else {
                if (customerService.customerExists(accountNumber)) {
                    req.setAttribute("error", "⚠️ Account number already exists.");
                    forwardToList(req, resp);
                    return;
                }
                customerService.addCustomer(customer);
            }
            resp.sendRedirect(req.getContextPath() + "/customers");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing customer: " + e.getMessage());
            forwardToList(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        try {
            if ("/customers/edit".equals(path)) {
                String accountNumber = req.getParameter("accountNumber");
                Customer customer = customerService.getCustomerById(accountNumber);
                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/editCustomer.jsp").forward(req, resp);

            } else if ("/customers/delete".equals(path)) {
                String accountNumber = req.getParameter("accountNumber");
                customerService.deleteCustomer(accountNumber);
                resp.sendRedirect(req.getContextPath() + "/customers");

            } else { // "/customers"
                forwardToList(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error loading customers: " + e.getMessage());
            forwardToList(req, resp);
        }
    }

    private void forwardToList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Customer> customers = customerService.listCustomers();
            req.setAttribute("customers", customers);
            req.getRequestDispatcher("/customer.jsp").forward(req, resp); // Outside WEB-INF
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error loading customers: " + e.getMessage());
        }
    }
}
