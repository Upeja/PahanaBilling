package com.pahana.pahanabilling.customer.servlet;

import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/customers", "/customers/edit", "/customers/delete"})
public class CustomerServlet extends HttpServlet {
    private final CustomerService service = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        String accountNumber = req.getParameter("accountNumber");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        int units = Integer.parseInt(req.getParameter("unitsConsumed"));

        Customer customer = new Customer(accountNumber, name, address, phone, units);

        try {
            if (!customer.isValid()) {
                req.setAttribute("error", "Invalid input data.");
                forwardToList(req, resp);
                return;
            }

            if ("/customers/edit".equals(path)) {
                service.updateCustomer(customer);
            } else {
                if (service.customerExists(accountNumber)) {
                    req.setAttribute("error", "Account already exists.");
                    forwardToList(req, resp);
                    return;
                }
                service.registerCustomer(customer);
            }

            resp.sendRedirect(req.getContextPath() + "/customers");

        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            e.printStackTrace();
            forwardToList(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/customers/edit".equals(path)) {
                String id = req.getParameter("accountNumber");
                Customer customer = service.getCustomerById(id);
                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/WEB-INF/editCustomer.jsp").forward(req, resp);

            } else if ("/customers/delete".equals(path)) {
                String id = req.getParameter("accountNumber");
                service.deleteCustomer(id);
                resp.sendRedirect(req.getContextPath() + "/customers");

            } else {
                List<Customer> customers = service.listCustomers();
                req.setAttribute("customers", customers);
                req.getRequestDispatcher("/WEB-INF/customers.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error: " + e.getMessage());
            forwardToList(req, resp);
        }
    }

    private void forwardToList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("customers", service.listCustomers());
            req.getRequestDispatcher("/WEB-INF/customers.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.getWriter().write("Error loading customers: " + e.getMessage());
        }
    }
}
