package com.pahana.pahanabilling.customer.servlet;

import com.pahana.pahanabilling.customer.entity.Customer;
import com.pahana.pahanabilling.customer.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/register")
public class CustomerServlet extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Customer customer = new Customer();
        customer.setAccountNumber(req.getParameter("accountNumber"));
        customer.setName(req.getParameter("name"));
        customer.setAddress(req.getParameter("address"));
        customer.setPhone(req.getParameter("phone"));

        service.registerCustomer(customer);
        resp.sendRedirect("success.jsp");
    }
}
